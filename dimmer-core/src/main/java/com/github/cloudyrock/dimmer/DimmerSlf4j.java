package com.github.cloudyrock.dimmer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.function.BiConsumer;

import static org.slf4j.event.Level.DEBUG;
import static org.slf4j.event.Level.ERROR;
import static org.slf4j.event.Level.INFO;
import static org.slf4j.event.Level.TRACE;
import static org.slf4j.event.Level.WARN;

//TODO add test
public class DimmerSlf4j {

    private static final String PREFIX = "[DIMMER] ";

    private final Logger logger;

    static DimmerSlf4j nullLogger() {
        Logger logger = null;
        return new DimmerSlf4j(logger);
    }


    public DimmerSlf4j() {
        this(LoggerFactory.getLogger("DIMMER"));
    }

    public DimmerSlf4j(Class clazz) {
        this(LoggerFactory.getLogger(clazz));
    }

    public DimmerSlf4j(String name) {
        this(LoggerFactory.getLogger(name));
    }

    DimmerSlf4j(Logger logger) {
        this.logger = logger;
    }

    public void log(Level logLevel, String format, Object... args) {
        if (isLoggeable(logLevel)) {
            getLoggerConsumer("", logLevel).accept(format, args);
        }
    }

    public void logWithPrefix(Level logLevel, String format, Object... args) {
        if (isLoggeable(logLevel)) {
            getLoggerConsumer(PREFIX, logLevel).accept(format, args);
        }
    }

    private boolean isLoggeable(Level logLevel) {
        if (logger == null || logLevel == null) {
            return false;
        }
        return (logLevel.toInt() >= TRACE.toInt() && logger.isTraceEnabled() ) ||
                (logLevel.toInt() >= DEBUG.toInt() && logger.isDebugEnabled()) ||
                (logLevel.toInt() >= INFO.toInt() && logger.isInfoEnabled()) ||
                (logLevel.toInt() >= WARN.toInt() && logger.isWarnEnabled()) ||
                (logLevel.toInt() >= ERROR.toInt() && logger.isErrorEnabled());
    }

    private BiConsumer<String, Object[]> getLoggerConsumer(String prefix, Level level) {
        switch (level) {
            case ERROR:
                return (format, args) -> logger.error(prefix + format, (Object[]) args);
            case WARN:
                return (format, args) -> logger.warn(prefix + format, (Object[]) args);
            case DEBUG:
                return (format, args) -> logger.debug(prefix + format, (Object[]) args);
            case TRACE:
                return (format, args) -> logger.trace(prefix + format, (Object[]) args);
            case INFO:
            default:
                return (format, args) -> logger.info(prefix + format, (Object[]) args);
        }
    }

}
