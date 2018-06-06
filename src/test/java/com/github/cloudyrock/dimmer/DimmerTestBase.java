package com.github.cloudyrock.dimmer;

public abstract class DimmerTestBase {
    protected static DimmerConfiguration dimmerConfiguration;

    static {
        dimmerConfiguration = DimmerConfiguration.singletonBuilder.build();
    }
}
