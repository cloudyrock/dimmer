package com.github.cloudyrock.dimmer;

import java.util.Map;

public abstract class DimmerProcessorBuilderBase {

    Map<String, FeatureMetadata> configMetadata;

    protected DimmerProcessor instance;

    protected Class<? extends RuntimeException> defaultExceptionType =
            DimmerInvocationException.class;


    /**
     * Set the default exception type to be thrown as behaviour.
     * <p>
     * Notice the exception type must have either an empty constructor or a contractor with only
     * one parameter, (@{@link FeatureInvocation})
     *
     * @param newDefaultExceptionType new default exception type
     * @return Singleton DimmerProcessor builder
     */
    public DimmerProcessorBuilderBase setDefaultExceptionType(
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
     * TODO change doc
     * If not initialised yet, it builds a the singleton DimmerProcessor instance
     * with the configured parameters.
     *
     * @return the DimmerProcessor instance.
     */
    public abstract  DimmerProcessor build();
}
