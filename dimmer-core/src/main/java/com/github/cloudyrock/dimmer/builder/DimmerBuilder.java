package com.github.cloudyrock.dimmer.builder;

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
public final class DimmerBuilder {

    private DimmerBuilder() {
    }

    /**
     * Indicates the following configurations for dimmer feature will be applied
     * to the given environment, until the environment is changed with
     * the method 'environments(String...)' or 'defaultEnvironment()'
     *
     * @return a new immutable instance of the builder with the current configuration.
     */
    public static BehaviourBuilder environments(String... environments) {
        return new BehaviourBuilder(
                asList(environments), new HashMap<>(), getDimmerConfigReader());
    }

    private static DimmerConfigReader getDimmerConfigReader() {
        /**
         * TODO: Change this to a service locator that dynamically Injects the correct factory depending
         * on the extension type
         */

        final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        return new DimmerConfigReaderYamlImpl(objectMapper);
    }

}
