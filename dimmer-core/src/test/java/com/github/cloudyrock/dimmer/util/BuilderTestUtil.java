package com.github.cloudyrock.dimmer.util;

import com.github.cloudyrock.dimmer.logic.BehaviourBuilder;
import com.github.cloudyrock.dimmer.logic.DimmerBuilder;

import static com.github.cloudyrock.dimmer.util.ConstantsTestUtil.*;
import static org.junit.Assert.assertEquals;

public final class BuilderTestUtil {

    private BuilderTestUtil() {

    }

    public static void setUp() {
        setUp(null, null);
    }

    public static <T extends RuntimeException> void setUp(Class<T> defaultException, String executedEnv) {
        BehaviourBuilder builder = DimmerBuilder
                .environments(DEV_ENVIRONMENT, DEFAULT_ENVIRONMENT)
                .featureWithBehaviour(FEATURE_FIXED, OPERATION_BEHAVIOUR, f -> BEHAVIOUR_VALUE)
                .featureWithBehaviour(FEATURE_FIXED, OPERATION_BEHAVIOUR_THROWING_EXCEPTION, f -> {throw new DummyRuntimeException();})
                .featureWithBehaviour(FEATURE_FIXED, OPERATION_BEHAVIOUR_CHECKING_FEATURE_INVOCATION, f -> {
                    assertEquals(FEATURE_FIXED, f.getFeature());
                    assertEquals(OPERATION_BEHAVIOUR_CHECKING_FEATURE_INVOCATION, f.getOperation());
                    assertEquals("operationWithBehaviourCheckingInvocation", f.getMethodName());
                    assertEquals(String.class, f.getReturnType());
                    assertEquals(TestFeaturedClass.class, f.getDeclaringType());
                    assertEquals("value-1", f.getArgs()[0]);
                    assertEquals(new ArgumentClass("value1"), f.getArgs()[1]);
                    return BEHAVIOUR_VALUE;
                })
                .featureWithValue(FEATURE_FIXED, OPERATION_VALUE, TOGGLED_OFF_VALUE)
                .featureWithValue(FEATURE_FIXED, OPERATION_VALUE_NULL, null)
                .featureWithValue(FEATURE_FIXED, OPERATION_VALUE_MISMATCHING, 1L)
                .featureWithCustomException(FEATURE_FIXED, OPERATION_CUSTOM_EXCEPTION, CustomException.class)
                .featureWithDefaultException(FEATURE_FIXED, OPERATION_DEFAULT_EXCEPTION)
                .featureWithBehaviour(FEATURE_FIXED, OPERATION_RETURNS_CUSTOM_OBJECT, f-> new TestFeaturedClass.ReturnedClassChild(CHILD_VALUE))

                //conditional configuration non executing(false)
                .featureWithBehaviourConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_BEHAVIOUR, f -> BEHAVIOUR_VALUE)
                .featureWithValueConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_VALUE, TOGGLED_OFF_VALUE)
                .featureWithCustomExceptionConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_CUSTOM_EXCEPTION, CustomException.class)
                .featureWithDefaultExceptionConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_DEFAULT_EXCEPTION)

                //conditional configuration executing(true)
                .featureWithBehaviourConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_BEHAVIOUR, f -> BEHAVIOUR_VALUE)
                .featureWithValueConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_VALUE, TOGGLED_OFF_VALUE)
                .featureWithCustomExceptionConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_CUSTOM_EXCEPTION, CustomException.class)
                .featureWithDefaultExceptionConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_DEFAULT_EXCEPTION)
                .withProperties(LOCAL_CONFIG_FILE);

        run(setExceptionIfRequired(defaultException, builder), executedEnv);
    }

    private static <T extends RuntimeException> BehaviourBuilder setExceptionIfRequired(Class<T> defaultException, BehaviourBuilder builder) {
        if(defaultException != null) {
            builder = builder.setDefaultExceptionType(defaultException);
        }
        return builder;
    }

    private static void run(BehaviourBuilder builder, String executedEnv) {
        if(executedEnv != null ) {
            builder.runWithEnvironment(executedEnv);
        } else {
            builder.runWithDefaultEnvironment();
        }
    }
}
