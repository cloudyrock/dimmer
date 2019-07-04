package com.github.cloudyrock.dimmer;

class DummyRuntimeException extends RuntimeException {

    static final String MESSAGE = "DUMMY_MESSAGE";

    DummyRuntimeException() {
        super(MESSAGE);
    }
}
