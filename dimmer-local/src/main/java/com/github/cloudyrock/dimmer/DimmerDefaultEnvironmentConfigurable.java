package com.github.cloudyrock.dimmer;

public interface DimmerDefaultEnvironmentConfigurable<CONFIGURABLE extends DimmerConfigurableRunner<CONFIGURABLE>>
        extends DimmerEnvironmentConfigurable<CONFIGURABLE> {


    CONFIGURABLE defaultEnvironment();
}
