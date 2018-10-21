package com.github.cloudyrock.dimmer.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.cloudyrock.dimmer.DimmerConfigException;
import com.github.cloudyrock.dimmer.reader.models.yaml.DimmerYamlConfig;

import java.io.File;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

final class DimmerConfigReaderYamlImpl implements DimmerConfigReader {

    @Override
    public DimmerConfig fromProperties(String filePath) {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            final File file = new File(getClass().getClassLoader().getResource(filePath).getFile());
            final DimmerYamlConfig dimmerYamlConfig = mapper.readValue(file, DimmerYamlConfig.class);
            return toDimmerConfig(dimmerYamlConfig);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DimmerConfigException(e);
        }
    }


    private DimmerConfig toDimmerConfig(DimmerYamlConfig dimmerYamlConfig) {
        final DimmerConfig dimmerConfig = new DimmerConfig();
        final Map<String, EnvironmentConfig> environmentConfigMap =
                dimmerYamlConfig.getDimmer().getEnvironments().entrySet().stream()
                        .collect(toMap(Map.Entry::getKey,
                                k -> new EnvironmentConfig(k.getValue().getServer(), k.getValue().getFeatureIntercept())));

        dimmerConfig.setEnvironments(environmentConfigMap);
        return dimmerConfig;
    }

}
