package dimmer;

import com.github.cloudyrock.dimmer.DimmerEnvironmentConfigurable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

final class DimmerFeatureServiceBuilder
        extends DimmerFeatureConfigurable<DimmerFeatureServiceBuilder>
        implements DimmerEnvironmentConfigurable<DimmerFeatureServiceBuilder> {

    DimmerFeatureServiceBuilder(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> newDefaultExceptionType) {
        super(environments, configMetadata, newDefaultExceptionType);
    }

    @Override
    protected DimmerFeatureServiceBuilder newInstance(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> newDefaultExceptionType) {

        return new DimmerFeatureServiceBuilder(environments, configMetadata,
                defaultExceptionType);
    }

    //This builder will be configured and this build method executed in a bean injection
    //method in spring config to be used in the
    public DimmerFeatureService build() throws Throwable {
        Map<String, FeatureExecutor> featureExecutorEnvironmentsMap = environments
                .stream()
                .collect(toMap(identity(), env -> newExecutor(configMetadata.get(env))));

        return new DimmerFeatureServiceImpl(featureExecutorEnvironmentsMap);
    }

}