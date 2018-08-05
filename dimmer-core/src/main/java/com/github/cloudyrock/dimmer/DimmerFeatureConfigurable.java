package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DimmerInvocationException;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

abstract class DimmerFeatureConfigurable<RUNNER extends DimmerFeatureConfigurable> {

    protected static final Class<? extends RuntimeException> DEFAULT_EXCEPTION_TYPE =
            DimmerInvocationException.class;

    protected final Collection<String> environments;

    protected final Map<String, Set<FeatureMetadata>> configMetadata;

    protected final Class<? extends RuntimeException> defaultExceptionType;

    protected final DimmerSlf4j logger;

    protected DimmerFeatureConfigurable(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType,
            DimmerSlf4j logger) {

        this.environments = environments;
        this.configMetadata = configMetadata;
        this.defaultExceptionType = defaultExceptionType;
        this.logger = logger;
    }

    public RUNNER environments(String... environments) {
        Util.checkArgumentNullEmpty(environments, "environments");
        return newInstance(
                Arrays.asList(environments),
                this.configMetadata,
                this.defaultExceptionType,
                logger);
    }

    public RUNNER featureWithBehaviour(
            boolean condition,
            String feature,
            Function<FeatureInvocation, ?> behaviour) {
        if (condition) {
            return featureWithBehaviour(feature, behaviour);
        } else {
            return newInstance(this.environments,
                    configMetadata,
                    defaultExceptionType,
                    logger);
        }

    }

    public RUNNER featureWithBehaviour(
            String feature,
            Function<FeatureInvocation, ?> behaviour) {
        final FeatureMetadataBehaviour metadata = new FeatureMetadataBehaviour(
                feature,
                behaviour
        );
        addFeatureMetadata(metadata);
        return newInstance(this.environments, configMetadata,
                this.defaultExceptionType, logger);

    }

    public RUNNER featureWithDefaultException(boolean condition, String feature) {
        if (condition) {
            return featureWithDefaultException(feature);
        } else {
            return newInstance(environments, configMetadata,
                    defaultExceptionType, logger);
        }

    }

    public RUNNER featureWithDefaultException(String feature) {
        final FeatureMetadata metadata = new FeatureMetadataDefaultException(
                feature
        );
        addFeatureMetadata(metadata);
        return newInstance(environments, configMetadata,
                defaultExceptionType, logger);
    }

    public RUNNER featureWithException(
            boolean condition,
            String feature,
            Class<? extends RuntimeException> exceptionType) {
        if (condition) {
            return featureWithException(feature, exceptionType);
        } else {
            return newInstance(environments, configMetadata,
                    defaultExceptionType, logger);
        }
    }

    public RUNNER featureWithException(
            String feature,
            Class<? extends RuntimeException> exceptionType) {
        ExceptionUtil.checkExceptionConstructorType(exceptionType);
        final FeatureMetadata metadata = new FeatureMetadataException(
                feature,
                exceptionType
        );
        addFeatureMetadata(metadata);
        return newInstance(environments, configMetadata,
                defaultExceptionType, logger);
    }

    public RUNNER featureWithValue(boolean condition,
                                   String feature,
                                   Object valueToReturn) {
        if (condition) {
            return featureWithValue(feature, valueToReturn);
        } else {
            return newInstance(environments, configMetadata,
                    defaultExceptionType, logger);
        }
    }

    public RUNNER featureWithValue(String feature,
                                   Object valueToReturn) {
        final FeatureMetadata metadata = new FeatureMetadataValue(
                feature,
                valueToReturn
        );
        addFeatureMetadata(metadata);
        return newInstance(environments, configMetadata,
                defaultExceptionType, logger);

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
        ExceptionUtil.checkExceptionConstructorType(newDefaultExceptionType);
        return newInstance(environments, configMetadata,
                newDefaultExceptionType, logger);
    }

                                                                                                                                                                                                                                                                                                                FeatureProcessorBase newFeatureProcessor(Set<FeatureMetadata> featureMetadataSet) {

        FeatureProcessorBase processor = newFeatureProcessorInstance();
        if (featureMetadataSet != null) {
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

            final Class<? extends RuntimeException> exceptionType =
                    getDefaultExceptionType();

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
        return processor;

    }

    private Class<? extends RuntimeException> getDefaultExceptionType() {
        return this.defaultExceptionType != null
                ? this.defaultExceptionType
                : DEFAULT_EXCEPTION_TYPE;
    }

    protected abstract RUNNER newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> newDefaultExceptionType,
            DimmerSlf4j logger);

    protected abstract FeatureProcessorBase newFeatureProcessorInstance();
}
