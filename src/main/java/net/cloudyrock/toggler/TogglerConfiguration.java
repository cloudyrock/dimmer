package net.cloudyrock.toggler;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class TogglerConfiguration {

    private static final Map<String, Function<FeatureInvocation, Object>> handlersMap =
            new ConcurrentHashMap<>();

    public static boolean toggleOffWithBehaviour(
            String featureId,
            Function<FeatureInvocation, Object> behaviour) {
        checkArgument(behaviour, "behaviour");
        return putBehaviour(featureId, behaviour);
    }

    public static boolean toggleOffWithDefaultException(String featureId) {
        return toggleOffWithException(featureId, TogglerInvocationException.class);
    }

    public static boolean toggleOffWithException(
            String featureId,
            Class<? extends RuntimeException> exceptionType) {

        checkArgument(exceptionType, "exceptionClass");
        final RuntimeException ex;
        try {
            ex = exceptionType.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                InvocationTargetException e) {
            throw new TogglerConfigurationException(e);
        }

        return putBehaviour(featureId, signature -> {
            throw ex;
        });
    }

    public static boolean toggleOffWithValue(String featureId, Object valueToReturn) {

        return putBehaviour(featureId, signature -> valueToReturn);
    }

    private static void checkArgument(Object arg, String argName) {
        if (Objects.isNull(arg)) {
            throw new IllegalArgumentException(
                    String.format("arg %s cannot be null or empty", argName));
        }
    }

    static boolean contains(String feature) {
        return handlersMap.containsKey(feature);
    }

    static Function<FeatureInvocation, Object> getBehaviour(String feature) {
        return handlersMap.get(feature);
    }

    private static boolean putBehaviour(String featureId,
                                        Function<FeatureInvocation, Object> handler) {
        checkArgument(featureId, "featureId");
        return handlersMap.putIfAbsent(featureId, handler) == null;
    }
}