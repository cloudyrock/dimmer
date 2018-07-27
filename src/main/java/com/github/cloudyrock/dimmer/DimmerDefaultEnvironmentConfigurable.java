package com.github.cloudyrock.dimmer;

public interface DimmerDefaultEnvironmentConfigurable
        <CONFIGURABLE extends DimmerFeatureConfigurable<CONFIGURABLE>>
        extends DimmerEnvironmentConfigurable<CONFIGURABLE> {


    CONFIGURABLE defaultEnvironment();
}
