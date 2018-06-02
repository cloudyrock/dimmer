package com.github.cloudyrock.dimmer;

import org.aspectj.lang.Aspects;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class DimmerProcessor {

    private static final SingletonBuilder builder = new SingletonBuilder();

    private final Class<? extends RuntimeException> defaultExceptionType;

    private final Map<String, Function<FeatureInvocation, ? extends Object>> behaviours =
            new ConcurrentHashMap<>();

    public final static SingletonBuilder builder() {
        return builder;
    }

    private DimmerProcessor(Class<? extends RuntimeException> defaultExceptionType) {
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

        Util.checkArgument(exceptionType, "exceptionType");
        final RuntimeException ex;
        try {
            ex = exceptionType.getConstructor().newInstance();
        } catch (Exception e) {
            throw new DimmerConfigException(e);
        }

        return putBehaviour(featureId, signature -> {
            throw ex;
        });
    }

    //TODO: Java doc to specify whatever the value's type is, must be compatible with the real method
    public boolean featureWithValue(String featureId, Object valueToReturn) {

        return putBehaviour(featureId, signature -> valueToReturn);
    }

    private boolean putBehaviour(String featureId,
                                 Function<FeatureInvocation, ? extends Object> behaviour) {
        Util.checkArgument(featureId, "featureId");
        return behaviours.putIfAbsent(featureId, behaviour) == null;
    }

    Object runBehaviourIfExistsOrReaInvocation(String feature,
                                               FeatureInvocation featureInvocation,
                                               ProceedingJoinPoint realMethod) throws Throwable {
        if (behaviours.containsKey(feature)) {
            final Function<FeatureInvocation, ? extends Object> function =
                    behaviours.get(feature);
            Object r = function.apply(featureInvocation);
            return r;
        } else {
            return realMethod.proceed();
        }
    }

    Object runFeatureOff(FeatureOffBehaviour featureOffBehaviour,
                         Class<? extends RuntimeException> exceptionType) throws Throwable {
        switch (featureOffBehaviour) {
            case RETURN_NULL:
                return null;

            case THROW_EXCEPTION:
            default:
                throw (exceptionType.equals(NULL_EXCEPTION.class)
                        ? defaultExceptionType
                        : exceptionType).getConstructor().newInstance();
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
            return getInstance() != null;
        }

        public DimmerProcessor getInstance() {
            return instance;
        }

        public synchronized DimmerProcessor build() {
            if (!isInitialised()) {
                instance = new DimmerProcessor(defaultExceptionType);
                Aspects.aspectOf(DimmerAspect.class).setDimmerProcessor(instance);
            }
            return instance;
        }
    }

    static class NULL_EXCEPTION extends RuntimeException {
    }
}