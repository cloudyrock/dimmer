package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DimmerConfigException;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Singleton class to configure feature's behaviour.
 * <p>
 * Threadsafe.
 *
 * @author Antonio Perez Dieppa
 * @see Function
 * @see DimmerConfigException
 * @see FeatureInvocation
 * @since 11/06/2018
 */
abstract class FeatureProcessorBase {

    private static final String EXCEPTION_MESSAGE_CAST =
            "The expected return types between the real method and the configured function are mismatched";

    private final Map<String, Function<FeatureInvocation, ?>> behaviours =
            new ConcurrentHashMap<>();
    protected static final DimmerSlf4j logger = new DimmerSlf4j(FeatureProcessorBase.class);

    FeatureProcessorBase() {
    }

    /**
     * If the specified feature is not already associated with a behaviour(or is mapped to null),
     * associates it with the given (@{@link Function}) that represents the desired behaviour
     * and returns true, else returns false.
     * <p>
     * Notice that the function that represents the feature's behaviour must ensure compatibility
     * with the real method's returning type or a (@{@link DimmerConfigException}) will be thrown.
     *
     * @param feature   feature with which the specified behaviour is to be associated
     * @param behaviour (@{@link Function}) to be associated with the specified key as behaviour
     * @return true, or false if the key was already associated to a behaviour.
     * @see Function
     * @see DimmerConfigException
     */
    boolean featureWithBehaviour(
            String feature,
            Function<FeatureInvocation, ?> behaviour) {
        Util.checkArgumentNullEmpty(behaviour, "behaviour");
        return putBehaviour(feature, behaviour);
    }

    /**
     * If the specified feature is not already associated with a behaviour(or is mapped to null),
     * associates it with the given exception and returns true, else returns false.
     * <p>
     * Notice the exception type must have either an empty constructor or a contractor with only
     * one parameter, (@{@link FeatureInvocation})
     *
     * @param feature       feature with which the specified behaviour is to be associated
     * @param exceptionType exception type to be associated with the specified key
     * @return true, or false if the key was already associated to a behaviour.
     * @see FeatureInvocation
     */
    boolean featureWithException(String feature,
                                 Class<? extends RuntimeException> exceptionType) {

        return putBehaviour(
                feature,
                featureInv -> ExceptionUtil.throwException(exceptionType, featureInv));
    }

    /**
     * If the specified feature is not already associated with a behaviour(or is mapped to null),
     * associates it with the given value and returns true, else returns false.
     * <p>
     * Notice that the value must be compatibility with the real method's returning type
     * or a (@{@link DimmerConfigException}) will be thrown.
     *
     * @param feature       feature with which the specified behaviour is to be associated
     * @param valueToReturn value to be associated with the specified key
     * @return true, or false if the key was already associated to a behaviour.
     */
    boolean featureWithValue(String feature, Object valueToReturn) {
        return putBehaviour(feature, signature -> valueToReturn);
    }

    @SuppressWarnings("unchecked")
    protected static void checkReturnType(Class returnType, Object behaviourResult) {
        if (!Objects.isNull(behaviourResult)
                && !returnType.isAssignableFrom(behaviourResult.getClass())) {
            throw new DimmerConfigException(EXCEPTION_MESSAGE_CAST);
        }
    }

    private boolean putBehaviour(String featureId,
                                 Function<FeatureInvocation, ?> behaviour) {
        Util.checkArgumentNullEmpty(featureId, "featureId");
        return behaviours.putIfAbsent(featureId, behaviour) == null;
    }

    protected boolean isFeatureConfigured(String feature) {
        return behaviours.containsKey(feature);
    }

    protected Object runFeature(String feature, FeatureInvocation featureInvocation) {
        final Object result = behaviours.get(feature).apply(featureInvocation);
        checkReturnType(featureInvocation.getReturnType(), result);
        return result;
    }

}