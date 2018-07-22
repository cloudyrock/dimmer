package com.github.cloudyrock.dimmer;

import org.aspectj.lang.Aspects;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class DimmerConfigurableRunner implements DimmerEnvironmentConfigurable {

    private final Collection<String> environments;

    protected final Map<String, Set<FeatureMetadata>> configMetadata;

    protected DimmerProcessor instance;

    protected Class<? extends RuntimeException> defaultExceptionType =
            DimmerInvocationException.class;


    protected DimmerConfigurableRunner(Collection<String> environments,
                                       Map<String, Set<FeatureMetadata>> configMetadata) {
        this.environments = environments;
        this.configMetadata = configMetadata;
    }
    

    public DimmerConfigurableRunner environments(String... environments) {
        return new DimmerConfigurableRunner(
                Arrays.asList(environments),
                this.configMetadata);
    }

    public DimmerConfigurableRunner featureWithBehaviour(
            String feature,
            Function<FeatureInvocation, ?> behaviour) {
        final FeatureMetadataBehaviour metadata = new FeatureMetadataBehaviour(
                feature,
                behaviour
        );
        addFeatureMetadata(metadata);
        return new DimmerConfigurableRunner(this.environments, this.configMetadata);

    }


    public DimmerConfigurableRunner featureWithDefaultException(String feature) {
        final FeatureMetadata metadata = new FeatureMetadataDefaultException(
                feature
        );
        addFeatureMetadata(metadata);
        return new DimmerConfigurableRunner(this.environments, this.configMetadata);
    }

    public DimmerConfigurableRunner featureWithException(
            String feature,
            Class<? extends RuntimeException> exceptionType) {
        final FeatureMetadata metadata = new FeatureMetadataException(
                feature,
                exceptionType
        );
        addFeatureMetadata(metadata);
        return new DimmerConfigurableRunner(this.environments, this.configMetadata);
    }

    public DimmerConfigurableRunner featureWithValue(String feature,
                                                     Object valueToReturn) {
        final FeatureMetadata metadata = new FeatureMetadataValue(
                feature,
                valueToReturn
        );
        addFeatureMetadata(metadata);
        return new DimmerConfigurableRunner(this.environments, this.configMetadata);

    }

    private void addFeatureMetadata(FeatureMetadata metadata) {
        this.environments.forEach(env-> {
            if(!configMetadata.containsKey(env)) {
                configMetadata.put(env, new HashSet<>());
            }
            configMetadata.get(env).add(metadata);
        });
    }


    /**
     * Set the default exception type to be thrown as behaviour.
     * <p>
     * Notice the exception type must have either an empty constructor or a contractor with only
     * one parameter, (@{@link FeatureInvocation})
     *
     * @param newDefaultExceptionType new default exception type
     * @return Singleton DimmerProcessor builder
     */
    public DimmerConfigurableRunner setDefaultExceptionType(
            Class<? extends RuntimeException> newDefaultExceptionType) {
        Util.checkArgument(newDefaultExceptionType, "defaultExceptionType");
        this.defaultExceptionType = newDefaultExceptionType;
        return this;
    }

    /**
     * @return If the singleton DimmerProcessor instance has been already initialised
     */
    protected boolean isInitialised() {
        return instance != null;
    }

    public synchronized DimmerProcessor runAsLocal(String environment) {
        if (!isInitialised()) {
            instance = new DimmerProcessor(defaultExceptionType);
            Set<FeatureMetadata> featureMetadataSet = configMetadata.get(environment);
            applyFeatures(instance, featureMetadataSet);
            Aspects.aspectOf(DimmerAspect.class).setDimmerProcessor(instance);
        }
        return instance;
    }

    public synchronized DimmerProcessor runAsServer() {
        return null;
    }

    private void applyFeatures(DimmerProcessor processor,
                               Set<FeatureMetadata> featureMetadataSet) {

        if(featureMetadataSet == null) {
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
                .forEach(fme-> processor.featureWithException(
                        fme.getFeature(),
                        fme.getException()
                ));

        featureMetadataSet.stream()
                .filter(fm -> fm instanceof FeatureMetadataDefaultException)
                .map(fm -> (FeatureMetadataDefaultException) fm)
                .forEach(fmde-> processor.featureWithDefaultException(fmde.getFeature()));

        featureMetadataSet.stream()
                .filter(fm -> fm instanceof FeatureMetadataValue)
                .map(fm -> (FeatureMetadataValue) fm)
                .forEach(fmv-> processor.featureWithValue(
                        fmv.getFeature(),
                        fmv.getValueToReturn())
                );


    }
}
