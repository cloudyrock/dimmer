package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.reader.DimmerConfigReader;
import com.github.cloudyrock.dimmer.reader.models.DimmerConfig;
import com.github.cloudyrock.dimmer.reader.models.EnvironmentConfig;
import org.aspectj.lang.Aspects;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Builder to configure the feature behaviours.
 *
 * @see DimmerConfigException
 * @see DimmerFeature
 */
final class FeatureConfigurationBuilder extends DimmerFeatureConfigurable<FeatureConfigurationBuilder>
        implements DimmerEnvironmentConfigurable<FeatureConfigurationBuilder> {

    private static final DimmerLogger LOGGER = new DimmerLogger(FeatureConfigurationBuilder.class);
    public static final String DIMMER_CONFIG_EXCEPTION_ENVIRONMENT_DOESNT_EXIST_IN_CONFIG_FILE =
            "The selected environment doesn't exist in the Dimmer configuration file. ";

    static FeatureConfigurationBuilder withEnvironmentsAndMetadata(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            DimmerConfigReader dimmerConfigReader
    ) {
        return new FeatureConfigurationBuilder(environments, configMetadata, DimmerInvocationException.class, dimmerConfigReader);
    }

    private FeatureConfigurationBuilder(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> newDefaultExceptionType,
            DimmerConfigReader dimmerConfigReader) {
        super(environments, configMetadata, newDefaultExceptionType, dimmerConfigReader);
    }

    @Override
    protected FeatureConfigurationBuilder newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType,
            DimmerConfigReader dimmerConfigReader
    ) {
        return new FeatureConfigurationBuilder(environments, configMetadata, defaultExceptionType, dimmerConfigReader);
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
     * @param environment Environment to build
     * @return Feature executor
     */
    public FeatureExecutorImpl build(String environment) {

        final DimmerConfig dimmerConfig = dimmerConfigReader.loadConfiguration();
        final EnvironmentConfig environmentConfig = loadMatchingEnvironmentFromConfig(dimmerConfig, environment);
        return getFeatureExecutor(environment, environmentConfig);
    }

    /**
     * Builds a feature executor with the default environment, inject it to the
     * dimmer aspect (which will intercept the calls to all methods annotated
     * with {@link DimmerFeature}) and return it.
     *
     * @return Feature executor
     */
    public FeatureExecutorImpl buildWithDefaultEnvironment() {

        final DimmerConfig dimmerConfig = dimmerConfigReader.loadConfiguration();
        final Map.Entry<String,EnvironmentConfig> environmentConfig = dimmerConfigReader.getDefaultEnvironment(dimmerConfig);

        return getFeatureExecutor(environmentConfig.getKey(), environmentConfig.getValue());
    }

    private FeatureExecutorImpl getFeatureExecutor(String environment, EnvironmentConfig environmentConfig) {
        LOGGER.info("Building local executor");

        final FeatureExecutorImpl executor = new FeatureExecutorImpl(
                loadConfigMetadata(environment, environmentConfig),
                getDefaultExceptionType());

        Aspects.aspectOf(DimmerAspect.class).setFeatureExecutor(executor);
        LOGGER.info("Dimmer Aspect running");
        return executor;
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
            LOGGER.warn("No behaviours have been defined for selected environment.");
            return featureBehaviours;
        }

        List<String> featuresForEnvironmentConfigFile = environmentConfig.getFeatureIntercept();
        //No features set in the environment of the config file
        if (featuresForEnvironmentConfigFile == null || featuresForEnvironmentConfigFile.isEmpty()) {
            LOGGER.warn("No Features intercepted in configuration file for environment.");
            return featureBehaviours;
        }

        //Creates feature metadata only with selected interceptors from Config file
        return featureBehaviours.stream()
                .filter(featureMetadata -> featuresForEnvironmentConfigFile.contains(featureMetadata.getFeature()))
                .peek(featureMetadata -> LOGGER.info("Feature {} will be intercepted.", featureMetadata.getFeature()))
                .collect(Collectors.toSet());
    }

    private static EnvironmentConfig loadMatchingEnvironmentFromConfig(DimmerConfig dimmerConfig, String env) {
        final Map<String, EnvironmentConfig> dimmerConfigEnvironments = dimmerConfig.getEnvironments();
        final EnvironmentConfig environmentConfig = dimmerConfigEnvironments.get(env);

        if (environmentConfig == null) {
            throw new DimmerConfigException(
                    String.format(DIMMER_CONFIG_EXCEPTION_ENVIRONMENT_DOESNT_EXIST_IN_CONFIG_FILE +
                            "Please add the environment %s in the configuration file", env));
        }
        return environmentConfig;
    }
}
