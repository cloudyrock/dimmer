package com.github.cloudyrock.dimmer;

public class FeatureLocalExecutor extends FeatureProcessorBase
        implements FeatureExecutor {

    @Override
    public Object executeDimmerFeature(String feature,
                                       FeatureInvocation featureInvocation,
                                       MethodCaller realMethod) throws Throwable {
        if (isFeatureConfigured(feature)) {
            return runFeature(feature, featureInvocation);
        } else {
            return realMethod.call();
        }
    }



}
