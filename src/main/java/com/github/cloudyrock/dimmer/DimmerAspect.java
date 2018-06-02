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

    @Pointcut("@annotation(featureCheckAnnotation) && execution(* *(..))")
    public void featureCheckPointCutDef(FeatureCheck featureCheckAnnotation) {
    }

    @Pointcut("@annotation(featureOffAnnotation) && execution(* *(..))")
    public void featureOffPointCutDef(FeatureOff featureOffAnnotation) {
    }

    @Around("featureCheckPointCutDef(featureCheckAnn)")
    public Object featureCheckAdvice(ProceedingJoinPoint joinPoint,
                                     FeatureCheck featureCheckAnn) throws Throwable {
        return dimmerProcessor.runBehaviourIfExistsOrReaInvocation(
                featureCheckAnn.feature(),
                generateFeatureInvocation(joinPoint),
                joinPoint
        );
    }

    @Around("featureOffPointCutDef(featureOffAnn)")
    public Object featureOffAdvice(FeatureOff featureOffAnn) throws Throwable {
        return dimmerProcessor.runFeatureOff(
                featureOffAnn.value(),
                featureOffAnn.exception());

    }

    private FeatureInvocation generateFeatureInvocation(ProceedingJoinPoint joinPoint) {
        return new FeatureInvocation(
                joinPoint.getSignature().getName(),
                joinPoint.getSignature().getDeclaringType(),
                joinPoint.getArgs()
        );
    }

}
