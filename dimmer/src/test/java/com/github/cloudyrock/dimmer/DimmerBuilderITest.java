package com.github.cloudyrock.dimmer;

import org.junit.Test;

import java.util.Set;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class DimmerBuilderITest {

    private static final String operation = "operation";
    private static final String ENV_1 = "env1";
    private static final String ENV_2 = "env2";
    private static final String ENV_3 = "env3";
    private static final String ENV_4 = "env4";

    @Test
    public void shouldApplyConfigToRightEnvironment() {

        final Function<FeatureInvocation, ?> behaviour1 = FeatureInvocation::getArgs;
        final String value = "VALUE";

        final String feature1 = "feature1";
        final String feature2 = "feature2";
        final String feature3 = "feature3";
        final String feature4 = "feature4";

        final FeatureConfigurationBuilder builder = DimmerBuilder.local()
                .environments(ENV_1, ENV_2)
                .featureWithBehaviour(feature1, operation, behaviour1)
                .environments(ENV_3)
                .featureWithValue(feature2, operation, value)
                .environments(ENV_4)
                .featureWithBehaviour(feature1, operation, behaviour1)
                .featureWithValue(feature2, operation, value)
                .featureWithException(feature3, operation, RuntimeException.class)
                .featureWithDefaultException(feature4, operation);

        final Set<FeatureMetadata> env1Metadata = builder.configMetadata.get(ENV_1);
        assertEquals(1, env1Metadata.size());
        env1Metadata.stream()
                .filter(fm -> fm instanceof FeatureMetadataBehaviour)
                .map(fm -> (FeatureMetadataBehaviour) fm)
                .filter(fm -> fm.getBehaviour().equals(behaviour1))
                .filter(fm -> feature1.equals(fm.getFeature()))
                .findAny()
                .get();

        final Set<FeatureMetadata> env2Metadata = builder.configMetadata.get(ENV_2);
        assertEquals(1, env2Metadata.size());
        env2Metadata.stream()
                .filter(fm -> fm instanceof FeatureMetadataBehaviour)
                .map(fm -> (FeatureMetadataBehaviour) fm)
                .filter(fm -> fm.getBehaviour().equals(behaviour1))
                .filter(fm -> feature1.equals(fm.getFeature()))
                .findAny()
                .get();

        final Set<FeatureMetadata> env3Metadata = builder.configMetadata.get(ENV_3);
        assertEquals(1, env3Metadata.size());
        env3Metadata.stream()
                .filter(fm -> fm instanceof FeatureMetadataValue)
                .map(fm -> (FeatureMetadataValue) fm)
                .filter(fm -> value.equals(fm.getValueToReturn()))
                .filter(fm -> feature2.equals(fm.getFeature()))
                .findAny()
                .get();

        final Set<FeatureMetadata> env4Metadata = builder.configMetadata.get(ENV_4);
        assertEquals(4, env4Metadata.size());
        env4Metadata.stream()
                .filter(fm -> fm instanceof FeatureMetadataValue)
                .map(fm -> (FeatureMetadataValue) fm)
                .filter(fm -> value.equals(fm.getValueToReturn()))
                .filter(fm -> feature2.equals(fm.getFeature()))
                .findAny()
                .get();

        env4Metadata.stream()
                .filter(fm -> fm instanceof FeatureMetadataBehaviour)
                .map(fm -> (FeatureMetadataBehaviour) fm)
                .filter(fm -> fm.getBehaviour().equals(behaviour1))
                .filter(fm -> feature1.equals(fm.getFeature()))
                .findAny()
                .get();

        env4Metadata.stream()
                .filter(fm -> fm instanceof FeatureMetadataException)
                .map(fm -> (FeatureMetadataException) fm)
                .filter(fm -> RuntimeException.class.equals(fm.getException()))
                .filter(fm -> feature3.equals(fm.getFeature()))
                .findAny()
                .get();

        env4Metadata.stream()
                .filter(fm -> fm instanceof FeatureMetadataDefaultException)
                .map(fm -> (FeatureMetadataDefaultException) fm)
                .filter(fm -> feature4.equals(fm.getFeature()))
                .findAny()
                .get();

    }
}