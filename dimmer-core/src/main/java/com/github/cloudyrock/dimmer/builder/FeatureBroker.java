package com.github.cloudyrock.dimmer.builder;

import com.github.cloudyrock.dimmer.*;
import com.github.cloudyrock.dimmer.metadata.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

class FeatureBroker {

    private static final DimmerLogger logger = new DimmerLogger(FeatureBroker.class);

    private final FeatureObservable featureObservable;
    private final Set<FeatureMetadata> featureActions;
    private final Class<? extends RuntimeException> defaultException;

    private Consumer<Map<BehaviourKey, Function<FeatureInvocation, ?>>> observer;

    FeatureBroker(FeatureObservable featureObservable,
                  Set<FeatureMetadata> featureActions,
                  Class<? extends RuntimeException> defaultException) {
        this.featureActions = featureActions;
        this.featureObservable = featureObservable;
        this.defaultException = defaultException;
    }

    void start() {
        featureObservable.subscribe(this::process);
    }

    void setSubscriber(Consumer<Map<BehaviourKey, Function<FeatureInvocation, ?>>> observer) {
        this.observer = observer;
    }


    void process(FeatureUpdateEvent featureUpdateEvent) {
        if (featureActions != null) {
            final Map<BehaviourKey, Function<FeatureInvocation, ?>> behaviours = new HashMap<>();
            addWithBehaviour(featureUpdateEvent, behaviours);
            addWithException(featureUpdateEvent, behaviours);
            addWithDefaultException(featureUpdateEvent, behaviours);
            addWithValue(featureUpdateEvent, behaviours);
            notifyExecutor(behaviours);
        }
    }

    private void notifyExecutor(Map<BehaviourKey, Function<FeatureInvocation, ?>> behaviours) {
        if (observer != null) {
            observer.accept(behaviours);
        }
    }

    private void addWithValue(FeatureUpdateEvent featureUpdateEvent, Map<BehaviourKey, Function<FeatureInvocation, ?>> behaviours) {
        featureActions.stream()
                .filter(fm -> featureUpdateEvent.getFeaturesToggledOff().contains(fm.getFeature()))
                .filter(fm -> fm instanceof ValueFeatureMetadata)
                .map(fm -> (ValueFeatureMetadata) fm)
                .peek(fm -> logFeature("APPLIED feature {} with value {}", fm.getFeature(), fm.getValueToReturn()))
                .forEach(fmv -> behaviours.putIfAbsent(getKey(fmv), signature -> fmv.getValueToReturn()));
    }

    private void addWithDefaultException(FeatureUpdateEvent featureUpdateEvent, Map<BehaviourKey, Function<FeatureInvocation, ?>> behaviours) {
        featureActions.stream()
                .filter(fm -> featureUpdateEvent.getFeaturesToggledOff().contains(fm.getFeature()))
                .filter(fm -> fm instanceof DefaultExceptionFeatureMetadata)
                .map(fm -> (DefaultExceptionFeatureMetadata) fm)
                .peek(fm -> logFeature("APPLIED feature {} with default exception {}", fm.getFeature(), defaultException))
                .forEach(fmde -> behaviours.putIfAbsent(getKey(fmde), featureInv -> ExceptionUtil.throwException(defaultException, featureInv)));
    }

    private void addWithException(FeatureUpdateEvent featureUpdateEvent, Map<BehaviourKey, Function<FeatureInvocation, ?>> behaviours) {
        featureActions.stream()
                .filter(fm -> featureUpdateEvent.getFeaturesToggledOff().contains(fm.getFeature()))
                .filter(fm -> fm instanceof ExceptionFeatureMetadata)
                .map(fm -> (ExceptionFeatureMetadata) fm)
                .peek(fm -> logFeature("APPLIED feature {} with exception {}", fm.getFeature(), fm.getException()))
                .forEach(fme -> {
                            Preconditions.checkNullOrEmpty(fme.getException(), "exceptionType");
                            behaviours.putIfAbsent(getKey(fme), featureInv -> ExceptionUtil.throwException(fme.getException(), featureInv));
                        }
                );
    }

    private void addWithBehaviour(FeatureUpdateEvent featureUpdateEvent, Map<BehaviourKey, Function<FeatureInvocation, ?>> behaviours) {
        featureActions.stream()
                .filter(fm -> featureUpdateEvent.getFeaturesToggledOff().contains(fm.getFeature()))
                .filter(fm -> fm instanceof BehaviourFeatureMetadata)
                .map(fm -> (BehaviourFeatureMetadata) fm)
                .peek(fm -> logFeature("APPLIED feature {} with behaviour", fm.getFeature()))
                .forEach(fmb -> {
                    Preconditions.checkNullOrEmpty(fmb.getBehaviour(), "behaviour");
                    behaviours.putIfAbsent(getKey(fmb), fmb.getBehaviour());
                });
    }

    private BehaviourKey getKey(FeatureMetadata featureMetadata) {
        Preconditions.checkNullOrEmpty(featureMetadata.getFeature(), "feature");
        Preconditions.checkNullOrEmpty(featureMetadata.getOperation(), "operation");
        return new BehaviourKey(featureMetadata.getFeature(), featureMetadata.getOperation());
    }

    private void logFeature(String format, String feature, Object... args) {
        logger.info(format, feature, args);
    }


}
