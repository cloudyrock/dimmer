package com.github.cloudyrock.dimmer.samples.configuration;

import com.github.cloudyrock.dimmer.DimmerBuilder;
import com.github.cloudyrock.dimmer.FeatureExecutor;
import com.github.cloudyrock.dimmer.samples.controller.model.UserApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;


@Configuration
public class DimmerConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DimmerConfiguration.class);

    public static final String ADD_USER = "ADD_USER";
    public static final String GET_USERS = "GET_USERS";

    public static final String DEV = "dev";
    public static final String DEFAULT = "default";
    public static final String PROD = "prod";

    @Value("${endpointAvailableForIntegration}")
    private String endpointAvailableForIntegration;

    /**
     * This example executes the Default profile, where the configuration has all features executed in their normal
     * behaviour without any toggling.
     */
    @Bean
    @Profile(DEFAULT)
    public FeatureExecutor dimmerBuilderDev(Environment environment) {
        LOGGER.debug("Starting Dimmer Builder on environment {}", environment.getActiveProfiles()[0]);
        return DimmerBuilder.local()
                .defaultEnvironment()
                .buildWithDefaultEnvironment();
    }

    @Bean
    @Profile(DEV)
    public FeatureExecutor dimmerBuilderDefault(Environment environment) {
        LOGGER.debug("Starting Dimmer Builder on environment {}", environment.getActiveProfiles()[0]);
        return DimmerBuilder.local()
                .environments(DEV)
                    .featureWithException(GET_USERS, NotImplementedException.class)
                    .featureWithValue(ADD_USER, new UserApiResponse(1L, "MOCKED VALUE"))
                .build(DEV);
    }

    @Bean
    @Profile(PROD)
    public FeatureExecutor dimmerBuilderProd(Environment environment) {
        LOGGER.debug("Starting Dimmer Builder on environment {}", environment.getActiveProfiles()[0]);
        return DimmerBuilder.local()
                .environments(PROD)
                .featureWithDefaultException(isIntegrationAvailable(), ADD_USER)
                .build(PROD);
    }

    // Conditional example of a behaviour: configure the feature behaviour depending on the value of a parameter in configuration
    // Some other examples where this could be applied would be trying to reach out a service or business logic.
    private boolean isIntegrationAvailable() {
        LOGGER.info("Endpoint {} is available", endpointAvailableForIntegration);
        if (endpointAvailableForIntegration != null && !endpointAvailableForIntegration.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

}
