package com.github.cloudyrock.dimmer;

import com.google.common.base.Preconditions;

import java.util.*;

public class FileConfigRepositoryImpl implements ConfigRepository {

    private final Map<String, Set<String>> configByEnv;

    public FileConfigRepositoryImpl(Map<String, Set<String>> configByEnv) {
        Preconditions.checkArgument(configByEnv != null);
        this.configByEnv = configByEnv;
    }

    @Override
    public Set<String> findFeaturesInterceptedByEnvironment(String environment) {
        return configByEnv.getOrDefault(environment, Collections.emptySet());
    }
}
