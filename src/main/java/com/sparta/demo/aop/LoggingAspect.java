package com.sparta.demo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Order(1)
public class LoggingAspect {

    @Before("@annotation(com.sparta.demo.common.annotation.Loggable)")
    public void logMethodDetails(JoinPoint joinPoint) {
        log.info("Method name: {}", joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            log.info("Arguments: {}", args);
        } else {
            log.info("Arguments: (none)");
        }
    }
}