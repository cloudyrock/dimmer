package com.github.cloudyrock.dimmer.reader;

import java.util.List;

public class EnvironmentConfig {

    private List<String> featureIntercept;
    private String server;
    private boolean isDefault;

    public EnvironmentConfig (String server, List featureIntercept, boolean isDefault){
        this.server = server;
        this.featureIntercept = featureIntercept;
        this.isDefault = isDefault;
    }

    public EnvironmentConfig (String server){
        this.server = server;
    }

    public EnvironmentConfig (List features){
        this.featureIntercept = features;
    }

    public List<String> getFeatureIntercept() {
        return featureIntercept;
    }

    public void setFeatureIntercept(List<String> featureIntercept) {
        this.featureIntercept = featureIntercept;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
