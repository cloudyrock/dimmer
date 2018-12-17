package com.github.cloudyrock.dimmer.reader;

import com.github.cloudyrock.dimmer.DimmerConfigException;
import com.github.cloudyrock.dimmer.reader.models.DimmerConfig;
import com.github.cloudyrock.dimmer.reader.models.EnvironmentConfig;

public interface DimmerConfigReader {
    DimmerConfig loadConfiguration() throws DimmerConfigException;
    EnvironmentConfig getDefaultEnvironment(DimmerConfig dimmerConfig);
}
