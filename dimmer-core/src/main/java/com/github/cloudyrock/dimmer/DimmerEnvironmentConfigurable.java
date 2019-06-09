package com.github.cloudyrock.dimmer;

/**
 * @param <CONFIGURABLE> Class to be returned.
 */
public interface DimmerEnvironmentConfigurable<CONFIGURABLE extends DimmerFeatureConfigurable> {

    /**
     * Indicates the list of environments for which the following set of configuration will be applied to
     * @param environments List of environments
     * @return An instance of a DimmerFeatureConfigurable.
     */
    CONFIGURABLE environments(String... environments);

}
