package com.github.cloudyrock.dimmer.reader;

import com.github.cloudyrock.dimmer.reader.models.DimmerConfig;
import com.github.cloudyrock.dimmer.reader.models.EnvironmentConfig;

public interface DimmerConfigReader {

    @Deprecated
    EnvironmentConfig getEnvironment(DimmerConfig dimmerConfig, String environment);
    @Deprecated
    EnvironmentConfig getDefaultEnvironment(DimmerConfig dimmerConfig);
    EnvironmentConfig loadEnvironmentOrDefault(String environment);
    void setPropertiesLocation(String propertiesLocation);
}
