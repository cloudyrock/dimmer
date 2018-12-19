package com.github.cloudyrock.dimmer;

/**
 * @param <FEATURE_CONFIGURABLE> Class to be returned.
 */
public interface DimmerDefaultEnvironmentConfigurable
        <FEATURE_CONFIGURABLE extends DimmerFeatureConfigurable<FEATURE_CONFIGURABLE>>{

    /**
     * Indicates the following set of configuration will be applied to the selected list of environments.
     * @return An instance of a DimmerFeatureConfigurable.
     */
    FEATURE_CONFIGURABLE environments(String... environments);
}
