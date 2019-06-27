package com.github.cloudyrock.dimmer.builder;

import com.github.cloudyrock.dimmer.*;
import com.github.cloudyrock.dimmer.metadata.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class FeatureBroker {

    private static final DimmerLogger logger = new DimmerLogger(FeatureBroker.class);

    private final FeatureObservable featureObservable;
    private final Set<FeatureMetadata> featureActions;
    private final Class<? extends RuntimeException> defaultException;

    private Consumer<Map<BehaviourKey, Function<FeatureInvocation, ?>>> subscriber;

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

    void setSubscriber(Consumer<Map<BehaviourKey, Function<FeatureInvocation, ?>>> subscriber) {
        this.subscriber = subscriber;
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
        if (subscriber != null) {
            subscriber.accept(behaviours);
        }
    }

    private void addWithValue(FeatureUpdateEvent newFeatures, Map<BehaviourKey, Function<FeatureInvocation, ?>> behaviours) {

        addFeatures(newFeatures, behaviours, fm -> fm instanceof ValueFeatureMetadata,
                featureMetadataValue -> invocation -> ((ValueFeatureMetadata)featureMetadataValue).getValueToReturn());
    }


    private void addWithDefaultException(FeatureUpdateEvent featureUpdateEvent, Map<BehaviourKey, Function<FeatureInvocation, ?>> behaviours) {
        addFeatures(featureUpdateEvent, behaviours, fm -> fm instanceof DefaultExceptionFeatureMetadata,
                fmde -> featureInv -> ExceptionUtil.throwException(defaultException, featureInv));
    }

    private void addWithException(FeatureUpdateEvent featureUpdateEvent, Map<BehaviourKey, Function<FeatureInvocation, ?>> behaviours) {
        addFeatures(featureUpdateEvent, behaviours, fm -> fm instanceof ExceptionFeatureMetadata,
                fme -> featureInv -> ExceptionUtil.throwException(((ExceptionFeatureMetadata)fme).getException(), featureInv));
    }

    private void addWithBehaviour(FeatureUpdateEvent featureUpdateEvent, Map<BehaviourKey, Function<FeatureInvocation, ?>> behaviours) {
        addFeatures(featureUpdateEvent, behaviours, fm -> fm instanceof DefaultExceptionFeatureMetadata,
                fmb -> featureInv -> ((BehaviourFeatureMetadata)fmb).getBehaviour());
    }


    private void addFeatures(FeatureUpdateEvent featureUpdateEvent,
                             Map<BehaviourKey, Function<FeatureInvocation, ?>> behaviours,
                             Predicate<FeatureMetadata> filter,
                             Function<FeatureMetadata, Function<FeatureInvocation, Object>> additionFunction) {
        featureActions.stream()
                .filter(fm -> featureUpdateEvent.getFeaturesToggledOff().contains(fm.getFeature()))
                .filter(filter)
                .peek(fm -> logFeature("APPLIED feature[{}]", fm.toString()))
                .forEach(fm -> behaviours.putIfAbsent(getKey(fm), additionFunction.apply(fm)));
    }

    private BehaviourKey getKey(FeatureMetadata featureMetadata) {
        return new BehaviourKey(featureMetadata.getFeature(), featureMetadata.getOperation());
    }

    private void logFeature(String format, String feature, Object... args) {
        logger.info(format, feature, args);
    }


}
