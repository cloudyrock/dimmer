package com.github.cloudyrock.dimmer.logic;

import com.github.cloudyrock.dimmer.DimmerConfigException;
import com.github.cloudyrock.dimmer.FeatureInvocation;
import com.github.cloudyrock.dimmer.Preconditions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

final class ExceptionUtil {

    private ExceptionUtil() {

    }

    static Object throwException(Class<? extends RuntimeException> exceptionType, FeatureInvocation featureInvocation) {
        try {
            throw createExceptionInstance(exceptionType, featureInvocation);
        } catch (InstantiationException | IllegalAccessException |
                InvocationTargetException | NoSuchMethodException e) {
            throw new DimmerConfigException(e);
        }
    }

    static RuntimeException createExceptionInstance(Class<? extends RuntimeException> exceptionType,
                                                    FeatureInvocation featureInvocation)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        return getInstanceFromType(exceptionType, featureInvocation, getExceptionConstructorType(exceptionType))
                .orElseThrow(() -> new DimmerConfigException(String.format(
                        "Exception [%s] must have either empty constructor or with FeatureInvocation argument",
                        exceptionType.getSimpleName())));
    }

    static void checkExceptionConstructorType(Class<? extends RuntimeException> exceptionType) {

        if (getExceptionConstructorType(exceptionType) == ExceptionConstructorType.NO_COMPATIBLE_CONSTRUCTOR) {
            throw new DimmerConfigException(String.format(
                    "Exception %s must have either empty constructor or with FeatureInvocation argument",
                    exceptionType.getSimpleName()));
        }
    }

    private static ExceptionConstructorType getExceptionConstructorType(Class<? extends RuntimeException> exceptionType) {

        Preconditions.checkNullOrEmpty(exceptionType, "exceptionType");
        final Constructor<?>[] constructors = exceptionType.getConstructors();
        ExceptionConstructorType constructorType = ExceptionConstructorType.NO_COMPATIBLE_CONSTRUCTOR;
        for (Constructor<?> c : constructors) {
            if (c.getParameterCount() == 1 && FeatureInvocation.class.equals(c.getParameterTypes()[0])) {
                return ExceptionConstructorType.FEATURE_INVOCATION_CONSTRUCTOR;

            } else if (c.getParameterCount() == 0) {
                constructorType = ExceptionConstructorType.EMPTY_CONSTRUCTOR;
            }
        }
        return constructorType;
    }

    private static Optional<? extends RuntimeException> getInstanceFromType(Class<? extends RuntimeException> exceptionType,
                                                                            FeatureInvocation featureInvocation,
                                                                            ExceptionConstructorType constructorType)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        switch (constructorType) {
            case EMPTY_CONSTRUCTOR:
                return Optional.of(exceptionType.getConstructor().newInstance());

            case FEATURE_INVOCATION_CONSTRUCTOR:
                return Optional.of(exceptionType.getConstructor(FeatureInvocation.class).newInstance(featureInvocation));

            case NO_COMPATIBLE_CONSTRUCTOR:
            default:
                return Optional.empty();
        }
    }

    private enum ExceptionConstructorType {
        NO_COMPATIBLE_CONSTRUCTOR, EMPTY_CONSTRUCTOR, FEATURE_INVOCATION_CONSTRUCTOR
    }
}
