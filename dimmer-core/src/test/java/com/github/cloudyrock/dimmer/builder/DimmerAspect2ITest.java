package com.github.cloudyrock.dimmer.builder;

import com.github.cloudyrock.dimmer.*;
import org.aspectj.lang.Aspects;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

public class DimmerAspect2ITest {
    private static final String DEV = "dev";
    private static final String LOCAL_CONFIG_FILE = "dimmer-it-test.yml";


    private static final String FEATURE_1 = "FEATURE_1";
    private static final String OPERATION_1_BEHAVIOUR = "OPERATION_1_BEHAVIOUR";
    private static final String OPERATION_1_VALUE = "OPERATION_1_VALUE";
    private static final String OPERATION_1_EXCEPTION = "OPERATION_1_EXCEPTION";
    private static final String OPERATION_1_DEFAULT_EXCEPTION = "OPERATION_1_DEFAULT_EXCEPTION";


    private final FeaturedClass featuredClass = new FeaturedClass();


    private static final String TOGGLED_OFF_VALUE = "feature1_value";
    private static final String REAL_VALUE = "real_value";

    @Rule public ExpectedException exception = ExpectedException.none();


    @BeforeClass
    public static void staticSetup() {
        DimmerBuilder
                .environments(DEV)
                .featureWithBehaviour(FEATURE_1, OPERATION_1_BEHAVIOUR, f -> TOGGLED_OFF_VALUE)
                .featureWithValue(FEATURE_1, OPERATION_1_VALUE, TOGGLED_OFF_VALUE)
                .featureWithException(FEATURE_1, OPERATION_1_EXCEPTION, DummyException.class)
                .featureWithDefaultException(FEATURE_1, OPERATION_1_DEFAULT_EXCEPTION)
                .withProperties(LOCAL_CONFIG_FILE)
                .runWithDefaultEnvironment();
    }

    @Test
    public void hasAspect() {
        assertTrue(Aspects.hasAspect(DimmerAspect.class));
    }

    @Test
    @DisplayName("Should run behaviour when featureWithBehaviour")
    public void shouldRunBehaviour() {
        assertEquals(TOGGLED_OFF_VALUE, featuredClass.methodForFeature1());
    }

    class FeaturedClass {

        @DimmerFeature(value = FEATURE_1, op = OPERATION_1_BEHAVIOUR)
        String methodForFeature1() {
            return REAL_VALUE;
        }

    }

    public class RetrunedClassParent {
    }

    public class ReturnedClassChild extends RetrunedClassParent {
    }

}
