package com.github.cloudyrock.dimmer;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DimmerFeatureConfigurableITest {

    private final Function<FeatureInvocation, ?> behaviour1 = FeatureInvocation::getArgs;
    private final String value = "VALUE";
    private final String feature1 = "feature1";
    private final String feature2 = "feature2";
    private final String feature3 = "feature3";
    private final String feature4 = "feature4";
    private final String operation = "opertaion";

    private DummyDimmerFeatureConfigurable runner;

    @Before
    public void setUp() {
        runner = new DummyDimmerFeatureConfigurable();
    }

    @Test
    public void shouldApplyConfigToRightEnvironment_ifNoFlag_WhenSettingConfiguration() {

        runner.environments("env1", "env2")
                .featureWithBehaviour(feature1, operation, behaviour1)
                .environments("env3")
                .featureWithValue(feature2, operation, value)
                .environments("env4")
                .featureWithBehaviour(feature1, operation, behaviour1)
                .featureWithValue(feature2, operation, value)
                .featureWithException(feature3, operation, RuntimeException.class)
                .featureWithDefaultException(feature4, operation);

        checkFeatureConfigApplied();

    }

    @Test
    public void shouldApplyConfigToRightEnvironment_ifFlagTrue_WhenSettingConfiguration() {
        setConfigWithFlag(true);
        checkFeatureConfigApplied();
    }

    @Test
    public void shouldNotApplyConfigToRightEnvironment_ifFlagFalse_WhenSettingConfiguration() {
        setConfigWithFlag(false);
        assertNull(runner.configMetadata.get("env1"));
        assertNull(runner.configMetadata.get("env2"));
        assertNull(runner.configMetadata.get("env3"));
        assertNull(runner.configMetadata.get("env4"));
    }

    private void setConfigWithFlag(boolean intercepting) {
        runner.environments("env1", "env2")
                .featureWithBehaviour(intercepting, feature1, operation, behaviour1)
                .environments("env3")
                .featureWithValue(intercepting, feature2, operation, value)
                .environments("env4")
                .featureWithBehaviour(intercepting, feature1, operation, behaviour1)
                .featureWithValue(intercepting, feature2, operation, value)
                .featureWithException(intercepting, feature3, operation, RuntimeException.class)
                .featureWithDefaultException(intercepting, feature4, operation);

    }

    private void checkFeatureConfigApplied() {
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