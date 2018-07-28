package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DimmerInvocationException;
import org.aspectj.lang.Aspects;
import org.springframework.beans.factory.InitializingBean;

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
        return new FeatureLocalBuilder(environments, configMetadata, defaultExceptionType);
    }

    @Override
    protected FeatureProcessorBase newFeatureProcessorInstance() {
        return new FeatureLocalProcessor();
    }

    public void buildAndRun(String environment) {
        final FeatureLocalProcessor processor = (FeatureLocalProcessor)
                newFeatureProcessor(configMetadata.get(environment));
        Aspects.aspectOf(DimmerAspect.class).setFeatureExecutor(processor);
    }

    public void buildAndWithDefaultEnvironment() {
        buildAndRun(DEFAULT_ENV);
    }

    public InitializingBean buildSpringBean(String environment) {
        return () -> buildAndRun(environment);
    }

    public InitializingBean buildSpringBeanWithDefaultEnvironment() {
        return buildSpringBean(DEFAULT_ENV);
    }

}
