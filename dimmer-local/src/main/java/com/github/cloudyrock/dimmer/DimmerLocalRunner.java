package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DimmerInvocationException;
import org.aspectj.lang.Aspects;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//TODO move javadoc from processor to here
public final class DimmerLocalRunner extends DimmerFeatureConfigurable<DimmerLocalRunner>
        implements DimmerEnvironmentConfigurable<DimmerLocalRunner> {

    private static final String DEFAULT_ENV = "DEFAULT_DIMMER_ENV";

    static DimmerLocalRunner withDefaultEnvironment() {
        return new DimmerLocalRunner();
    }

    static DimmerLocalRunner withEnvironmentsAndMetadata(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata) {
        return new DimmerLocalRunner(environments, configMetadata);
    }

    static DimmerLocalRunner withEnvironmentsMetadataAndException(
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

    @Override
    protected FeatureProcessorBase newFeatureProcessorInstance() {
        return new FeatureProcessorLocal();
    }

    public void runWithDefaultEnvironment() {
        run(DEFAULT_ENV);
    }

    public void run(String environment) {
        final FeatureProcessorLocal processor = new FeatureProcessorLocal();
        applyFeatures(processor, configMetadata.get(environment));
        Aspects.aspectOf(DimmerAspect.class).setFeatureExecutor(processor);

    }

    private void applyFeatures(FeatureProcessorLocal processor,
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

        final Class<? extends RuntimeException> exceptionType = getDefaultExceptionType();

        featureMetadataSet.stream()
                .filter(fm -> fm instanceof FeatureMetadataDefaultException)
                .map(fm -> (FeatureMetadataDefaultException) fm)
                .forEach(fmde -> processor
                                .featureWithException(fmde.getFeature(), exceptionType));

        featureMetadataSet.stream()
                .filter(fm -> fm instanceof FeatureMetadataValue)
                .map(fm -> (FeatureMetadataValue) fm)
                .forEach(fmv -> processor.featureWithValue(
                        fmv.getFeature(),
                        fmv.getValueToReturn())
                );

    }

    private Class<? extends RuntimeException> getDefaultExceptionType() {
        return this.defaultExceptionType != null
                ? this.defaultExceptionType
                : DEFAULT_EXCEPTION_TYPE;
    }

}
