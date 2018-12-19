package com.github.cloudyrock.dimmer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.cloudyrock.dimmer.reader.DimmerConfigReader;
import com.github.cloudyrock.dimmer.reader.DimmerConfigReaderYamlImpl;

import java.util.HashMap;

import static java.util.Arrays.asList;

/**
 * Builder to configure the initial environment.
 * This forces to select a environment before starting to add configuration.
 */
public class DimmerBuilder {


    private DimmerBuilder() {
    }

    /**
     * @return A local version of the Dimmer builder, so it gets the entire configuration
     * form the current application.
     */
    public static DimmerDefaultEnvironmentConfigurable<FeatureConfigurationBuilder> local() {

        return new DimmerDefaultEnvironmentConfigurable<FeatureConfigurationBuilder>() {

            /**
             * Indicates the following configurations for dimmer feature will be applied
             * to the given environment, until the environment is changed with
             * the method 'environments(String...)' or 'defaultEnvironment()'
             * @return a new immutable instance of the builder with the current configuration.
             */
            @Override
            public FeatureConfigurationBuilder environments(String... environments) {
                final DimmerConfigReader dimmerConfigReader = getDimmerConfigReader();
                return FeatureConfigurationBuilder
                        .withEnvironmentsAndMetadata(asList(environments), new HashMap<>(), dimmerConfigReader);
            }

            private DimmerConfigReader getDimmerConfigReader() {
                /**
                 * TODO: Change this to a service locator that dynamically Injects the correct factory depending
                 * on the extension type
                 */

                final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                return new DimmerConfigReaderYamlImpl(objectMapper);
            }
        };
    }

}
