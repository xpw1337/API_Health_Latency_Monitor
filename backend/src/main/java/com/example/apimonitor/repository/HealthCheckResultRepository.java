package com.example.apimonitor.repository;

import com.example.apimonitor.model.HealthCheckResult;
import com.example.apimonitor.model.MonitoredService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface HealthCheckResultRepository extends JpaRepository<HealthCheckResult, Long> {

    List<HealthCheckResult> findByServiceAndTimestampAfterOrderByTimestampAsc(
            MonitoredService service,
            Instant after
    );

    Optional<HealthCheckResult> findFirstByServiceOrderByTimestampDesc(MonitoredService service);

    @Query("""
            select count(r) from HealthCheckResult r
            where r.service = :service
              and r.timestamp >= :from
            """)
    long countChecksSince(MonitoredService service, Instant from);

    @Query("""
            select count(r) from HealthCheckResult r
            where r.service = :service
              and r.timestamp >= :from
              and r.success = false
            """)
    long countFailuresSince(MonitoredService service, Instant from);
}

