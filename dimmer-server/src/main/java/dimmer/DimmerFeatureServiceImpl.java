package dimmer;

import java.util.Map;

public class DimmerFeatureServiceImpl implements DimmerFeatureService {

    private final Map<String, FeatureExecutor> featureExecutorEnvironmentsMap;

    public DimmerFeatureServiceImpl(Map<String, FeatureExecutor> featureExecutorEnvironmentsMap) {
        this.featureExecutorEnvironmentsMap = featureExecutorEnvironmentsMap;
    }

    @Override
    public Object invokeFeatureConfigurationForEnvironment(String environment,
                                                           String feature,
                                                           FeatureInvocation featureInvocation) {
        checkEnvironment(environment);
        return featureExecutorEnvironmentsMap.get(environment)
                .executeDimmerFeature(feature, null, null);
    }

    private void checkEnvironment(String environment) {

    }
}
