package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DimmerConfigException;

import java.util.Map;

public final class DimmerFeatureServiceImpl implements DimmerFeatureService {

    private final Map<String, FeatureServerExecutor> featureExecutorEnvironmentsMap;

    public DimmerFeatureServiceImpl(Map<String, FeatureServerExecutor> featureExecutorEnvironmentsMap) {
        this.featureExecutorEnvironmentsMap = featureExecutorEnvironmentsMap;
    }

    @Override
    public Object invokeFeatureForEnvironment(String environment,
                                              String feature,
                                              FeatureInvocation featureInvocation) {

        checkEnvironmentAndFeatureConfig(environment, feature);
        return featureExecutorEnvironmentsMap.get(environment).executeDimmerFeature(feature, featureInvocation);
    }

    private  void checkEnvironmentAndFeatureConfig(String environment, String feature) {

        if (featureExecutorEnvironmentsMap.get(environment) == null) {
            throw new DimmerConfigException("The environment " + environment + " is not configured.");
        } else if (!featureExecutorEnvironmentsMap.get(environment).isFeatureConfigured(feature)) {
            throw new DimmerConfigException("The feature " + feature + " is not configured");
        }
    }
}
