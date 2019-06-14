package com.github.cloudyrock.dimmer.reader.models.yaml;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Environment {

    @JsonProperty(value = "server", required = false)
    private String server;

    @JsonProperty(value = "featureIntercept", required = false)
    private List<String> featureIntercept;

    @JsonProperty(value = "default", required = false)
    private boolean isDefault;

    public List<String> getFeatureIntercept() {
        return featureIntercept;
    }

    public void setFeatureIntercept(ArrayList<String> featureIntercept) {
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
