package com.github.cloudyrock.dimmer;


import com.github.cloudyrock.dimmer.logic.BehaviourBuilder;
import com.github.cloudyrock.dimmer.logic.DimmerBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.cloudyrock.dimmer.util.ConstantsTestUtil.*;
import static org.junit.Assert.assertEquals;


public class DimmerBuilderConfigFileIT {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    private BehaviourBuilder getBuilderWithBasicConfiguration(String file) {
        return DimmerBuilder
                .environments(DEV_ENVIRONMENT, DEFAULT_ENVIRONMENT)
                .withProperties(file);
    }

    @Test
    @DisplayName("Should throw config exception when config file's sintaxis is invalid")
    public void ShouldThrowConfigExceptionWhenConfigFilesSintaxisIsInvalid() {
        expectedException.expect(DimmerConfigException.class);
        expectedException.expectMessage("Dimmer configuration file could not be read");
        getBuilderWithBasicConfiguration("dimmer-sintactically-invalid.json").runWithDefaultEnvironment();
    }




    @Test
    @DisplayName("Should not throw exception when config file's feature list is empty")
    public void shouldNoThrowConfigurationExceptionWhenFeatureListIsEmpty() {
        expectedException.expect(DimmerConfigException.class);
        expectedException.expectMessage("Environment configuration is empty");
        getBuilderWithBasicConfiguration("dimmer-semantically-invalid.yml").runWithDefaultEnvironment();
    }
}
