package com.github.cloudyrock.dimmer;

import java.util.Objects;

/**
 *
 * @author Antonio Perez Dieppa
 * @since 11/06/2018
 */
public final class Util {

    static void checkArgument(Object arg, String argName) {
        if (Objects.isNull(arg)) {
            throw new IllegalArgumentException(
                    String.format("arg %s cannot be null or empty", argName));
        }
    }
}
