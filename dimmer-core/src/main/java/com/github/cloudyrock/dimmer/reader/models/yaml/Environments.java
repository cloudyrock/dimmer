package com.github.cloudyrock.dimmer.reader.models.yaml;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Environments {

    @JsonProperty(value = "server", required = false)
    private String server;

    @JsonProperty(value = "featureIntercept", required = false)
    private ArrayList<String> featureIntercept;

    public ArrayList<String> getFeatureIntercept() {
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
}
