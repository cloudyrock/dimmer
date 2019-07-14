package com.github.cloudyrock.dimmer.reader.models.yaml;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Environment {

    @JsonProperty(value = "server", required = false)
    private String server;

    @JsonProperty(value = "toggledOn", required = false)
    private List<String> toggledOn;

    @JsonProperty(value = "default", required = false)
    private boolean isDefault;

    public List<String> getToggledOn() {
        return toggledOn;
    }

    public void setToggledOn(ArrayList<String> toggledOn) {
        this.toggledOn = toggledOn;
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
