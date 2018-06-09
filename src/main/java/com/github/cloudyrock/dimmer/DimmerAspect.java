package com.github.cloudyrock.dimmer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class DimmerAspect {

    private DimmerProcessor dimmerProcessor;

    void setDimmerProcessor(DimmerProcessor dimmerProcessor) {
        this.dimmerProcessor = dimmerProcessor;
    }

    @Pointcut("@annotation(dimmerFeatureAnnotation) && execution(* *(..))")
    public void dimmerFeaturePointCutDef(DimmerFeature dimmerFeatureAnnotation) {
    }

    @Around("dimmerFeaturePointCutDef(dimmerFeatureAnn)")
    public Object dimmerFeatureAdvice(ProceedingJoinPoint joinPoint,
                                      DimmerFeature dimmerFeatureAnn) throws Throwable {
        return dimmerProcessor.executeDimmerFeature(
                dimmerFeatureAnn,
                generateFeatureInvocation(joinPoint),
                joinPoint
        );
    }

    private FeatureInvocation generateFeatureInvocation(ProceedingJoinPoint joinPoint) {
        return new FeatureInvocation(
                joinPoint.getSignature().getName(),
                joinPoint.getSignature().getDeclaringType(),
                joinPoint.getArgs()
        );
    }

}
