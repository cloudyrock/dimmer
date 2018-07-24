package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.displayname.DisplayName;
import com.github.cloudyrock.dimmer.exceptions.DimmerConfigException;
import com.github.cloudyrock.dimmer.exceptions.DummyException;
import com.github.cloudyrock.dimmer.exceptions.DummyExceptionWithFeatureInvocation;
import org.junit.Before;
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

public class DimmerAspectITest {

    private static final String REAL_VALUE = "real_value";
    private static final String FEATURE1 = "FEATURE1";
    private static final String FEATURE2 = "FEATURE2";
    private static final String FEATURE3 = "FEATURE3";
    private static final String FEATURE4 = "FEATURE4";
    private static final String FEATURE5 = "FEATURE5";
    private static final String FEATURE6 = "FEATURE6";
    private static final String FEATURE7 = "FEATURE7";

    private static final String VALUE1 = "VALUE1";
    public static final String FEATURE8 = "FEATURE8";
    public static final String FEATURE9 = "FEATURE9";
    public static final String FEATURE10 = "FEATURE10";
    private final AnnotatedClass annotatedClass = new AnnotatedClass();

    @Rule public ExpectedException exception = ExpectedException.none();

    private static final String ENV = "ENV";
    private DimmerLocalRunner runner;


    @Before
    public void setUp() {
        runner = DimmerBuilder.local().environments(ENV);

    }


    @Test
    @DisplayName("Should run behaviour when featureWithBehaviour")
    public void featureWithBehaviour() {
        //given
        final String value = "feature1_value";
        runner.featureWithBehaviour(FEATURE1, f -> value);

        //when
        runner.run(ENV);
        final String actual = annotatedClass.methodForFeature1();

        //then
        assertEquals(value, actual);
    }

    @Test
    @DisplayName("Behaviour should get FeatureInvocation as parameter when featureWithBehaviour")
    public void passingFeatureInvocationToBehaviour() {
        //given
        Function<FeatureInvocation, Object> mockBehaviour = mock(Function.class);
        given(mockBehaviour.apply(any(FeatureInvocation.class)))
                .willReturn("not_checked");
        runner.featureWithBehaviour(FEATURE2, mockBehaviour);
        runner.run(ENV);

        //when
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
    public void featureWithExceptionInnBehaviour() {
        runner.featureWithBehaviour(FEATURE3, f -> {
            throw new DummyException();
        });
        runner.run(ENV);

        //when
        final String actual = annotatedClass.methodForFeature3();

        //then
        assertNull(actual);
    }

    @Test
    @DisplayName("Should return " + VALUE1 + " behaviour when featureWithValue")
    public void featureWithValue() {
        runner.featureWithValue(FEATURE4, VALUE1);
        runner.run(ENV);
        assertEquals(VALUE1, annotatedClass.methodForFeature4());
    }

    @Test(expected = DummyException.class)
    @DisplayName("Should throw DummyException when featureWithException")
    public void featureWithException() {
        runner.featureWithException(FEATURE5, DummyException.class);
        runner.run(ENV);
        annotatedClass.methodForFeature5();
    }


    @Test(expected = DimmerConfigException.class)
    @DisplayName("Should throw DimmerConfigException exception expected return type of the caller and configuration mismatch")
    public void featureAndDimmerConfigException() {
        runner.featureWithValue(FEATURE7, 1);
        runner.run(ENV);
        annotatedClass.methodForFeature7();
    }

    @Test
    @DisplayName("Should return a NULL value")
    public void featureNullMismatch() {
        runner.featureWithValue(FEATURE8, null);
        runner.run(ENV);
        annotatedClass.methodForFeature8();
    }

    @Test
    @DisplayName("Behaviour should return child class when executing parent compatible type")
    public void featureAndDimmerSubtypeClass() {
        runner.featureWithValue(FEATURE9, new Child());
        runner.run(ENV);
        annotatedClass.methodForFeature9();
    }

    @Test(expected = DimmerConfigException.class)
    @DisplayName("Should throw DimmerConfigException when real method is void and Configuration of the Feature Invocation has a return type")
    public void featureAndDimmerConfigExceptionWhenRealMethodIsVoid() {
        runner.featureWithValue(FEATURE10, "VALUE");
        runner.run(ENV);
        annotatedClass.methodForFeature10();
    }

    @Test
    @DisplayName("Should exception should get FeatureInvocation as parameter when featureWithException")
    public void passingFeatureInvocationToException() {
        runner.featureWithException(FEATURE6,
                DummyExceptionWithFeatureInvocation.class);
        runner.run(ENV);
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

        @DimmerFeature(value = FEATURE1)
        String methodForFeature1() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE2)
        String methodForFeature2(String param1, Integer param2) {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE3)
        String methodForFeature3() {
            return "ERROR";
        }

        @DimmerFeature(value = FEATURE4)
        String methodForFeature4() {
            return "REAL VALUE";
        }

        @DimmerFeature(value = FEATURE5)
        void methodForFeature5() {
        }

        @DimmerFeature(value = FEATURE6)
        String methodForFeature6(String param1, Integer param2) {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE7)
        String methodForFeature7() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE8)
        String methodForFeature8() {
            return null;
        }

        @DimmerFeature(value = FEATURE9)
        Parent methodForFeature9() {
            return new Parent();
        }

        @DimmerFeature(value = FEATURE10)
        public void methodForFeature10() {
        }
    }

    public class Parent{}
    public class Child extends Parent{}

}

