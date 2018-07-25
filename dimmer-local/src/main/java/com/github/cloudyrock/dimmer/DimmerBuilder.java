package com.github.cloudyrock.dimmer;

import java.util.HashMap;

import static java.util.Arrays.asList;

public class DimmerBuilder {

    public static DimmerDefaultEnvironmentConfigurable<DimmerLocalRunner> local() {

        return new DimmerDefaultEnvironmentConfigurable<DimmerLocalRunner>() {
            @Override
            public DimmerLocalRunner defaultEnvironment() {
                return DimmerLocalRunner.withDefaultEnvironment();
            }

            @Override
            public DimmerLocalRunner environments(String... environments) {
                return DimmerLocalRunner
                        .withEnvironmentsAndMetadata(asList(environments), new HashMap<>());
            }
        };
    }

    public static DimmerRemoteRunner remote(String url) {
        return new DimmerRemoteRunner(url);
    }
}
