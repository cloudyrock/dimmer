package com.github.cloudyrock.dimmer;

import java.math.BigDecimal;
import java.util.function.Function;

public class Main {

    private static final String FEATURE_NAME = "feature_name";

    public static class MyException extends RuntimeException {

        private final String feature;
        private final String methodName;

        public MyException(FeatureInvocation featureInvocation) {
            this.feature = featureInvocation.getFeature();
            this.methodName = featureInvocation.getMethodName();
        }

        @Override
        public String getMessage() {
            return String.format("Feature %s called via method %s", feature, methodName);
        }
    }

    public static void main(String... args) {
        DimmerBuilder
                .local()
                .defaultEnvironment()
                .featureWithException(FEATURE_NAME, MyException.class)
                .buildWithDefaultEnvironment();

        new Main().runFeaturedMethod();
        //It will throw exception and print message 'Feature feature_name called via method runFeaturedMethod'
    }

    @DimmerFeature(FEATURE_NAME)
    private String runFeaturedMethod() {
        return "real value";
    }

}
