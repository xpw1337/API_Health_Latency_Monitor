package com.example.apimonitor.web.dto;

public class ServiceSummaryDto {

    private Long id;
    private String name;
    private String displayName;
    private String category;
    private String status;
    private Long latestLatencyMs;
    private Double uptimePercent;
    private String lastCheckedIso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getLatestLatencyMs() {
        return latestLatencyMs;
    }

    public void setLatestLatencyMs(Long latestLatencyMs) {
        this.latestLatencyMs = latestLatencyMs;
    }

    public Double getUptimePercent() {
        return uptimePercent;
    }

    public void setUptimePercent(Double uptimePercent) {
        this.uptimePercent = uptimePercent;
    }

    public String getLastCheckedIso() {
        return lastCheckedIso;
    }

    public void setLastCheckedIso(String lastCheckedIso) {
        this.lastCheckedIso = lastCheckedIso;
    }
}

