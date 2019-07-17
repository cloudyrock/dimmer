package com.github.cloudyrock.dimmer.reader.models;

import java.util.ArrayList;
import java.util.List;

public final class EnvironmentConfig {

    private final String name;
    private final List<String> toggledOn;
    private final String server;
    private final boolean isDefault;

    public EnvironmentConfig (String name, String server, List<String> toggledOnFeatures, boolean isDefault){
        this.name = name;
        this.server = server;
        this.toggledOn = toggledOnFeatures;
        this.isDefault = isDefault;
    }

    public String getName() {
        return name;
    }

    public List<String> getToggledOn() {
        return toggledOn != null ? new ArrayList<>(toggledOn) : new ArrayList<>();
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getServer() {
        return server;
    }
}
