package com.github.cloudyrock.dimmer;

import org.aspectj.lang.Aspects;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//TODO move javadoc from processor to here
public final class FeatureConfigurationBuilder extends DimmerFeatureConfigurable<FeatureConfigurationBuilder>
        implements DimmerEnvironmentConfigurable<FeatureConfigurationBuilder> {

    private static final String DEFAULT_ENV = "DEFAULT_DIMMER_ENV";

    private static final DimmerLogger logger =
            new DimmerLogger(FeatureConfigurationBuilder.class);

    static FeatureConfigurationBuilder withDefaultEnvironment() {
        return new FeatureConfigurationBuilder(Collections.singleton(DEFAULT_ENV),
                new HashMap<>(), DimmerInvocationException.class);
    }

    static FeatureConfigurationBuilder withEnvironmentsAndMetadata(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata) {
        return new FeatureConfigurationBuilder(environments, configMetadata,
                DimmerInvocationException.class);
    }

    private FeatureConfigurationBuilder(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> newDefaultExceptionType) {
        super(environments, configMetadata, newDefaultExceptionType);

    }

    @Override
    protected FeatureConfigurationBuilder newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType) {
        return new FeatureConfigurationBuilder(environments, configMetadata,
                defaultExceptionType);
    }

    public FeatureExecutorImpl build(String environment) {
        logger.info("Building local executor");
        final FeatureExecutorImpl executor = new FeatureExecutorImpl(
                configMetadata.get(environment),
                getDefaultExceptionType());
        Aspects.aspectOf(DimmerAspect.class).setFeatureExecutor(executor);
        logger.info("Dimmer Aspect running");
        return executor;
    }

    public FeatureExecutorImpl buildWithDefaultEnvironment() {
        return build(DEFAULT_ENV);
    }

}
