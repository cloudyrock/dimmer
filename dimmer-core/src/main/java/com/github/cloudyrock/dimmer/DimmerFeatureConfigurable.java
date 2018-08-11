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

    private static final DimmerLogger logger =
            new DimmerLogger(DimmerFeatureConfigurable.class);

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

    public RUNNER featureWithBehaviour(
            boolean condition,
            String feature,
            Function<FeatureInvocation, ?> behaviour) {

        return condition
                ? featureWithBehaviour(feature, behaviour)
                : newInstance(environments, configMetadata, defaultExceptionType);

    }

    public RUNNER featureWithBehaviour(
            String feature,
            Function<FeatureInvocation, ?> behaviour) {

        final FeatureMetadataBehaviour metadata = new FeatureMetadataBehaviour(
                feature,
                behaviour
        );
        addFeatureMetadata(metadata);
        return newInstance(environments, configMetadata, defaultExceptionType);

    }

    public RUNNER featureWithDefaultException(boolean condition, String feature) {
        return condition
                ? featureWithDefaultException(feature)
                : newInstance(environments, configMetadata, defaultExceptionType);

    }

    public RUNNER featureWithDefaultException(String feature) {
        final FeatureMetadata metadata = new FeatureMetadataDefaultException(feature);
        addFeatureMetadata(metadata);
        return newInstance(environments, configMetadata, defaultExceptionType);
    }

    public RUNNER featureWithException(
            boolean condition,
            String feature,
            Class<? extends RuntimeException> exceptionType) {

        return condition
                ? featureWithException(feature, exceptionType)
                : newInstance(environments, configMetadata, defaultExceptionType);
    }

    public RUNNER featureWithException(
            String feature,
            Class<? extends RuntimeException> exType) {

        ExceptionUtil.checkExceptionConstructorType(exType);
        final FeatureMetadata metadata = new FeatureMetadataException(feature, exType);
        addFeatureMetadata(metadata);
        return newInstance(environments, configMetadata, defaultExceptionType);
    }

    public RUNNER featureWithValue(boolean condition,
                                   String feature,
                                   Object valueToReturn) {
        return condition
                ? featureWithValue(feature, valueToReturn)
                : newInstance(environments, configMetadata, defaultExceptionType);
    }

    public RUNNER featureWithValue(String feature,
                                   Object valueToReturn) {
        final FeatureMetadata metadata = new FeatureMetadataValue(feature, valueToReturn);
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
     * one parameter, (@{@link FeatureInvocation})
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

    FeatureProcessorBase newFeatureProcessor(Set<FeatureMetadata> featureMetadataSet) {

        FeatureProcessorBase processor = newFeatureProcessorInstance();
        if (featureMetadataSet != null) {
            featureMetadataSet.stream()
                    .filter(fm -> fm instanceof FeatureMetadataBehaviour)
                    .map(fm -> (FeatureMetadataBehaviour) fm)
                    .peek(fm -> logFeature("APPLIED feature {} with behaviour",
                            fm.getFeature()))
                    .forEach(fmb -> processor.featureWithBehaviour(
                            fmb.getFeature(),
                            fmb.getBehaviour()));

            featureMetadataSet.stream()
                    .filter(fm -> fm instanceof FeatureMetadataException)
                    .map(fm -> (FeatureMetadataException) fm)
                    .peek(fm -> logFeature("APPLIED feature {} with exception {}",
                            fm.getFeature(), fm.getException()))
                    .forEach(fme -> processor.featureWithException(
                            fme.getFeature(),
                            fme.getException()
                    ));

            final Class<? extends RuntimeException> exceptionType =
                    getDefaultExceptionType();

            featureMetadataSet.stream()
                    .filter(fm -> fm instanceof FeatureMetadataDefaultException)
                    .map(fm -> (FeatureMetadataDefaultException) fm)
                    .peek(fm -> logFeature("APPLIED feature {} with default exception {}",
                            fm.getFeature(), exceptionType))
                    .forEach(fmde -> processor
                            .featureWithException(fmde.getFeature(), exceptionType));

            featureMetadataSet.stream()
                    .filter(fm -> fm instanceof FeatureMetadataValue)
                    .map(fm -> (FeatureMetadataValue) fm)
                    .peek(fm -> logFeature("APPLIED feature {} with value {}",
                            fm.getFeature(), fm.getValueToReturn()))
                    .forEach(fmv -> processor.featureWithValue(
                            fmv.getFeature(),
                            fmv.getValueToReturn())
                    );
        }
        return processor;

    }

    private void logFeature(String format, String feature, Object... args) {
        logger.info(format, feature, args);
    }

    private Class<? extends RuntimeException> getDefaultExceptionType() {
        return this.defaultExceptionType != null
                ? defaultExceptionType
                : DEFAULT_EXCEPTION_TYPE;
    }

    protected abstract RUNNER newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> newDefaultExceptionType);

    protected abstract FeatureProcessorBase newFeatureProcessorInstance();
}
