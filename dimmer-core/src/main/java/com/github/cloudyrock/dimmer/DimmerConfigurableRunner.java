package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DimmerInvocationException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

abstract  class DimmerConfigurableRunner<RUNNER extends DimmerConfigurableRunner> {
    

    protected final Collection<String> environments;

    protected final Map<String, Set<FeatureMetadata>> configMetadata;


    protected Class<? extends RuntimeException> defaultExceptionType =
            DimmerInvocationException.class;

    protected DimmerConfigurableRunner(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType) {
        this.environments = environments;
        this.configMetadata = configMetadata;
        this.defaultExceptionType = defaultExceptionType;
    }

    public RUNNER environments(String... environments) {
        Util.checkArgumentNullEmpty(environments, "environments");
        return newInstance(
                Arrays.asList(environments),
                this.configMetadata,
                this.defaultExceptionType);
    }

    public RUNNER featureWithBehaviour(
            String feature,
            Function<FeatureInvocation, ?> behaviour) {
        checkConfigurationAccess();
        final FeatureMetadataBehaviour metadata = new FeatureMetadataBehaviour(
                feature,
                behaviour
        );
        addFeatureMetadata(metadata);
        return newInstance(this.environments, this.configMetadata,
                this.defaultExceptionType);

    }

    public RUNNER featureWithDefaultException(String feature) {
        checkConfigurationAccess();
        final FeatureMetadata metadata = new FeatureMetadataDefaultException(
                feature
        );
        addFeatureMetadata(metadata);
        return newInstance(this.environments, this.configMetadata,
                this.defaultExceptionType);
    }

    public RUNNER featureWithException(
            String feature,
            Class<? extends RuntimeException> exceptionType) {
        checkConfigurationAccess();
        final FeatureMetadata metadata = new FeatureMetadataException(
                feature,
                exceptionType
        );
        addFeatureMetadata(metadata);
        return newInstance(this.environments, this.configMetadata,
                this.defaultExceptionType);
    }

    public RUNNER featureWithValue(String feature,
                                   Object valueToReturn) {
        checkConfigurationAccess();
        final FeatureMetadata metadata = new FeatureMetadataValue(
                feature,
                valueToReturn
        );
        addFeatureMetadata(metadata);
        return newInstance(this.environments, this.configMetadata,
                this.defaultExceptionType);

    }

    private void addFeatureMetadata(FeatureMetadata metadata) {
        this.environments.forEach(env -> {
            if (!configMetadata.containsKey(env)) {
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
    public RUNNER setDefaultExceptionType(
            Class<? extends RuntimeException> newDefaultExceptionType) {
        Util.checkArgumentNullEmpty(newDefaultExceptionType, "defaultExceptionType");
        return newInstance(this.environments, this.configMetadata,
                newDefaultExceptionType);
    }


    protected abstract RUNNER newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> newDefaultExceptionType);


    protected abstract void checkConfigurationAccess();
}
