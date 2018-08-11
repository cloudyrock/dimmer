package com.github.cloudyrock.dimmer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.MockitoAnnotations.initMocks;

public class DimmerAspectUTest {

    private String feature;

    @Mock
    private MethodSignature signatureMock;

    @Mock
    private ProceedingJoinPoint jointPointMock;

    @Mock
    private FeatureExecutorImpl dimmerProcessor;

    @Mock
    private DimmerFeature dimmerFeatureMock;

    private DimmerAspect dimmerAspect;

    @Before
    public void setUp() {
        feature = "FEATURE" + UUID.randomUUID().toString();
        initMocks(this);
        dimmerAspect = new DimmerAspect();
        dimmerAspect.setFeatureExecutor(dimmerProcessor);
    }

    @Test
//    @DisplayName("Aspect should delegate to dimmerProcessor")
    public void aspect_delegate_dimmerProcessor_with_right_parameters() throws Throwable {

        final Object expectedReturnedValue = "returnedValue";
        final String methodName = "METHOD_NAME";
        final Object[] expectedArgs = {new Object(), new Object()};

        given(signatureMock.getName()).willReturn(methodName);
        given(signatureMock.getDeclaringType()).willReturn(ArrayList.class);
        given(signatureMock.getReturnType()).willReturn(String.class);
        given(jointPointMock.getSignature()).willReturn(signatureMock);
        given(jointPointMock.getArgs()).willReturn(expectedArgs);
        String expectedJoinPointValue = "value";
        given(jointPointMock.proceed()).willReturn(expectedJoinPointValue);
        given(dimmerFeatureMock.value()).willReturn(feature);
        given(dimmerProcessor.executeDimmerFeature(
                anyString(),
                any(FeatureInvocation.class),
                any(MethodCaller.class))).willReturn(expectedReturnedValue);

        Object actualReturnedValue = dimmerAspect.dimmerFeatureAdvice(
                jointPointMock,
                dimmerFeatureMock);

        //then
        ArgumentCaptor<FeatureInvocation> featureInvocationCaptor =
                ArgumentCaptor.forClass(FeatureInvocation.class);
        ArgumentCaptor<String> featureCaptor =
                ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MethodCaller> methodCallerCaptor =
                ArgumentCaptor.forClass(MethodCaller.class);

        then(dimmerProcessor).should().executeDimmerFeature(
                featureCaptor.capture(),
                featureInvocationCaptor.capture(),
                methodCallerCaptor.capture()
        );
        assertEquals(expectedReturnedValue, actualReturnedValue);
        assertEquals(feature, featureCaptor.getValue());
        assertEquals(expectedJoinPointValue, methodCallerCaptor.getValue().call());

        FeatureInvocation invocation = featureInvocationCaptor.getValue();

        assertEquals(expectedReturnedValue, actualReturnedValue);
        assertEquals(methodName, invocation.getMethodName());
        assertEquals(expectedArgs.length, invocation.getArgs().length);
        for (int i = 0; i < expectedArgs.length; i++) {
            assertEquals(expectedArgs[i], invocation.getArgs()[i]);
        }
    }

}
