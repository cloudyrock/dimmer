package com.github.cloudyrock.dimmer;

/**
 * If thrown indicates an error when configuring feature behaviours.
 *
 * @author Antonio Perez Dieppa
 * @since 11/06/2018
 */
public class DimmerConfigException extends RuntimeException {
    public DimmerConfigException(Exception e) {
        super(e);
    }

    public DimmerConfigException(String message) {
        super(message);
    }
}
