package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DimmerInvocationException;
import org.aspectj.lang.Aspects;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

final class DimmerLocalRunner extends DimmerConfigurableRunner<DimmerLocalRunner>
        implements DimmerEnvironmentConfigurable<DimmerLocalRunner> {

    private static final String DEFAULT_ENV = "DEFAULT_DIMMER_ENV";

    static DimmerLocalRunner withDefaultEnviroment() {
        return new DimmerLocalRunner();
    }

    static DimmerLocalRunner withEnvsAndMetadata(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata) {
        return new DimmerLocalRunner(environments, configMetadata);
    }

    static DimmerLocalRunner withEnvsMetadataAndException(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> newDefaultExceptionType) {
        return new DimmerLocalRunner(environments, configMetadata,
                newDefaultExceptionType);
    }

    private DimmerLocalRunner() {
        this(Collections.singleton(DEFAULT_ENV), new HashMap<>(),
                DimmerInvocationException.class);
    }

    private DimmerLocalRunner(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata) {
        this(environments, configMetadata, DimmerInvocationException.class);
    }

    private DimmerLocalRunner(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> newDefaultExceptionType) {
        super(environments, configMetadata, newDefaultExceptionType);

    }

    @Override
    protected DimmerLocalRunner newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType) {
        return new DimmerLocalRunner(environments, configMetadata, defaultExceptionType);
    }

    public void runWithDefaultEnvironment() {
        run(DEFAULT_ENV);
    }

    public void run(String environment) {
        final Class<? extends RuntimeException> exceptionType =
                this.defaultExceptionType != null
                        ? this.defaultExceptionType
                        : DEFAULT_EXCEPTION_TYPE;
        final DimmerLocalProcessor processor = new DimmerLocalProcessor(exceptionType);
        applyFeatures(processor, configMetadata.get(environment));
        Aspects.aspectOf(DimmerAspect.class).setFeatureExecutor(processor);

    }

    private void applyFeatures(DimmerLocalProcessor processor,
                               Set<FeatureMetadata> featureMetadataSet) {

        if (featureMetadataSet == null) {
            return;
        }
        featureMetadataSet.stream()
                .filter(fm -> fm instanceof FeatureMetadataBehaviour)
                .map(fm -> (FeatureMetadataBehaviour) fm)
                .forEach(fmb -> processor.featureWithBehaviour(
                        fmb.getFeature(),
                        fmb.getBehaviour()));

        featureMetadataSet.stream()
                .filter(fm -> fm instanceof FeatureMetadataException)
                .map(fm -> (FeatureMetadataException) fm)
                .forEach(fme -> processor.featureWithException(
                        fme.getFeature(),
                        fme.getException()
                ));

        featureMetadataSet.stream()
                .filter(fm -> fm instanceof FeatureMetadataDefaultException)
                .map(fm -> (FeatureMetadataDefaultException) fm)
                .forEach(
                        fmde -> processor.featureWithDefaultException(fmde.getFeature()));

        featureMetadataSet.stream()
                .filter(fm -> fm instanceof FeatureMetadataValue)
                .map(fm -> (FeatureMetadataValue) fm)
                .forEach(fmv -> processor.featureWithValue(
                        fmv.getFeature(),
                        fmv.getValueToReturn())
                );

    }

}
