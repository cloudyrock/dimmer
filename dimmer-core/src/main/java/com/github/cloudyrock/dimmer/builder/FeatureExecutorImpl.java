package com.github.cloudyrock.dimmer.builder;

import com.github.cloudyrock.dimmer.*;
import com.github.cloudyrock.dimmer.exception.DimmerConfigException;
import com.github.cloudyrock.dimmer.metadata.*;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Class in charge of executing the Dimmer feature. It doesn't get involve in
 * intercepting, it just execute a feature with the given information matching
 * the metadata injected by the builder.
 */
public class FeatureExecutorImpl implements FeatureExecutor {

    private static final String EXCEPTION_MESSAGE_CAST =
            "The expected return types between the real method and the configured function are mismatched";

    private final Map<BehaviourKey, Function<FeatureInvocation, ?>> behaviours =
            new ConcurrentHashMap<>();
    private final FeatureObservable featureObservable;
    private final Set<FeatureMetadata> featureActions;
    private final Class<? extends RuntimeException> defaultException;


    private static final DimmerLogger logger = new DimmerLogger(FeatureExecutorImpl.class);

    FeatureExecutorImpl(FeatureObservable featureObservable,
                        Set<FeatureMetadata> featureActions,
                        Class<? extends RuntimeException> defaultException) {
        this.featureObservable = featureObservable;
        this.featureActions = featureActions;
        this.defaultException = defaultException;
    }

    void start() {
        this.featureObservable.observe(this::process);
    }

    void process(FeatureUpdateEvent featureUpdateEvent) {

        if (featureActions != null) {
            featureActions.stream()
                    .filter(fm-> featureUpdateEvent.getFeaturesToggledOff().contains(fm.getFeature()))
                    .filter(fm -> fm instanceof BehaviourFeatureMetadata)
                    .map(fm -> (BehaviourFeatureMetadata) fm)
                    .peek(fm -> logFeature("APPLIED feature {} with behaviour",
                            fm.getFeature()))
                    .forEach(fmb -> featureWithBehaviour(
                            fmb.getFeature(),
                            fmb.getOperation(),
                            fmb.getBehaviour()));

            featureActions.stream()
                    .filter(fm-> featureUpdateEvent.getFeaturesToggledOff().contains(fm.getFeature()))
                    .filter(fm -> fm instanceof ExceptionFeatureMetadata)
                    .map(fm -> (ExceptionFeatureMetadata) fm)
                    .peek(fm -> logFeature("APPLIED feature {} with exception {}",
                            fm.getFeature(), fm.getException()))
                    .forEach(fme -> featureWithException(
                            fme.getFeature(),
                            fme.getOperation(),
                            fme.getException()
                    ));


            featureActions.stream()
                    .filter(fm-> featureUpdateEvent.getFeaturesToggledOff().contains(fm.getFeature()))
                    .filter(fm -> fm instanceof DefaultExceptionFeatureMetadata)
                    .map(fm -> (DefaultExceptionFeatureMetadata) fm)
                    .peek(fm -> logFeature("APPLIED feature {} with default exception {}",
                            fm.getFeature(), defaultException))
                    .forEach(fmde ->
                            featureWithException(
                                    fmde.getFeature(),
                                    fmde.getOperation(),
                                    defaultException));

            featureActions.stream()
                    .filter(fm-> featureUpdateEvent.getFeaturesToggledOff().contains(fm.getFeature()))
                    .filter(fm -> fm instanceof ValueFeatureMetadata)
                    .map(fm -> (ValueFeatureMetadata) fm)
                    .peek(fm -> logFeature("APPLIED feature {} with value {}",
                            fm.getFeature(), fm.getValueToReturn()))
                    .forEach(fmv -> featureWithValue(
                            fmv.getFeature(),
                            fmv.getOperation(),
                            fmv.getValueToReturn())
                    );
        }
    }

    @Override
    public Object executeDimmerFeature(String feature,
                                       String operation,
                                       FeatureInvocation featureInvocation,
                                       MethodCaller realMethod) throws Throwable {
        if (isConditionPresent(feature, operation)) {
            logDimmerInterception(feature, operation, featureInvocation);
            return runFeature(feature, operation, featureInvocation);
        } else {
            logger.trace("Dimmer ignored due to feature {} is not configured", feature);
            return realMethod.call();
        }
    }

    boolean featureWithBehaviour(
            String feature,
            String operation,
            Function<FeatureInvocation, ?> behaviour) {
        Preconditions.checkNullOrEmpty(behaviour, "behaviour");
        return putBehaviour(feature, operation, behaviour);
    }

    boolean featureWithException(String feature,
                                 String operation,
                                 Class<? extends RuntimeException> exceptionType) {

        Preconditions.checkNullOrEmpty(exceptionType, "exceptionType");
        return putBehaviour(
                feature,
                operation,
                featureInv -> ExceptionUtil.throwException(exceptionType, featureInv));
    }

    boolean featureWithValue(String feature, String operation, Object valueToReturn) {
        return putBehaviour(feature, operation, signature -> valueToReturn);
    }

    @SuppressWarnings("unchecked")
    static void checkReturnType(Class returnType, Object behaviourResult) {
        if (!Objects.isNull(behaviourResult)
                && !returnType.isAssignableFrom(behaviourResult.getClass())) {
            throw new DimmerConfigException(EXCEPTION_MESSAGE_CAST);
        }
    }

    private boolean putBehaviour(String feature,
                                 String operation,
                                 Function<FeatureInvocation, ?> behaviour) {
        Preconditions.checkNullOrEmpty(feature, "featureId");
        Preconditions.checkNullOrEmpty(feature, "operation");
        if (operation == null || operation.isEmpty()) {
            logger.warn("Adding behaviour to feature {} with empty operation", feature);
        }
        final BehaviourKey behaviourKey = new BehaviourKey(
                feature,
                operation
        );
        return behaviours.putIfAbsent(behaviourKey, behaviour) == null;
    }

    boolean isConditionPresent(String feature, String operation) {
        return behaviours.containsKey(new BehaviourKey(feature, operation));
    }

    Object runFeature(String feature, String operation, FeatureInvocation featureInvocation) {
        final BehaviourKey key = new BehaviourKey(feature, operation);
        final Object result = behaviours.get(key).apply(featureInvocation);
        checkReturnType(featureInvocation.getReturnType(), result);
        return result;
    }

    private void logDimmerInterception(String feature,
                                       String operation,
                                       FeatureInvocation featureInvocation) {
        logger.info(
                "Intercepted method {}.{}() for feature {} and operation {}",
                featureInvocation.getDeclaringType(),
                featureInvocation.getMethodName(),
                feature,
                operation);
    }

    private void logFeature(String format, String feature, Object... args) {
        logger.info(format, feature, args);
    }


}
