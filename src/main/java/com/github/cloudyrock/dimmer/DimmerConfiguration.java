package com.github.cloudyrock.dimmer;

import org.aspectj.lang.Aspects;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class DimmerConfiguration {

    private DimmerConfiguration() {
    }

    private final Map<String, Function<FeatureInvocation, Object>> behavioursMap =
            new ConcurrentHashMap<>();

    //TODO: Java doc to specify whatever the function returns, must be compatible with the real method
    public boolean featureOffWithBehaviour(
            String featureId,
            Function<FeatureInvocation, Object> behaviour) {
        checkArgument(behaviour, "behaviour");
        return putBehaviour(featureId, behaviour);
    }

    public boolean featureOffWithDefaultException(String featureId) {
        return featureOffWithException(featureId, DimmerInvocationException.class);
    }

    //exceptionType must have an empty constructor
    public boolean featureOffWithException(
            String featureId,
            Class<? extends RuntimeException> exceptionType) {

        checkArgument(exceptionType, "exceptionClass");
        final RuntimeException ex;
        try {
            ex = exceptionType.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                InvocationTargetException e) {
            throw new DimmerConfigException(e);
        }

        return putBehaviour(featureId, signature -> {
            throw ex;
        });
    }

    //TODO: Java doc to specify whatever the value's type is, must be compatible with the real method
    public boolean featureOffWithValue(String featureId, Object valueToReturn) {

        return putBehaviour(featureId, signature -> valueToReturn);
    }

    private static void checkArgument(Object arg, String argName) {
        if (Objects.isNull(arg)) {
            throw new IllegalArgumentException(
                    String.format("arg %s cannot be null or empty", argName));
        }
    }

    boolean contains(String feature) {
        return behavioursMap.containsKey(feature);
    }

    Function<FeatureInvocation, Object> getBehaviour(String feature) {
        return behavioursMap.get(feature);
    }

    private boolean putBehaviour(String featureId,
                                 Function<FeatureInvocation, Object> behaviour) {
        checkArgument(featureId, "featureId");
        return behavioursMap.putIfAbsent(featureId, behaviour) == null;
    }

    /**
     * Extra mechanism to implement the singleton pattern.
     * This "kind of builder" is to be able to add properties that won't be changeable post-initialization.
     * Although doesn't need to be a inner class(could just be static methods), this way helps to
     * encapsulate the initialization.
     */
    public static class singletonBuilder {

        private static volatile DimmerConfiguration instance;

        public static boolean isInitialised() {
            return instance != null;
        }

        public static DimmerConfiguration getInstance() {
            return instance;
        }

        public static synchronized DimmerConfiguration build() {
            if (instance == null) {
                instance = new DimmerConfiguration();
                Aspects.aspectOf(DimmerAspect.class).setConfiguration(instance);
            }
            return instance;
        }
    }
}