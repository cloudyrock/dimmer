package com.github.cloudyrock.dimmer;

public class Main {

    private static final String FEATURE_NAME = "feature_name";

    private static final String ENV1 = "e1", ENV2 = "e2", ENV3 = "e3";

    //args[0] provides the environment where the application is running
    public static void main(String... args) {
        DimmerBuilder
                .local()
                .defaultEnvironment()
                .featureWithDefaultException(FEATURE_NAME)
                .environments(ENV1, ENV2)
                .featureWithValue(FEATURE_NAME, "value for environments ENV1 and ENV2")
                .build(args[0]);

        new Main().runFeaturedMethod();
    }

    @DimmerFeature(FEATURE_NAME)
    private String runFeaturedMethod() {
        return "real value";
    }

}
