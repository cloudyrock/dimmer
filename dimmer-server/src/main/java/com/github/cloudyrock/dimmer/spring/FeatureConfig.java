package com.github.cloudyrock.dimmer.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

@Configuration
@ConfigurationProperties(prefix = "dimmer.features.config")
public class FeatureConfig {

    private Map<String, Set<String>> environments;


    public Map<String, Set<String>> getEnvironments() {
        return environments;
    }

    public void setEnvironments(Map<String, Set<String>> environments) {
        this.environments = environments;
    }

}
