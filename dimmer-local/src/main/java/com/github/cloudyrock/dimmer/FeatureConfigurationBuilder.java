package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.reader.DimmerConfigReader;
import com.github.cloudyrock.dimmer.reader.DimmerConfigReaderYamlImpl;
import com.github.cloudyrock.dimmer.reader.models.DimmerConfig;
import com.github.cloudyrock.dimmer.reader.models.EnvironmentConfig;
import org.aspectj.lang.Aspects;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Builder to configure the feature behaviours.
 *
 * @see DimmerConfigException
 * @see DimmerFeature
 */
public final class FeatureConfigurationBuilder extends DimmerFeatureConfigurable<FeatureConfigurationBuilder>
        implements DimmerEnvironmentConfigurable<FeatureConfigurationBuilder> {

    private static final String DEFAULT_ENV = "DEFAULT_DIMMER_ENV";
    private static final DimmerLogger LOGGER = new DimmerLogger(FeatureConfigurationBuilder.class);

    private DimmerConfigReader dimmerConfigReader;

    static FeatureConfigurationBuilder withDefaultEnvironment() {
        return new FeatureConfigurationBuilder(Collections.singleton(DEFAULT_ENV),
                new HashMap<>(), DimmerInvocationException.class);
    }

    static FeatureConfigurationBuilder withEnvironmentsAndMetadata(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata) {
        return new FeatureConfigurationBuilder(environments, configMetadata, DimmerInvocationException.class);
    }

    private FeatureConfigurationBuilder(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> newDefaultExceptionType) {
        super(environments, configMetadata, newDefaultExceptionType);
    }

    @Override
    protected FeatureConfigurationBuilder newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> defaultExceptionType) {
        return new FeatureConfigurationBuilder(environments, configMetadata, defaultExceptionType);
    }

    /**
     * Sets the Dimmer configuration Reader. The DimmerConfigReader is used to load the dimmer properties
     * from the desired location.
     *
     * @param propertiesReader
     * @return
     */
    public FeatureConfigurationBuilder withPropertiesReader(DimmerConfigReader propertiesReader) {
        this.dimmerConfigReader = propertiesReader;
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
        return getFeatureExecutor(environment, false);
    }

    /**
     * Builds a feature executor with the default environment, inject it to the
     * dimmer aspect (which will intercept the calls to all methods annotated
     * with {@link DimmerFeature}) and return it.
     *
     * @return Feature executor
     */
    public FeatureExecutorImpl buildWithDefaultEnvironment() {
        return getFeatureExecutor(DEFAULT_ENV, true);
    }

    private FeatureExecutorImpl getFeatureExecutor(String environment, boolean isDefaultEnv) {
        LOGGER.info("Building local executor");

        final FeatureExecutorImpl executor = new FeatureExecutorImpl(
                loadConfigMetadata(environment, isDefaultEnv),
                getDefaultExceptionType());

        Aspects.aspectOf(DimmerAspect.class).setFeatureExecutor(executor);
        LOGGER.info("Dimmer Aspect running");
        return executor;
    }

    private Set<FeatureMetadata> loadConfigMetadata(String environment, boolean isDefaultEnv) {

        //Load configuration from Configuration file
        final DimmerConfigReader dimmerConfigReader = getDimmerConfigReader();
        final DimmerConfig dimmerConfig = dimmerConfigReader.loadConfiguration();
        final EnvironmentConfig environmentConfig;

        if (isDefaultEnv) {
            environmentConfig = dimmerConfigReader.getDefaultEnvironment(dimmerConfig);
        } else {
            environmentConfig = loadMatchingEnvironmentFromConfig(dimmerConfig, environment);
        }

        //Load configuration list of behaviours set up programmatically
        final Set<FeatureMetadata> featureBehaviours = configMetadata.get(environment);

        //Merge both
        return mergeConfigurations(environmentConfig, featureBehaviours);
    }

    private Set<FeatureMetadata> mergeConfigurations(EnvironmentConfig environmentConfig, Set<FeatureMetadata> featureBehaviours) {

        //No behaviours defined for environment
        if (featureBehaviours == null) {
            LOGGER.warn("No behaviours have been defined for selected environment.");
            return null;
        }

        List<String> featuresForEnvironmentConfigFile = environmentConfig.getFeatureIntercept();
        //No features set in the environment of the config file
        if (featuresForEnvironmentConfigFile == null || featuresForEnvironmentConfigFile.isEmpty()) {
            LOGGER.warn("No Features intercepted in configuration file for environment.");
            return null;
        }

        //Creates feature metadata only with selected interceptors from Config file
        final Set<FeatureMetadata> featureMetadataSet = new HashSet<>();
        featureBehaviours.forEach(featureMetadata -> {
            if (featuresForEnvironmentConfigFile.contains(featureMetadata.getFeature())) {
                LOGGER.info("Feature {} will be intercepted.", featureMetadata.getFeature());
                featureMetadataSet.add(featureMetadata);
            }
        });
        return featureMetadataSet;
    }

    private DimmerConfigReader getDimmerConfigReader() {
        return dimmerConfigReader != null
                ? dimmerConfigReader
                : new DimmerConfigReaderYamlImpl();
    }

    private EnvironmentConfig loadMatchingEnvironmentFromConfig(DimmerConfig dimmerConfig, String env) {
        final Map<String, EnvironmentConfig> dimmerConfigEnvironments = dimmerConfig.getEnvironments();
        final EnvironmentConfig environmentConfig = dimmerConfigEnvironments.get(env);

        if (environmentConfig == null) {
            throw new DimmerConfigException("The selected environment doesn't exist in the Dimmer configuration file. "
                    + "Please add the environment " + env + " in the configuration file");
        }
        return environmentConfig;
    }
}
