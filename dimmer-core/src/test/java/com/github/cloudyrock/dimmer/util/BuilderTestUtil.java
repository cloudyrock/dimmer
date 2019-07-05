package com.github.cloudyrock.dimmer.util;

import com.github.cloudyrock.dimmer.logic.BehaviourBuilder;
import com.github.cloudyrock.dimmer.logic.DimmerBuilder;

import static com.github.cloudyrock.dimmer.util.ConstantsTestUtil.*;
import static org.junit.Assert.assertEquals;

public final class BuilderTestUtil {

    private BuilderTestUtil() {

    }

    public static BehaviourBuilder basicSetUp() {
        return DimmerBuilder
                .environments(DEV_ENVIRONMENT, DEFAULT_ENVIRONMENT)
                .featureWithBehaviour(FEATURE_FIXED, OPERATION_BEHAVIOUR, f -> BEHAVIOUR_VALUE)
                .featureWithValue(FEATURE_FIXED, OPERATION_VALUE, TOGGLED_OFF_VALUE)
                .featureWithCustomException(FEATURE_FIXED, OPERATION_CUSTOM_EXCEPTION, CustomException.class)
                .featureWithDefaultException(FEATURE_FIXED, OPERATION_DEFAULT_EXCEPTION)

                //conditional configuration non executing(false)
                .featureWithBehaviourConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_BEHAVIOUR, f -> BEHAVIOUR_VALUE)
                .featureWithValueConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_VALUE, TOGGLED_OFF_VALUE)
                .featureWithCustomExceptionConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_CUSTOM_EXCEPTION, CustomException.class)
                .featureWithDefaultExceptionConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_DEFAULT_EXCEPTION)

                //conditional configuration executing(true)
                .featureWithBehaviourConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_BEHAVIOUR, f -> BEHAVIOUR_VALUE)
                .featureWithValueConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_VALUE, TOGGLED_OFF_VALUE)
                .featureWithCustomExceptionConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_CUSTOM_EXCEPTION, CustomException.class)
                .featureWithDefaultExceptionConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_DEFAULT_EXCEPTION);

    }

//    private static <T extends RuntimeException> BehaviourBuilder setExceptionIfRequired(Class<T> defaultException, BehaviourBuilder builder) {
//        if(defaultException != null) {
//            builder = builder.setDefaultExceptionType(defaultException);
//        }
//        return builder;
//    }
//
//    private static void run(BehaviourBuilder builder, String executedEnv) {
//        if(executedEnv != null ) {
//            builder.runWithEnvironment(executedEnv);
//        } else {
//            builder.runWithDefaultEnvironment();
//        }
//    }
}
