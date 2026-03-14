const API_BASE = import.meta.env.VITE_API_URL ?? '';

/**
 * @param {number} [windowMinutes=60]
 * @returns {Promise<Array<{ id: number; name: string; displayName: string; category: string; status: string; latestLatencyMs: number | null; uptimePercent: number | null; lastCheckedIso: string | null }>>}
 */
export async function fetchServices(windowMinutes = 60) {
  const res = await fetch(
    `${API_BASE}/api/services?windowMinutes=${windowMinutes}`,
    { headers: { Accept: 'application/json' } }
  );
  if (!res.ok) throw new Error(`Services: ${res.status}`);
  return res.json();
}

/**
 * @param {number} id
 * @param {number} [windowMinutes=60]
 * @returns {Promise<Array<{ timestampIso: string; latencyMs: number; success: boolean }>>}
 */
export async function fetchServiceLatency(id, windowMinutes = 60) {
  const res = await fetch(
    `${API_BASE}/api/services/${id}/latency?windowMinutes=${windowMinutes}`,
    { headers: { Accept: 'application/json' } }
  );
  if (!res.ok) throw new Error(`Latency: ${res.status}`);
  return res.json();
}

/**
 * @param {number} id
 * @param {number} [windowMinutes=60]
 * @returns {Promise<{ serviceId: number; uptimePercent: number; totalChecks: number; failedChecks: number }>}
 */
export async function fetchServiceUptime(id, windowMinutes = 60) {
  const res = await fetch(
    `${API_BASE}/api/services/${id}/uptime?windowMinutes=${windowMinutes}`,
    { headers: { Accept: 'application/json' } }
  );
  if (!res.ok) throw new Error(`Uptime: ${res.status}`);
  return res.json();
}
