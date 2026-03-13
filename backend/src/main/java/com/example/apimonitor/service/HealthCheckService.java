package com.example.apimonitor.service;

import com.example.apimonitor.model.HealthCheckResult;
import com.example.apimonitor.model.MonitoredService;
import com.example.apimonitor.repository.HealthCheckResultRepository;
import com.example.apimonitor.repository.MonitoredServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class HealthCheckService {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckService.class);

    private final MonitoredServiceRepository serviceRepository;
    private final HealthCheckResultRepository resultRepository;
    private final HttpClient httpClient;

    public HealthCheckService(MonitoredServiceRepository serviceRepository,
                              HealthCheckResultRepository resultRepository) {
        this.serviceRepository = serviceRepository;
        this.resultRepository = resultRepository;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    @Transactional
    public void runHealthChecks() {
        List<MonitoredService> services = serviceRepository.findAll();
        if (services.isEmpty()) {
            return;
        }

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        try {
            for (MonitoredService service : services) {
                executor.submit(() -> checkService(service));
            }
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.warn("Health check executor did not terminate within timeout");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void checkService(MonitoredService service) {
        Instant start = Instant.now();
        HealthCheckResult result = new HealthCheckResult();
        result.setService(service);
        result.setTimestamp(start);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(service.getBaseUrl()))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            long latency = Duration.between(start, Instant.now()).toMillis();

            result.setStatusCode(response.statusCode());
            result.setLatencyMs(latency);
            result.setSuccess(response.statusCode() >= 200 && response.statusCode() < 400);
        } catch (Exception ex) {
            long latency = Duration.between(start, Instant.now()).toMillis();
            result.setLatencyMs(latency);
            result.setSuccess(false);
            String message = ex.getMessage();
            if (message != null && message.length() > 900) {
                message = message.substring(0, 900);
            }
            result.setErrorMessage(message);
            log.debug("Health check failed for service {}: {}", service.getName(), message);
        }

        resultRepository.save(result);
    }
}

