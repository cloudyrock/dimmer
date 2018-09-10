package com.github.cloudyrock.dimmer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

public class DimmerLoggerTest {

    private static final String PREFIX = "[DIMMER] ";

    private Logger slf4jLogger;

    private DimmerLogger dimmerLoger;

    @Before
    public void setup() {
        slf4jLogger = Mockito.mock(Logger.class);
        dimmerLoger = new DimmerLogger(slf4jLogger);
    }

    private static String buildFormatMessageWithPrefix(String formatMessage) {
        return PREFIX + formatMessage;
    }

    @Test
    public void traceLogging() {
        given(slf4jLogger.isTraceEnabled()).willReturn(true);

        final String formatMessage = "message {}";
        dimmerLoger.trace(formatMessage, "value", "value2", "value3");

        then(slf4jLogger).should(times(1))
                .trace(buildFormatMessageWithPrefix(formatMessage), "value", "value2",
                        "value3");
    }

    @Test
    public void debugLogging() {
        given(slf4jLogger.isDebugEnabled()).willReturn(true);

        final String formatMessage = "message {}";
        dimmerLoger
                .debug(formatMessage, "value", "value2", "value3");

        then(slf4jLogger).should(times(1))
                .debug(buildFormatMessageWithPrefix(formatMessage), "value", "value2",
                        "value3");
    }

    @Test
    public void infoLogging() {
        given(slf4jLogger.isInfoEnabled()).willReturn(true);

        final String formatMessage = "message {}";
        dimmerLoger
                .info(formatMessage, "value", "value2", "value3");

        then(slf4jLogger).should(times(1))
                .info(buildFormatMessageWithPrefix(formatMessage), "value", "value2",
                        "value3");
    }


    @Test
    public void warnLogging() {
        given(slf4jLogger.isWarnEnabled()).willReturn(true);

        final String formatMessage = "message {}";
        dimmerLoger
                .warn(formatMessage, "value", "value2", "value3");

        then(slf4jLogger).should(times(1))
                .warn(buildFormatMessageWithPrefix(formatMessage), "value", "value2",
                        "value3");
    }

    @Test
    public void errorLogging() {
        given(slf4jLogger.isErrorEnabled()).willReturn(true);

        final String formatMessage = "message {}";
        dimmerLoger
                .error(formatMessage, "value", "value2", "value3");

        then(slf4jLogger).should(times(1))
                .error(buildFormatMessageWithPrefix(formatMessage), "value", "value2",
                        "value3");
    }


}