package com.github.cloudyrock.dimmer;

import java.util.*;

public class FileConfigRepositoryImpl implements ConfigRepository {

    private final Map<String, Set<String>> configByEnv;

    public FileConfigRepositoryImpl(Map<String, Set<String>> configByEnv) {
        this.configByEnv = configByEnv;
    }

    @Override
    public Set<String> findFeaturesInterceptedByEnvironment(String environment) {
        return configByEnv.getOrDefault(environment, Collections.emptySet());
    }
}
