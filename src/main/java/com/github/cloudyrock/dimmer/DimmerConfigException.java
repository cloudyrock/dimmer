package com.github.cloudyrock.dimmer;

public class DimmerConfigException extends RuntimeException{
    public DimmerConfigException(ReflectiveOperationException e) {
        super(e);
    }
}
