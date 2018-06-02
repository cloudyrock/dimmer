package com.github.cloudyrock.dimmer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Function;

import static com.github.cloudyrock.dimmer.FeatureOffBehaviour.RETURN_NULL;
import static com.github.cloudyrock.dimmer.FeatureOffBehaviour.THROW_EXCEPTION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assume.assumeTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DimmerAspectUTest extends DimmerTestBase {

    private String feature;

    @Mock
    private Signature signatureMock = mock(Signature.class);

    @Mock
    private ProceedingJoinPoint jointPointMock = mock(ProceedingJoinPoint.class);

    @Mock
    private Function<FeatureInvocation, Object> behaviourMock = mock(Function.class);

    @Mock
    private FeatureCheck featureCheck;

    @Mock
    private FeatureOff featureOffAnn;

    private DimmerAspect dimmerAspect;

    @Before
    public void setUp() {
        feature = "FEATURE" + UUID.randomUUID().toString();
        initMocks(this);
        dimmerAspect = new DimmerAspect();
    }

    @Test
    public void featureCheckAdvice_call_behaviour_with_args() throws Throwable {

        final Object expectedReturnedValue = "returnedValue";
        final String methodName = "METHOD_NAME";
        final Object[] expectedArgs = {new Object(), new Object()};

        given(behaviourMock.apply(any(FeatureInvocation.class)))
                .willReturn(expectedReturnedValue);
        given(signatureMock.getName()).willReturn(methodName);
        given(signatureMock.getDeclaringType()).willReturn(ArrayList.class);
        given(jointPointMock.getSignature()).willReturn(signatureMock);
        given(jointPointMock.getArgs()).willReturn(expectedArgs);
        given(featureCheck.feature()).willReturn(feature);


        dimmerProcessor.featureWithBehaviour(feature, behaviourMock);
        dimmerAspect.setDimmerProcessor(dimmerProcessor);
        Object actualReturnedValue = dimmerAspect.featureCheckAdvice(
                jointPointMock,
                featureCheck);

        //then
        ArgumentCaptor<FeatureInvocation> captor =
                ArgumentCaptor.forClass(FeatureInvocation.class);
        then(behaviourMock).should().apply(captor.capture());
        FeatureInvocation invocation = captor.getValue();

        assertEquals(expectedReturnedValue, actualReturnedValue);
        assertEquals(methodName, invocation.getMethodName());
        assertEquals(expectedArgs.length, invocation.getArgs().length);
        for (int i = 0; i < expectedArgs.length; i++) {
            assertEquals(expectedArgs[i], invocation.getArgs()[i]);
        }
    }

    @Test
    public void featureCheckAdvice_execute_real_method_when_not_feature_off()
            throws Throwable {
        //given
        final String methodName = "METHOD_NAME";
        final Object expectedReturnedValue = "returnedValue";
        final Object[] expectedArgs = {new Object(), new Object()};

        given(signatureMock.getName()).willReturn(methodName);
        given(signatureMock.getDeclaringType()).willReturn(ArrayList.class);
        given(jointPointMock.getSignature()).willReturn(signatureMock);
        given(jointPointMock.getArgs()).willReturn(expectedArgs);
        given(jointPointMock.proceed()).willReturn(expectedReturnedValue);
        given(featureCheck.feature()).willReturn(feature);

        DimmerAspect dimmerAspect = new DimmerAspect();
        dimmerAspect.setDimmerProcessor(dimmerProcessor);

        //when
        assertEquals(
                expectedReturnedValue,
                dimmerAspect.featureCheckAdvice(jointPointMock, featureCheck));

        //then
        then(jointPointMock).should().proceed();

    }

    @Test
    public void featureOff_return_null_when_RETURN_NULL() throws Throwable {

        DimmerAspect dimmerAspect = new DimmerAspect();
        dimmerAspect.setDimmerProcessor(dimmerProcessor);

        given(featureOffAnn.value()).willReturn(RETURN_NULL);
        assertNull(dimmerAspect.featureOffAdvice(featureOffAnn));
    }

    @Test(expected = IllegalArgumentException.class)
    public void featureOff_throw_exception_when_THROW_EXCEPTION() throws Throwable {

        DimmerAspect dimmerAspect = new DimmerAspect();
        dimmerAspect.setDimmerProcessor(dimmerProcessor);
        FeatureOff featureOffAnn = getFeatureOffException();
        assertNull(dimmerAspect.featureOffAdvice(featureOffAnn));
    }

    private FeatureOff getFeatureOffException() {
        return new FeatureOff() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public FeatureOffBehaviour value() {
                return THROW_EXCEPTION;
            }

            @Override
            public Class<? extends RuntimeException> exception() {
                return IllegalArgumentException.class;
            }
        };
    }

}
