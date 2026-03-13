package com.example.apimonitor.web.dto;

public class UptimeDto {

    private Long serviceId;
    private Double uptimePercent;
    private long totalChecks;
    private long failedChecks;

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Double getUptimePercent() {
        return uptimePercent;
    }

    public void setUptimePercent(Double uptimePercent) {
        this.uptimePercent = uptimePercent;
    }

    public long getTotalChecks() {
        return totalChecks;
    }

    public void setTotalChecks(long totalChecks) {
        this.totalChecks = totalChecks;
    }

    public long getFailedChecks() {
        return failedChecks;
    }

    public void setFailedChecks(long failedChecks) {
        this.failedChecks = failedChecks;
    }
}

