package com.github.cloudyrock.dimmer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DummyConfigurableRunner extends DimmerFeatureConfigurable<DummyConfigurableRunner> {

    DummyConfigurableRunner() {
        super(new HashSet<>(), new HashMap<>(), RuntimeException.class,
                DimmerSlf4j.nullLogger());
    }

    DummyConfigurableRunner(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType,
            DimmerSlf4j logger) {
        super(environments, configMetadata, defaultExceptionType, logger);
    }

    @Override
    protected DummyConfigurableRunner newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExcType,
            DimmerSlf4j logger) {
        return new DummyConfigurableRunner(environments, configMetadata, defaultExcType,
                logger);
    }

    @Override
    protected FeatureProcessorBase newFeatureProcessorInstance() {
        return new DummyFeatureProcessor(logger);
    }

}
