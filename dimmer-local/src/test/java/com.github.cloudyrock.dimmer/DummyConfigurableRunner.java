package com.github.cloudyrock.dimmer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DummyConfigurableRunner extends DimmerFeatureConfigurable<DummyConfigurableRunner> {

    DummyConfigurableRunner() {
        super(new HashSet<>(), new HashMap<>(), RuntimeException.class);
    }

    DummyConfigurableRunner(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType) {
        super(environments, configMetadata, defaultExceptionType);
    }

    @Override
    protected DummyConfigurableRunner newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExcType) {
        return new DummyConfigurableRunner(environments, configMetadata, defaultExcType);
    }

    @Override
    protected FeatureProcessorBase newFeatureProcessorInstance() {
        return new FeatureLocalProcessor();
    }
}
