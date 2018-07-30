package com.github.cloudyrock.dimmer;

public final class FeatureRemoteBuilder {

    private final String url;
    private boolean alreadyRun = false;

    public FeatureRemoteBuilder(String url) {
        this.url = url;
    }

    public synchronized void buildAndRun(String environment) {
        if(!alreadyRun) {
            alreadyRun = true;
            //TODO
        }
    }
}
