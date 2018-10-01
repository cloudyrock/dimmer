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

    /**
     * Indicates the list of environments for which the following set of configuration will be applied to
     * @param environments List of environments
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     */
    public RUNNER environments(String... environments) {
        Util.checkArgumentNullEmpty(environments, "environments");
        final List<String> envs = Arrays.asList(environments);
        return newInstance(envs, configMetadata, defaultExceptionType);
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
     * @param feature feature covering the generic functionality
     * @param operation operation representing the specific method
     * @param behaviour {@link Function} to be associated with the specified key as behaviour
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     * @see Function
     * @see DimmerConfigException
     */
    public RUNNER featureWithBehaviourConditional(
            boolean interceptingFeature,
            String feature,
            String operation,
            Function<FeatureInvocation, Object> behaviour) {

        return interceptingFeature
                ? featureWithBehaviour(feature, operation, behaviour)
                : newInstance(environments, configMetadata, defaultExceptionType);

    }


    /**
     * If the specified feature is not already associated
     * with a behaviour(or is mapped to null), associates it with the given {@link Function}
     * that represents the desired behaviour, otherwise it's ignored.
     * <p>
     * Notice that the function that represents the feature's behaviour must ensure compatibility
     * with the real method's returning type or a {@link DimmerConfigException} will be thrown.
     *
     * @param feature feature covering the generic functionality
     * @param operation operation representing the specific method
     * @param behaviour {@link Function} to be associated with the specified key as behaviour
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     * @see Function
     * @see DimmerConfigException
     */
    public RUNNER featureWithBehaviour(
            String feature,
            String operation,
            Function<FeatureInvocation, Object> behaviour) {

        final BehaviourFeatureMetadata metadata = new BehaviourFeatureMetadata(
                feature,
                operation,
                behaviour
        );
        addFeatureMetadata(metadata);
        return newInstance(environments, configMetadata, defaultExceptionType);

    }


    /**
     * If interceptingFeature is true and the specified feature is not already associated 
     * with a behaviour(or is mapped to null), associates it with the default exception, 
     * otherwise it's ignored.
     *
     * @param interceptingFeature if true indicates the configuration must be added, ignore otherwise
     * @param feature feature covering the generic functionality
     * @param operation operation representing the specific method
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     */
    public RUNNER featureWithDefaultExceptionConditional(boolean interceptingFeature,
                                                         String feature,
                                                         String operation) {
        return interceptingFeature
                ? featureWithDefaultException(feature, operation)
                : newInstance(environments, configMetadata, defaultExceptionType);

    }

    /**
     * If the specified feature is not already associated
     * with a behaviour(or is mapped to null), associates it with the default exception,
     * otherwise it's ignored.
     *
     * @param feature feature covering the generic functionality
     * @param operation operation representing the specific method
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     */
    public RUNNER featureWithDefaultException(String feature, String operation) {
        final FeatureMetadata metadata = new DefaultExceptionFeatureMetadata(
                feature,
                operation);
        addFeatureMetadata(metadata);
        return newInstance(environments, configMetadata, defaultExceptionType);
    }
    
    /**
     * If interceptingFeature is true and the specified feature is not already associated 
     * with a behaviour(or is mapped to null), associates it with the given exception and 
     * returns true, otherwise it's ignored.
     * <p>
     * Notice the exception type must have either an empty constructor or a contractor with only
     * one parameter, {@link FeatureInvocation}
     *
     *
     * @param interceptingFeature if true indicates the configuration must be added, ignore otherwise
     * @param feature feature covering the generic functionality
     * @param operation operation representing the specific method
     * @param exceptionType exception type to be associated with the specified key
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     * @see FeatureInvocation
     */
    public RUNNER featureWithExceptionConditional(
            boolean interceptingFeature,
            String feature,
            String operation,
            Class<? extends RuntimeException> exceptionType) {

        return interceptingFeature
                ? featureWithException(feature, operation, exceptionType)
                : newInstance(environments, configMetadata, defaultExceptionType);
    }

    /**
     * If the specified feature is not already associated 
     * with a behaviour(or is mapped to null), associates it with the given exception and 
     * returns true, otherwise it's ignored.
     * <p>
     * Notice the exception type must have either an empty constructor or a contractor with only
     * one parameter, {@link FeatureInvocation}
     *
     *
     * @param feature feature covering the generic functionality
     * @param operation operation representing the specific method
     * @param exceptionType exception type to be associated with the specified key
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     * @see FeatureInvocation
     */
    public RUNNER featureWithException(
            String feature,
            String operation,
            Class<? extends RuntimeException> exceptionType) {

        ExceptionUtil.checkExceptionConstructorType(exceptionType);
        final FeatureMetadata metadata = new ExceptionFeatureMetadata(
                feature, operation, exceptionType);
        addFeatureMetadata(metadata);
        return newInstance(environments, configMetadata, defaultExceptionType);
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
     * @param feature feature covering the generic functionality
     * @param operation operation representing the specific method
     * @param valueToReturn value to be associated with the specified key
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     */
    public RUNNER featureWithValueConditional(boolean interceptingFeature,
                                              String feature,
                                              String operation,
                                              Object valueToReturn) {
        return interceptingFeature
                ? featureWithValue(feature, operation, valueToReturn)
                : newInstance(environments, configMetadata, defaultExceptionType);
    }

    /**
     * If the specified feature is not already associated 
     * with a behaviour(or is mapped to null), associates it with the given value and returns 
     * true, otherwise it's ignored.
     * <p>
     * Notice that the value must be compatibility with the real method's returning type
     * or a {@link DimmerConfigException} will be thrown.
     *
     * @param feature feature covering the generic functionality
     * @param operation operation representing the specific method
     * @param valueToReturn value to be associated with the specified key
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     */
    public RUNNER featureWithValue(String feature,
                                   String operation,
                                   Object valueToReturn) {
        final FeatureMetadata metadata =
                new ValueFeatureMetadata(feature, operation, valueToReturn);
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
     * Switch the default exception to the given one. This method can be executed several
     * times, but only the last one will be taken into account.
     * <p/>
     * Notice the exception type must have either an empty constructor or a contractor with only
     * one parameter, {@link FeatureInvocation}
     *
     * @param newDefaultExceptionType new default exception type
     *
     * @return A new immutable instance of a DimmerFeatureConfigurable with the current configuration applied.
     */
    public RUNNER setDefaultExceptionType(
            Class<? extends RuntimeException> newDefaultExceptionType) {
        Util.checkArgumentNullEmpty(newDefaultExceptionType, "defaultExceptionType");
        ExceptionUtil.checkExceptionConstructorType(newDefaultExceptionType);
        return newInstance(environments, configMetadata, newDefaultExceptionType);
    }

    Class<? extends RuntimeException> getDefaultExceptionType() {
        return this.defaultExceptionType != null
                ? defaultExceptionType
                : DEFAULT_EXCEPTION_TYPE;
    }

    protected abstract RUNNER newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> newDefaultExceptionType);

}
