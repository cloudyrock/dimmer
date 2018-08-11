package com.github.cloudyrock.dimmer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DummyConfigurableBuilder extends DimmerFeatureConfigurable<DummyConfigurableBuilder> {

    DummyConfigurableBuilder() {
        super(new HashSet<>(), new HashMap<>(), RuntimeException.class);
    }

    DummyConfigurableBuilder(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType) {
        super(environments, configMetadata, defaultExceptionType);
    }

    @Override
    protected DummyConfigurableBuilder newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExcType) {
        return new DummyConfigurableBuilder(environments, configMetadata, defaultExcType);
    }

    @Override
    protected FeatureProcessorBase newFeatureProcessorInstance() {
        return new FeatureLocalExecutor();
    }
}
