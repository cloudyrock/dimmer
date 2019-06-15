package com.github.cloudyrock.dimmer;

public class FileConfigException extends RuntimeException {

    public FileConfigException(String message, Exception e) {
        super(message, e);
    }

    public FileConfigException(String message) {
        super(message);
    }


}
