import { useState, useEffect, useCallback } from 'react'
import { fetchServices } from '../lib/api'
import { useInterval } from '../hooks/useInterval'
import { ServiceCard } from '../components/ServiceCard'

const WINDOW_OPTIONS = [
  { label: '15m', value: 15 },
  { label: '1h', value: 60 },
  { label: '4h', value: 240 },
]

export function DashboardPage() {
  const [services, setServices] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [windowMinutes, setWindowMinutes] = useState(60)
  const [autoRefresh, setAutoRefresh] = useState(true)
  const [statusFilter, setStatusFilter] = useState('ALL')

  const load = useCallback(async () => {
    setError(null)
    try {
      const list = await fetchServices(windowMinutes)
      setServices(list)
    } catch (e) {
      setError(e.message)
      setServices([])
    } finally {
      setLoading(false)
    }
  }, [windowMinutes])

  useEffect(() => {
    setLoading(true)
    load()
  }, [load])

  useInterval(load, autoRefresh ? 15 * 1000 : null)

  const filtered = statusFilter === 'ALL'
    ? services
    : services.filter((s) => s.status === statusFilter)

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-900">
      <header className="border-b border-slate-200 bg-white dark:border-slate-700 dark:bg-slate-800">
        <div className="mx-auto max-w-7xl px-4 py-4 sm:px-6 lg:px-8">
          <h1 className="text-xl font-semibold text-slate-900 dark:text-slate-100">
            API Health & Latency Monitor
          </h1>
          <p className="mt-1 text-sm text-slate-500 dark:text-slate-400">
            Monitored services with status and latency sparklines
          </p>
        </div>
      </header>

      <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8">
        <div className="mb-4 flex flex-wrap items-center gap-3">
          <div className="flex items-center gap-2">
            <label className="text-sm font-medium text-slate-700 dark:text-slate-300">Window</label>
            <select
              value={windowMinutes}
              onChange={(e) => setWindowMinutes(Number(e.target.value))}
              className="rounded-md border border-slate-300 bg-white px-2 py-1 text-sm dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200"
            >
              {WINDOW_OPTIONS.map((o) => (
                <option key={o.value} value={o.value}>{o.label}</option>
              ))}
            </select>
          </div>
          <div className="flex items-center gap-2">
            <label className="text-sm font-medium text-slate-700 dark:text-slate-300">Status</label>
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="rounded-md border border-slate-300 bg-white px-2 py-1 text-sm dark:border-slate-600 dark:bg-slate-700 dark:text-slate-200"
            >
              <option value="ALL">All</option>
              <option value="UP">Up</option>
              <option value="DEGRADED">Degraded</option>
              <option value="DOWN">Down</option>
              <option value="UNKNOWN">Unknown</option>
            </select>
          </div>
          <label className="flex cursor-pointer items-center gap-2">
            <input
              type="checkbox"
              checked={autoRefresh}
              onChange={(e) => setAutoRefresh(e.target.checked)}
              className="rounded border-slate-300"
            />
            <span className="text-sm text-slate-600 dark:text-slate-400">Auto-refresh (15s)</span>
          </label>
          <button
            type="button"
            onClick={() => { setLoading(true); load() }}
            disabled={loading}
            className="rounded-md bg-slate-200 px-3 py-1 text-sm font-medium text-slate-700 hover:bg-slate-300 disabled:opacity-50 dark:bg-slate-600 dark:text-slate-200 dark:hover:bg-slate-500"
          >
            {loading ? 'Loading…' : 'Refresh'}
          </button>
        </div>

        {error && (
          <div className="mb-4 rounded-lg bg-red-50 p-3 text-sm text-red-800 dark:bg-red-900/30 dark:text-red-300">
            {error}
          </div>
        )}

        {loading && services.length === 0 ? (
          <div className="py-12 text-center text-slate-500 dark:text-slate-400">Loading services…</div>
        ) : (
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
            {filtered.map((s) => (
              <ServiceCard
                key={s.id}
                id={s.id}
                displayName={s.displayName}
                category={s.category}
                status={s.status}
                latestLatencyMs={s.latestLatencyMs}
                uptimePercent={s.uptimePercent}
                lastCheckedIso={s.lastCheckedIso}
                windowMinutes={windowMinutes}
              />
            ))}
          </div>
        )}

        {!loading && filtered.length === 0 && !error && (
          <p className="py-8 text-center text-slate-500 dark:text-slate-400">No services match the filter.</p>
        )}
      </main>
    </div>
  )
}
