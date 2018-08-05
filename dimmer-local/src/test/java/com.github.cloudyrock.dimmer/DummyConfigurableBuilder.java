package com.github.cloudyrock.dimmer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DummyConfigurableBuilder extends DimmerFeatureConfigurable<DummyConfigurableBuilder> {

    DummyConfigurableBuilder() {
        super(new HashSet<>(), new HashMap<>(), RuntimeException.class,
                DimmerSlf4j.nullLogger());
    }

    DummyConfigurableBuilder(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType,
            DimmerSlf4j logger) {
        super(environments, configMetadata, defaultExceptionType, logger);
    }

    @Override
    protected DummyConfigurableBuilder newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExcType,
            DimmerSlf4j logger) {
        return new DummyConfigurableBuilder(environments, configMetadata, defaultExcType,
                logger);
    }

    @Override
    protected FeatureProcessorBase newFeatureProcessorInstance() {
        return new FeatureLocalExecutor();
    }
}
