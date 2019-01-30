package com.github.cloudyrock.dimmer.reader.models;

import java.util.ArrayList;
import java.util.List;

public final class EnvironmentConfig {

    private final List<String> featureIntercept;
    private final String server;
    private final boolean isDefault;

    public EnvironmentConfig(String server, List<String> featureIntercept, boolean isDefault){
        this.server = server;
        this.featureIntercept = featureIntercept;
        this.isDefault = isDefault;
    }

    public List<String> getFeatureIntercept() {
        return new ArrayList<>(featureIntercept);
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getServer() {
        return server;
    }
}
