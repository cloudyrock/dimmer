package com.github.cloudyrock.dimmer;

import java.util.HashMap;

import static java.util.Arrays.asList;

public class DimmerBuilder {

    public static DimmerDefaultEnvironmentConfigurable<FeatureLocalBuilder> local() {

        return new DimmerDefaultEnvironmentConfigurable<FeatureLocalBuilder>() {
            @Override
            public FeatureLocalBuilder defaultEnvironment() {
                return FeatureLocalBuilder.withDefaultEnvironment();
            }

            @Override
            public FeatureLocalBuilder environments(String... environments) {
                return FeatureLocalBuilder
                        .withEnvironmentsAndMetadata(asList(environments), new HashMap<>());
            }
        };
    }

    public static FeatureRemoteBuilder remote(String url) {
        return new FeatureRemoteBuilder(url);
    }
}
