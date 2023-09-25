package com.ca.javaassessment.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* com.ca.javaassessment..*.*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result;

        try {
            result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            logger.info("Executed method {} in {} ms",
                    joinPoint.getSignature(),
                    elapsedTime);
        } catch (Exception e) {
            logger.error("Error executing {} with arguments {}",
                    joinPoint.getSignature(),
                    Arrays.toString(joinPoint.getArgs()),
                    e);
            throw e;
        }

        return result;
    }
}
