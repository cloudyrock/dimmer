package com.github.cloudyrock.dimmer.reader.models;

import java.util.ArrayList;
import java.util.List;

public final class EnvironmentConfig {

    private final String name;
    private final List<String> switchedOn;
    private final String server;
    private final boolean isDefault;

    public EnvironmentConfig (String name, String server, List<String> switchedOnFeatures, boolean isDefault){
        this.name = name;
        this.server = server;
        this.switchedOn = switchedOnFeatures;
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
    }

    public List<String> getSwitchedOn() {
        return new ArrayList<>(switchedOn);
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getServer() {
        return server;
    }
}
