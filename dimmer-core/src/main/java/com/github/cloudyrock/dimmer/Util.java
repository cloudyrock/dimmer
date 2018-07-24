package com.github.cloudyrock.dimmer;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Antonio Perez Dieppa
 * @since 11/06/2018
 */
final class Util {
    static void checkArgumentNullEmpty(Object arg, String argName) {
        if (Objects.isNull(arg)) {
            throw new IllegalArgumentException(
                    String.format("arg %s cannot be null", argName));
        }

        if ((arg.getClass().isArray() && Array.getLength(arg) <= 0)
                || (arg instanceof Collection && ((Collection) arg).isEmpty())) {
            throw new IllegalArgumentException(
                    String.format("arg %s cannot be empty", argName));
        }
    }

}
