package com.github.cloudyrock.dimmer;

import java.util.HashMap;

import static java.util.Arrays.asList;

public class DimmerBuilder {

    public static DimmerDefaultEnvironmentConfigurable<FeatureConfigurationBuilder> local() {

        return new DimmerDefaultEnvironmentConfigurable<FeatureConfigurationBuilder>() {
            @Override
            public FeatureConfigurationBuilder defaultEnvironment() {
                return FeatureConfigurationBuilder.withDefaultEnvironment();
            }

            @Override
            public FeatureConfigurationBuilder environments(String... environments) {
                return FeatureConfigurationBuilder
                        .withEnvironmentsAndMetadata(asList(environments), new HashMap<>());
            }
        };
    }

}
