package com.github.cloudyrock.dimmer.reader;

import java.util.Map;

public class DimmerConfig {

    private Map<String, EnvironmentConfig> environmentsConfig;

    public Map<String, EnvironmentConfig> getEnvironments() {
        return environmentsConfig;
    }

    public void setEnvironments(Map<String, EnvironmentConfig> environments) {
        this.environmentsConfig = environments;
    }
}
