package com.github.cloudyrock.dimmer;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public final class Preconditions {

    public static void isNullOrEmpty(Object obj) {
        isNullOrEmpty(obj, null);
    }

    public static void isNullOrEmpty(Object obj, String name) {
        if (Objects.isNull(obj) || "".equals(obj) ||
                (obj.getClass().isArray() && Array.getLength(obj) <= 0) ||
                (obj instanceof Collection && ((Collection) obj).isEmpty()) ||
                (obj instanceof Optional && !((Optional) obj).isPresent())) {
            throwException(name);
            return;
        }
    }

    private static void throwException(String name) {
        final String message = name == null || "".equals(name)
                ? String.format("Wrong argument: %s", name)
                : "Wrong parameter";
        throw new IllegalArgumentException(message);
    }
}