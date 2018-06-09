package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DefaultException;

public abstract class DimmerTestBase {
    protected static DimmerProcessor dimmerProcessor;

    static {
        dimmerProcessor = DimmerProcessor.builder()
                .setDefaultExceptionType(DefaultException.class)
                .build();
    }
}
