package com.github.cloudyrock.dimmer.samples;

import com.github.cloudyrock.dimmer.DimmerBuilder;
import com.github.cloudyrock.dimmer.DimmerFeature;
import com.github.cloudyrock.dimmer.FeatureExecutor;

import java.util.Optional;

/**
 * This example provides a basic configuration for using the Dimmer project in your application without any external
 * requirements.
 * <p>
 * Use cases covered in this example are:
 * 1) Defining and Configuring Feature toggles to:
 * a. Have the feature enabled and run the real method;
 * b. Have the feature disabled and return a value;
 * c. Have the feature disabled and return a custom behaviour;
 * d. Have the feature disabled and return a custom Exception;
 * 2) Configuring feature toggles for multiple Environment;
 */
public class StandaloneConfigurationExample {

    protected static final String USER_MANAGEMENT_GET_USERS = "USER_MANAGEMENT_GET_USERS";
    protected static final String USER_MANAGEMENT_ADD_USER = "USER_MANAGEMENT_ADD_USER";
    protected static final String USER_MANAGEMENT_GET_USER_DETAILS = "USER_MANAGEMENT_GET_USER_DETAILS";
    protected static final String USER_MANAGEMENT_UPDATE_USER_DETAILS = "USER_MANAGEMENT_UPDATE_USER_DETAILS";
    protected static final String USER_MANAGEMENT_REMOVE_USER = "USER_MANAGEMENT_REMOVE_USER";
    protected static final String MOCKED_BEHAVIOUR_VALUE = "Mocked behaviour value";
    protected static final String REAL_VALUE = "Real Value";

    protected enum Environment {DEFAULT, DEV, INT, PREPROD, PROD}

    public static void main(String[] environmentArg) {

        final Environment environment = extractEnvironmentFromArg(environmentArg);
        final StandaloneConfigurationExample standaloneConfigurationExample = new StandaloneConfigurationExample(environment);

        System.out.println(standaloneConfigurationExample.getUserList());
        System.out.println(standaloneConfigurationExample.addUser());
        System.out.println(standaloneConfigurationExample.getUserDetails());
        System.out.println(standaloneConfigurationExample.updateUser());
        System.out.println(standaloneConfigurationExample.removeUser());
    }

    protected StandaloneConfigurationExample(Environment runDefaultEnvironment) {
        if (runDefaultEnvironment == null || runDefaultEnvironment.equals(Environment.DEFAULT)) {
            initialiseLocalDefaultEnvironmentDimmerConfiguration();
        } else {
            initialiseCustomEnvironmentsDimmerConfiguration(runDefaultEnvironment);
        }
    }

    protected static FeatureExecutor initialiseLocalDefaultEnvironmentDimmerConfiguration() {
        // Provides the next configuration for any environment it has been deployed to
        return DimmerBuilder.local()
                .defaultEnvironment()
                .featureWithValue(USER_MANAGEMENT_ADD_USER, MOCKED_BEHAVIOUR_VALUE)
                .featureWithBehaviour(USER_MANAGEMENT_GET_USER_DETAILS, featureInvocation -> MOCKED_BEHAVIOUR_VALUE)
                .featureWithDefaultException(USER_MANAGEMENT_UPDATE_USER_DETAILS)
                .featureWithException(USER_MANAGEMENT_REMOVE_USER, MyRuntimeException.class)
                .buildWithDefaultEnvironment();
    }

    protected static FeatureExecutor initialiseCustomEnvironmentsDimmerConfiguration(Environment env) {
        // Configuration changes depending on the environment
        return DimmerBuilder.local()
                .environments(Environment.DEV.name())
                    // Run features only for a mocked environment
                    // If the value is not configured for an environment, it will run the real method (i.e. USER_MANAGEMENT_GET_USERS feature)
                    .featureWithValue(USER_MANAGEMENT_ADD_USER, MOCKED_BEHAVIOUR_VALUE)
                    .featureWithBehaviour(USER_MANAGEMENT_GET_USER_DETAILS, featureInvocation -> MOCKED_BEHAVIOUR_VALUE)
                    .featureWithDefaultException(USER_MANAGEMENT_UPDATE_USER_DETAILS)
                    .featureWithException(USER_MANAGEMENT_REMOVE_USER, MyRuntimeException.class)
                .environments(Environment.INT.name())
                    // Conditionals! Run real features when values are false. Custom logic could be applied here.
                    .featureWithValue(activateFeatureForAdminUsers(), USER_MANAGEMENT_ADD_USER, MOCKED_BEHAVIOUR_VALUE)
                    .featureWithException(false, USER_MANAGEMENT_REMOVE_USER, MyRuntimeException.class)
                .environments(Environment.PREPROD.name(), Environment.PROD.name())
                    //Disable one of the features, but run the rest
                    .featureWithException(activateFeatureForAdminUsers(), USER_MANAGEMENT_REMOVE_USER, MyRuntimeException.class)
                .build(env.name());
    }

    @DimmerFeature(USER_MANAGEMENT_GET_USERS)
    protected String getUserList() {
        return USER_MANAGEMENT_GET_USERS + REAL_VALUE;
    }

    @DimmerFeature(USER_MANAGEMENT_ADD_USER)
    protected String addUser() {
        return USER_MANAGEMENT_ADD_USER + REAL_VALUE;
    }

    @DimmerFeature(USER_MANAGEMENT_GET_USER_DETAILS)
    protected String getUserDetails()  {
        return USER_MANAGEMENT_GET_USER_DETAILS + REAL_VALUE;
    }

    @DimmerFeature(USER_MANAGEMENT_UPDATE_USER_DETAILS)
    protected String updateUser() {
        return USER_MANAGEMENT_UPDATE_USER_DETAILS + REAL_VALUE;
    }

    @DimmerFeature(USER_MANAGEMENT_REMOVE_USER)
    protected String removeUser() {
        return USER_MANAGEMENT_REMOVE_USER + REAL_VALUE;
    }

    private static Environment extractEnvironmentFromArg(String[] environmentArg) {
        try {
            final String environment = environmentArg[0];
            return Environment.valueOf(environment);
        } catch (IllegalArgumentException | NullPointerException e) {
            return Environment.DEFAULT;
        }
    }

    private static boolean activateFeatureForAdminUsers() {
        return true;
    }
}
