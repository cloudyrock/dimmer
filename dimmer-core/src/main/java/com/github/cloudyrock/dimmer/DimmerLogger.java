package com.github.cloudyrock.dimmer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DimmerLogger {

    private static final String PREFIX = "[DIMMER] ";

    private final Logger logger;

    DimmerLogger(Class clazz) {
        this(LoggerFactory.getLogger(clazz));
    }

    DimmerLogger(Logger logger) {
        this.logger = logger;
    }

    void trace(String message, Object... args) {
        logger.trace(PREFIX + message, (Object[]) args);
    }

    void debug(String message, Object... args) {
        logger.debug(PREFIX + message, (Object[]) args);
    }

    void info(String message, Object... args) {
        logger.info(PREFIX + message, (Object[]) args);
    }

    void warn(String message, Object... args) {
        logger.warn(PREFIX + message, (Object[]) args);
    }

    void error(String message, Object... args) {
        logger.error(PREFIX + message, (Object[]) args);
    }
}
