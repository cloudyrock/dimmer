package com.github.cloudyrock.dimmer.builder;

import com.github.cloudyrock.dimmer.*;
import com.github.cloudyrock.dimmer.DimmerConfigException;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Class in charge of executing the Dimmer feature. It doesn't get involve in
 * intercepting, it just execute a feature with the given information matching
 * the behaviour injected by the builder.
 */
class FeatureExecutorImpl implements FeatureExecutor {

    private static final String EXCEPTION_MESSAGE_CAST =
            "The expected return types between the real method and the configured function are mismatched";

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
        checkReturnType(featureInvocation.getReturnType(), result);
        return result;
    }

    @SuppressWarnings("unchecked")
    static void checkReturnType(Class returnType, Object behaviourResult) {
        if (!Objects.isNull(behaviourResult)
                && !returnType.isAssignableFrom(behaviourResult.getClass())) {
            throw new DimmerConfigException(EXCEPTION_MESSAGE_CAST);
        }
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
