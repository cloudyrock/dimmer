package com.github.cloudyrock.dimmer;

import java.util.HashMap;

import static java.util.Arrays.asList;

/**
 * Builder to configure the initial environment.
 * This forces to select a environment before starting to add configuration.
 *
 * @since 11/06/2018
 */
public class DimmerBuilder {

    /**
     * @return A local version of the Dimmer builder, so it gets the entire configuration
     * form the current application.
     */
    public static DimmerDefaultEnvironmentConfigurable<FeatureConfigurationBuilder> local() {

        return new DimmerDefaultEnvironmentConfigurable<FeatureConfigurationBuilder>() {
            /**
             * Indicates the following configurations for dimmer feature will be applied
             * to the default environment, until the environment is changed with
             * the method 'environments(String...)'
             * @return a new immutable instance of the builder with the current configuration.
             */
            @Override
            public FeatureConfigurationBuilder defaultEnvironment() {
                return FeatureConfigurationBuilder.withDefaultEnvironment();
            }

            /**
             * Indicates the following configurations for dimmer feature will be applied
             * to the given environment, until the environment is changed with
             * the method 'environments(String...)' or 'defaultEnvironment()'
             * @return a new immutable instance of the builder with the current configuration.
             */
            @Override
            public FeatureConfigurationBuilder environments(String... environments) {
                return FeatureConfigurationBuilder
                        .withEnvironmentsAndMetadata(asList(environments), new HashMap<>());
            }
        };
    }

}
