package com.github.cloudyrock.dimmer.reader.models;

import java.util.ArrayList;
import java.util.List;

public final class EnvironmentConfig {

    private final String name;
    private final List<String> featureIntercept;
    private final String server;
    private final boolean isDefault;

    public EnvironmentConfig (String name, String server, List<String> featureIntercept, boolean isDefault){
        this.name = name;
        this.server = server;
        this.featureIntercept = featureIntercept;
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
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
