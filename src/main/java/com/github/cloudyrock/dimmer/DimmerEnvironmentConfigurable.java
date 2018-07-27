package com.github.cloudyrock.dimmer;

public interface DimmerEnvironmentConfigurable<CONFIGURABLE extends DimmerFeatureConfigurable<CONFIGURABLE>> {

    CONFIGURABLE environments(String... environments);

}
