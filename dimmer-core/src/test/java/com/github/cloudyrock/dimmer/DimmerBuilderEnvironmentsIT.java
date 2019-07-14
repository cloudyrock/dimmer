package com.github.cloudyrock.dimmer;


import com.github.cloudyrock.dimmer.logic.BehaviourBuilder;
import com.github.cloudyrock.dimmer.logic.DimmerBuilder;
import com.github.cloudyrock.dimmer.util.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.matchers.Contains;

import static com.github.cloudyrock.dimmer.util.ConstantsTestUtil.*;
import static org.junit.Assert.*;


public class DimmerBuilderEnvironmentsIT {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static final TestFeaturedClassEnvironments testedClass = new TestFeaturedClassEnvironments();

    private static final String FEATURE_ALL_ENVS = "FEATURE_ALL_ENVS";
    private static final String FEATURE_DEV_ENV = "FEATURE_DEV_ENV";
    private static final String FEATURE_DEFAULT_ENV = "FEATURE_DEFAULT_ENV";

    private static final String OPERATION_ENV_BEHAVIOUR = "OPERATION_ENV_BEHAVIOUR";
    private static final String OPERATION_ENV_VALUE = "OPERATION_ENV_VALUE";


    private BehaviourBuilder getBuilderWithBasicConfiguration() {
        return DimmerBuilder
                .environments(DEV_ENVIRONMENT, DEFAULT_ENVIRONMENT)
                .featureWithBehaviour(FEATURE_ALL_ENVS, OPERATION_ENV_BEHAVIOUR, f-> "NEUTRAL_ENVS_BEHAVIOUR")
                .featureWithValue(FEATURE_ALL_ENVS, OPERATION_ENV_VALUE, "NEUTRAL_ENVS_VALUE")
                .environments(DEV_ENVIRONMENT)
                .featureWithBehaviour(FEATURE_DEV_ENV, OPERATION_ENV_BEHAVIOUR, f-> "DEV_ENV_BEHAVIOUR")
                .featureWithValue(FEATURE_DEV_ENV, OPERATION_ENV_VALUE, "DEV_ENV_VALUE")
                .environments(DEFAULT_ENVIRONMENT)
                .featureWithBehaviour(FEATURE_DEFAULT_ENV, OPERATION_ENV_BEHAVIOUR, f-> "DEFAULT_ENV_BEHAVIOUR")
                .featureWithValue(FEATURE_DEFAULT_ENV, OPERATION_ENV_VALUE, "DEFAULT_ENV_VALUE")
                .withProperties("dimmer-config-file-for-environments-test.yml");
    }

    @Test
    @DisplayName("Should throw default exception when it's conditional-true default-exception-configured")
    public void shouldThrowConfigurationExceptionWhenRunningNonExistingEnvironment() {
        expectedException.expect(DimmerConfigException.class);
        expectedException.expectMessage(new Contains("The selected environment doesn't exist in the Dimmer configuration file"));
        expectedException.expectMessage(new Contains("WRONG-ENV"));
        getBuilderWithBasicConfiguration().runWithEnvironment("WRONG-ENV");
    }

    @Test
    @DisplayName("Should return toggled-off values for features/operations for the default and all environments")
    public void shouldReturnToggledOffValuesForDefaultAndAllEnvironments() {
        BehaviourBuilder builder = getBuilderWithBasicConfiguration();
        builder.runWithDefaultEnvironment();

        assertEquals("NEUTRAL_ENVS_BEHAVIOUR", testedClass.operationForAllEnvsBehaviour());
        assertEquals("NEUTRAL_ENVS_VALUE", testedClass.operationForAllEnvsValue());

        assertEquals("DEFAULT_ENV_BEHAVIOUR", testedClass.operationDefaultEnvBehaviour());
        assertEquals("DEFAULT_ENV_VALUE", testedClass.operationDefaultEnvValue());

        try {
            testedClass.operationDevEnvBehaviour();
            fail();
        } catch (DimmerInvocationException ex) {
            // Exception successfully thrown
        }

        try {
            testedClass.operationDevEnvValue();
            fail();
        } catch (DimmerInvocationException ex) {
            // Exception successfully thrown
        }
    }

    @Test
    @DisplayName("Should return toggled-off values for features/operations for the dev and both-configured environments")
    public void shouldReturnToggledOffValuesForDevAndAllEnvironments() {
        getBuilderWithBasicConfiguration().runWithEnvironment(DEV_ENVIRONMENT);

        assertEquals("NEUTRAL_ENVS_BEHAVIOUR", testedClass.operationForAllEnvsBehaviour());
        assertEquals("NEUTRAL_ENVS_VALUE", testedClass.operationForAllEnvsValue());

        assertEquals("DEV_ENV_BEHAVIOUR", testedClass.operationDevEnvBehaviour());
        assertEquals("DEV_ENV_VALUE", testedClass.operationDevEnvValue());

        try {
            testedClass.operationDefaultEnvBehaviour();
            fail();
        } catch (DimmerInvocationException ex) {
            // Exception successfully thrown
        }

        try {
            testedClass.operationDefaultEnvValue();
            fail();
        } catch (DimmerInvocationException ex) {
            // Exception successfully thrown
        }
    }

    public static class TestFeaturedClassEnvironments extends TestFeaturedClass{

        @DimmerFeature(value = FEATURE_ALL_ENVS, op = OPERATION_ENV_BEHAVIOUR)
        String operationForAllEnvsBehaviour() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_ALL_ENVS, op = OPERATION_ENV_VALUE)
        String operationForAllEnvsValue() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_DEV_ENV, op = OPERATION_ENV_BEHAVIOUR)
        String operationDevEnvBehaviour() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_DEV_ENV, op = OPERATION_ENV_VALUE)
        String operationDevEnvValue() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_DEFAULT_ENV, op = OPERATION_ENV_BEHAVIOUR)
        String operationDefaultEnvBehaviour() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_DEFAULT_ENV, op = OPERATION_ENV_VALUE)
        String operationDefaultEnvValue() {
            return REAL_VALUE;
        }
    }
}
