package net.cloudyrock.toggler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.function.Function;

@Aspect
public class TogglerAspect {

    @Pointcut("@annotation(toggleCheck) && execution(* *(..))")
    public void toggleCheckPointCutDef(ToggleCheck toggleCheck) {
    }

    @Pointcut("@annotation(toggleOffAndThrow) && execution(* *(..))")
    public void toggleOffAndThrowPointCutDef(ToggleOffAndThrow toggleOffAndThrow) {
    }

    @Pointcut("@annotation(toggleOffAndNull) && execution(* *(..))")
    public void toggleOffAndNullPointCutDef(ToggleOffAndNull toggleOffAndNull) {
    }

    @Around("toggleCheckPointCutDef(toggleCheck)")
    public Object toggleCheckAdvice(ProceedingJoinPoint joinPoint,
                                    ToggleCheck toggleCheck) throws Throwable {
        String feature = toggleCheck.feature();
        if (!TogglerConfiguration.contains(feature)) {
            return joinPoint.proceed();
        } else {
            Function<FeatureInvocation, Object> handler =
                    TogglerConfiguration.getBehaviour(feature);
            FeatureInvocation invocation = generateFeatureInvocation(joinPoint);
            return handler.apply(invocation);
        }

    }

    @Around("toggleOffAndThrowPointCutDef(toggleOffAndThrow)")
    public Object toggleOffAndThrowAdvice(
            ProceedingJoinPoint joinPoint,
            ToggleOffAndThrow toggleOffAndThrow) throws Throwable {
        throw toggleOffAndThrow.value().getConstructor().newInstance();

    }

    @Around("toggleOffAndNullPointCutDef(toggleOffAndNull)")
    public Object toggleOffAndNullAdvice(
            ProceedingJoinPoint joinPoint,
            ToggleOffAndNull toggleOffAndNull) throws Throwable {
        return null;

    }

    private FeatureInvocation generateFeatureInvocation(ProceedingJoinPoint joinPoint) {
        return new FeatureInvocation(
                joinPoint.getSignature().getName(),
                joinPoint.getSignature().getDeclaringType(),
                joinPoint.getArgs()
        );
    }

}
