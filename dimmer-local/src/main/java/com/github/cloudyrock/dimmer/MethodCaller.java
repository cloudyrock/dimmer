package com.github.cloudyrock.dimmer;

@FunctionalInterface
public interface MethodCaller {

    Object call() throws Throwable;
}
