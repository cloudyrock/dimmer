package com.github.cloudyrock.dimmer;

public interface FeatureExecutor {

    Object executeDimmerFeature(
            String feature,
            String operation,
            FeatureInvocation featureInvocation,
            MethodCaller realMethod) throws Throwable;
}
