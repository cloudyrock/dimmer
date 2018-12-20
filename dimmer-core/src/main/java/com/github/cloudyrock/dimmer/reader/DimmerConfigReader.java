package com.github.cloudyrock.dimmer.reader;

import com.github.cloudyrock.dimmer.DimmerConfigException;
import com.github.cloudyrock.dimmer.reader.models.DimmerConfig;
import com.github.cloudyrock.dimmer.reader.models.EnvironmentConfig;

import java.util.Map;

public interface DimmerConfigReader {
    DimmerConfig loadConfiguration() throws DimmerConfigException;
    Map.Entry<String, EnvironmentConfig> getDefaultEnvironment(DimmerConfig dimmerConfig);
    void setPropertiesLocation(String propertiesLocation);
}
