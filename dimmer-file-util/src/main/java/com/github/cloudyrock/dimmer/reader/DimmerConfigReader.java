package com.github.cloudyrock.dimmer.reader;

import com.github.cloudyrock.dimmer.FileConfigException;
import com.github.cloudyrock.dimmer.reader.models.EnvironmentConfig;

public interface DimmerConfigReader {

    EnvironmentConfig loadEnvironmentOrDefault(String environment) throws FileConfigException;

    void setPropertiesLocation(String propertiesLocation);
}
