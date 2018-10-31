package com.github.cloudyrock.dimmer.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.cloudyrock.dimmer.DimmerConfigException;
import com.github.cloudyrock.dimmer.reader.models.yaml.DimmerYamlConfig;
import com.github.cloudyrock.dimmer.reader.models.yaml.Environments;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;

final class DimmerConfigReaderYamlImpl implements DimmerConfigReader {

    private static Logger LOGGER = getLogger(DimmerConfigReaderYamlImpl.class);
    public static final String DIMMER_CONFIGURATION_FILE_COULD_NOT_BE_READ = "Dimmer configuration file could not be read.";

    @Override
    public DimmerConfig fromProperties(final String filePath) {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            final File file = readFileFromClassPath(filePath);
            final DimmerYamlConfig dimmerYamlConfig = mapper.readValue(file, DimmerYamlConfig.class);
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
                                    final Environments value = k.getValue();
                                    return new EnvironmentConfig(
                                            value.getServer(),
                                            value.getFeatureIntercept(),
                                            value.isDefault()
                                    );
                                }));

        dimmerConfig.setEnvironments(environmentConfigMap);
        return dimmerConfig;
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
