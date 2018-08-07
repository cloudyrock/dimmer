package com.github.cloudyrock.dimmer;

public interface FeatureExecutor {

    Object executeDimmerFeature(
            String feature,
            FeatureInvocationLocal featureInvocation,
            MethodCaller realMethod) throws Throwable;
}
