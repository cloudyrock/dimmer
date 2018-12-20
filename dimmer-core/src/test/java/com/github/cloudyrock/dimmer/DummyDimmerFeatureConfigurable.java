package com.github.cloudyrock.dimmer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cloudyrock.dimmer.reader.DimmerConfigReader;
import com.github.cloudyrock.dimmer.reader.DimmerConfigReaderYamlImpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DummyDimmerFeatureConfigurable
        extends DimmerFeatureConfigurable<DummyDimmerFeatureConfigurable> {

    DummyDimmerFeatureConfigurable() {
        super(new HashSet<>(), new HashMap<>(), RuntimeException.class, new DimmerConfigReaderYamlImpl(new ObjectMapper()));
    }

    DummyDimmerFeatureConfigurable(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType,
            DimmerConfigReader dimmerConfigReader) {
        super(environments, configMetadata, defaultExceptionType, dimmerConfigReader);
    }

    @Override
    protected DummyDimmerFeatureConfigurable newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExcType,
            DimmerConfigReader dimmerConfigReader) {
        return new DummyDimmerFeatureConfigurable(environments, configMetadata, defaultExcType, dimmerConfigReader);
    }

}
