package com.github.cloudyrock.dimmer.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cloudyrock.dimmer.DimmerConfigException;
import com.github.cloudyrock.dimmer.reader.models.DimmerConfig;
import com.github.cloudyrock.dimmer.reader.models.EnvironmentConfig;
import com.github.cloudyrock.dimmer.reader.models.yaml.DimmerYamlConfig;
import com.github.cloudyrock.dimmer.reader.models.yaml.Environment;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;

public final class DimmerConfigReaderYamlImpl implements DimmerConfigReader {

    private static Logger LOGGER = getLogger(DimmerConfigReaderYamlImpl.class);
    private ObjectMapper objectMapper;

    public static final String DIMMER_CONFIG_EXCEPTION_ENVIRONMENT_CONFIGURATION_IS_EMPTY = "Environment configuration is empty";
    public static final String DIMMER_CONFIG_EXCEPTION_SERVER_CONFIGURATION_AND_FEATURE_INTERCEPTOR_MISMATCH = "Server Configuration and Feature Interceptor must not be configured for the same environment.";
    public static final String DIMMER_CONFIG_EXCEPTION_INVALID_URL = "Invalid URL set in server configuration.";
    public static final String DIMMER_CONFIGURATION_FILE_COULD_NOT_BE_READ = "Dimmer configuration file could not be read.";

    private static final String DEFAULT_DIMMER_PROPERTIES_LOCATION = "dimmer.yml";

    private String propertiesLocation;

    /**
     * Creates a new instance DimmerConfigReaderYaml instance with the default Yaml ObjectMapper
     */
    public DimmerConfigReaderYamlImpl(ObjectMapper objectMapper) {
        //Default to YAML object mapper
        this(DEFAULT_DIMMER_PROPERTIES_LOCATION, objectMapper);
    }

    /**
     * Creates a new instance DimmerConfigReaderYaml with an ObjectMapper chosen by the creator of the instance
     */
    public DimmerConfigReaderYamlImpl(String propertiesLocation, ObjectMapper objectMapper) {
        this.propertiesLocation = propertiesLocation;
        this.objectMapper = objectMapper;
    }

    @Override
    public void setPropertiesLocation(String propertiesLocation) {
        this.propertiesLocation = propertiesLocation;
    }

    @Override
    public DimmerConfig loadConfiguration() {
        try {
            final File file = readFileFromClassPath(propertiesLocation);
            final DimmerYamlConfig dimmerYamlConfig = objectMapper.readValue(file, DimmerYamlConfig.class);
            return toDimmerConfig(dimmerYamlConfig);
        } catch (IOException e) {
            throw new DimmerConfigException("Failed mapping Dimmer configuration file.", e);
        }
    }

    @Override
    public Map.Entry<String, EnvironmentConfig> getDefaultEnvironment(DimmerConfig dimmerConfig) {

        return dimmerConfig.getEnvironments().entrySet().stream()
                .filter(stringEnvironmentConfigEntry ->
                        stringEnvironmentConfigEntry.getValue().isDefault())
                .findFirst()
                .orElseThrow(() -> new DimmerConfigException("No Default environment found in configuration"))
                ;
    }

    private static DimmerConfig toDimmerConfig(DimmerYamlConfig dimmerYamlConfig) {
        final DimmerConfig dimmerConfig = new DimmerConfig();
        final Map<String, EnvironmentConfig> environmentConfigMap =
                dimmerYamlConfig.getDimmer().getEnvironments().entrySet().stream()
                        .collect(toMap(Map.Entry::getKey, getEntryEnvironmentConfigFunction()));

        dimmerConfig.setEnvironments(environmentConfigMap);
        return dimmerConfig;
    }

    private static Function<Map.Entry<String, Environment>, EnvironmentConfig> getEntryEnvironmentConfigFunction() {
        return k -> {
            final Environment value = k.getValue();
            final List featuresList = value.getFeatureIntercept();
            final String server = value.getServer();

            checkEnvironmentSettings(featuresList, server);

            return new EnvironmentConfig(
                    k.getKey(),
                    value.getServer(),
                    value.getFeatureIntercept(),
                    value.isDefault()
            );
        };
    }

    private static void checkEnvironmentSettings(List featuresList, String server) throws DimmerConfigException {

        //server or featureIntercept don't exist
        if ((featuresList == null) && (server == null)) {
            throw new DimmerConfigException(DIMMER_CONFIG_EXCEPTION_ENVIRONMENT_CONFIGURATION_IS_EMPTY);
        }

        //server and featureIntercept both exist simultaneously
        if ((featuresList != null && !featuresList.isEmpty()) && (server != null && !server.isEmpty())) {
            throw new DimmerConfigException(DIMMER_CONFIG_EXCEPTION_SERVER_CONFIGURATION_AND_FEATURE_INTERCEPTOR_MISMATCH);
        }

        checkUrl(server);
        //TODO: Add call to load config from server
    }

    private static void checkUrl(String server) {
        if (server != null && !server.isEmpty()) {
            try {
                new URL(server);
            } catch (MalformedURLException e) {
                throw new DimmerConfigException(DIMMER_CONFIG_EXCEPTION_INVALID_URL);
            }
        }
    }

    private static File readFileFromClassPath(String filePath) {
        final URL url = DimmerConfigReader.class.getClassLoader().getResource(filePath);
        if (url == null) {
            LOGGER.error("Could not find dimmer config file from classpath, throwing DimmerConfigException.");
            throw new DimmerConfigException(DIMMER_CONFIGURATION_FILE_COULD_NOT_BE_READ);
        }
        return new File(url.getFile());
    }

}
