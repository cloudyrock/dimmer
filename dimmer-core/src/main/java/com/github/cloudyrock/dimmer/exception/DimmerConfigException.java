package com.github.cloudyrock.dimmer.exception;

/**
 * If thrown indicates an error when configuring feature behaviours.
 */
public class DimmerConfigException extends RuntimeException {
    public DimmerConfigException(Exception e) {
        super(e);
    }

    public DimmerConfigException(String message) {
        super(message);
    }

    public DimmerConfigException(String message, Exception e) {
        super(message, e);
    }
}
