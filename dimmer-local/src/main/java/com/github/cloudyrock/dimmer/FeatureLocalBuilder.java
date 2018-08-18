package com.github.cloudyrock.dimmer;

import org.aspectj.lang.Aspects;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//TODO move javadoc from processor to here
public final class FeatureLocalBuilder extends DimmerFeatureConfigurable<FeatureLocalBuilder>
        implements DimmerEnvironmentConfigurable<FeatureLocalBuilder> {

    private static final String DEFAULT_ENV = "DEFAULT_DIMMER_ENV";

    private static final DimmerLogger logger =
            new DimmerLogger(FeatureLocalBuilder.class);

    static FeatureLocalBuilder withDefaultEnvironment() {
        return new FeatureLocalBuilder();
    }

    static FeatureLocalBuilder withEnvironmentsAndMetadata(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata) {
        return new FeatureLocalBuilder(environments, configMetadata);
    }

    private FeatureLocalBuilder() {
        this(Collections.singleton(DEFAULT_ENV), new HashMap<>(),
                DimmerInvocationException.class);
    }

    private FeatureLocalBuilder(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata) {
        this(environments, configMetadata, DimmerInvocationException.class);
    }

    private FeatureLocalBuilder(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> newDefaultExceptionType) {
        super(environments, configMetadata, newDefaultExceptionType);

    }

    @Override
    protected FeatureLocalBuilder newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType) {
        return new FeatureLocalBuilder(environments, configMetadata, defaultExceptionType);
    }

    @Override
    protected FeatureProcessorBase newFeatureProcessorInstance() {
        return new FeatureLocalExecutor();
    }

    public FeatureLocalExecutor build(String environment) {
        logger.info("Building local executor");
        final FeatureLocalExecutor executor = (FeatureLocalExecutor)
                newFeatureProcessor(configMetadata.get(environment));
        Aspects.aspectOf(DimmerAspect.class).setFeatureExecutor(executor);
        logger.info("Dimmer Aspect running");
        return executor;
    }

    public FeatureLocalExecutor buildWithDefaultEnvironment() {
        return build(DEFAULT_ENV);
    }

}