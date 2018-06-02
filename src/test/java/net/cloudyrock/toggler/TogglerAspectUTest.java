package net.cloudyrock.toggler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TogglerAspectUTest {

    private String feature;

    @Before
    public void setUp() {
        feature = "FEATURE" + UUID.randomUUID().toString();
    }

    @Test
    public void toggleCheckAnnotationAdvice_call_behaviour_with_args() throws Throwable {

        //given
        Object expectedReturnedValue = "returnedValue";
        Function<FeatureInvocation, Object> handlerMock = mock(Function.class);
        when(handlerMock.apply(any(FeatureInvocation.class)))
                .thenReturn(expectedReturnedValue);

        Signature signatureMock = mock(Signature.class);
        String methodName = "METHOD_NAME";
        when(signatureMock.getName()).thenReturn(methodName);
        when(signatureMock.getDeclaringType()).thenReturn(ArrayList.class);

        Object[] expectedArgs = {new Object(), new Object()};

        ProceedingJoinPoint jointPointMock = mock(ProceedingJoinPoint.class);
        when(jointPointMock.getSignature()).thenReturn(signatureMock);
        when(jointPointMock.getArgs()).thenReturn(expectedArgs);

        ToggleCheck toggleCheck = mock(ToggleCheck.class);
        when(toggleCheck.feature()).thenReturn(feature);

        TogglerConfiguration.toggleOffWithBehaviour(feature, handlerMock);

        //when
        Object actualReturnedValue = new TogglerAspect()
                .toggleCheckAdvice(jointPointMock, toggleCheck);

        //then
        ArgumentCaptor<FeatureInvocation> captor =
                ArgumentCaptor.forClass(FeatureInvocation.class);
        verify(handlerMock).apply(captor.capture());
        FeatureInvocation invocation = captor.getValue();

        assertEquals(expectedReturnedValue, actualReturnedValue);
        assertEquals(methodName, invocation.getMethodName());
        assertEquals(expectedArgs.length, invocation.getArgs().length);
        for (int i = 0; i < expectedArgs.length; i++) {
            assertEquals(expectedArgs[i], invocation.getArgs()[i]);
        }
    }

    @Test
    public void toggleCheckAnnotationAdvice_execute_real_method_when_not_toggled_off()
            throws Throwable {
        //given
        ProceedingJoinPoint jointPointMock = mock(ProceedingJoinPoint.class);
        Object expectedReturnedValue = "returnedValue";
        when(jointPointMock.proceed()).thenReturn(expectedReturnedValue);
        ToggleCheck toggleCheck = mock(ToggleCheck.class);
        when(toggleCheck.feature()).thenReturn(feature);

        //when
        Object actualReturnedValue = new TogglerAspect()
                .toggleCheckAdvice(jointPointMock, toggleCheck);

        //then
        verify(jointPointMock).proceed();
        assertEquals(expectedReturnedValue, actualReturnedValue);
    }

}
