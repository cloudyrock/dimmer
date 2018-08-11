package com.github.cloudyrock.dimmer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DummyDimmerFeatureConfigurable
        extends DimmerFeatureConfigurable<DummyDimmerFeatureConfigurable> {

    DummyDimmerFeatureConfigurable() {
        super(new HashSet<>(), new HashMap<>(), RuntimeException.class);
    }

    DummyDimmerFeatureConfigurable(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType) {
        super(environments, configMetadata, defaultExceptionType);
    }

    @Override
    protected DummyDimmerFeatureConfigurable newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExcType) {
        return new DummyDimmerFeatureConfigurable(environments, configMetadata, defaultExcType);
    }

    @Override
    protected FeatureProcessorBase newFeatureProcessorInstance() {
        return new DummyFeatureProcessor();
    }

}
