package com.github.cloudyrock.dimmer;

import java.util.*;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public final class DimmerFeatureServiceBuilder extends DimmerFeatureConfigurable<DimmerFeatureServiceBuilder>
        implements DimmerEnvironmentConfigurable<DimmerFeatureServiceBuilder> {

    public static DimmerFeatureServiceBuilder createInstance() {
        return new DimmerFeatureServiceBuilder(new HashSet<>(), new HashMap<>(), DEFAULT_EXCEPTION_TYPE);
    }

    private DimmerFeatureServiceBuilder(
            Collection<String> environments,
            Map<String, Set<FeatureMetadata>> configMetadata,
            Class<? extends RuntimeException> newDefaultExceptionType) {
        super(environments, configMetadata, newDefaultExceptionType);
    }

    @Override
    protected DimmerFeatureServiceBuilder newInstance(Collection<String> environments,
                                                      Map<String, Set<FeatureMetadata>> configMetadata,
                                                      Class<? extends RuntimeException> newDefaultExceptionType) {

        return new DimmerFeatureServiceBuilder(environments, configMetadata, newDefaultExceptionType);
    }

    @Override
    protected FeatureProcessorBase newFeatureProcessorInstance() {

        return new FeatureServerExecutor();
    }

    public DimmerFeatureService build() throws Throwable {
        final Map<String, FeatureServerExecutor> featureExecutorEnvironmentsMap = environments
                .stream()
                .collect(toMap(identity(), env -> createFeatureExecutor(configMetadata.get(env))));

        return new DimmerFeatureServiceImpl(featureExecutorEnvironmentsMap);
    }

    private FeatureServerExecutor createFeatureExecutor(Set<FeatureMetadata> featureMetadata) {
        return (FeatureServerExecutor) newFeatureProcessor(featureMetadata);
    }

}