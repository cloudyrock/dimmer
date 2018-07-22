package com.github.cloudyrock.dimmer;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.HashMap;
import java.util.HashSet;
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
class DimmerProcessor {

    private static final String EXCEPTION_MESSAGE_CAST = "The expected return types between the real method and the configured function are mismatched";

    private final Class<? extends RuntimeException> defaultExceptionType;

    private final Map<String, Function<FeatureInvocation, ?>> behaviours =
            new ConcurrentHashMap<>();

    DimmerProcessor(Class<? extends RuntimeException> defaultExceptionType) {
        ExceptionUtil.checkAndGetExceptionConstructorType(defaultExceptionType);
        this.defaultExceptionType = defaultExceptionType;
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
        Util.checkArgument(behaviour, "behaviour");
        return putBehaviour(feature, behaviour);
    }

    /**
     * If the specified feature is not already associated with a behaviour(or is mapped to null),
     * associates it with the default exception and returns true, else returns false.
     *
     * @param feature feature with which the specified behaviour is to be associated
     * @return true, or false if the key was already associated to a behaviour.
     */
    boolean featureWithDefaultException(String feature) {
        return featureWithException(feature, defaultExceptionType);
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
    boolean featureWithException(
            String feature,
            Class<? extends RuntimeException> exceptionType) {

        final ExceptionConstructorType constructorType =
                ExceptionUtil.checkAndGetExceptionConstructorType(exceptionType);
        return putBehaviour(feature,
                featureInvocation -> ExceptionUtil.throwException(
                        exceptionType,
                        constructorType,
                        featureInvocation));
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

    Object executeDimmerFeature(
            DimmerFeature dimmerFeature,
            FeatureInvocation featureInvocation,
            ProceedingJoinPoint realMethod) throws Throwable {
        final String feature = dimmerFeature.value();
        if (behaviours.containsKey(feature)) {
            final Object result = behaviours.get(feature).apply(featureInvocation);

            checkReturnType(featureInvocation.getReturnType(), result);
            return result;
        } else {
            return realMethod.proceed();
        }
    }

    @SuppressWarnings("unchecked")
    private static void checkReturnType(Class returnType, Object behaviourResult) {
        if (!Objects.isNull(behaviourResult)
                && !returnType.isAssignableFrom(behaviourResult.getClass())) {
            throw new DimmerConfigException(EXCEPTION_MESSAGE_CAST);
        }
    }

    private boolean putBehaviour(String featureId,
                                 Function<FeatureInvocation, ?> behaviour) {
        Util.checkArgument(featureId, "featureId");
        return behaviours.putIfAbsent(featureId, behaviour) == null;
    }



}