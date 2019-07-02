package com.github.cloudyrock.dimmer.builder;

import com.github.cloudyrock.dimmer.*;
import com.github.cloudyrock.dimmer.exception.DimmerConfigException;
import com.github.cloudyrock.dimmer.exception.DimmerInvocationException;
import com.github.cloudyrock.dimmer.behaviour.*;
import com.github.cloudyrock.dimmer.reader.DimmerConfigReader;
import com.github.cloudyrock.dimmer.reader.models.EnvironmentConfig;
import com.github.cloudyrock.dimmer.util.ExceptionUtil;
import org.aspectj.lang.Aspects;

import java.util.*;
import java.util.function.Function;

/**
 * Builder to configure the feature behaviours.
 *
 * @see DimmerConfigException
 * @see DimmerFeature
 */
public final class BehaviourBuilder {

    private static final DimmerLogger logger = new DimmerLogger(BehaviourBuilder.class);

    private static final Class<? extends RuntimeException> DEFAULT_EXCEPTION_TYPE = DimmerInvocationException.class;

    private final Collection<String> environments;

    final Map<String, Set<Behaviour>> configMetadata;

    private final Class<? extends RuntimeException> defaultExceptionType;

    private final DimmerConfigReader dimmerConfigReader;


    BehaviourBuilder(
            Collection<String> environments,
            Map<String, Set<Behaviour>> configMetadata,
            DimmerConfigReader dimmerConfigReader) {
        this(environments, configMetadata, DimmerInvocationException.class, dimmerConfigReader);
    }

    BehaviourBuilder(
            Collection<String> environments,
            Map<String, Set<Behaviour>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType,
            DimmerConfigReader dimmerConfigReader) {
        this.environments = environments;
        this.configMetadata = configMetadata;
        this.defaultExceptionType = defaultExceptionType;
        this.dimmerConfigReader = dimmerConfigReader;
    }

    private BehaviourBuilder newInstance(
            Collection<String> environments,
            Map<String, Set<Behaviour>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType,
            DimmerConfigReader dimmerConfigReader) {
        return new BehaviourBuilder(environments, configMetadata, defaultExceptionType, dimmerConfigReader);
    }


    /**
     * Indicates the list of environments for which the following set of configuration will be applied to
     *
     * @param environments List of environments
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     */
    public BehaviourBuilder environments(String... environments) {
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
    public BehaviourBuilder withProperties(String propertiesPath) {
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
    public BehaviourBuilder featureWithBehaviourConditional(
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
    public BehaviourBuilder featureWithBehaviour(
            String feature,
            String operation,
            Function<FeatureInvocation, Object> behaviour) {
        Preconditions.checkNullOrEmpty(behaviour, "behaviour");
        final Behaviour metadata = new Behaviour(
                feature,
                operation,
                behaviour
        );
        addBehaviour(metadata);
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
    public BehaviourBuilder featureWithDefaultExceptionConditional(boolean interceptingFeature,
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
    public BehaviourBuilder featureWithDefaultException(String feature, String operation) {

        return featureWithBehaviour(
                feature,
                operation,
                signature -> ExceptionUtil.throwException(DimmerInvocationException.class, signature));
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
    public BehaviourBuilder featureWithCustomExceptionConditional(
            boolean interceptingFeature,
            String feature,
            String operation,
            Class<? extends RuntimeException> exceptionType) {

        return interceptingFeature
                ? featureWithCustomException(feature, operation, exceptionType)
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
    public BehaviourBuilder featureWithCustomException(
            String feature,
            String operation,
            Class<? extends RuntimeException> exceptionType) {
        ExceptionUtil.checkExceptionConstructorType(exceptionType);
        return featureWithBehaviour(feature, operation, signature-> ExceptionUtil.throwException(exceptionType, signature));
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
    public BehaviourBuilder featureWithValueConditional(boolean interceptingFeature,
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
    public BehaviourBuilder featureWithValue(String feature,
                                             String operation,
                                             Object valueToReturn) {
        return featureWithBehaviour(feature, operation, signature -> valueToReturn);

    }

    private void addBehaviour(Behaviour behaviour) {
        Preconditions.checkNullOrEmpty(behaviour);
        Preconditions.checkNullOrEmpty(behaviour.getFeature(), "feature");
        Preconditions.checkNullOrEmpty(behaviour.getOperation(), "operation");
        Preconditions.checkNullOrEmpty(behaviour.getBehaviour(), "behaviour");
        environments.forEach(env -> {
            if (!configMetadata.containsKey(env)) {
                configMetadata.put(env, new HashSet<>());
            }
            configMetadata.get(env).add(behaviour);
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
    public BehaviourBuilder setDefaultExceptionType(
            Class<? extends RuntimeException> newDefaultExceptionType) {
        Preconditions.checkNullOrEmpty(newDefaultExceptionType, "defaultExceptionType");
        ExceptionUtil.checkExceptionConstructorType(newDefaultExceptionType);
        return newInstance(environments, configMetadata, newDefaultExceptionType, dimmerConfigReader);
    }


    Class<? extends RuntimeException> getDefaultExceptionType() {
        return this.defaultExceptionType != null ? defaultExceptionType : DEFAULT_EXCEPTION_TYPE;
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
        final FeatureExecutorImpl featureExecutor = getFeatureExecutor(environmentConfig);
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
