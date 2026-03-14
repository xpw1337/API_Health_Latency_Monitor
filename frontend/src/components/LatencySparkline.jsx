const W = 120
const H = 32

/**
 * @param {{ data: Array<{ latencyMs: number }>; className?: string }} props
 */
export function LatencySparkline({ data, className = '' }) {
  if (!data?.length) {
    return (
      <svg width={W} height={H} className={className} aria-hidden>
        <text x={W / 2} y={H / 2} textAnchor="middle" className="fill-gray-400 text-[10px]">No data</text>
      </svg>
    )
  }

  const values = data.map((d) => d.latencyMs)
  const min = Math.min(...values)
  const max = Math.max(...values)
  const range = max - min || 1
  const padding = 2
  const innerW = W - padding * 2
  const innerH = H - padding * 2

  const points = values.map((v, i) => {
    const x = padding + (i / (values.length - 1 || 1)) * innerW
    const y = padding + innerH - ((v - min) / range) * innerH
    return `${x},${y}`
  }).join(' ')

  return (
    <svg width={W} height={H} className={className} aria-hidden>
      <polyline
        fill="none"
        stroke="currentColor"
        strokeWidth="1.5"
        strokeLinecap="round"
        strokeLinejoin="round"
        className="text-slate-500"
        points={points}
      />
    </svg>
  )
}
