package com.github.cloudyrock.dimmer.logic;

import com.github.cloudyrock.dimmer.*;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Class in charge of executing the Dimmer feature. It doesn't get involve in
 * intercepting, it just execute a feature with the given information matching
 * the behaviour injected by the builder.
 */
class FeatureExecutorImpl implements FeatureExecutor {

    private static final String EXCEPTION_MISMATCHED_RETURNED_TYPE =
            "Mismatched returned type for method[%s.%s()] with feature[%s] and operation[%s]: expected[%s], actual returned in behaviour[%s]";

    private Map<Behaviour.BehaviourKey, Function<FeatureInvocation, ?>> behaviours;
    private FeatureBroker broker;


    private static final DimmerLogger logger = new DimmerLogger(FeatureExecutorImpl.class);

    FeatureExecutorImpl(FeatureBroker broker) {
        this.broker = broker;
        this.broker.setSubscriber(this::updateBehaviours);
    }

    FeatureBroker getBroker() {
        return broker;
    }

    void updateBehaviours(Map<Behaviour.BehaviourKey, Function<FeatureInvocation, ?>> behaviours) {
        this.behaviours = behaviours;
    }


    @Override
    public Object executeDimmerFeature(String feature,
                                       String operation,
                                       FeatureInvocation featureInvocation,
                                       MethodCaller realMethod) throws Throwable {
        if (isConditionPresent(feature, operation)) {
            logDimmerInterception(feature, operation, featureInvocation);
            return executeFeature(feature, operation, featureInvocation);
        } else {
            logger.trace("Dimmer ignored due to feature {} is not configured", feature);
            return realMethod.call();
        }
    }


    boolean isConditionPresent(String feature, String operation) {
        return behaviours.containsKey(new Behaviour.BehaviourKey(feature, operation));
    }

    Object executeFeature(String feature, String operation, FeatureInvocation featureInvocation) {
        final Behaviour.BehaviourKey key = new Behaviour.BehaviourKey(feature, operation);
        final Object result = behaviours.get(key).apply(featureInvocation);
        if (!isRightReturnedType(featureInvocation.getReturnType(), result)) {

            String message = String.format(EXCEPTION_MISMATCHED_RETURNED_TYPE,
                    featureInvocation.getDeclaringType().getSimpleName(),
                    featureInvocation.getMethodName(),
                    feature,
                    operation,
                    featureInvocation.getReturnType().getSimpleName(),
                    result.getClass().getSimpleName());


            throw new DimmerConfigException(message);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    static boolean isRightReturnedType(Class returnType, Object behaviourResult) {
        return Objects.isNull(behaviourResult) || returnType.isAssignableFrom(behaviourResult.getClass());
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


}
