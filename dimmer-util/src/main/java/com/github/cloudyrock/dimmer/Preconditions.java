package com.github.cloudyrock.dimmer;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class Preconditions {

    public static void checkNullOrEmpty(Object obj) {
        checkNullOrEmpty(obj, null);
    }

    public static void checkNullOrEmpty(Object obj, String name) {
        if (isNullOrEmpty(obj)) {
            throwException(name);
        }
    }

    public static boolean isNullOrEmpty(Object obj) {
        return (Objects.isNull(obj) || "".equals(obj) ||
                (obj.getClass().isArray() && Array.getLength(obj) <= 0) ||
                (obj instanceof Collection && ((Collection) obj).isEmpty()) ||
                (obj instanceof Optional && !((Optional) obj).isPresent()) ||
                (obj instanceof Map && ((Map) obj).isEmpty()));
    }


    public static boolean notEmpty(Object obj) {
        return !isNullOrEmpty(obj);
    }

    private static void throwException(String name) {
        final String message = name == null || "".equals(name)
                ? String.format("Wrong argument: %s", name)
                : "Wrong parameter";
        throw new IllegalArgumentException(message);
    }
}