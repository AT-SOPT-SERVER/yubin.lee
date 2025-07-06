package org.sopt.global.domain.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("execution(* org.sopt..*Service.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("호출: {}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* org.sopt..*Service.*(..))", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        log.info("반환:{} -> {}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(pointcut = "execution(* org.sopt..*Service.*(..))", throwing = "e")
    public void logException(JoinPoint joinPoint, Throwable e) {
        log.error("예외: {} → {}", joinPoint.getSignature(), e.getMessage());
    }
}
