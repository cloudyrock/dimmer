package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DimmerInvocationException;
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

    static FeatureLocalBuilder withDefaultEnvironment() {
        return new FeatureLocalBuilder();
    }

    static FeatureLocalBuilder withEnvironmentsAndMetadata(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata) {
        return new FeatureLocalBuilder(environments, configMetadata);
    }

    static FeatureLocalBuilder withEnvironmentsMetadataAndException(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> newDefaultExceptionType) {
        return new FeatureLocalBuilder(environments, configMetadata,
                newDefaultExceptionType);
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
        return new FeatureLocalBuilder(environments, configMetadata,
                defaultExceptionType);
    }

    @Override
    protected FeatureProcessorBase newFeatureProcessorInstance() {
        return new FeatureLocalExecutor();
    }

    public FeatureLocalExecutor build(String environment) {
        final FeatureLocalExecutor executor = (FeatureLocalExecutor)
                newFeatureProcessor(configMetadata.get(environment));
        Aspects.aspectOf(DimmerAspect.class).setFeatureExecutor(executor);
        return executor;
    }

    public FeatureLocalExecutor buildWithDefaultEnvironment() {
        return build(DEFAULT_ENV);
    }



}
