package com.demo.departments.demoDepartments.config.metrics;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Configuration for custom application metrics with Micrometer
 */
@Slf4j
@Configuration
public class MetricsConfig {

    // Custom metrics for departments application
    private final AtomicInteger activePersonsGauge = new AtomicInteger(0);
    private final AtomicInteger activeAddressesGauge = new AtomicInteger(0);
    private final AtomicInteger activeContactsGauge = new AtomicInteger(0);
    private final AtomicInteger activeRolesGauge = new AtomicInteger(0);

    @Bean
    public TimedAspect timedAspect(MeterRegistry meterRegistry) {
        return new TimedAspect(meterRegistry);
    }

    @Bean
    public ClassLoaderMetrics classLoaderMetrics() {
        return new ClassLoaderMetrics();
    }

    @Bean
    public JvmMemoryMetrics jvmMemoryMetrics() {
        return new JvmMemoryMetrics();
    }

    @Bean
    public JvmGcMetrics jvmGcMetrics() {
        return new JvmGcMetrics();
    }

    @Bean
    public JvmThreadMetrics jvmThreadMetrics() {
        return new JvmThreadMetrics();
    }

    @Bean
    public ProcessorMetrics processorMetrics() {
        return new ProcessorMetrics();
    }

    @Bean
    public UptimeMetrics uptimeMetrics() {
        return new UptimeMetrics();
    }

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer() {
        return registry -> {
            // Register custom gauges
            Gauge.builder("departments.persons.active", activePersonsGauge, AtomicInteger::get)
                    .description("The number of active persons in the system")
                    .tags("type", "person")
                    .register(registry);
    
            Gauge.builder("departments.addresses.active", activeAddressesGauge, AtomicInteger::get)
                    .description("The number of active addresses in the system")
                    .tags("type", "address")
                    .register(registry);
    
            Gauge.builder("departments.contacts.active", activeContactsGauge, AtomicInteger::get)
                    .description("The number of active contacts in the system")
                    .tags("type", "contact")
                    .register(registry);
    
            Gauge.builder("departments.roles.active", activeRolesGauge, AtomicInteger::get)
                    .description("The number of active roles in the system")
                    .tags("type", "role")
                    .register(registry);
    
            // Create API request counters
            Counter.builder("departments.api.requests")
                    .description("Number of API requests")
                    .tags("status", "success")
                    .register(registry);
    
            Counter.builder("departments.api.requests")
                    .description("Number of API requests")
                    .tags("status", "error")
                    .register(registry);
    
            // Create API request timers
            Timer.builder("departments.api.request.duration")
                    .description("API request duration")
                    .tags("endpoint", "person")
                    .register(registry);
    
            Timer.builder("departments.api.request.duration")
                    .description("API request duration")
                    .tags("endpoint", "address")
                    .register(registry);
    
            Timer.builder("departments.api.request.duration")
                    .description("API request duration")
                    .tags("endpoint", "contact")
                    .register(registry);
    
            Timer.builder("departments.api.request.duration")
                    .description("API request duration")
                    .tags("endpoint", "role")
                    .register(registry);
        };
    }

    // Getters for metrics gauges to be used by services
    public AtomicInteger getActivePersonsGauge() {
        return activePersonsGauge;
    }

    public AtomicInteger getActiveAddressesGauge() {
        return activeAddressesGauge;
    }

    public AtomicInteger getActiveContactsGauge() {
        return activeContactsGauge;
    }

    public AtomicInteger getActiveRolesGauge() {
        return activeRolesGauge;
    }
}