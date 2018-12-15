package com.github.cloudyrock.dimmer.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.cloudyrock.dimmer.DimmerConfigException;
import com.github.cloudyrock.dimmer.reader.models.yaml.DimmerYamlConfig;
import com.github.cloudyrock.dimmer.reader.models.yaml.Environment;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;

final class DimmerConfigReaderYamlImpl implements DimmerConfigReader {

    private static Logger LOGGER = getLogger(DimmerConfigReaderYamlImpl.class);
    private ObjectMapper objectMapper;

    public static final String DIMMER_CONFIG_EXCEPTION_ENVIRONMENT_CONFIGURATION_IS_EMPTY = "Environment configuration is empty";
    public static final String DIMMER_CONFIG_EXCEPTION_SERVER_CONFIGURATION_AND_FEATURE_INTERCEPTOR_MISMATCH = "Server Configuration and Feature Interceptor must not be configured for the same environment.";
    public static final String DIMMER_CONFIG_EXCEPTION_INVALID_URL = "Invalid URL set in server configuration.";
    public static final String DIMMER_CONFIGURATION_FILE_COULD_NOT_BE_READ = "Dimmer configuration file could not be read.";

    public DimmerConfigReaderYamlImpl() {
        //Default to YAML object mapper
        objectMapper = new ObjectMapper(new YAMLFactory());
    }

    public DimmerConfigReaderYamlImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public DimmerConfig fromProperties(final String filePath) {
        try {
            final File file = readFileFromClassPath(filePath);
            final DimmerYamlConfig dimmerYamlConfig = objectMapper.readValue(file, DimmerYamlConfig.class);
            return toDimmerConfig(dimmerYamlConfig);
        } catch (IOException e) {
            LOGGER.error("Failed mapping Dimmer configuration file.", e);
            throw new DimmerConfigException(e);
        }
    }

    private DimmerConfig toDimmerConfig(DimmerYamlConfig dimmerYamlConfig) {
        final DimmerConfig dimmerConfig = new DimmerConfig();
        final Map<String, EnvironmentConfig> environmentConfigMap =
                dimmerYamlConfig.getDimmer().getEnvironments().entrySet().stream()
                    .collect(toMap(Map.Entry::getKey,
                        k -> {
                            final Environment value = k.getValue();
                            final List featuresList = value.getFeatureIntercept();
                            final String server = value.getServer();
                            final Boolean isDefault = value.isDefault();

                            checkEnvironmentSettings(featuresList, server, isDefault);

                            return new EnvironmentConfig(
                                    value.getServer(),
                                    value.getFeatureIntercept(),
                                    value.isDefault()
                            );
                        }));

        dimmerConfig.setEnvironments(environmentConfigMap);
        return dimmerConfig;
    }

    private void checkEnvironmentSettings(List featuresList, String server, Boolean isDefault) throws DimmerConfigException {

        //server or featureIntercept don't exist
        if ((featuresList == null) && (server == null)) {
            throw new DimmerConfigException(DIMMER_CONFIG_EXCEPTION_ENVIRONMENT_CONFIGURATION_IS_EMPTY);
        }

        //server and featureIntercept both exist simultaneously
        if ((featuresList != null && !featuresList.isEmpty()) && (server != null && !server.isEmpty())) {
            throw new DimmerConfigException(DIMMER_CONFIG_EXCEPTION_SERVER_CONFIGURATION_AND_FEATURE_INTERCEPTOR_MISMATCH);
        }
        checkUrl(server);
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

    private File readFileFromClassPath(String filePath) {
        final URL url = getClass().getClassLoader().getResource(filePath);
        if (url == null) {
            LOGGER.error("Could not find dimmer config file from classpath, throwing DimmerConfigException.");
            throw new DimmerConfigException(DIMMER_CONFIGURATION_FILE_COULD_NOT_BE_READ);
        }
        return new File(url.getFile());
    }

}
