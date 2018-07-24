package com.github.cloudyrock.dimmer;

public interface DimmerEnvironmentConfigurable<CONFIGURABLE extends DimmerConfigurableRunner<CONFIGURABLE>> {

    CONFIGURABLE environments(String... environments);

}
