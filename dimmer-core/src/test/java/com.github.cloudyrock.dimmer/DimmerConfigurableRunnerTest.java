package com.github.cloudyrock.dimmer;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class DimmerConfigurableRunnerTest {


    private DummyConfigurableRunner runner;

    @Before
    public void setUp() {
        runner = new DummyConfigurableRunner();
    }

    @Test
    public void shouldApplyConfigToRightEnvironment() {

        final Function<FeatureInvocationBase, ?> behaviour1 = FeatureInvocationBase::getArgs;
        final String value = "VALUE";

        final String feature1 = "feature1";
        final String feature2 = "feature2";
        final String feature3 = "feature3";
        final String feature4 = "feature4";

        runner.environments("env1", "env2")
                .featureWithBehaviour(feature1, behaviour1)
                .environments("env3")
                .featureWithValue(feature2, value)
                .environments("env4")
                .featureWithBehaviour(feature1, behaviour1)
                .featureWithValue(feature2, value)
                .featureWithException(feature3, RuntimeException.class)
                .featureWithDefaultException(feature4);

        final Set<FeatureMetadata> env1Metadata = runner.configMetadata.get("env1");
        assertEquals(1, env1Metadata.size());
        env1Metadata.stream()
                .filter(fm -> fm instanceof FeatureMetadataBehaviour)
                .map(fm -> (FeatureMetadataBehaviour) fm)
                .filter(fm -> fm.getBehaviour().equals(behaviour1))
                .filter(fm -> feature1.equals(fm.getFeature()))
                .findAny()
                .get();

        final Set<FeatureMetadata> env2Metadata = runner.configMetadata.get("env2");
        assertEquals(1, env2Metadata.size());
        env2Metadata.stream()
                .filter(fm -> fm instanceof FeatureMetadataBehaviour)
                .map(fm -> (FeatureMetadataBehaviour) fm)
                .filter(fm -> fm.getBehaviour().equals(behaviour1))
                .filter(fm -> feature1.equals(fm.getFeature()))
                .findAny()
                .get();

        final Set<FeatureMetadata> env3Metadata = runner.configMetadata.get("env3");
        assertEquals(1, env3Metadata.size());
        env3Metadata.stream()
                .filter(fm -> fm instanceof FeatureMetadataValue)
                .map(fm -> (FeatureMetadataValue) fm)
                .filter(fm -> value.equals(fm.getValueToReturn()))
                .filter(fm -> feature2.equals(fm.getFeature()))
                .findAny()
                .get();

        final Set<FeatureMetadata> env4Metadata = runner.configMetadata.get("env4");
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