package com.github.cloudyrock.dimmer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

abstract class DimmerFeatureConfigurable<RUNNER extends DimmerFeatureConfigurable> {

    protected static final Class<? extends RuntimeException> DEFAULT_EXCEPTION_TYPE =
            DimmerInvocationException.class;

    protected final Collection<String> environments;

    protected final Map<String, Set<FeatureMetadata>> configMetadata;

    protected final Class<? extends RuntimeException> defaultExceptionType;

    protected DimmerFeatureConfigurable(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType) {

        this.environments = environments;
        this.configMetadata = configMetadata;
        this.defaultExceptionType = defaultExceptionType;
    }


    public RUNNER environments(String... environments) {
        Util.checkArgumentNullEmpty(environments, "environments");
        final List<String> envs = Arrays.asList(environments);
        return newInstance(envs, configMetadata, defaultExceptionType);
    }

    public RUNNER featureWithBehaviourConditional(
            boolean interceptingFeature,
            String feature,
            String operation,
            Function<FeatureInvocation, ?> behaviour) {

        return interceptingFeature
                ? featureWithBehaviour(feature, operation, behaviour)
                : newInstance(environments, configMetadata, defaultExceptionType);

    }

    public RUNNER featureWithBehaviour(
            String feature,
            String operation,
            Function<FeatureInvocation, ?> behaviour) {

        final FeatureMetadataBehaviour metadata = new FeatureMetadataBehaviour(
                feature,
                operation,
                behaviour
        );
        addFeatureMetadata(metadata);
        return newInstance(environments, configMetadata, defaultExceptionType);

    }

    public RUNNER featureWithDefaultExceptionConditional(boolean interceptingFeature,
                                                         String feature,
                                                         String operation) {
        return interceptingFeature
                ? featureWithDefaultException(feature, operation)
                : newInstance(environments, configMetadata, defaultExceptionType);

    }

    public RUNNER featureWithDefaultException(String feature, String operation) {
        final FeatureMetadata metadata = new FeatureMetadataDefaultException(
                feature,
                operation);
        addFeatureMetadata(metadata);
        return newInstance(environments, configMetadata, defaultExceptionType);
    }

    public RUNNER featureWithExceptionConditional(
            boolean interceptingFeature,
            String feature,
            String operation,
            Class<? extends RuntimeException> exceptionType) {

        return interceptingFeature
                ? featureWithException(feature, operation, exceptionType)
                : newInstance(environments, configMetadata, defaultExceptionType);
    }

    public RUNNER featureWithException(
            String feature,
            String operation,
            Class<? extends RuntimeException> exType) {

        ExceptionUtil.checkExceptionConstructorType(exType);
        final FeatureMetadata metadata = new FeatureMetadataException(
                feature, operation, exType);
        addFeatureMetadata(metadata);
        return newInstance(environments, configMetadata, defaultExceptionType);
    }

    public RUNNER featureWithValueConditional(boolean interceptingFeature,
                                              String feature,
                                              String operation,
                                              Object valueToReturn) {
        return interceptingFeature
                ? featureWithValue(feature, operation, valueToReturn)
                : newInstance(environments, configMetadata, defaultExceptionType);
    }

    public RUNNER featureWithValue(String feature,
                                   String operation,
                                   Object valueToReturn) {
        final FeatureMetadata metadata =
                new FeatureMetadataValue(feature, operation, valueToReturn);
        addFeatureMetadata(metadata);
        return newInstance(environments, configMetadata, defaultExceptionType);

    }

    private void addFeatureMetadata(FeatureMetadata metadata) {
        environments.forEach(env -> {
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
     * one parameter, {@link FeatureInvocation}
     *
     * @param newDefaultExceptionType new default exception type
     * @return Singleton DimmerProcessor builder
     */
    public RUNNER setDefaultExceptionType(
            Class<? extends RuntimeException> newDefaultExceptionType) {
        Util.checkArgumentNullEmpty(newDefaultExceptionType, "defaultExceptionType");
        ExceptionUtil.checkExceptionConstructorType(newDefaultExceptionType);
        return newInstance(environments, configMetadata, newDefaultExceptionType);
    }

    protected Class<? extends RuntimeException> getDefaultExceptionType() {
        return this.defaultExceptionType != null
                ? defaultExceptionType
                : DEFAULT_EXCEPTION_TYPE;
    }

    protected abstract RUNNER newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> newDefaultExceptionType);

}
