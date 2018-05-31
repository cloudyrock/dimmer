package net.cloudyrock.toggler;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.After;

@Aspect
public class ToggleAspect {

    @Pointcut("@annotation(toggleAnnotation) && execution(* *(..))")
    public void annotationPointCutDefinition(ToggleOff toggleAnnotation){
    }

    @Around("annotationPointCutDefinition(toggleAnnotation)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, ToggleOff toggleAnnotation) throws Throwable {
        System.out.println("AROUND ADVICE");
        return joinPoint.proceed();
    }

    @After("annotationPointCutDefinition(toggleAnnotation)")
    public void printNewLine(JoinPoint pointcut, ToggleOff toggleAnnotation){
        System.out.println("AFTER");
    }
}
