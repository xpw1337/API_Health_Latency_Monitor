package com.example.apimonitor.web.dto;

public class LatencyPointDto {

    private String timestampIso;
    private long latencyMs;
    private boolean success;

    public LatencyPointDto() {
    }

    public LatencyPointDto(String timestampIso, long latencyMs, boolean success) {
        this.timestampIso = timestampIso;
        this.latencyMs = latencyMs;
        this.success = success;
    }

    public String getTimestampIso() {
        return timestampIso;
    }

    public void setTimestampIso(String timestampIso) {
        this.timestampIso = timestampIso;
    }

    public long getLatencyMs() {
        return latencyMs;
    }

    public void setLatencyMs(long latencyMs) {
        this.latencyMs = latencyMs;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

