package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DefaultException;
import org.aspectj.lang.Aspects;

public abstract class DimmerTestBase {
    protected static DimmerProcessor dimmerProcessor;

    static {
        dimmerProcessor = new DimmerProcessor(DefaultException.class);
        Aspects.aspectOf(DimmerAspect.class).setDimmerProcessor(dimmerProcessor);
    }
}
