package com.github.cloudyrock.dimmer;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;


abstract class FeatureProcessorBase {

    private static final String EXCEPTION_MESSAGE_CAST =
            "The expected return types between the real method and the configured function are mismatched";

    private final Map<BehaviourKey, Function<FeatureInvocation, ?>> behaviours =
            new ConcurrentHashMap<>();


    private static final DimmerLogger logger =
            new DimmerLogger(DimmerFeatureConfigurable.class);


    FeatureProcessorBase() {
    }

    FeatureProcessorBase(Set<FeatureMetadata> featureMetadataSet,
                         Class<? extends RuntimeException> defaultException) {

        if (featureMetadataSet != null) {
            featureMetadataSet.stream()
                    .filter(fm -> fm instanceof FeatureMetadataBehaviour)
                    .map(fm -> (FeatureMetadataBehaviour) fm)
                    .peek(fm -> logFeature("APPLIED feature {} with behaviour",
                            fm.getFeature()))
                    .forEach(fmb -> featureWithBehaviour(
                            fmb.getFeature(),
                            fmb.getOperation(),
                            fmb.getBehaviour()));

            featureMetadataSet.stream()
                    .filter(fm -> fm instanceof FeatureMetadataException)
                    .map(fm -> (FeatureMetadataException) fm)
                    .peek(fm -> logFeature("APPLIED feature {} with exception {}",
                            fm.getFeature(), fm.getException()))
                    .forEach(fme -> featureWithException(
                            fme.getFeature(),
                            fme.getOperation(),
                            fme.getException()
                    ));


            featureMetadataSet.stream()
                    .filter(fm -> fm instanceof FeatureMetadataDefaultException)
                    .map(fm -> (FeatureMetadataDefaultException) fm)
                    .peek(fm -> logFeature("APPLIED feature {} with default exception {}",
                            fm.getFeature(), defaultException))
                    .forEach(fmde ->
                            featureWithException(
                                    fmde.getFeature(),
                                    fmde.getOperation(),
                                    defaultException));

            featureMetadataSet.stream()
                    .filter(fm -> fm instanceof FeatureMetadataValue)
                    .map(fm -> (FeatureMetadataValue) fm)
                    .peek(fm -> logFeature("APPLIED feature {} with value {}",
                            fm.getFeature(), fm.getValueToReturn()))
                    .forEach(fmv -> featureWithValue(
                            fmv.getFeature(),
                            fmv.getOperation(),
                            fmv.getValueToReturn())
                    );
        }

    }


    private void logFeature(String format, String feature, Object... args) {
        logger.info(format, feature, args);
    }

    boolean featureWithBehaviour(
            String feature,
            String operation,
            Function<FeatureInvocation, ?> behaviour) {
        Util.checkArgumentNullEmpty(behaviour, "behaviour");
        return putBehaviour(feature, operation, behaviour);
    }

    boolean featureWithException(String feature,
                                 String operation,
                                 Class<? extends RuntimeException> exceptionType) {

        Util.checkArgumentNullEmpty(exceptionType, "exceptionType");
        return putBehaviour(
                feature,
                operation,
                featureInv -> ExceptionUtil.throwException(exceptionType, featureInv));
    }

    boolean featureWithValue(String feature, String operation, Object valueToReturn) {
        return putBehaviour(feature, operation, signature -> valueToReturn);
    }

    @SuppressWarnings("unchecked")
    protected static void checkReturnType(Class returnType, Object behaviourResult) {
        if (!Objects.isNull(behaviourResult)
                && !returnType.isAssignableFrom(behaviourResult.getClass())) {
            throw new DimmerConfigException(EXCEPTION_MESSAGE_CAST);
        }
    }

    private boolean putBehaviour(String feature,
                                 String operation,
                                 Function<FeatureInvocation, ?> behaviour) {
        Util.checkArgumentNullEmpty(feature, "featureId");
        Util.checkArgumentNullEmpty(feature, "operation");
        if(operation == null || operation.isEmpty()) {
            logger.warn("Adding behaviour to feature {} with empty operation", feature);
        }
        final BehaviourKey behaviourKey = new BehaviourKey(
                feature,
                operation
        );
        return behaviours.putIfAbsent(behaviourKey, behaviour) == null;
    }

    protected boolean isConditionPresent(String feature, String operation) {
        return behaviours.containsKey(new BehaviourKey(feature, operation));
    }

    protected Object runFeature(String feature, String operation, FeatureInvocation featureInvocation) {
        final BehaviourKey key = new BehaviourKey(feature, operation);
        final Object result = behaviours.get(key).apply(featureInvocation);
        checkReturnType(featureInvocation.getReturnType(), result);
        return result;
    }

}