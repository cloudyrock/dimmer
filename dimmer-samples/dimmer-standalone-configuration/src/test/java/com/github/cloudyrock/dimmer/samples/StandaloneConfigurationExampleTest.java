package com.github.cloudyrock.dimmer.samples;

import com.github.cloudyrock.dimmer.DimmerInvocationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.PrintStream;

import static com.github.cloudyrock.dimmer.samples.StandaloneConfigurationExample.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Test class for examples of the Standard configuration for the different environments
 */
@RunWith(MockitoJUnitRunner.class)
public class StandaloneConfigurationExampleTest {

    @InjectMocks
    StandaloneConfigurationExample standaloneConfigurationExample;

    @Mock
    private PrintStream printStreamMock;

    @Before
    public void setUp() {
        initMocks(this);
        System.setOut(printStreamMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(printStreamMock);
    }

    @Test(expected = DimmerInvocationException.class)
    public void testThatNullArgsExecutesDefaultEnv() throws Throwable {
        try {
            StandaloneConfigurationExample.main(null);
        } catch (RuntimeException e) {
            verifyDefaultEnvironmentCall(e);
        }
    }

    @Test(expected = DimmerInvocationException.class)
    public void testThatEmptyArgsExecutesDefaultEnv() {
        try {
            StandaloneConfigurationExample.main(new String[]{""});
        } catch (RuntimeException e) {
            verifyDefaultEnvironmentCall(e);
        }
    }


    @Test(expected = DimmerInvocationException.class)
    public void testThatWrongArgsExecutesDefaultEnv() {
        try {
            StandaloneConfigurationExample.main(new String[]{"random"});
        } catch (RuntimeException e) {
            verifyDefaultEnvironmentCall(e);
        }
    }

    @Test(expected = DimmerInvocationException.class)
    public void testThatDevEnvironmentExecutesMockedBehaviours() {
        try {
            StandaloneConfigurationExample.main(new String[]{"DEV"});
        } catch (RuntimeException e) {
            verifyDefaultEnvironmentCall(e);
        }
    }

    @Test
    public void testThatIntEnvironmentExecutesRealMethods() {
        StandaloneConfigurationExample.main(new String[]{"INT"});
        verify(printStreamMock).println(GET_USERS + REAL_VALUE);
        verify(printStreamMock).println(GET_USER_DETAILS + REAL_VALUE);
        verify(printStreamMock).println(MOCKED_BEHAVIOUR_VALUE);
        verify(printStreamMock).println(REMOVE_USER + REAL_VALUE);
        verify(printStreamMock).println(UPDATE_USER_DETAILS + REAL_VALUE);
    }

    @Test(expected = MyRuntimeException.class)
    public void testThatProdEnvironmentExecutesRealMethods() {
        try {
            StandaloneConfigurationExample.main(new String[]{"PROD"});
        }catch (MyRuntimeException e) {
            verify(printStreamMock).println(GET_USERS + REAL_VALUE);
            verify(printStreamMock).println(ADD_USER + REAL_VALUE);
            verify(printStreamMock).println(GET_USER_DETAILS + REAL_VALUE);
            verify(printStreamMock).println(UPDATE_USER_DETAILS + REAL_VALUE);
            throw e;
        }
    }


    private void verifyDefaultEnvironmentCall(RuntimeException e) {
        verify(printStreamMock).println(GET_USERS + REAL_VALUE);
        verify(printStreamMock, times(2)).println(MOCKED_BEHAVIOUR_VALUE);
        throw e;
    }
}
