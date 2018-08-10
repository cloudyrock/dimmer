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

    public DimmerSlf4j(Class clazz) {
        this(LoggerFactory.getLogger(clazz));
    }

    DimmerSlf4j(Logger logger) {
        this.logger = logger;
    }
    public void trace(String message, Object... args) {
        logger.trace(PREFIX + message, (Object[]) args);
    }

    public void debug(String message, Object... args) {
        logger.debug(PREFIX + message, (Object[]) args);
    }

    public void info(String message, Object... args) {
        logger.info(PREFIX + message, (Object[]) args);
    }

    public void warn(String message, Object... args) {
        logger.warn(PREFIX + message, (Object[]) args);
    }

    public void error(String message, Object... args) {
        logger.error(PREFIX + message, (Object[]) args);
    }


}
