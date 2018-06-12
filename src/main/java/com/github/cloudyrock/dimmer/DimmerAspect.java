package com.github.cloudyrock.dimmer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
/**
 * This aspect intercepts all the calls to methods annotated with (@{@link DimmerFeature})
 *
 * @author Antonio Perez Dieppa
 * @see DimmerFeature
 * @since 11/06/2018
 */
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
                generateFeatureInvocation(dimmerFeatureAnn.value(), joinPoint),
                joinPoint
        );
    }

    private FeatureInvocation generateFeatureInvocation(String feature, ProceedingJoinPoint joinPoint) {
        return new FeatureInvocation(
                feature,
                joinPoint.getSignature().getName(),
                joinPoint.getSignature().getDeclaringType(),
                joinPoint.getArgs()
        );
    }

}
