package com.github.cloudyrock.dimmer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public abstract class DimmerConfigurableBuilder extends DimmerBuilder {

    private final Collection<String> environments;

    protected final Map<String, Set<FeatureMetadata>> configMetadata;

    protected DimmerProcessor instance;

    protected Class<? extends RuntimeException> defaultExceptionType =
            DimmerInvocationException.class;

    protected DimmerConfigurableBuilder(Collection<String> environments,
                                        Map<String, Set<FeatureMetadata>> configMetadata) {
        this.environments = environments;
        this.configMetadata = configMetadata;
    }

    protected abstract DimmerConfigurableBuilder createNewInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata);

    public DimmerConfigurableBuilder environments(String... environments) {
        final Collection<String> newEnvironments = Arrays.asList(environments);
        return createNewInstance(newEnvironments, configMetadata);
    }

    public DimmerConfigurableBuilder featureWithBehaviour(
            String feature,
            Function<FeatureInvocation, ?> behaviour) {
        final FeatureMetadataBehaviour metadata = new FeatureMetadataBehaviour(
                feature,
                behaviour
        );
        addFeatureMetadata(metadata);
        return createNewInstance(this.environments, configMetadata);

    }


    public DimmerConfigurableBuilder featureWithDefaultException(String feature) {
        final FeatureMetadata metadata = new FeatureMetadataDefaultException(
                feature
        );
        addFeatureMetadata(metadata);
        return createNewInstance(this.environments, configMetadata);
    }

    public DimmerConfigurableBuilder featureWithException(
            String feature,
            Class<? extends RuntimeException> exceptionType) {
        final FeatureMetadata metadata = new FeatureMetadataException(
                feature,
                exceptionType
        );
        addFeatureMetadata(metadata);
        return createNewInstance(this.environments, configMetadata);
    }

    public DimmerConfigurableBuilder featureWithValue(String feature,
                                                      Object valueToReturn) {
        final FeatureMetadata metadata = new FeatureMetadataValue(
                feature,
                valueToReturn
        );
        addFeatureMetadata(metadata);
        return createNewInstance(this.environments, configMetadata);

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
    public DimmerConfigurableBuilder setDefaultExceptionType(
            Class<? extends RuntimeException> newDefaultExceptionType) {
        Util.checkArgument(newDefaultExceptionType, "defaultExceptionType");
        this.defaultExceptionType = newDefaultExceptionType;
        return this;
    }

    /**
     * @return If the singleton DimmerProcessor instance has been already initialised
     */
    public boolean isInitialised() {
        return instance != null;
    }

}
