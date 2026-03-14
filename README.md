# API Health & Latency Monitor

A small infrastructure-focused monitoring app: it continuously pings a list of public APIs, stores latency and status in an in-memory database, and exposes a React dashboard with status cards and latency sparklines.

## Architecture

- **Backend (Java 21, Spring Boot)**  
  - Scheduled job (every 30s) using **virtual threads** to run HTTP GETs against 50+ public APIs concurrently.  
  - Results (latency, status code, success/failure) are stored in **H2 in-memory** via Spring Data JPA.  
  - REST API: list services with latest status, per-service latency time series, and uptime over a configurable window.  
  - CORS enabled for the frontend origin.

- **Frontend (React 17+, Vite, Tailwind)**  
  - Dashboard: responsive grid of **service cards** (name, status badge, latest latency, uptime %, last checked).  
  - Each card shows a **latency sparkline** (SVG polyline).  
  - Global controls: time window (15m / 1h / 4h), status filter, auto-refresh (15s), manual refresh.

## Running locally

1. **Backend**  
   From project root:
   ```bash
   cd backend
   mvn spring-boot:run
   ```
   API: `http://localhost:8080`.  
   H2 console (dev): `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:apimonitordb`).

2. **Frontend**  
   In another terminal:
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
   App: `http://localhost:5173`.  
   Vite proxies `/api` to `http://localhost:8080`, so the UI talks to the backend without CORS in dev.

## Infra concepts demonstrated

- **Virtual threads (Java 21)** for high-concurrency, non-blocking health checks.  
- **Scheduled tasks** for periodic monitoring.  
- **In-memory DB (H2)** for fast, ephemeral metrics.  
- **REST API + CORS** for a decoupled frontend.  
- **Time-windowed metrics** (latency series, uptime %) for dashboards.
