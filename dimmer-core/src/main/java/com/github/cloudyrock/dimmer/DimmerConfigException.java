package com.github.cloudyrock.dimmer;

/**
 * If thrown indicates an error when configuring feature behaviours.
 */
public class DimmerConfigException extends RuntimeException {
    DimmerConfigException(Exception e) {
        super(e);
    }

    DimmerConfigException(String message) {
        super(message);
    }
}
