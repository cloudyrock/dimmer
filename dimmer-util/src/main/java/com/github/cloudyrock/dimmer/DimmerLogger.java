package com.github.cloudyrock.dimmer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DimmerLogger {

    private static final String PREFIX = "[DIMMER] ";

    private final Logger logger;

    public DimmerLogger(Class clazz) {
        this(LoggerFactory.getLogger(clazz));
    }

    public DimmerLogger(Logger logger) {
        this.logger = logger;
    }

    public void trace(String message, Object... args) {
        if(logger.isTraceEnabled()) {
            logger.trace(PREFIX + message, (Object[]) args);
        }

    }

    public void debug(String message, Object... args) {
        if(logger.isDebugEnabled()) {
            logger.debug(PREFIX + message, (Object[]) args);
        }
    }

    public void info(String message, Object... args) {
        if(logger.isInfoEnabled()) {
            logger.info(PREFIX + message, (Object[]) args);
        }
    }

    public void warn(String message, Object... args) {
        if(logger.isWarnEnabled()) {
            logger.warn(PREFIX + message, (Object[]) args);
        }
    }

    public void error(String message, Object... args) {
        if(logger.isErrorEnabled()) {
            logger.error(PREFIX + message, (Object[]) args);
        }
    }

    public void error(String message, Throwable ex) {
        if(logger.isErrorEnabled()) {
            logger.error(PREFIX + message, ex);
        }
    }
}
