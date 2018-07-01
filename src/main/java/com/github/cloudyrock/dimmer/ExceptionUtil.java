package com.github.cloudyrock.dimmer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static com.github.cloudyrock.dimmer.ExceptionConstructorType.EMPTY_CONSTRUCTOR;
import static com.github.cloudyrock.dimmer.ExceptionConstructorType.FEATURE_INVOCATION_CONSTRUCTOR;
import static com.github.cloudyrock.dimmer.ExceptionConstructorType.NO_COMPATIBLE_CONSTRUCTOR;

final class ExceptionUtil {
    
    static Object throwException(Class<? extends RuntimeException> exceptionType,
                          ExceptionConstructorType constructorType,
                          FeatureInvocation f) {
        try {
            switch (constructorType) {
                case EMPTY_CONSTRUCTOR:
                    throw exceptionType.getConstructor().newInstance();

                case FEATURE_INVOCATION_CONSTRUCTOR:
                    throw exceptionType.getConstructor(FeatureInvocation.class)
                            .newInstance(f);
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

    static ExceptionConstructorType checkAndGetExceptionConstructorType(
            Class<? extends RuntimeException> exceptionType) {
        Util.checkArgument(exceptionType, "exceptionType");
        final Constructor<?>[] constructors = exceptionType.getConstructors();
        ExceptionConstructorType constructorType = NO_COMPATIBLE_CONSTRUCTOR;
        for (Constructor<?> c : constructors) {
            if (c.getParameterCount() == 1 && FeatureInvocation.class
                    .equals(c.getParameterTypes()[0])) {
                constructorType = FEATURE_INVOCATION_CONSTRUCTOR;
                break;
            } else if (c.getParameterCount() == 0) {
                constructorType = EMPTY_CONSTRUCTOR;
            }
        }

        if (NO_COMPATIBLE_CONSTRUCTOR.equals(constructorType)) {
            throw new DimmerConfigException(String.format(
                    "Exception %s must have either empty constructor or with " +
                            "FeatureInvocation argument",
                    exceptionType.getSimpleName()));
        }
        return constructorType;
    }

}
