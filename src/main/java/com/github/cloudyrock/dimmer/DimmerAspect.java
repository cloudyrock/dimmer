package com.github.cloudyrock.dimmer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.function.Function;

@Aspect
public class DimmerAspect {

    @Pointcut("@annotation(featureCheckAnnotation) && execution(* *(..))")
    public void featureCheckPointCutDef(FeatureCheck featureCheckAnnotation) {
    }

    @Pointcut("@annotation(featureOffAnnotation) && execution(* *(..))")
    public void featureOffPointCutDef(FeatureOff featureOffAnnotation) {
    }

    @Around("featureCheckPointCutDef(featureCheckAnn)")
    public Object featureCheckAdvice(ProceedingJoinPoint joinPoint,
                                     FeatureCheck featureCheckAnn) throws Throwable {

        if (DimmerConfiguration.contains(featureCheckAnn.feature())) {

            Function<FeatureInvocation, Object> behaviour =
                    DimmerConfiguration.getBehaviour(featureCheckAnn.feature());
            return behaviour.apply(generateFeatureInvocation(joinPoint));

        }
        return joinPoint.proceed();
    }

    @Around("featureOffPointCutDef(featureOffAnn)")
    public Object featureOffAdvice(FeatureOff featureOffAnn) throws Throwable {
        switch (featureOffAnn.value()) {
            case RETURN_NULL:
                return null;

            case THROW_EXCEPTION:
            default:
                throw featureOffAnn.exception().getConstructor().newInstance();
        }

    }

    private FeatureInvocation generateFeatureInvocation(ProceedingJoinPoint joinPoint) {
        return new FeatureInvocation(
                joinPoint.getSignature().getName(),
                joinPoint.getSignature().getDeclaringType(),
                joinPoint.getArgs()
        );
    }

}
