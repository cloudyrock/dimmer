package com.github.cloudyrock.dimmer;

import org.aspectj.lang.Aspects;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static com.github.cloudyrock.dimmer.DimmerFeature.ALWAYS_OFF;

/**
 * Singleton class to configure feature's behaviour.
 *
 * Threadsafe.
 *
 * @author Antonio Perez Dieppa
 * @since 11/06/2018
 */
public class DimmerProcessor {

    private static final SingletonBuilder builder = new SingletonBuilder();

    private final Class<? extends RuntimeException> defaultExceptionType;

    private final Map<String, Function<FeatureInvocation, ? extends Object>> behaviours =
            new ConcurrentHashMap<>();

    /**
     * Singleton builder for DimmerProcessor
     * @return Singleton DimmerProcessor builder
     */
    public final static SingletonBuilder builder() {
        return builder;
    }

    private DimmerProcessor(Class<? extends RuntimeException> defaultExceptionType) {
        this.defaultExceptionType = defaultExceptionType;
    }

    /**
     * If the specified feature is not already associated with a behaviour(or is mapped to null),
     * associates it with the given (@{@link Function}) that represents the desired behaviour
     * and returns true, else returns false.
     *
     * Notice that the function that represents the feature's behaviour must ensure compatibility
     * with the real method's returning type or a (@{@link DimmerConfigException}) will be thrown.
     *
     * @see Function
     * @see DimmerConfigException
     * @param feature feature with which the specified behaviour is to be associated
     * @param behaviour (@{@link Function}) to be associated with the specified key as behaviour
     * @return true, or false if the key was already associated to a behaviour.
     */
    public boolean featureWithBehaviour(
            String feature,
            Function<FeatureInvocation, ? extends Object> behaviour) {
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
    public boolean featureWithDefaultException(String feature) {
        return featureWithException(feature, defaultExceptionType);
    }



    /**
     * If the specified feature is not already associated with a behaviour(or is mapped to null),
     * associates it with the given exception and returns true, else returns false.
     *
     * Notice the exception type must have either an empty constructor or a contractor with only
     * one parameter, (@{@link FeatureInvocation})
     *
     * @see FeatureInvocation
     * @param feature feature with which the specified behaviour is to be associated
     * @param exceptionType exception type to be associated with the specified key
     * @return true, or false if the key was already associated to a behaviour.
     */
    public boolean featureWithException(
            String feature,
            Class<? extends RuntimeException> exceptionType) {

        Util.checkArgument(exceptionType, "exceptionType");
        final RuntimeException ex;
        try {
            ex = exceptionType.getConstructor().newInstance();
        } catch (Exception e) {
            throw new DimmerConfigException(e);
        }

        return putBehaviour(feature, signature -> {
            throw ex;
        });
    }

    /**
     * If the specified feature is not already associated with a behaviour(or is mapped to null),
     * associates it with the given value and returns true, else returns false.
     *
     * Notice that the value must be compatibility with the real method's returning type
     * or a (@{@link DimmerConfigException}) will be thrown.
     *
     * @param feature feature with which the specified behaviour is to be associated
     * @param valueToReturn value to be associated with the specified key
     * @return true, or false if the key was already associated to a behaviour.
     */
    public boolean featureWithValue(String feature, Object valueToReturn) {

        return putBehaviour(feature, signature -> valueToReturn);
    }

    private boolean putBehaviour(String featureId,
                                 Function<FeatureInvocation, ? extends Object> behaviour) {
        Util.checkArgument(featureId, "featureId");
        if (ALWAYS_OFF.equals(featureId)) {
            throw new IllegalArgumentException(
                    String.format("Value %s for feature not allowed", ALWAYS_OFF)
            );
        }
        return behaviours.putIfAbsent(featureId, behaviour) == null;
    }

    Object executeDimmerFeature(
            DimmerFeature dimmerFeature,
            FeatureInvocation featureInvocation,
            ProceedingJoinPoint realMethod) throws Throwable {

        if (dimmerFeature.runRealMethod()) {
            return realMethod.proceed();
        } else {
            switch (dimmerFeature.value()) {
                case ALWAYS_OFF:
                    return processAlwaysOff(dimmerFeature);
                default:
                    return processFeature(dimmerFeature, featureInvocation, realMethod);
            }
        }

    }

    private Object processAlwaysOff(DimmerFeature dimmerFeature) throws Exception {
        switch (dimmerFeature.behaviour()) {
            case RETURN_NULL:
                return null;
            case THROW_EXCEPTION:
            case DEFAULT:
            default:
                return throwException(dimmerFeature.exception());
        }
    }

    private Object processFeature(DimmerFeature dimmerFeature,
                                  FeatureInvocation featureInvocation,
                                  ProceedingJoinPoint realMethod) throws Throwable {
        final String feature = dimmerFeature.value();
        if (behaviours.containsKey(feature)) {
            switch (dimmerFeature.behaviour()) {
                case RETURN_NULL:
                    return null;
                case THROW_EXCEPTION:
                    throwException(dimmerFeature.exception());
                case DEFAULT:
                default:
                    return behaviours.get(feature).apply(featureInvocation);
            }
        } else {
            return realMethod.proceed();
        }
    }

    private Object throwException(Class<? extends RuntimeException> exceptionTypeFromAn) {
        try {
            Class<? extends RuntimeException> exType =
                    exceptionTypeFromAn != DimmerFeature.NULL_EXCEPTION.class
                            ? exceptionTypeFromAn
                            : defaultExceptionType;
            throw exType.getConstructor().newInstance();
        } catch (InstantiationException |
                IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Singleton builder to configure (@{@link DimmerProcessor}).
     *
     * Threadsafe
     */
    public static class SingletonBuilder {

        private DimmerProcessor instance;

        private Class<? extends RuntimeException> defaultExceptionType =
                DimmerInvocationException.class;

        private SingletonBuilder() {
        }

        /**
         * Set the default exception type to be thrown as behaviour.
         *
         * Notice the exception type must have either an empty constructor or a contractor with only
         * one parameter, (@{@link FeatureInvocation})
         *
         * @param newDefaultExceptionType new default exception type
         * @return Singleton DimmerProcessor builder
         */
        public SingletonBuilder setDefaultExceptionType(
                Class<? extends RuntimeException> newDefaultExceptionType) {
            Util.checkArgument(newDefaultExceptionType, "defaultExceptionType");
            this.defaultExceptionType = newDefaultExceptionType;
            return this;
        }

        /**
         * @return If the singleton DimmerProcessor instance has been already initialised
         */
        public boolean isInitialised() {
            return instance != null;
        }

        /**
         * If not initialised yet, it build a the singleton DimmerProcessor instance
         * with the configured parameters.
         * @return the DimmerProcessor instance.
         */
        public synchronized DimmerProcessor build() {
            if (!isInitialised()) {
                instance = new DimmerProcessor(defaultExceptionType);
                Aspects.aspectOf(DimmerAspect.class).setDimmerProcessor(instance);
            }
            return instance;
        }
    }

}