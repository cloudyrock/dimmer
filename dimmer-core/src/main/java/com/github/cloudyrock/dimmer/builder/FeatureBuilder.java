package com.github.cloudyrock.dimmer.builder;

import com.github.cloudyrock.dimmer.*;
import com.github.cloudyrock.dimmer.exception.DimmerConfigException;
import com.github.cloudyrock.dimmer.exception.DimmerInvocationException;
import com.github.cloudyrock.dimmer.metadata.*;
import com.github.cloudyrock.dimmer.reader.DimmerConfigReader;
import com.github.cloudyrock.dimmer.reader.models.EnvironmentConfig;
import org.aspectj.lang.Aspects;

import java.util.*;
import java.util.function.Function;

/**
 * Builder to configure the feature behaviours.
 *
 * @see DimmerConfigException
 * @see DimmerFeature
 */
final class FeatureBuilder {

    private static final DimmerLogger logger = new DimmerLogger(FeatureBuilder.class);

    private static final Class<? extends RuntimeException> DEFAULT_EXCEPTION_TYPE = DimmerInvocationException.class;

    private final Collection<String> environments;

    final Map<String, Set<FeatureMetadata>> configMetadata;

    private final Class<? extends RuntimeException> defaultExceptionType;

    private final DimmerConfigReader dimmerConfigReader;


    FeatureBuilder(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            DimmerConfigReader dimmerConfigReader) {
        this(environments, configMetadata, DimmerInvocationException.class, dimmerConfigReader);
    }

    FeatureBuilder(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType,
            DimmerConfigReader dimmerConfigReader) {
        this.environments = environments;
        this.configMetadata = configMetadata;
        this.defaultExceptionType = defaultExceptionType;
        this.dimmerConfigReader = dimmerConfigReader;
    }

    private FeatureBuilder newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType,
            DimmerConfigReader dimmerConfigReader) {
        return new FeatureBuilder(environments, configMetadata, defaultExceptionType, dimmerConfigReader);
    }


    /**
     * Indicates the list of environments for which the following set of configuration will be applied to
     *
     * @param environments List of environments
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     */
    public FeatureBuilder environments(String... environments) {
        Preconditions.checkNullOrEmpty(environments, "environments");
        final List<String> envs = Arrays.asList(environments);
        return newInstance(envs, configMetadata, defaultExceptionType, dimmerConfigReader);
    }

    /**
     * Sets the Dimmer configuration Reader. The DimmerConfigReader is used to load the dimmer properties
     * from the desired location.
     *
     * @param propertiesPath
     * @return
     */
    public FeatureBuilder withProperties(String propertiesPath) {
        dimmerConfigReader.setPropertiesLocation(propertiesPath);
        return this;
    }



    /**
     * If interceptingFeature is true and the specified feature is not already associated
     * with a behaviour(or is mapped to null), associates it with the given {@link Function}
     * that represents the desired behaviour, otherwise it's ignored.
     * <p>
     * Notice that the function that represents the feature's behaviour must ensure compatibility
     * with the real method's returning type or a {@link DimmerConfigException} will be thrown.
     *
     * @param interceptingFeature if true indicates the configuration must be added, ignore otherwise
     * @param feature             feature covering the generic functionality
     * @param operation           operation representing the specific method
     * @param behaviour           {@link Function} to be associated with the specified key as behaviour
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     * @see Function
     * @see DimmerConfigException
     */
    public FeatureBuilder featureWithBehaviourConditional(
            boolean interceptingFeature,
            String feature,
            String operation,
            Function<FeatureInvocation, Object> behaviour) {

        return interceptingFeature
                ? featureWithBehaviour(feature, operation, behaviour)
                : newInstance(environments, configMetadata, defaultExceptionType, dimmerConfigReader);

    }


    /**
     * If the specified feature is not already associated
     * with a behaviour(or is mapped to null), associates it with the given {@link Function}
     * that represents the desired behaviour, otherwise it's ignored.
     * <p>
     * Notice that the function that represents the feature's behaviour must ensure compatibility
     * with the real method's returning type or a {@link DimmerConfigException} will be thrown.
     *
     * @param feature   feature covering the generic functionality
     * @param operation operation representing the specific method
     * @param behaviour {@link Function} to be associated with the specified key as behaviour
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     * @see Function
     * @see DimmerConfigException
     */
    public FeatureBuilder featureWithBehaviour(
            String feature,
            String operation,
            Function<FeatureInvocation, Object> behaviour) {

        final BehaviourFeatureMetadata metadata = new BehaviourFeatureMetadata(
                feature,
                operation,
                behaviour
        );
        addFeatureMetadata(metadata);
        return newInstance(environments, configMetadata, defaultExceptionType, dimmerConfigReader);

    }


    /**
     * If interceptingFeature is true and the specified feature is not already associated
     * with a behaviour(or is mapped to null), associates it with the default exception,
     * otherwise it's ignored.
     *
     * @param interceptingFeature if true indicates the configuration must be added, ignore otherwise
     * @param feature             feature covering the generic functionality
     * @param operation           operation representing the specific method
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     */
    public FeatureBuilder featureWithDefaultExceptionConditional(boolean interceptingFeature,
                                                                 String feature,
                                                                 String operation) {
        return interceptingFeature
                ? featureWithDefaultException(feature, operation)
                : newInstance(environments, configMetadata, defaultExceptionType, dimmerConfigReader);

    }

    /**
     * If the specified feature is not already associated
     * with a behaviour(or is mapped to null), associates it with the default exception,
     * otherwise it's ignored.
     *
     * @param feature   feature covering the generic functionality
     * @param operation operation representing the specific method
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     */
    public FeatureBuilder featureWithDefaultException(String feature, String operation) {
        final FeatureMetadata metadata = new DefaultExceptionFeatureMetadata(
                feature,
                operation);
        addFeatureMetadata(metadata);
        return newInstance(environments, configMetadata, defaultExceptionType, dimmerConfigReader);
    }

    /**
     * If interceptingFeature is true and the specified feature is not already associated
     * with a behaviour(or is mapped to null), associates it with the given exception and
     * returns true, otherwise it's ignored.
     * <p>
     * Notice the exception type must have either an empty constructor or a contractor with only
     * one parameter, {@link FeatureInvocation}
     *
     * @param interceptingFeature if true indicates the configuration must be added, ignore otherwise
     * @param feature             feature covering the generic functionality
     * @param operation           operation representing the specific method
     * @param exceptionType       exception type to be associated with the specified key
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     * @see FeatureInvocation
     */
    public FeatureBuilder featureWithExceptionConditional(
            boolean interceptingFeature,
            String feature,
            String operation,
            Class<? extends RuntimeException> exceptionType) {

        return interceptingFeature
                ? featureWithException(feature, operation, exceptionType)
                : newInstance(environments, configMetadata, defaultExceptionType, dimmerConfigReader);
    }

    /**
     * If the specified feature is not already associated
     * with a behaviour(or is mapped to null), associates it with the given exception and
     * returns true, otherwise it's ignored.
     * <p>
     * Notice the exception type must have either an empty constructor or a contractor with only
     * one parameter, {@link FeatureInvocation}
     *
     * @param feature       feature covering the generic functionality
     * @param operation     operation representing the specific method
     * @param exceptionType exception type to be associated with the specified key
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     * @see FeatureInvocation
     */
    public FeatureBuilder featureWithException(
            String feature,
            String operation,
            Class<? extends RuntimeException> exceptionType) {

        ExceptionUtil.checkExceptionConstructorType(exceptionType);
        final FeatureMetadata metadata = new ExceptionFeatureMetadata(
                feature, operation, exceptionType);
        addFeatureMetadata(metadata);
        return newInstance(environments, configMetadata, defaultExceptionType, dimmerConfigReader);
    }


    /**
     * If interceptingFeature is true and the specified feature is not already associated
     * with a behaviour(or is mapped to null), associates it with the given value and returns
     * true, otherwise it's ignored.
     * <p>
     * Notice that the value must be compatibility with the real method's returning type
     * or a {@link DimmerConfigException} will be thrown.
     *
     * @param interceptingFeature if true indicates the configuration must be added, ignore otherwise
     * @param feature             feature covering the generic functionality
     * @param operation           operation representing the specific method
     * @param valueToReturn       value to be associated with the specified key
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     */
    public FeatureBuilder featureWithValueConditional(boolean interceptingFeature,
                                                      String feature,
                                                      String operation,
                                                      Object valueToReturn) {
        return interceptingFeature
                ? featureWithValue(feature, operation, valueToReturn)
                : newInstance(environments, configMetadata, defaultExceptionType, dimmerConfigReader);
    }

    /**
     * If the specified feature is not already associated
     * with a behaviour(or is mapped to null), associates it with the given value and returns
     * true, otherwise it's ignored.
     * <p>
     * Notice that the value must be compatibility with the real method's returning type
     * or a {@link DimmerConfigException} will be thrown.
     *
     * @param feature       feature covering the generic functionality
     * @param operation     operation representing the specific method
     * @param valueToReturn value to be associated with the specified key
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     */
    public FeatureBuilder featureWithValue(String feature,
                                           String operation,
                                           Object valueToReturn) {
        final FeatureMetadata metadata =
                new ValueFeatureMetadata(feature, operation, valueToReturn);
        addFeatureMetadata(metadata);
        return newInstance(environments, configMetadata, defaultExceptionType, dimmerConfigReader);

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
     * Switch the default exception to the given one. This method can be executed several
     * times, but only the last one will be taken into account.
     * <p/>
     * Notice the exception type must have either an empty constructor or a contractor with only
     * one parameter, {@link FeatureInvocation}
     *
     * @param newDefaultExceptionType new default exception type
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     */
    public FeatureBuilder setDefaultExceptionType(
            Class<? extends RuntimeException> newDefaultExceptionType) {
        Preconditions.checkNullOrEmpty(newDefaultExceptionType, "defaultExceptionType");
        ExceptionUtil.checkExceptionConstructorType(newDefaultExceptionType);
        return newInstance(environments, configMetadata, newDefaultExceptionType, dimmerConfigReader);
    }


    Class<? extends RuntimeException> getDefaultExceptionType() {
        return this.defaultExceptionType != null
                ? defaultExceptionType
                : DEFAULT_EXCEPTION_TYPE;
    }



    /**
     * Builds a feature executor with the given environment, inject it to the
     * dimmer aspect (which will intercept the calls to all methods annotated
     * with {@link DimmerFeature}) and return it.
     *
     * @param environment Environment to run
     * @return Feature executor
     */
    public void run(String environment) {

        final EnvironmentConfig environmentConfig;
        try {
            environmentConfig = dimmerConfigReader.loadEnvironmentOrDefault(environment);
        } catch (FileConfigException ex) {
            throw new DimmerConfigException(ex);
        }
        FeatureExecutorImpl featureExecutor = getFeatureExecutor(environmentConfig);
        Aspects.aspectOf(DimmerAspect.class).setFeatureExecutor(featureExecutor);
        logger.info("Dimmer Aspect running");
        featureExecutor.getBroker().start();

    }

    /**
     * Builds a feature executor with the default environment, inject it to the
     * dimmer aspect (which will intercept the calls to all methods annotated
     * with {@link DimmerFeature}) and return it.
     *
     * @return Feature executor
     */
    public void runWithDefaultEnvironment() {
        run(null);
    }


    private FeatureExecutorImpl getFeatureExecutor(EnvironmentConfig environmentConfig) {
        logger.debug("Building local executor");
        FeatureBroker broker = new FeatureBroker(
                new StaticLocalFeatureObservable(new HashSet<>(environmentConfig.getFeatureIntercept())),
                configMetadata.getOrDefault(environmentConfig.getName(), new HashSet<>()),
                defaultExceptionType);
        return new FeatureExecutorImpl(broker);
    }


}
