package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DefaultException;

public abstract class DimmerTestBase {
    protected static DimmerProcessor dimmerProcessor;

    static {



        DimmerBuilder.local()
                .environments("production")
                .featureWithValue("FEATURE1", "value1")
                .featureWithValue("FEATURE2", "value2")
                .environments("dev", "staging")
                .featureWithValue("FEATURE1", "value1_1")
                .build();

        dimmerProcessor = DimmerBuilder.local()
                .setDefaultExceptionType(DefaultException.class)
                .build();
    }
}
