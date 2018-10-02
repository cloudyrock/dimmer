package com.github.cloudyrock.dimmer;

/**
 * @param <FEATURE_CONFIGURABLE> Class to be returned.
 */
public interface DimmerDefaultEnvironmentConfigurable
        <FEATURE_CONFIGURABLE extends DimmerFeatureConfigurable<FEATURE_CONFIGURABLE>>
        extends DimmerEnvironmentConfigurable<FEATURE_CONFIGURABLE> {


    /**
     * Indicates the following set of configuration will be applied to the defaultEnvironment.
     * @return An instance of a DimmerFeatureConfigurable.
     */
    FEATURE_CONFIGURABLE defaultEnvironment();
}
