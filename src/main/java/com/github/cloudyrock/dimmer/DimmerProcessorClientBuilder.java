package com.github.cloudyrock.dimmer;

import org.aspectj.lang.Aspects;

public class DimmerProcessorClientBuilder extends DimmerProcessorBuilderBase {

    public synchronized DimmerProcessor build() {
        if (!isInitialised()) {
            instance = new DimmerProcessor(defaultExceptionType);
            Aspects.aspectOf(DimmerAspect.class).setDimmerProcessor(instance);
        }
        return instance;
    }
}
