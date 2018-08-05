package com.github.cloudyrock.dimmer;

public interface DimmerFeatureService {

    Object invokeFeatureForEnvironment(String environment, String feature, FeatureInvocation featureInvocation);
}
