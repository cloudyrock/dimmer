package com.github.cloudyrock.dimmer;

import org.aspectj.lang.Aspects;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class DimmerLocalRunner extends DimmerConfigurableRunner<DimmerLocalRunner>
        implements DimmerEnvironmentConfigurable<DimmerLocalRunner> {

    DimmerLocalRunner(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata) {
        super(environments, configMetadata, DimmerInvocationException.class, false);
    }

    DimmerLocalRunner(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> newDefaultExceptionType,
            boolean alreadyRun) {
        super(environments, configMetadata, newDefaultExceptionType, alreadyRun);
    }

    @Override
    public DimmerLocalRunner environments(String... environments) {
        Util.checkArgumentNullEmpty(environments, "environments");
        return new DimmerLocalRunner(
                Arrays.asList(environments),
                this.configMetadata,
                this.defaultExceptionType,
                alreadyRun);
    }

    @Override
    protected DimmerLocalRunner newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType) {
        return new DimmerLocalRunner(environments, configMetadata, defaultExceptionType,
                alreadyRun);
    }

    public synchronized void run(String environment) {
        if (!alreadyRun) {
            alreadyRun = true;
            final DimmerProcessor processor = new DimmerProcessor(defaultExceptionType);
            Set<FeatureMetadata> featureMetadataSet = configMetadata.get(environment);
            applyFeatures(processor, featureMetadataSet);
            Aspects.aspectOf(DimmerAspect.class).setDimmerProcessor(processor);
        }

    }

    private void applyFeatures(DimmerProcessor processor,
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
