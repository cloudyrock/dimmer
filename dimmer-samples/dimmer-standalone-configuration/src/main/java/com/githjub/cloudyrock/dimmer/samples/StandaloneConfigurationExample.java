package com.githjub.cloudyrock.dimmer.samples;

import com.github.cloudyrock.dimmer.DimmerBuilder;
import com.github.cloudyrock.dimmer.DimmerFeature;

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
 * 2) Configuring feature toggles for multiple environments;
 */
public class StandaloneConfigurationExample {

    private static final String USER_MANAGEMENT_FEATURE1 = "USER_MANAGEMENT_ADD_USER";
    private static final String USER_MANAGEMENT_FEATURE2 = "USER_MANAGEMENT_GET_USER_DETAILS";
    private static final String USER_MANAGEMENT_FEATURE3 = "USER_MANAGEMENT_UPDATE_USER_DETAILS";
    private static final String USER_MANAGEMENT_FEATURE4 = "USER_MANAGEMENT_REMOVE_USER";

    public static void main(String[] args) {


        DimmerBuilder.local()
                .defaultEnvironment()
                .featureWithValue(USER_MANAGEMENT_FEATURE1, "Mocked behaviour value")
                .featureWithBehaviour(USER_MANAGEMENT_FEATURE2, featureInvocation -> "{}")
                .featureWithDefaultException(USER_MANAGEMENT_FEATURE3)
                .featureWithException(USER_MANAGEMENT_FEATURE4, ExampleRuntimeException.class)
                .buildWithDefaultEnvironment();

        final StandaloneConfigurationExample standaloneConfigurationExample = new StandaloneConfigurationExample();

        System.out.println(standaloneConfigurationExample.addUser());
        System.out.println(standaloneConfigurationExample.getUserDetails());

        try{
            standaloneConfigurationExample.updateUser();
        }catch (RuntimeException e){
            System.out.println("Call to " + USER_MANAGEMENT_FEATURE3 + " threw the default exception: " + e);
        }

        try{
            standaloneConfigurationExample.removeUser();
        }catch (RuntimeException e){
            System.out.println("Call to " + USER_MANAGEMENT_FEATURE4 + " threw the custom exception: " + e);
        }
    }


    @DimmerFeature(USER_MANAGEMENT_FEATURE1)
    private String addUser() {
        return "Real Value";
    }

    @DimmerFeature(USER_MANAGEMENT_FEATURE2)
    private String getUserDetails() {
        return "Real Value";
    }

    @DimmerFeature(USER_MANAGEMENT_FEATURE3)
    private String updateUser() {
        return "Real Value";
    }

    @DimmerFeature(USER_MANAGEMENT_FEATURE4)
    private String removeUser() {
        return "Real Value";
    }

}
