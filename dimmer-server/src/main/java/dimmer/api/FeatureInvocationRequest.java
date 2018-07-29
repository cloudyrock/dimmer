package dimmer.api;

import com.github.cloudyrock.dimmer.FeatureInvocation;

public class FeatureInvocationRequest {

    private String environment;
    private String featureName;
    private FeatureInvocation featureInvocation;

    public FeatureInvocationRequest(String environment, String featureName, FeatureInvocation featureInvocation) {
        this.environment = environment;
        this.featureName = featureName;
        this.featureInvocation = featureInvocation;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getFeatureName() {
        return featureName;
    }

    public FeatureInvocation getFeatureInvocation() {
        return featureInvocation;
    }

}
