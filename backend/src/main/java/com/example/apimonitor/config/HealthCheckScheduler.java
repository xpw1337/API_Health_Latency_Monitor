package com.example.apimonitor.config;

import com.example.apimonitor.service.HealthCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HealthCheckScheduler {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckScheduler.class);

    private final HealthCheckService healthCheckService;

    public HealthCheckScheduler(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @Scheduled(fixedDelayString = "30000")
    public void scheduleHealthChecks() {
        log.debug("Starting scheduled health checks");
        healthCheckService.runHealthChecks();
    }
}

