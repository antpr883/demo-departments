package com.demo.departments.demoDepartments.config.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Aspect for tracking API metrics
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MetricsAspect {

    private final MeterRegistry meterRegistry;

    @Around("execution(* com.demo.departments.demoDepartments.controller.*Controller.*(..))")
    public Object measureApiRequestDuration(ProceedingJoinPoint joinPoint) throws Throwable {
        // Extract controller and method names
        String controllerName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String endpoint = controllerName.replace("Controller", "").toLowerCase();

        // Get timer for this endpoint
        Timer timer = meterRegistry.timer("departments.api.request.duration", "endpoint", endpoint, "method", methodName);

        // Get success counter
        Counter successCounter = meterRegistry.counter("departments.api.requests", "status", "success", "endpoint", endpoint);
        Counter errorCounter = meterRegistry.counter("departments.api.requests", "status", "error", "endpoint", endpoint);

        long startTime = System.nanoTime();
        try {
            // Invoke the intercepted method
            Object result = joinPoint.proceed();
            long duration = System.nanoTime() - startTime;

            // Record timing and success counter
            timer.record(duration, TimeUnit.NANOSECONDS);
            successCounter.increment();
            log.debug("API call to {}.{} completed in {} ns", controllerName, methodName, duration);

            return result;
        } catch (Throwable t) {
            // Record error counter
            errorCounter.increment();
            log.error("API call to {}.{} failed: {}", controllerName, methodName, t.getMessage());
            throw t;
        }
    }
}