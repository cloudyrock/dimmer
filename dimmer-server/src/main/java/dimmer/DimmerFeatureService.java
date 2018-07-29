package dimmer;

import com.github.cloudyrock.dimmer.FeatureInvocation;

public interface DimmerFeatureService {

    Object invokeFeatureConfigurationForEnvironment(String environment, String feature, FeatureInvocation featureInvocation);
}
