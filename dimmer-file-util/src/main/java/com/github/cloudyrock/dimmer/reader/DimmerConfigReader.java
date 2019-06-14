package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.reader.models.EnvironmentConfig;

public interface DimmerConfigReader {

    EnvironmentConfig loadEnvironmentOrDefault(String environment);
    void setPropertiesLocation(String propertiesLocation);

    default DimmerConfigReader getDefaultImplementation() {
        return null;
    }
}
