package com.github.cloudyrock.dimmer;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

//TODO: This implementation is static. Makes sense right now but may not be as convenient
//TODO when injecting dependencies, if needed
public class DimmerConfiguration {

    private static final Map<String, Function<FeatureInvocation, Object>> behavioursMap =
            new ConcurrentHashMap<>();

    //TODO: Java doc to specify whatever the function returns, must be compatible with the real method
    public static boolean featureOffWithBehaviour(
            String featureId,
            Function<FeatureInvocation, Object> behaviour) {
        checkArgument(behaviour, "behaviour");
        return putBehaviour(featureId, behaviour);
    }

    public static boolean featureOffWithDefaultException(String featureId) {
        return featureOffWithException(featureId, DimmerInvocationException.class);
    }

    public static boolean featureOffWithException(
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
    public static boolean featureOffWithValue(String featureId, Object valueToReturn) {

        return putBehaviour(featureId, signature -> valueToReturn);
    }

    private static void checkArgument(Object arg, String argName) {
        if (Objects.isNull(arg)) {
            throw new IllegalArgumentException(
                    String.format("arg %s cannot be null or empty", argName));
        }
    }

    static boolean contains(String feature) {
        return behavioursMap.containsKey(feature);
    }

    static Function<FeatureInvocation, Object> getBehaviour(String feature) {
        return behavioursMap.get(feature);
    }

    private static boolean putBehaviour(String featureId,
                                        Function<FeatureInvocation, Object> behaviour) {
        checkArgument(featureId, "featureId");
        return behavioursMap.putIfAbsent(featureId, behaviour) == null;
    }
}