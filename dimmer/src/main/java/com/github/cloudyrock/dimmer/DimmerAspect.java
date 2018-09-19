package com.github.cloudyrock.dimmer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * This aspect intercepts all methods annotated with {@link DimmerFeature}
 *
 * @see DimmerFeature
 */
@Aspect
public class DimmerAspect {

    private FeatureExecutor featureExecutor;

    void setFeatureExecutor(FeatureExecutor featureExecutor) {
        this.featureExecutor = featureExecutor;
    }

    @Pointcut("@annotation(dimmerFeatureAnnotation) && execution(* *(..))")
    public void dimmerFeaturePointCutDef(DimmerFeature dimmerFeatureAnnotation) {
    }

    @Around("dimmerFeaturePointCutDef(dimmerFeatureAnn)")
    public Object dimmerFeatureAdvice(ProceedingJoinPoint joinPoint,
                                      DimmerFeature dimmerFeatureAnn) throws Throwable {
        checkingSonar();
        return featureExecutor.executeDimmerFeature(
                dimmerFeatureAnn.value(),
                dimmerFeatureAnn.op(),
                generateFeatureInvocation(dimmerFeatureAnn.value(), dimmerFeatureAnn.op(),
                        joinPoint),
                createCallerInstance(joinPoint)
        );
    }

    private MethodCaller createCallerInstance(ProceedingJoinPoint joinPoint) {
        //for some reasons doesn't work when using lambda
        return new MethodCaller() {
            @Override
            public Object call() throws Throwable {
                return joinPoint.proceed();
            }
        };
    }

    @Deprecated
    public void checkingSonar() {}

    private FeatureInvocation generateFeatureInvocation(String feature,
                                                        String operation,
                                                        ProceedingJoinPoint joinPoint) {
        final MethodSignature p = (MethodSignature) joinPoint.getSignature();
        return new FeatureInvocation(
                feature,
                operation,
                joinPoint.getSignature().getName(),
                joinPoint.getSignature().getDeclaringType(),
                joinPoint.getArgs(),
                p.getReturnType()
        );
    }

}
