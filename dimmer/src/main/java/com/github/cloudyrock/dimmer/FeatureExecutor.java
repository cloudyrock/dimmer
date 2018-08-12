package com.github.cloudyrock.dimmer;

public interface FeatureExecutor {

    Object executeDimmerFeature(
            String feature,
            FeatureInvocation featureInvocation,
            MethodCaller realMethod) throws Throwable;
}
