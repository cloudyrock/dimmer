package com.github.cloudyrock.dimmer;


import com.github.cloudyrock.dimmer.logic.BehaviourBuilder;
import com.github.cloudyrock.dimmer.logic.DimmerBuilder;
import com.github.cloudyrock.dimmer.util.TestFeaturedClass;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.cloudyrock.dimmer.util.ConstantsTestUtil.*;
import static org.junit.Assert.assertEquals;


public class DimmerBuilderConfigFileIT {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static final TestFeaturedClass testFeaturedClass = new TestFeaturedClass();

    private BehaviourBuilder getBuilderWithBasicConfiguration(String file) {
        return DimmerBuilder
                .environments(DEV_ENVIRONMENT, DEFAULT_ENVIRONMENT, ENVIRONMENT_WITH_TOGGLE_ON_FEATURES)
                .withProperties(file);
    }

    @Test
    @DisplayName("Should throw config exception when config file's sintaxis is invalid")
    public void shouldThrowConfigExceptionWhenConfigFilesSintaxisIsInvalid() {
        expectedException.expect(DimmerConfigException.class);
        expectedException.expectMessage("Dimmer configuration file could not be read");
        getBuilderWithBasicConfiguration("dimmer-sintactically-invalid.json").runWithDefaultEnvironment();
    }


    @Test
    @DisplayName("Should not throw exception when config file's feature list is empty")
    public void shouldNoThrowConfigurationExceptionWhenFeatureListIsEmpty() {
        getBuilderWithBasicConfiguration("dimmer-with-empty-feature-list.yml").runWithDefaultEnvironment();
    }

    @Test
    @DisplayName("Should not throw exception when building a behaviour which is not in the config file")
    public void shouldNoThrowConfigurationExceptionWhenBuildingBehaviourForNonExistingFeatureInConfigFile() {
        getBuilderWithBasicConfiguration(LOCAL_CONFIG_FILE)
                .featureWithValue("NON_EXISTING_FEATURE", "NON_EXISTING_OPERATION", "whateverValue")
                .runWithEnvironment(DEV_ENVIRONMENT);
    }

    @Test
    @DisplayName("Should not throw exception when not building a behaviour which appears in the config file")
    public void shouldNoThrowConfigurationExceptionWhenNotBuildingBehaviourForExistingFeatureInConfigFile() {
        getBuilderWithBasicConfiguration(LOCAL_CONFIG_FILE)
                .runWithEnvironment(DEV_ENVIRONMENT);
    }

    @Test
    @DisplayName("Should run real method when the feature is in toggledOn and behaviour configured")
    public void shouldRunRealMethodWhenFeatureIsToggledOn() {
        getBuilderWithBasicConfiguration(LOCAL_CONFIG_FILE)
                .featureWithValue(FEATURE_FIXED, OPERATION_BEHAVIOUR, BEHAVIOUR_VALUE)
                .runWithEnvironment(ENVIRONMENT_WITH_TOGGLE_ON_FEATURES);
        Assert.assertEquals(REAL_VALUE, testFeaturedClass.operationWithBehaviourFixed());
    }

    @Test
    @DisplayName("Should run real method when the feature is in toggledOn and behaviour no configured")
    public void shouldRunRealMethodWhenFeatureIsToggledOnOn() {
        getBuilderWithBasicConfiguration(LOCAL_CONFIG_FILE).runWithEnvironment(ENVIRONMENT_WITH_TOGGLE_ON_FEATURES);
        Assert.assertEquals(REAL_VALUE, testFeaturedClass.operationWithBehaviourFixed());
    }
}
