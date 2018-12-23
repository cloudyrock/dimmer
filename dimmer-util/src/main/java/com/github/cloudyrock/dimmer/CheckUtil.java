package com.github.cloudyrock.dimmer;

import java.util.Collection;

public final class CheckUtil {

    public static void checkNullEmpty(Object obj, String name) {
        if (obj == null || "".equals(obj) || (obj instanceof Collection && ((Collection) obj).isEmpty())) {
            throw new RuntimeException(String.format("Wrong argument: %s", name));
        }
    }
}
