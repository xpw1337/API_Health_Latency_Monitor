/** @typedef {{ id: number; name: string; displayName: string; category: string; status: string; latestLatencyMs: number | null; uptimePercent: number | null; lastCheckedIso: string | null }} ServiceSummary */

/** @typedef {{ timestampIso: string; latencyMs: number; success: boolean }} LatencyPoint */

/** @typedef {{ serviceId: number; uptimePercent: number; totalChecks: number; failedChecks: number }} UptimeResponse */
