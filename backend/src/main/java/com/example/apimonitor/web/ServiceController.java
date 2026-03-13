package com.example.apimonitor.web;

import com.example.apimonitor.model.HealthCheckResult;
import com.example.apimonitor.model.MonitoredService;
import com.example.apimonitor.repository.HealthCheckResultRepository;
import com.example.apimonitor.repository.MonitoredServiceRepository;
import com.example.apimonitor.web.dto.LatencyPointDto;
import com.example.apimonitor.web.dto.ServiceSummaryDto;
import com.example.apimonitor.web.dto.UptimeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private static final DateTimeFormatter ISO_FORMATTER =
            DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC);

    private final MonitoredServiceRepository serviceRepository;
    private final HealthCheckResultRepository resultRepository;

    public ServiceController(MonitoredServiceRepository serviceRepository,
                             HealthCheckResultRepository resultRepository) {
        this.serviceRepository = serviceRepository;
        this.resultRepository = resultRepository;
    }

    @GetMapping
    public List<ServiceSummaryDto> listServices(
            @RequestParam(name = "windowMinutes", defaultValue = "60") long windowMinutes
    ) {
        Instant from = Instant.now().minusSeconds(windowMinutes * 60);

        return serviceRepository.findAll().stream()
                .map(service -> toSummary(service, from))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/latency")
    public ResponseEntity<List<LatencyPointDto>> latencySeries(
            @PathVariable Long id,
            @RequestParam(name = "windowMinutes", defaultValue = "60") long windowMinutes
    ) {
        Optional<MonitoredService> maybeService = serviceRepository.findById(id);
        if (maybeService.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        MonitoredService service = maybeService.get();

        Instant from = Instant.now().minusSeconds(windowMinutes * 60);
        List<HealthCheckResult> results =
                resultRepository.findByServiceAndTimestampAfterOrderByTimestampAsc(service, from);

        List<LatencyPointDto> points = results.stream()
                .map(result -> new LatencyPointDto(
                        ISO_FORMATTER.format(result.getTimestamp()),
                        result.getLatencyMs(),
                        result.isSuccess()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(points);
    }

    @GetMapping("/{id}/uptime")
    public ResponseEntity<UptimeDto> uptime(
            @PathVariable Long id,
            @RequestParam(name = "windowMinutes", defaultValue = "60") long windowMinutes
    ) {
        Optional<MonitoredService> maybeService = serviceRepository.findById(id);
        if (maybeService.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        MonitoredService service = maybeService.get();

        Instant from = Instant.now().minusSeconds(windowMinutes * 60);
        long total = resultRepository.countChecksSince(service, from);
        long failed = resultRepository.countFailuresSince(service, from);

        UptimeDto dto = new UptimeDto();
        dto.setServiceId(service.getId());
        dto.setTotalChecks(total);
        dto.setFailedChecks(failed);

        double uptimePercent = total == 0 ? 0.0 : ((double) (total - failed) / (double) total) * 100.0;
        dto.setUptimePercent(Math.round(uptimePercent * 10.0) / 10.0);

        return ResponseEntity.ok(dto);
    }

    private ServiceSummaryDto toSummary(MonitoredService service, Instant from) {
        Optional<HealthCheckResult> latestOpt = resultRepository.findFirstByServiceOrderByTimestampDesc(service);

        String status = "UNKNOWN";
        Long latency = null;
        String lastChecked = null;
        if (latestOpt.isPresent()) {
            HealthCheckResult latest = latestOpt.get();
            status = latest.isSuccess()
                    ? (latest.getLatencyMs() > 1000 ? "DEGRADED" : "UP")
                    : "DOWN";
            latency = latest.getLatencyMs();
            lastChecked = ISO_FORMATTER.format(latest.getTimestamp());
        }

        long total = resultRepository.countChecksSince(service, from);
        long failed = resultRepository.countFailuresSince(service, from);
        Double uptimePercent = null;
        if (total > 0) {
            uptimePercent = Math.round(((double) (total - failed) / (double) total) * 1000.0) / 10.0;
        }

        ServiceSummaryDto dto = new ServiceSummaryDto();
        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setDisplayName(service.getDisplayName());
        dto.setCategory(service.getCategory());
        dto.setStatus(status);
        dto.setLatestLatencyMs(latency);
        dto.setUptimePercent(uptimePercent);
        dto.setLastCheckedIso(lastChecked);
        return dto;
    }
}

