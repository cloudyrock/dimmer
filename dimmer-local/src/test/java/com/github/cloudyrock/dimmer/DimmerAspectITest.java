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
        runner.featureWithBehaviour(AnnotatedClass.FEATURE1, f -> value);

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
        runner.featureWithBehaviour(AnnotatedClass.FEATURE2, mockBehaviour);
        runner.run(ENV);

        //when
        final String param1 = "parameter1";
        final int param2 = 2;
        annotatedClass.methodForFeature2(param1, param2);

        //then
        ArgumentCaptor<FeatureInvocation> captor =
                ArgumentCaptor.forClass(FeatureInvocation.class);
        then(mockBehaviour).should().apply(captor.capture());
        assertFeatureInvocation(captor.getValue(), AnnotatedClass.FEATURE2, "methodForFeature2", param1,
                param2);
    }

    @Test(expected = DummyException.class)
    @DisplayName("Should throw exception thrown inside behaviour when featureWithBehaviour")
    public void featureWithExceptionInnBehaviour() {
        runner.featureWithBehaviour(AnnotatedClass.FEATURE3, f -> {
            throw new DummyException();
        });
        runner.run(ENV);

        //when
        final String actual = annotatedClass.methodForFeature3();

        //then
        assertNull(actual);
    }

    @Test
    @DisplayName("Should return " + AnnotatedClass.VALUE1 + " behaviour when featureWithValue")
    public void featureWithValue() {
        runner.featureWithValue(AnnotatedClass.FEATURE4, AnnotatedClass.VALUE1);
        runner.run(ENV);
        assertEquals(AnnotatedClass.VALUE1, annotatedClass.methodForFeature4());
    }

    @Test(expected = DummyException.class)
    @DisplayName("Should throw DummyException when featureWithException")
    public void featureWithException() {
        runner.featureWithException(AnnotatedClass.FEATURE5, DummyException.class);
        runner.run(ENV);
        annotatedClass.methodForFeature5();
    }


    @Test(expected = DimmerConfigException.class)
    @DisplayName("Should throw DimmerConfigException exception expected return type of the caller and configuration mismatch")
    public void featureAndDimmerConfigException() {
        runner.featureWithValue(AnnotatedClass.FEATURE7, 1);
        runner.run(ENV);
        annotatedClass.methodForFeature7();
    }

    @Test
    @DisplayName("Should return a NULL value")
    public void featureNullMismatch() {
        runner.featureWithValue(AnnotatedClass.FEATURE8, null);
        runner.run(ENV);
        annotatedClass.methodForFeature8();
    }

    @Test
    @DisplayName("Behaviour should return child class when executing parent compatible type")
    public void featureAndDimmerSubtypeClass() {
        runner.featureWithValue(AnnotatedClass.FEATURE9, new Child());
        runner.run(ENV);
        annotatedClass.methodForFeature9();
    }

    @Test(expected = DimmerConfigException.class)
    @DisplayName("Should throw DimmerConfigException when real method is void and Configuration of the Feature Invocation has a return type")
    public void featureAndDimmerConfigExceptionWhenRealMethodIsVoid() {
        runner.featureWithValue(AnnotatedClass.FEATURE10, "VALUE");
        runner.run(ENV);
        annotatedClass.methodForFeature10();
    }

    @Test
    @DisplayName("Should exception should get FeatureInvocation as parameter when featureWithException")
    public void passingFeatureInvocationToException() {
        runner.featureWithException(AnnotatedClass.FEATURE6,
                DummyExceptionWithFeatureInvocation.class);
        runner.run(ENV);
        final String param1 = "parameter1";
        final int param2 = 2;
        boolean thrownException = false;
        try {
            annotatedClass.methodForFeature6(param1, param2);
        } catch (DummyExceptionWithFeatureInvocation ex) {
            //then
            assertFeatureInvocation(ex.getFeatureInvocation(), AnnotatedClass.FEATURE6,
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



}

