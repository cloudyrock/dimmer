package com.github.cloudyrock.dimmer.displayname;

import com.github.cloudyrock.dimmer.DimmerFeature;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * This aspect intercepts all the calls to methods annotated with (@{@link DimmerFeature})
 *
 * @author Antonio Perez Dieppa
 * @see DimmerFeature
 * @since 11/06/2018
 */
@Aspect
public class DisplayNameAspect {

    private static final boolean ENABLED = true;
    private static final String RED = (char) 27 + "[31m";
    private static final String GREEN = (char) 27 + "[32m";
    private static final String BLUE = (char) 27 + "[34m";
    private static final String DEFAULT = (char) 27 + "[39m";

    private static final Set<String> loggedClasses = new HashSet<>();

    private ProxyFactory proxyFactory = new ProxyFactory(
            (PreInterceptor) () -> {
            },
            Collections.emptySet(),
            Collections.emptySet());

    @Pointcut("@annotation(displayNameAnn) && execution(* *(..))")
    public void displayNamePointCutDef(DisplayName displayNameAnn) {
    }

    @Around("displayNamePointCutDef(displayNameAnn)")
    public Object dimmerFeatureAdvice(ProceedingJoinPoint joinPoint,
                                      DisplayName displayNameAnn) throws Throwable {
        return ENABLED ? processDisplayName(joinPoint, displayNameAnn) : joinPoint
                .proceed();
    }

    private Object processDisplayName(ProceedingJoinPoint joinPoint,
                                      DisplayName displayNameAnn) throws Throwable {

        printClass(joinPoint);
        final boolean classContainsRuleEx = isRuleManaged(joinPoint);
        InternalChecker ruleChecker = new InternalChecker();
        if (classContainsRuleEx) {
            replaceRuleException(joinPoint, ruleChecker);
        }
        final String methodName = joinPoint.getSignature().getName();
        final Class<? extends Throwable> expected = extractJunitExpected(joinPoint);
        try {
            Object result = joinPoint.proceed();
            if (Test.None.class
                    .equals(expected) && !(classContainsRuleEx && ruleChecker.checked)) {
                printSuccess(displayNameAnn.value(), methodName);
            } else {
                printFail(displayNameAnn.value(), methodName);
            }
            return result;
        } catch (Throwable thrownException) {
            if (((classContainsRuleEx && ruleChecker.checked) && Test.None.class
                    .equals(expected)) ||
                    expected.isInstance(thrownException)) {
                printSuccess(displayNameAnn.value(), methodName);
            } else {
                printFail(displayNameAnn.value(), methodName);
            }
            throw thrownException;
        }
    }

    private void replaceRuleException(ProceedingJoinPoint joinPoint, InternalChecker checker) throws IllegalAccessException {
        Field exField =
                Stream.of(joinPoint.getSignature().getDeclaringType().getDeclaredFields())
                        .filter(field -> ExpectedException.class.equals(field.getType()))
                        .findFirst().get();
        final ExpectedException proxiedExpected = proxyFactory.createProxyFromOriginal(
                (ExpectedException) exField.get(joinPoint.getThis()),
                () -> checker.setChecked(true));
        exField.set(joinPoint.getThis(), proxiedExpected);

    }

    private boolean isRuleManaged(ProceedingJoinPoint joinPoint) {
        return Stream.of(joinPoint.getSignature().getDeclaringType().getDeclaredFields())
                .anyMatch(field -> ExpectedException.class.equals(field.getType()));
    }

    private void printClass(ProceedingJoinPoint joinPoint) {
        final String declaringClass =
                joinPoint.getSignature().getDeclaringType().getSimpleName();
        if (!loggedClasses.contains(declaringClass)) {
            System.out.println(BLUE + ">>> " + declaringClass + DEFAULT);
            loggedClasses.add(declaringClass);
        }
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Throwable> extractJunitExpected(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Method m = joinPoint.getSignature().getDeclaringType()
                .getMethod(joinPoint.getSignature().getName());
        Test testAnn = m.getAnnotation(Test.class);
        return testAnn.expected();
    }

    private String getStartingMessage(String displayNameMsg) {
        return "\t- " + displayNameMsg;
    }

    private void printSuccess(String displayNameMsg, String methodName) {
        System.out.println(
                GREEN + getStartingMessage(
                        displayNameMsg) + " (" + methodName + ")" + DEFAULT);
    }

    private void printFail(String displayNameMsg, String methodName) {
        System.out.println(
                RED + getStartingMessage(
                        displayNameMsg) + " (" + methodName + ")" + DEFAULT);
    }

    class InternalChecker {
        private boolean checked = false;

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public boolean isChecked() {
            return checked;
        }
    }

}
