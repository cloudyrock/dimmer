package com.github.cloudyrock.dimmer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

public class DimmerSlf4jTest {

    private static final String PREFIX = "[DIMMER] ";

    private Logger slf4jLogger;

    private DimmerSlf4j dimmerLoger;

    @Before
    public void setup() {
        slf4jLogger = Mockito.mock(Logger.class);
        dimmerLoger = new DimmerSlf4j(slf4jLogger);
        given(slf4jLogger.isTraceEnabled()).willReturn(false);
        given(slf4jLogger.isDebugEnabled()).willReturn(false);
        given(slf4jLogger.isInfoEnabled()).willReturn(false);
        given(slf4jLogger.isWarnEnabled()).willReturn(false);
        given(slf4jLogger.isErrorEnabled()).willReturn(false);
    }

    private static String buildFormatMessageWithPrefix(String formatMessage) {
        return PREFIX + formatMessage;
    }

    @Test
    public void traceLogging() {
        given(slf4jLogger.isTraceEnabled()).willReturn(true);

        final String formatMessage = "message {}";
        dimmerLoger
                .logWithPrefix(Level.TRACE, formatMessage, "value", "value2", "value3");

        then(slf4jLogger).should(times(1))
                .trace(buildFormatMessageWithPrefix(formatMessage), "value", "value2",
                        "value3");
    }

    @Test
    public void debugLogging() {
        given(slf4jLogger.isDebugEnabled()).willReturn(true);

        final String formatMessage = "message {}";
        dimmerLoger
                .logWithPrefix(Level.DEBUG, formatMessage, "value", "value2", "value3");

        then(slf4jLogger).should(times(1))
                .debug(buildFormatMessageWithPrefix(formatMessage), "value", "value2",
                        "value3");
    }

    @Test
    public void infoLogging() {
        given(slf4jLogger.isInfoEnabled()).willReturn(true);

        final String formatMessage = "message {}";
        dimmerLoger
                .logWithPrefix(Level.INFO, formatMessage, "value", "value2", "value3");

        then(slf4jLogger).should(times(1))
                .info(buildFormatMessageWithPrefix(formatMessage), "value", "value2",
                        "value3");
    }


    @Test
    public void warnLogging() {
        given(slf4jLogger.isWarnEnabled()).willReturn(true);

        final String formatMessage = "message {}";
        dimmerLoger
                .logWithPrefix(Level.WARN, formatMessage, "value", "value2", "value3");

        then(slf4jLogger).should(times(1))
                .warn(buildFormatMessageWithPrefix(formatMessage), "value", "value2",
                        "value3");
    }

    @Test
    public void errorLogging() {
        given(slf4jLogger.isErrorEnabled()).willReturn(true);

        final String formatMessage = "message {}";
        dimmerLoger
                .logWithPrefix(Level.ERROR, formatMessage, "value", "value2", "value3");

        then(slf4jLogger).should(times(1))
                .error(buildFormatMessageWithPrefix(formatMessage), "value", "value2",
                        "value3");
    }



















    @Test
    public void shouldNotLogging_whenTRACE_if_notLevelEnable() {
        given(slf4jLogger.isWarnEnabled()).willReturn(true);
        given(slf4jLogger.isDebugEnabled()).willReturn(true);
        given(slf4jLogger.isInfoEnabled()).willReturn(true);
        given(slf4jLogger.isErrorEnabled()).willReturn(true);

        final String formatMessage = "message {}";
        dimmerLoger
                .logWithPrefix(Level.TRACE, formatMessage, "value", "value2", "value3");

        then(slf4jLogger).should(times(0))
                .trace(buildFormatMessageWithPrefix(formatMessage), "value", "value2",
                        "value3");
    }

    @Test
    public void shouldNotLogging_whenDEBUG_if_notLevelEnable() {
        given(slf4jLogger.isInfoEnabled()).willReturn(true);
        given(slf4jLogger.isErrorEnabled()).willReturn(true);

        final String formatMessage = "message {}";
        dimmerLoger
                .logWithPrefix(Level.DEBUG, formatMessage, "value", "value2", "value3");

        then(slf4jLogger).should(times(0))
                .debug(buildFormatMessageWithPrefix(formatMessage), "value", "value2",
                        "value3");
    }

    @Test
    public void shouldNotLogging_whenINFO_if_notLevelEnable() {
        given(slf4jLogger.isWarnEnabled()).willReturn(true);
        given(slf4jLogger.isErrorEnabled()).willReturn(true);

        final String formatMessage = "message {}";
        dimmerLoger
                .logWithPrefix(Level.INFO, formatMessage, "value", "value2", "value3");

        then(slf4jLogger).should(times(0))
                .info(buildFormatMessageWithPrefix(formatMessage), "value", "value2",
                        "value3");
    }
    @Test
    public void shouldNotLogging_whenWARN_if_notLevelEnable() {
        given(slf4jLogger.isErrorEnabled()).willReturn(true);

        final String formatMessage = "message {}";
        dimmerLoger
                .logWithPrefix(Level.WARN, formatMessage, "value", "value2", "value3");

        then(slf4jLogger).should(times(0))
                .warn(buildFormatMessageWithPrefix(formatMessage), "value", "value2",
                        "value3");
    }

    @Test
    public void shouldNotLogging_whenERROR_if_notLevelEnable() {

        final String formatMessage = "message {}";
        dimmerLoger
                .logWithPrefix(Level.ERROR, formatMessage, "value", "value2", "value3");

        then(slf4jLogger).should(times(0))
                .error(buildFormatMessageWithPrefix(formatMessage), "value", "value2",
                        "value3");
    }


}