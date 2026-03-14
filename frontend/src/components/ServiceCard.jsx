import { useState, useEffect } from 'react'
import { LatencySparkline } from './LatencySparkline'
import { fetchServiceLatency } from '../lib/api'

const statusStyles = {
  UP: 'bg-emerald-100 text-emerald-800 dark:bg-emerald-900/40 dark:text-emerald-300',
  DEGRADED: 'bg-amber-100 text-amber-800 dark:bg-amber-900/40 dark:text-amber-300',
  DOWN: 'bg-red-100 text-red-800 dark:bg-red-900/40 dark:text-red-300',
  UNKNOWN: 'bg-slate-100 text-slate-600 dark:bg-slate-700 dark:text-slate-400',
}

/**
 * @param {{
 *   id: number
 *   displayName: string
 *   category: string
 *   status: string
 *   latestLatencyMs: number | null
 *   uptimePercent: number | null
 *   lastCheckedIso: string | null
 *   windowMinutes: number
 * }} props
 */
export function ServiceCard({
  id,
  displayName,
  category,
  status,
  latestLatencyMs,
  uptimePercent,
  lastCheckedIso,
  windowMinutes,
}) {
  const [latencyData, setLatencyData] = useState([])

  useEffect(() => {
    let cancelled = false
    fetchServiceLatency(id, windowMinutes)
      .then((list) => { if (!cancelled) setLatencyData(list) })
      .catch(() => { if (!cancelled) setLatencyData([]) })
    return () => { cancelled = true }
  }, [id, windowMinutes])

  const lastChecked = lastCheckedIso
    ? new Date(lastCheckedIso).toLocaleString(undefined, { month: 'numeric', day: 'numeric', hour: '2-digit', minute: '2-digit' })
    : '—'

  const badgeClass = statusStyles[status] ?? statusStyles.UNKNOWN

  return (
    <article className="min-w-0 overflow-hidden rounded-xl border border-slate-200 bg-white p-4 shadow-sm transition hover:shadow dark:border-slate-700 dark:bg-slate-800">
      <div className="flex items-start justify-between gap-2">
        <div className="min-w-0 flex-1">
          <h3 className="truncate font-medium text-slate-900 dark:text-slate-100">{displayName}</h3>
          <p className="truncate text-xs text-slate-500 dark:text-slate-400">{category}</p>
        </div>
        <span className={`shrink-0 rounded-full px-2 py-0.5 text-xs font-medium ${badgeClass}`}>
          {status}
        </span>
      </div>
      <div className="mt-3 flex min-w-0 flex-wrap items-center gap-x-4 gap-y-1 overflow-hidden text-sm">
        {latestLatencyMs != null && (
          <span className="shrink-0 text-slate-600 dark:text-slate-300">
            <span className="font-mono">{latestLatencyMs}</span> ms
          </span>
        )}
        {uptimePercent != null && (
          <span className="shrink-0 text-slate-600 dark:text-slate-300">
            Uptime <span className="font-medium">{uptimePercent}%</span>
          </span>
        )}
      </div>
      <div className="mt-2 flex min-w-0 items-center justify-between gap-2">
        <LatencySparkline data={latencyData} className="shrink-0" />
        <time
          className="shrink-0 text-right text-[10px] text-slate-400 dark:text-slate-500"
          dateTime={lastCheckedIso ?? undefined}
        >
          {lastChecked}
        </time>
      </div>
    </article>
  )
}
