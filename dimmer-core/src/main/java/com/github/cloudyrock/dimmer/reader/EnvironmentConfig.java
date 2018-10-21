package com.github.cloudyrock.dimmer.reader;

import java.util.ArrayList;

public class EnvironmentConfig {

    private ArrayList<String> featureIntercept;
    private String server;

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
