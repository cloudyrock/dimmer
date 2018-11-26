package com.github.cloudyrock.dimmer;

import org.apache.commons.lang3.StringUtils;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

public class ConfigService {

    private final ConfigRepository repository;

    public ConfigService(ConfigRepository repository) {
        this.repository = repository;
    }

    public Set<String> getInterceptedFeaturesByEnvironment(String environment) {
        checkArgument(StringUtils.isNotEmpty(environment), "Environment cannot be empty");
        return repository.findFeaturesInterceptedByEnvironment(environment);
    }

}
