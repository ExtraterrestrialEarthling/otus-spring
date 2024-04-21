package ru.haidarov.hw.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

@Component
@Aspect
public class ExecutionTimeLoggingAspect {
    private final Logger logger;

    public ExecutionTimeLoggingAspect() {
        this.logger = LoggerFactory.getLogger(ExecutionTimeLoggingAspect.class);
    }

    @Around("within(ru.haidarov.hw..*)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {


        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        logger.debug("Method call: {}, executed in: {}",
                joinPoint.getSignature().toShortString(), executionTime);

        return result;
    }
}
