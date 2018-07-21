package com.github.cloudyrock.dimmer;

import org.aspectj.lang.Aspects;

public class SingletonBuilder {


    private DimmerProcessor instance;

    private Class<? extends RuntimeException> defaultExceptionType =
            DimmerInvocationException.class;

    SingletonBuilder() {
    }

    /**
     * Set the default exception type to be thrown as behaviour.
     * <p>
     * Notice the exception type must have either an empty constructor or a contractor with only
     * one parameter, (@{@link FeatureInvocation})
     *
     * @param newDefaultExceptionType new default exception type
     * @return Singleton DimmerProcessor builder
     */
    public SingletonBuilder setDefaultExceptionType(
            Class<? extends RuntimeException> newDefaultExceptionType) {
        Util.checkArgument(newDefaultExceptionType, "defaultExceptionType");
        this.defaultExceptionType = newDefaultExceptionType;
        return this;
    }

    /**
     * @return If the singleton DimmerProcessor instance has been already initialised
     */
    public boolean isInitialised() {
        return instance != null;
    }

    /**
     * If not initialised yet, it build a the singleton DimmerProcessor instance
     * with the configured parameters.
     *
     * @return the DimmerProcessor instance.
     */
    public synchronized DimmerProcessor build() {
        if (!isInitialised()) {
            instance = new DimmerProcessor(defaultExceptionType);
            Aspects.aspectOf(DimmerAspect.class).setDimmerProcessor(instance);
        }
        return instance;
    }
}
