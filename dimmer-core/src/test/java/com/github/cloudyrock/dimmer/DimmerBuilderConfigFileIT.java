package com.github.cloudyrock.dimmer;


import com.github.cloudyrock.dimmer.logic.BehaviourBuilder;
import com.github.cloudyrock.dimmer.logic.DimmerBuilder;
import com.github.cloudyrock.dimmer.util.TestFeaturedClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.matchers.Contains;

import static com.github.cloudyrock.dimmer.util.ConstantsTestUtil.*;
import static org.junit.Assert.assertEquals;


public class DimmerBuilderConfigFileIT {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    private BehaviourBuilder getBuilderWithBasicConfiguration() {
        return DimmerBuilder
                .environments(DEV_ENVIRONMENT, DEFAULT_ENVIRONMENT)
                .withProperties("dimmer-invalid.yml");
    }

    @Test
    @DisplayName("Should throw default exception when it's conditional-true default-exception-configured")
    public void shouldThrowConfigurationExceptionWhenRunningNonExistingEnvironment() {
        expectedException.expect(DimmerConfigException.class);
        expectedException.expectMessage("Failed mapping Dimmer configuration file.");
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();
    }

}
