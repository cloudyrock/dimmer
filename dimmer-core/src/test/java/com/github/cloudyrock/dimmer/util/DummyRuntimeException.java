package com.github.cloudyrock.dimmer.util;

public class DummyRuntimeException extends RuntimeException {

    public static final String MESSAGE = "DUMMY_MESSAGE";

    public DummyRuntimeException() {
        super(MESSAGE);
    }
}
