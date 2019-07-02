package com.github.cloudyrock.dimmer.logic;

import com.github.cloudyrock.dimmer.DimmerConfigException;
import com.github.cloudyrock.dimmer.FeatureInvocation;
import com.github.cloudyrock.dimmer.Preconditions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

final class ExceptionUtil {

    private ExceptionUtil() {

    }

    static Object throwException(Class<? extends RuntimeException> exceptionType,
                                 FeatureInvocation f) {
        final ExceptionConstructorType constructorType =
                getExceptionConstructorType(exceptionType);
        try {
            switch (constructorType) {
                case EMPTY_CONSTRUCTOR:
                    throw exceptionType.getConstructor().newInstance();

                case FEATURE_INVOCATION_CONSTRUCTOR:
                    throw exceptionType.getConstructor(FeatureInvocation.class).newInstance(f);
                case NO_COMPATIBLE_CONSTRUCTOR:
                default:
                    //This cannot happen...unless enum is changed and the code is not
                    //updated
                    throw new DimmerConfigException(constructorType + " not compatible");
            }
        } catch (InstantiationException | IllegalAccessException |
                InvocationTargetException | NoSuchMethodException e) {
            throw new DimmerConfigException(e);
        }
    }

    static void checkExceptionConstructorType(
            Class<? extends RuntimeException> exceptionType) {
        Preconditions.checkNullOrEmpty(exceptionType, "exceptionType");
        final Constructor<?>[] constructors = exceptionType.getConstructors();
        for (Constructor<?> c : constructors) {
            if (c.getParameterCount() == 0 ||
                    (c.getParameterCount() == 1 && FeatureInvocation.class.equals(c.getParameterTypes()[0]))) {
                return;
            }
        }
        throw new DimmerConfigException(String.format(
                "Exception %s must have either empty constructor or with " +
                        "FeatureInvocation argument",
                exceptionType.getSimpleName()));
    }

    private static ExceptionConstructorType getExceptionConstructorType(
            Class<? extends RuntimeException> exceptionType) {
        Preconditions.checkNullOrEmpty(exceptionType, "exceptionType");
        final Constructor<?>[] constructors = exceptionType.getConstructors();
        ExceptionConstructorType constructorType = ExceptionConstructorType.NO_COMPATIBLE_CONSTRUCTOR;
        for (Constructor<?> c : constructors) {
            if (c.getParameterCount() == 1 && FeatureInvocation.class
                    .equals(c.getParameterTypes()[0])) {
                constructorType = ExceptionConstructorType.FEATURE_INVOCATION_CONSTRUCTOR;
                break;
            } else if (c.getParameterCount() == 0) {
                constructorType = ExceptionConstructorType.EMPTY_CONSTRUCTOR;
            }
        }

        if (ExceptionConstructorType.NO_COMPATIBLE_CONSTRUCTOR.equals(constructorType)) {
            throw new DimmerConfigException(String.format(
                    "Exception %s must have either empty constructor or with " +
                            "FeatureInvocation argument",
                    exceptionType.getSimpleName()));
        }
        return constructorType;
    }

    private enum ExceptionConstructorType {
        NO_COMPATIBLE_CONSTRUCTOR, EMPTY_CONSTRUCTOR, FEATURE_INVOCATION_CONSTRUCTOR
    }
}
