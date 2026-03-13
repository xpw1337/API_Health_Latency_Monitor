package com.example.apimonitor.repository;

import com.example.apimonitor.model.MonitoredService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonitoredServiceRepository extends JpaRepository<MonitoredService, Long> {

    Optional<MonitoredService> findByName(String name);
}

