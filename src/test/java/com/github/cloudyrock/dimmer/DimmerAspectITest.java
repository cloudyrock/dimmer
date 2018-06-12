package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DefaultException;
import com.github.cloudyrock.dimmer.exceptions.DummyException;
import com.github.cloudyrock.dimmer.displayname.DisplayName;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.function.Function;

import static com.github.cloudyrock.dimmer.DimmerFeature.ALWAYS_OFF;
import static com.github.cloudyrock.dimmer.DimmerFeature.DimmerBehaviour.DEFAULT;
import static com.github.cloudyrock.dimmer.DimmerFeature.DimmerBehaviour.RETURN_NULL;
import static com.github.cloudyrock.dimmer.DimmerFeature.DimmerBehaviour.THROW_EXCEPTION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DimmerAspectITest extends DimmerTestBase {

    private static final String REAL_VALUE = "real_value";
    private static final String FEATURE_AND_DEFAULT = "WITH_DEFAULT";
    private static final String FEATURE_AND_NULL = "WITH_RETURN_NULL";
    private static final String FEATURE_AND_DEFAULT_EXCEPTION = "WITH_THROW_EXCEPTION";
    private static final String FEATURE_AND_CUSTOM_EXCEPTION =
            "FEATURE_AND_CUSTOM_EXCEPTION";
    private static final String FEATURE_AND_DISABLED = "DISABLED";

    private final AnnotatedClass dummyClass = new AnnotatedClass();

    Function<FeatureInvocation, Object> exceptionBehaviour = f -> {
        throw new NotImplementedException();
    };

    @Test(expected = DefaultException.class)
    @DisplayName("Should throw default exception when ALWAYS_OFF and behaviour is DEFAULT")
    public void offAndDefault() {
        dummyClass.offAndDefault();
    }

    @Test
    @DisplayName("Should return when ALWAYS_NULL and behaviour is RETURN_NULL")
    public void offAndNull() {
        assertNull(dummyClass.offAndNull());
    }

    @Test(expected = DefaultException.class)
    @DisplayName("Should throw default exception when ALWAYS_OFF and behaviour is THROW_EXCEPTION")
    public void offAndDefaultException() {
        dummyClass.offAndDefaultException();
    }

    @Test(expected = DummyException.class)
    @DisplayName("Should throw DummyException exception when ALWAYS_OFF and behaviour is THROW_EXCEPTION and exception is DummyException")
    public void offAndCustomException() {
        dummyClass.offAndCustomException();
    }

    @Test
    @DisplayName("Should call real method when ALWAYS_OFF and runRealMethod is true")
    public void offAndDisabled() {
        assertEquals(REAL_VALUE, dummyClass.offAndDisabled());
    }

    @Test
    @DisplayName("Should run behaviour when FEATURE and behaviour is DEFAULT")
    public void featureAndDefault() {
        dimmerProcessor.featureWithBehaviour(
                FEATURE_AND_DEFAULT,
                FeatureInvocation::getMethodName);
        assertEquals("featureAndDefault", dummyClass.featureAndDefault());
    }

    @Test
    @DisplayName("Should return when FEATURE and behaviour is RETURN_NULL")
    public void featureAndNull() {
        dimmerProcessor.featureWithBehaviour(FEATURE_AND_NULL, exceptionBehaviour);
        assertNull(dummyClass.featureAndNull());
    }

    @Test(expected = DefaultException.class)
    @DisplayName("Should throw default exception when FEATURE and behaviour is THROW_EXCEPTION")
    public void featureAndDefaultException() {
        dimmerProcessor
                .featureWithBehaviour(FEATURE_AND_DEFAULT_EXCEPTION, exceptionBehaviour);
        dummyClass.featureAndDefaultException();
    }

    @Test(expected = DummyException.class)
    @DisplayName("Should throw DummyException exception when FEATURE and behaviour is THROW_EXCEPTION and exception is DummyException")
    public void featureAndCustomException() {
        dimmerProcessor
                .featureWithBehaviour(FEATURE_AND_CUSTOM_EXCEPTION, exceptionBehaviour);
        dummyClass.featureAndCustomException();
    }

    @Test
    @DisplayName("Should call real method when FEATURE and runRealMethod is true")
    public void featureAndDisabled() {
        dimmerProcessor.featureWithBehaviour(FEATURE_AND_DISABLED, exceptionBehaviour);
        assertEquals(REAL_VALUE, dummyClass.featureAndDisabled());
    }

    @Test
    @DisplayName("A real method should run successfully when having parameters")
    public void executes_real_method_with_arguments() {
        assertEquals(
                "ARGUMENT enhanced other",
                dummyClass.methodWithArguments("ARGUMENT", new DummyClass("other")));
    }

    class AnnotatedClass {

        @DimmerFeature(value = ALWAYS_OFF, behaviour = DEFAULT)
        void offAndDefault() {
        }

        @DimmerFeature(value = ALWAYS_OFF, behaviour = RETURN_NULL)
        String offAndNull() {
            return "ERROR";
        }

        @DimmerFeature(value = ALWAYS_OFF, behaviour = THROW_EXCEPTION)
        void offAndDefaultException() {
        }

        @DimmerFeature(value = ALWAYS_OFF, behaviour = THROW_EXCEPTION, exception = DummyException.class)
        void offAndCustomException() {
        }

        @DimmerFeature(value = ALWAYS_OFF, behaviour = THROW_EXCEPTION, runRealMethod = true)
        String offAndDisabled() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_AND_DEFAULT, behaviour = DEFAULT)
        String featureAndDefault() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_AND_NULL, behaviour = RETURN_NULL)
        String featureAndNull() {
            return "ERROR";
        }

        @DimmerFeature(value = FEATURE_AND_DEFAULT_EXCEPTION, behaviour = THROW_EXCEPTION)
        void featureAndDefaultException() {
        }

        @DimmerFeature(value = FEATURE_AND_CUSTOM_EXCEPTION, behaviour = THROW_EXCEPTION, exception = DummyException.class)
        void featureAndCustomException() {
        }

        @DimmerFeature(value = FEATURE_AND_DISABLED, behaviour = THROW_EXCEPTION, runRealMethod = true)
        String featureAndDisabled() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = ALWAYS_OFF, runRealMethod = true)
        public String methodWithArguments(String value, DummyClass obj) {
            return value + " enhanced " + obj.getValue();
        }

    }

    private static class DummyClass {

        private final String value;

        DummyClass(String value) {
            this.value = value;
        }

        String getValue() {
            return value;
        }
    }

}

