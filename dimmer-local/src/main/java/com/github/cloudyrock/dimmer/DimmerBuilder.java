package com.github.cloudyrock.dimmer;

import java.util.Arrays;
import java.util.HashMap;

import static java.util.Arrays.asList;

public class DimmerBuilder {

    public static DimmerDefaultEnvironmentConfigurable<DimmerLocalRunner> local() {

        return new DimmerDefaultEnvironmentConfigurable<DimmerLocalRunner>() {
            @Override
            public DimmerLocalRunner defaultEnvironment() {
                return DimmerLocalRunner.withDefaultEnviroment();
            }

            @Override
            public DimmerLocalRunner environments(String... environments) {
                return DimmerLocalRunner
                        .withEnvsAndMetadata(asList(environments), new HashMap<>());
            }
        };
    }

    public static DimmerRemoteRunner remote(String url) {
        return new DimmerRemoteRunner(url);
    }
}
