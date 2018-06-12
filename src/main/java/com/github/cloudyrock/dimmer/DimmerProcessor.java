package com.github.cloudyrock.dimmer;

import org.aspectj.lang.Aspects;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static com.github.cloudyrock.dimmer.DimmerFeature.ALWAYS_OFF;

public class DimmerProcessor {

    private static final SingletonBuilder builder = new SingletonBuilder();

    private final Class<? extends RuntimeException> defaultExceptionType;

    private final Map<String, Function<FeatureInvocation, ? extends Object>> behaviours =
            new ConcurrentHashMap<>();

    public final static SingletonBuilder builder() {
        return builder;
    }

    private DimmerProcessor(Class<? extends RuntimeException> defaultExceptionType) {
        ExceptionUtil.checkAndGetExceptionConstructorType(defaultExceptionType);
        this.defaultExceptionType = defaultExceptionType;
    }

    //TODO: Java doc to specify whatever the function returns, must be compatible with the real method
    public boolean featureWithBehaviour(
            String featureId,
            Function<FeatureInvocation, ? extends Object> behaviour) {
        Util.checkArgument(behaviour, "behaviour");
        return putBehaviour(featureId, behaviour);
    }

    public boolean featureWithDefaultException(String featureId) {
        return featureWithException(featureId, defaultExceptionType);
    }

    //exceptionType must have an empty constructor
    public boolean featureWithException(
            String featureId,
            Class<? extends RuntimeException> exceptionType) {

        final ExceptionConstructorType constructorType =
                ExceptionUtil.checkAndGetExceptionConstructorType(exceptionType);
        return putBehaviour(featureId,
                f -> ExceptionUtil.throwException(exceptionType, constructorType, f));
    }

    //TODO: Java doc to specify whatever the value's type is, must be compatible with the real method
    public boolean featureWithValue(String featureId, Object valueToReturn) {
        return putBehaviour(featureId, signature -> valueToReturn);
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
                    return processAlwaysOff(dimmerFeature, featureInvocation);
                default:
                    return processFeature(dimmerFeature, featureInvocation, realMethod);
            }
        }

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

    private Object processAlwaysOff(DimmerFeature dimmerFeature,
                                    FeatureInvocation featureInvocation) throws Exception {
        switch (dimmerFeature.behaviour()) {
            case RETURN_NULL:
                return null;
            case THROW_EXCEPTION:
            case DEFAULT:
            default:
                return ExceptionUtil.checkAndThrowException(
                        dimmerFeature.exception(),
                        defaultExceptionType,
                        featureInvocation);
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
                    ExceptionUtil.checkAndThrowException(
                            dimmerFeature.exception(),
                            defaultExceptionType,
                            featureInvocation);
                case DEFAULT:
                default:
                    return behaviours.get(feature).apply(featureInvocation);
            }
        } else {
            return realMethod.proceed();
        }
    }

    /**
     * Extra mechanism to implement the singleton pattern.
     * This "kind of builder" is to be able to add properties that won't be changeable post-initialization.
     * Although doesn't need to be a inner class(could just be static methods), this way helps to
     * encapsulate the initialization.
     */
    public static class SingletonBuilder {

        private DimmerProcessor instance;

        private Class<? extends RuntimeException> defaultExceptionType =
                DimmerInvocationException.class;

        private SingletonBuilder() {
        }

        public SingletonBuilder setDefaultExceptionType(
                Class<? extends RuntimeException> newDefaultExceptionType) {
            Util.checkArgument(newDefaultExceptionType, "defaultExceptionType");
            this.defaultExceptionType = newDefaultExceptionType;
            return this;
        }

        public boolean isInitialised() {
            return instance != null;
        }

        public synchronized DimmerProcessor build() {
            if (!isInitialised()) {
                instance = new DimmerProcessor(defaultExceptionType);
                Aspects.aspectOf(DimmerAspect.class).setDimmerProcessor(instance);
            }
            return instance;
        }
    }

}