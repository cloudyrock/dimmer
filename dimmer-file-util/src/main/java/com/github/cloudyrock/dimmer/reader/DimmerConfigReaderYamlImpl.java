package com.github.cloudyrock.dimmer.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cloudyrock.dimmer.FileConfigException;
import com.github.cloudyrock.dimmer.reader.models.DimmerConfig;
import com.github.cloudyrock.dimmer.reader.models.EnvironmentConfig;
import com.github.cloudyrock.dimmer.reader.models.yaml.DimmerYamlConfig;
import com.github.cloudyrock.dimmer.reader.models.yaml.Environment;
import org.apache.commons.lang3.StringUtils;
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

    static final String DIMMER_CONFIG_EXCEPTION_ENVIRONMENT_CONFIGURATION_IS_EMPTY = "Environment configuration is empty";
    static final String DIMMER_CONFIG_EXCEPTION_SERVER_CONFIGURATION_AND_FEATURE_INTERCEPTOR_MISMATCH = "Server Configuration and Feature Interceptor must not be configured for the same environment.";
    static final String DIMMER_CONFIG_EXCEPTION_INVALID_URL = "Invalid URL set in server configuration.";
    static final String DIMMER_CONFIGURATION_FILE_COULD_NOT_BE_READ = "Dimmer configuration file could not be read.";
    public static final String DIMMER_CONFIG_EXCEPTION_ENVIRONMENT_DOESNT_EXIST_IN_CONFIG_FILE =
            "The selected environment doesn't exist in the Dimmer configuration file. ";

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

    DimmerConfig loadConfiguration() throws FileConfigException {
        try {
            final File file = readFileFromClassPath(propertiesLocation);
            final DimmerYamlConfig dimmerYamlConfig = objectMapper.readValue(file, DimmerYamlConfig.class);
            return toDimmerConfig(dimmerYamlConfig);
        } catch (IOException e) {
            throw new FileConfigException("Failed mapping Dimmer configuration file.", e);
        }
    }

    @Override
    public EnvironmentConfig loadEnvironmentOrDefault(String env) {
        final DimmerConfig dimmerConfig = loadConfiguration();
        return StringUtils.isEmpty(env) ? getDefaultEnvironment(dimmerConfig) : getEnvironment(dimmerConfig, env);
    }

    public EnvironmentConfig getEnvironment(DimmerConfig dimmerConfig, String env) throws FileConfigException {
        final Map<String, EnvironmentConfig> dimmerConfigEnvironments = dimmerConfig.getEnvironments();
        final EnvironmentConfig environmentConfig = dimmerConfigEnvironments.get(env);

        if (environmentConfig == null) {
            throw new FileConfigException(
                    String.format(DIMMER_CONFIG_EXCEPTION_ENVIRONMENT_DOESNT_EXIST_IN_CONFIG_FILE +
                            "Please add the environment %s in the configuration file", env));
        }
        return environmentConfig;
    }


    public EnvironmentConfig getDefaultEnvironment(DimmerConfig dimmerConfig) throws FileConfigException {
        return dimmerConfig.getEnvironments().values().stream()
                .filter(EnvironmentConfig::isDefault)
                .findFirst()
                .orElseThrow(() -> new FileConfigException("No Default environment found in configuration"));
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
            final List featuresList = value.getToggledOn();
            final String server = value.getServer();

            checkEnvironmentSettings(featuresList, server);

            return new EnvironmentConfig(
                    k.getKey(),
                    value.getServer(),
                    value.getToggledOn(),
                    value.isDefault()
            );
        };
    }

    private static void checkEnvironmentSettings(List<String> featuresList, String server) throws FileConfigException {

        //server and toggledOn both exist simultaneously
        if ((featuresList != null && !featuresList.isEmpty()) && (server != null && !server.isEmpty())) {
            throw new FileConfigException(DIMMER_CONFIG_EXCEPTION_SERVER_CONFIGURATION_AND_FEATURE_INTERCEPTOR_MISMATCH);
        }

        checkUrl(server);
        //TODO: Add call to load config from server
    }

    private static void checkUrl(String server) throws FileConfigException {
        if (server != null && !server.isEmpty()) {
            try {
                new URL(server);
            } catch (MalformedURLException e) {
                throw new FileConfigException(DIMMER_CONFIG_EXCEPTION_INVALID_URL);
            }
        }
    }

    private static File readFileFromClassPath(String filePath) throws FileConfigException {
        final URL url = DimmerConfigReader.class.getClassLoader().getResource(filePath);
        if (url == null) {
            LOGGER.error("Could not find dimmer config file from classpath, throwing FileConfigException.");
            throw new FileConfigException(DIMMER_CONFIGURATION_FILE_COULD_NOT_BE_READ);
        }
        return new File(url.getFile());
    }

}
