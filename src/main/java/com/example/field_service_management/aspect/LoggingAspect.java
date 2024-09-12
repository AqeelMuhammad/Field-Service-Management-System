package com.example.field_service_management.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public LoggingAspect(Environment env) {
    }

    // Define a Pointcut for the application package
    @Pointcut("within(com.example.field_service_management.*)")
    public void applicationPackagePointcut() {
        // Pointcut for methods within the application package
    }

    // Around advice for logging method entry and exit
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}-{}() with argument[s] = {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    Arrays.toString(joinPoint.getArgs()));
        }

        try {
            Object result = joinPoint.proceed(); // Proceed with method execution
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}",
                        joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (Throwable e) {
            logException(joinPoint, e);
            throw e;
        }
    }

    // After throwing advice for logging exceptions
    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        logException(joinPoint, e);
    }

    private void logException(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}() with cause = '{}'",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                e.getCause() != null ? e.getCause() : "NULL");
    }

    // Logging before the execution of any method in the specified package
    @Before("execution(* com.example.field_service_management.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.debug("Method called: {}", joinPoint.getSignature().getName());
    }

    // Logging after the method execution
    @After("execution(* com.example.field_service_management.service.*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        log.debug("Method execution finished: {}", joinPoint.getSignature().getName());
    }
}
