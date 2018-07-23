package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.displayname.DisplayName;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
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
import static org.mockito.MockitoAnnotations.initMocks;

public class DimmerAspectUTest {

    private String feature;

    @Mock
    private MethodSignature signatureMock;

    @Mock
    private ProceedingJoinPoint jointPointMock;

    @Mock
    private DimmerProcessor dimmerProcessor;

    @Mock
    private DimmerFeature dimmerFeatureMock;

    private DimmerAspect dimmerAspect;

    @Before
    public void setUp() {
        feature = "FEATURE" + UUID.randomUUID().toString();
        initMocks(this);
        dimmerAspect = new DimmerAspect();
        dimmerAspect.setDimmerProcessor(dimmerProcessor);
    }

    @Test
    @DisplayName("Aspect should delegate to dimmerProcessor")
    public void aspect_delegate_dimmerProcessor_with_right_parameters() throws Throwable {

        final Object expectedReturnedValue = "returnedValue";
        final String methodName = "METHOD_NAME";
        final Object[] expectedArgs = {new Object(), new Object()};

        given(signatureMock.getName()).willReturn(methodName);
        given(signatureMock.getDeclaringType()).willReturn(ArrayList.class);
        given(signatureMock.getReturnType()).willReturn(String.class);
        given(jointPointMock.getSignature()).willReturn(signatureMock);
        given(jointPointMock.getArgs()).willReturn(expectedArgs);
        given(dimmerFeatureMock.value()).willReturn(feature);
        given(dimmerProcessor.executeDimmerFeature(
                any(DimmerFeature.class),
                any(FeatureInvocation.class),
                any(ProceedingJoinPoint.class))).willReturn(expectedReturnedValue);

        Object actualReturnedValue = dimmerAspect.dimmerFeatureAdvice(
                jointPointMock,
                dimmerFeatureMock);

        //then
        ArgumentCaptor<FeatureInvocation> featureInvocationCaptor =
                ArgumentCaptor.forClass(FeatureInvocation.class);
        ArgumentCaptor<DimmerFeature> dimmerFeatureCaptor =
                ArgumentCaptor.forClass(DimmerFeature.class);
        ArgumentCaptor<ProceedingJoinPoint> jointPointCaptor =
                ArgumentCaptor.forClass(ProceedingJoinPoint.class);

        then(dimmerProcessor).should()
                .executeDimmerFeature(
                        dimmerFeatureCaptor.capture(),
                        featureInvocationCaptor.capture(),
                        jointPointCaptor.capture()
                );
        assertEquals(expectedReturnedValue, actualReturnedValue);
        assertEquals(dimmerFeatureMock, dimmerFeatureCaptor.getValue());
        assertEquals(jointPointMock, jointPointCaptor.getValue());

        FeatureInvocation invocation = featureInvocationCaptor.getValue();

        assertEquals(expectedReturnedValue, actualReturnedValue);
        assertEquals(methodName, invocation.getMethodName());
        assertEquals(expectedArgs.length, invocation.getArgs().length);
        for (int i = 0; i < expectedArgs.length; i++) {
            assertEquals(expectedArgs[i], invocation.getArgs()[i]);
        }
    }

}
