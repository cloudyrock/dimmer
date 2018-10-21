package com.github.cloudyrock.dimmer;

import java.util.HashMap;

import static java.util.Arrays.asList;

/**
 * Builder to configure the initial environment.
 * This forces to select a environment before starting to add configuration.
 */
public class DimmerBuilder {

    public static final String DIMMER_PROPERTIES_LOCATION = "dimmer.yaml";

    private DimmerBuilder(){

    }

    public static FeatureConfigurationBuilderDimmerEnvironmentConfigurable<FeatureConfigurationBuilder> withDefaultProperties() {
        return withProperties(DIMMER_PROPERTIES_LOCATION);
    }

    /**
     * @return A local version of the Dimmer builder, so it gets the entire configuration
     * form the current application.
     */
    public static FeatureConfigurationBuilderDimmerEnvironmentConfigurable<FeatureConfigurationBuilder> withProperties(String propertiesLocation) {
        return new FeatureConfigurationBuilderDimmerEnvironmentConfigurable<FeatureConfigurationBuilder>(propertiesLocation);
    }

    static class FeatureConfigurationBuilderDimmerEnvironmentConfigurable<T> implements DimmerDefaultEnvironmentConfigurable, DimmerEnvironmentConfigurable {

        private String propertiesLocation;

        FeatureConfigurationBuilderDimmerEnvironmentConfigurable(String propertiesLocation){
            this.propertiesLocation = propertiesLocation;
        }

        /**
         * Indicates the following configurations for dimmer feature will be applied
         * to the default environment, until the environment is changed with
         * the method 'environments(String...)'
         * @return a new immutable instance of the builder with the current configuration.
         */
        @Override
        public FeatureConfigurationBuilder defaultEnvironment() {
            return FeatureConfigurationBuilder.withDefaultEnvironment(propertiesLocation);
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
                    .withEnvironmentsAndMetadata(propertiesLocation, asList(environments), new HashMap<>());
        }
    }
}
