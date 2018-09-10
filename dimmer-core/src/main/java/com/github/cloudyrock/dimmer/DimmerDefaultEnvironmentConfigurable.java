package com.github.cloudyrock.dimmer;

/**
 * @param <CONFIGURABLE> Class to be returned.
 */
public interface DimmerDefaultEnvironmentConfigurable
        <CONFIGURABLE extends DimmerFeatureConfigurable<CONFIGURABLE>>
        extends DimmerEnvironmentConfigurable<CONFIGURABLE> {


    /**
     * Indicates the following set of configuration will be applied to the defaultEnvironment.
     * @return An instance of a DimmerFeatureConfigurable.
     */
    CONFIGURABLE defaultEnvironment();
}
