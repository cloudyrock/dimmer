package com.github.cloudyrock.dimmer;

final class DimmerRemoteRunner {

    private final String url;
    private boolean alreadyRun = false;

    public DimmerRemoteRunner(String url) {
        this.url = url;
    }

    public synchronized void run(String environment) {
        if(!alreadyRun) {
            alreadyRun = true;
            //TODO aspect for remote toggling
        }
    }
}
