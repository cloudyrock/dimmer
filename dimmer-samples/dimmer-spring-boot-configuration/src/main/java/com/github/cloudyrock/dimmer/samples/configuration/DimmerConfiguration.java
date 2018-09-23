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

    public static final String GET_USERS_CONTROLLER = "getUsersController";
    public static final String CREATE_USER_CONTROLLER = "createUserController";

    public static final String DEV = "dev";
    public static final String DEFAULT = "default";
    public static final String PROD = "prod";

    @Value("${endpointAvailableForIntegration:")
    private String endpointAvailableForIntegration;

    /**
     * This example executes the Default profile, where the configuration has all features executed in their normal
     * behaviour without any toggling.
     */
    @Bean
    @Profile(DEFAULT)
    public FeatureExecutor dimmerBuilderDev(Environment environment) {
        LOGGER.info("Starting Dimmer Builder on environment {}", environment.getActiveProfiles()[0]);
        return DimmerBuilder.local()            //Local picks up the configuration defined in this file instead from a remote location;
                .defaultEnvironment()           //DefaultEnvironment is used to set this configuration regardless of the environment is running on;
                .buildWithDefaultEnvironment(); //This operation creates the instance with the configuration set.

        //NOTE: When instantiated this way, Dimmer will execute the real methods across all definitions as no behaviours
        //have been defined for this configuration when calls are intercepted.
    }

    /**
     * Example configuration for the dev environment. In this example, Dimmer will intercept the calls for two of the
     * features, by throwing a custom exception and a static value when trying to invoke the methods.
     *
     * This method Bean will be instantiated when the system property -Dspring.profiles.active=dev is set.
     *
     * @param environment
     * @return
     */
    @Bean
    @Profile(DEV)
    public FeatureExecutor dimmerBuilderDefault(Environment environment) {
        LOGGER.info("Starting Dimmer Builder on environment {}", environment.getActiveProfiles()[0]);
        return DimmerBuilder.local() //Local picks up the configuration from this service
                .environments(DEV)   //Defines the configuration when running in the Dev environment
                    .featureWithException(GET_USERS, GET_USERS_CONTROLLER, NotImplementedException.class) //Throws the custom REST exception when calling the feature GET_USERS with operation getUsersController instead of calling the real method
                    .featureWithValue(ADD_USER, CREATE_USER_CONTROLLER, new UserApiResponse(1L, "MOCKED VALUE")) //Return the mocked Json component value for feature ADD_USER with operation createUserController instead of calling the real method
                .build(DEV); //Creates a new instance with the development profile configuration.
    }

    /**
     * Example configuration for the dev environment. In this example, Dimmer will intercept the calls for two of the
     * features, by throwing a custom exception and a static value when trying to invoke the methods.
     *
     * This method Bean will be instantiated when the system property -Dspring.profiles.active=dev is set.
     *
     * @param environment
     * @return
     */
    @Bean
    @Profile(PROD)
    public FeatureExecutor dimmerBuilderProd(Environment environment) {
        LOGGER.info("Starting Dimmer Builder on environment {}", environment.getActiveProfiles()[0]);
        return DimmerBuilder.local()  //Local picks up the configuration from this service
                .environments(PROD)   //Defines the configuration for the prod environment
                    .featureWithDefaultExceptionConditional(isIntegrationAvailable(), ADD_USER, CREATE_USER_CONTROLLER)  //Calls the real method or throws an the default Dimmer exception depending on the conditional result
                .build(PROD); //Creates a new instance with the development profile configuration.
    }

    // Conditional example of a behaviour: configure the feature behaviour depending on the value of a parameter in configuration
    // Some other examples where this could be applied would be trying to reach out a service or business logic.
    private boolean isIntegrationAvailable() {
        LOGGER.info("Endpoint {} is available", endpointAvailableForIntegration);
        return  (endpointAvailableForIntegration != null && !endpointAvailableForIntegration.isEmpty());
    }

}
