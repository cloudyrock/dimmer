package com.github.cloudyrock.dimmer;

public class FeatureLocalExecutor extends FeatureProcessorBase<FeatureInvocationLocal>
        implements FeatureExecutor {

    @Override
    public Object executeDimmerFeature(String feature,
                                       FeatureInvocationLocal featureInvocation,
                                       MethodCaller realMethod) throws Throwable {
        if (isFeatureConfigured(feature)) {
            return runFeature(feature, featureInvocation);
        } else {
            return realMethod.call();
        }
    }



}
