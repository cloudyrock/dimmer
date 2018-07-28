package com.github.cloudyrock.dimmer;

interface FeatureExecutor {

    Object executeDimmerFeature(
            String feature,
            FeatureInvocation featureInvocation,
            MethodCaller realMethod) throws Throwable;
}
