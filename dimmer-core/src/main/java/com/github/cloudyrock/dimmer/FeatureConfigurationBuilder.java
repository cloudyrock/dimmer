package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.reader.DimmerConfigReader;
import com.github.cloudyrock.dimmer.reader.models.EnvironmentConfig;
import org.aspectj.lang.Aspects;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Builder to configure the feature behaviours.
 *
 * @see DimmerConfigException
 * @see DimmerFeature
 */
final class FeatureConfigurationBuilder {

    private static final DimmerLogger logger = new DimmerLogger(FeatureConfigurationBuilder.class);

    protected static final Class<? extends RuntimeException> DEFAULT_EXCEPTION_TYPE =
            DimmerInvocationException.class;

    protected final Collection<String> environments;

    protected final Map<String, Set<FeatureMetadata>> configMetadata;

    protected final Class<? extends RuntimeException> defaultExceptionType;

    protected final DimmerConfigReader dimmerConfigReader;



    static FeatureConfigurationBuilder withEnvironmentsAndMetadata(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            DimmerConfigReader dimmerConfigReader) {
        return new FeatureConfigurationBuilder(environments, configMetadata, DimmerInvocationException.class, dimmerConfigReader);
    }

    protected FeatureConfigurationBuilder(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType,
            DimmerConfigReader dimmerConfigReader) {
        this.environments = environments;
        this.configMetadata = configMetadata;
        this.defaultExceptionType = defaultExceptionType;
        this.dimmerConfigReader = dimmerConfigReader;
    }

    protected FeatureConfigurationBuilder newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType,
            DimmerConfigReader dimmerConfigReader) {
        return new FeatureConfigurationBuilder(environments, configMetadata, defaultExceptionType, dimmerConfigReader);
    }


    /**
     * Indicates the list of environments for which the following set of configuration will be applied to
     *
     * @param environments List of environments
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     */
    public FeatureConfigurationBuilder environments(String... environments) {
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
    public FeatureConfigurationBuilder withProperties(String propertiesPath) {
        dimmerConfigReader.setPropertiesLocation(propertiesPath);
        return this;
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
        FeatureExecutor featureExecutor = getFeatureExecutor(environmentConfig);
        Aspects.aspectOf(DimmerAspect.class).setFeatureExecutor(featureExecutor);
        logger.info("Dimmer Aspect running");
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


    private FeatureExecutor getFeatureExecutor(EnvironmentConfig environmentConfig) {
        logger.debug("Building local executor");

        return new FeatureExecutorImpl(
                loadConfigMetadata(environmentConfig.getName(), environmentConfig),
                getDefaultExceptionType());
    }

    private Set<FeatureMetadata> loadConfigMetadata(String environment, EnvironmentConfig environmentConfig) {

        //Load configuration list of behaviours set up programmatically
        final Set<FeatureMetadata> featureBehaviours = configMetadata.get(environment);

        //Merge both
        return mergeConfigurations(environmentConfig, featureBehaviours);
    }

    private Set<FeatureMetadata> mergeConfigurations(EnvironmentConfig environmentConfig, Set<FeatureMetadata> featureBehaviours) {

        //No behaviours defined for environment
        if (featureBehaviours == null) {
            logger.warn("No behaviours have been defined for selected environment.");
            return null;
        }

        List<String> featuresForEnvironmentConfigFile = environmentConfig.getFeatureIntercept();
        //No features set in the environment of the config file
        if (featuresForEnvironmentConfigFile.isEmpty()) {
            logger.warn("No Features intercepted in configuration file for environment.");
            return featureBehaviours;
        }

        //Creates feature metadata only with selected interceptors from Config file
        return featureBehaviours.stream()
                .filter(featureMetadata -> featuresForEnvironmentConfigFile.contains(featureMetadata.getFeature()))
                .peek(featureMetadata -> logger.info("Feature {} will be intercepted.", featureMetadata.getFeature()))
                .collect(Collectors.toSet());
    }

    /****************************************************
     * FROM DimmerFeatureConfigurable
     ****************************************************/

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
    public FeatureConfigurationBuilder featureWithBehaviourConditional(
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
    public FeatureConfigurationBuilder featureWithBehaviour(
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
    public FeatureConfigurationBuilder featureWithDefaultExceptionConditional(boolean interceptingFeature,
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
    public FeatureConfigurationBuilder featureWithDefaultException(String feature, String operation) {
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
    public FeatureConfigurationBuilder featureWithExceptionConditional(
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
    public FeatureConfigurationBuilder featureWithException(
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
    public FeatureConfigurationBuilder featureWithValueConditional(boolean interceptingFeature,
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
    public FeatureConfigurationBuilder featureWithValue(String feature,
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
    public FeatureConfigurationBuilder setDefaultExceptionType(
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

}
