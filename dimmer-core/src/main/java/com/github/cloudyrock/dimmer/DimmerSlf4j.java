package com.github.cloudyrock.dimmer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.function.BiConsumer;

import static org.slf4j.event.Level.ERROR;

public class DimmerSlf4j {

    private static final String PREFIX = "[DIMMER] ";

    private final Logger logger;

    private final Level minLogLevel;

    static DimmerSlf4j nullLogger() {
        Logger logger = null;
        return new DimmerSlf4j(logger, ERROR);
    }

    public DimmerSlf4j() {
        this(ERROR);
    }

    public DimmerSlf4j(Level logLevel) {
        this(LoggerFactory.getLogger("DIMMER"), logLevel);
    }

    public DimmerSlf4j(Class clazz, Level logLevel) {
        this(LoggerFactory.getLogger(clazz), logLevel);
    }

    public DimmerSlf4j(String name, Level logLevel) {
        this(LoggerFactory.getLogger(name), logLevel);
    }

    DimmerSlf4j(Logger logger, Level logLevel) {
        this.minLogLevel = logger != null ? logLevel : ERROR;
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
        return logger != null && logLevel.toInt() >= minLogLevel.toInt();
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
