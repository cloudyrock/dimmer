package com.github.cloudyrock.dimmer;

import org.aspectj.lang.Aspects;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

public class DimmerAspectITest {//extends DimmerTestBase {

    private static final String operation = "operation";

    private static final String REAL_VALUE = "real_value";
    private static final String FEATURE1 = "FEATURE1";
    private static final String FEATURE2 = "FEATURE2";
    private static final String FEATURE3 = "FEATURE3";
    private static final String FEATURE4 = "FEATURE4";
    private static final String FEATURE5 = "FEATURE5";
    private static final String FEATURE6 = "FEATURE6";
    private static final String FEATURE7 = "FEATURE7";

    private static final String VALUE1 = "VALUE1";
    private static final String FEATURE8 = "FEATURE8";
    private static final String FEATURE9 = "FEATURE9";
    private static final String FEATURE10 = "FEATURE10";
    private static final String ENV = "environment";
    private static final String ENV_2 = "environment_other";
    private final AnnotatedClass annotatedClass = new AnnotatedClass();

    @Rule public ExpectedException exception = ExpectedException.none();

    @Test
    public void hasAspect() {
        assertTrue(Aspects.hasAspect(DimmerAspect.class));
    }

    @Test
    @DisplayName("Should run behaviour when featureWithBehaviour")
    public void shouldRunBehaviour() {
        final String value = "feature1_value";
        DimmerBuilder.local()
                .defaultEnvironment()
                .featureWithBehaviour(FEATURE1, operation, f -> value)
                .buildWithDefaultEnvironment();
        assertEquals(value, annotatedClass.methodForFeature1());
    }

    @Test
    @DisplayName("Should run behaviour when featureWithBehaviour")
    public void shouldRunBehaviourForEnvironment() {
        final String value = "feature1_value";
        DimmerBuilder.local()
                .environments(ENV)
                .featureWithBehaviour(FEATURE1, operation, f -> value)
                .build(ENV);
        assertEquals(value, annotatedClass.methodForFeature1());
    }

    @Test (expected = DimmerConfigException.class)
    @DisplayName("Should run behaviour when featureWithBehaviour")
    public void shouldThrowDimmerExceptionWhenEnvironmentNotConfigured() {
        final String value = "feature1_value";
        DimmerBuilder.local()
                .environments(ENV)
                .featureWithBehaviour(FEATURE1, operation, f -> value)
                .build(ENV_2);
        assertEquals(REAL_VALUE, annotatedClass.methodForFeature1());
    }

    @Test
    @DisplayName("Behaviour should get FeatureInvocation as parameter when featureWithBehaviour")
    public void passingFeatureInvocationToBehaviour() {
        //given
        Function<FeatureInvocation, Object> mockBehaviour = mock(Function.class);
        given(mockBehaviour.apply(any(FeatureInvocation.class)))
                .willReturn("not_checked");

        //when
        DimmerBuilder.local()
                .defaultEnvironment()
                .featureWithBehaviour(FEATURE2, operation, mockBehaviour)
                .buildWithDefaultEnvironment();
        final String param1 = "parameter1";
        final int param2 = 2;
        annotatedClass.methodForFeature2(param1, param2);

        //then
        ArgumentCaptor<FeatureInvocation> captor =
                ArgumentCaptor.forClass(FeatureInvocation.class);
        then(mockBehaviour).should().apply(captor.capture());
        assertFeatureInvocation(captor.getValue(), FEATURE2, "methodForFeature2", param1,
                param2);
    }

    @Test(expected = DummyException.class)
    @DisplayName("Should throw exception thrown inside behaviour when featureWithBehaviour")
    public void shouldThrowExceptionWhenBehaviourThrowsException() {
        DimmerBuilder.local()
                .defaultEnvironment().featureWithBehaviour(FEATURE3, operation, f -> {
            throw new DummyException();
        }).buildWithDefaultEnvironment();
        annotatedClass.methodForFeature3();
    }

    @Test
    @DisplayName("Should throw exception thrown inside behaviour when featureWithBehaviour")
    public void shouldNotThrowExceptionWithBehaviourWhenEnvironmentNotConfigured() {
        DimmerBuilder.local()
                .environments(ENV)
                .featureWithBehaviour(FEATURE3, operation, f -> {
                    throw new DummyException();
                })
                .build(ENV);
        assertEquals(REAL_VALUE, annotatedClass.methodForFeature3());
    }

    @Test
    @DisplayName("Should return " + VALUE1 + " behaviour when featureWithValue")
    public void shouldReturnValue() {
        DimmerBuilder.local()
                .defaultEnvironment()
                .featureWithValue(FEATURE4, operation, VALUE1)
                .buildWithDefaultEnvironment();
        assertEquals(VALUE1, annotatedClass.methodForFeature4());
    }

    @Test
    @DisplayName("Should return " + VALUE1 + " behaviour when featureWithValue")
    public void shouldNotReturnValueWhenEnvironmentNotConfigured() {
        DimmerBuilder.local()
                .environments(ENV_2)
                .featureWithValue(FEATURE4, operation, VALUE1)
                .build(ENV);
        assertEquals(REAL_VALUE, annotatedClass.methodForFeature4());
    }

    @Test(expected = DummyException.class)
    @DisplayName("Should throw DummyException when featureWithException")
    public void shouldThrowException() {
        DimmerBuilder.local()
                .defaultEnvironment()
                .featureWithException(FEATURE5, operation, DummyException.class)
                .buildWithDefaultEnvironment();
        annotatedClass.methodForFeature5();
    }

    @Test (expected = DimmerConfigException.class)
    @DisplayName("Should throw exception when build environment is not configured.")
    public void shouldThrowExceptionWhenEnvironmentNotConfigured() {
        DimmerBuilder.local()
                .environments(ENV)
                .featureWithException(FEATURE5, operation, DummyException.class)
                .build(ENV_2);
        assertEquals(REAL_VALUE, annotatedClass.methodForFeature5());
    }

    @Test(expected = DimmerConfigException.class)
    @DisplayName("Should throw DimmerConfigException exception expected return type of the caller and configuration mismatch")
    public void shouldThrowDimmerConfigException() {
        DimmerBuilder.local()
                .defaultEnvironment()
                .featureWithValue(FEATURE7, operation, 1)
                .buildWithDefaultEnvironment();
        annotatedClass.methodForFeature7();
    }

    @Test
    @DisplayName("Should throw DimmerConfigException exception expected return type of the caller and configuration mismatch")
    public void shouldNotThrowDimmerConfigException() {
        DimmerBuilder.local()
                .environments(ENV)
                .featureWithValue(FEATURE7, operation, 1)
                .build(ENV);
        assertEquals(REAL_VALUE, annotatedClass.methodForFeature7());
    }

    @Test
    @DisplayName("Should return a NULL value")
    public void shouldReturnNull() {
        DimmerBuilder.local()
                .defaultEnvironment()
                .featureWithValue(FEATURE8, operation, null)
                .buildWithDefaultEnvironment();
        assertNull(annotatedClass.methodForFeature8());
    }

    @Test
    @DisplayName("Behaviour should return child class when executing parent compatible type")
    public void featureAndDimmerSubtypeClass() {
        DimmerBuilder.local()
                .defaultEnvironment()
                .featureWithValue(FEATURE9, operation, new Child())
                .buildWithDefaultEnvironment();
        annotatedClass.methodForFeature9();
    }

    @Test(expected = DimmerConfigException.class)
    @DisplayName("Should throw DimmerConfigException when real method is void and Configuration of the Feature Invocation has a return type")
    public void featureAndDimmerConfigExceptionWhenRealMethodIsVoid() {
        DimmerBuilder.local()
                .defaultEnvironment()
                .featureWithValue(FEATURE10, operation, "VALUE")
                .buildWithDefaultEnvironment();
        annotatedClass.methodForFeature10();
    }

    @Test
    @DisplayName("Should get FeatureInvocation as parameter when featureWithException")
    public void passingFeatureInvocationToException() {
        DimmerBuilder.local()
                .defaultEnvironment()
                .featureWithException(FEATURE6, operation,
                        DummyExceptionWithFeatureInvocation.class)
                .buildWithDefaultEnvironment();
        final String param1 = "parameter1";
        final int param2 = 2;
        boolean thrownException = false;
        try {
            annotatedClass.methodForFeature6(param1, param2);
        } catch (DummyExceptionWithFeatureInvocation ex) {
            //then
            assertFeatureInvocation(ex.getFeatureInvocation(), FEATURE6,
                    "methodForFeature6", param1, param2);
            thrownException = true;
        }
        assertTrue("DummyExceptionWithFeatureInvocation not thrown", thrownException);

    }

    private void assertFeatureInvocation(FeatureInvocation actualFeatureInvocation,
                                         String feature,
                                         String methodName,
                                         String param1,
                                         int param2) {
        assertEquals(feature, actualFeatureInvocation.getFeature());
        assertEquals(methodName,
                actualFeatureInvocation.getMethodName());
        assertEquals(AnnotatedClass.class, actualFeatureInvocation.getDeclaringType());
        assertEquals(param1, actualFeatureInvocation.getArgs()[0]);
        assertEquals(param2, actualFeatureInvocation.getArgs()[1]);
    }

    class AnnotatedClass {

        @DimmerFeature(value = FEATURE1, op = operation)
        String methodForFeature1() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE2, op = operation)
        String methodForFeature2(String param1, Integer param2) {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE3, op = operation)
        String methodForFeature3() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE4, op = operation)
        String methodForFeature4() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE5, op = operation)
        String methodForFeature5() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE6, op = operation)
        String methodForFeature6(String param1, Integer param2) {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE7, op = operation)
        String methodForFeature7() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE8, op = operation)
        String methodForFeature8() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE9, op = operation)
        Parent methodForFeature9() {
            return new Parent();
        }

        @DimmerFeature(value = FEATURE10, op = operation)
        public void methodForFeature10() {
        }
    }

    public class Parent {
    }

    public class Child extends Parent {
    }

}
