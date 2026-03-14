import { useEffect, useRef } from 'react'

/**
 * Run a callback on a fixed interval. Respects pause and delay in ms.
 * @param {() => void} callback
 * @param {number | null} delayMs null to pause
 */
export function useInterval(callback, delayMs) {
  const savedCallback = useRef(callback)
  useEffect(() => {
    savedCallback.current = callback
  }, [callback])

  useEffect(() => {
    if (delayMs == null || delayMs <= 0) return
    const id = setInterval(() => savedCallback.current(), delayMs)
    return () => clearInterval(id)
  }, [delayMs])
}
