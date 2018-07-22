package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DefaultException;

public abstract class DimmerTestBase {
    protected static DimmerProcessor dimmerProcessor;

    static {
        dimmerProcessor = DimmerBuilder
                .configure()
                .environments("TEST")
                .setDefaultExceptionType(DefaultException.class)
                .runAsLocal("TEST");
    }
}
