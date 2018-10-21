package com.github.cloudyrock.dimmer.reader.models.yaml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.Map;

@JsonRootName(value = "dimmer")
public class Dimmer {

    @JsonProperty(value = "environments")
    private Map<String, Environments> environments;

    public Map<String, Environments> getEnvironments() {
        return environments;
    }

    public void setEnvironments(Map<String, Environments> environments) {
        this.environments = environments;
    }
}

