# API Health & Latency Monitor

A small, infrastructure-focused monitoring app that continuously pings dozens of public APIs, stores latency and status in an in-memory database, and serves a React dashboard with status cards and latency sparklines. Built to showcase SRE/platform-style tooling with **Java 21 virtual threads** and a modern React + Tailwind UI.

---

## Features

- **50+ public APIs** monitored on a schedule (GitHub, Stripe, Cloudflare, JSONPlaceholder, and more)
- **Concurrent health checks** via Java 21 virtual threads; results stored in H2
- **REST API** for service list, per-service latency series, and uptime over configurable windows
- **Dashboard**: responsive grid of service cards with status badges (Up / Degraded / Down), latest latency, uptime %, and inline latency sparklines
- **Controls**: time window (15m / 1h / 4h), status filter, auto-refresh every 15s, manual refresh

---

## Tech stack

| Layer   | Stack |
|--------|--------|
| Backend | Java 21, Spring Boot 3, Spring Data JPA, H2 (in-memory), Actuator |
| Frontend | React 19, Vite 5, Tailwind CSS 4 |
| Infra   | Virtual threads, scheduled tasks, CORS, proxy in dev |

---

## Project structure

```
├── backend/          # Spring Boot API
│   ├── src/main/java/com/example/apimonitor/
│   │   ├── config/   # DataInitializer, HealthCheckScheduler, WebConfig (CORS)
│   │   ├── model/    # MonitoredService, HealthCheckResult
│   │   ├── repository/
│   │   ├── service/  # HealthCheckService (virtual-thread executor)
│   │   └── web/      # ServiceController, DTOs
│   └── src/main/resources/
│       └── application.yml
├── frontend/         # Vite + React app
│   ├── src/
│   │   ├── components/  # ServiceCard, LatencySparkline
│   │   ├── pages/       # DashboardPage
│   │   ├── hooks/       # useInterval
│   │   └── lib/         # api.js
│   └── index.html
└── README.md
```

---

## Running locally

**Prerequisites:** Java 21, Maven, Node.js (LTS).

1. **Start the backend**

   ```bash
   cd backend
   mvn spring-boot:run
   ```

   - API base: `http://localhost:8080`
   - H2 console: `http://localhost:8080/h2-console`  
     (JDBC URL: `jdbc:h2:mem:apimonitordb`, user: `sa`, password: blank)

2. **Start the frontend** (in another terminal)

   ```bash
   cd frontend
   npm install
   npm run dev
   ```

   - App: `http://localhost:5173`  
   - Vite proxies `/api` to the backend, so no CORS issues in dev.

3. Open the app in the browser; the dashboard will show services and fill in as health checks run (every 30s).

---

## API overview

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/services?windowMinutes=60` | List all services with latest status, latency, uptime % |
| GET | `/api/services/{id}/latency?windowMinutes=60` | Time series of latency (for sparklines) |
| GET | `/api/services/{id}/uptime?windowMinutes=60` | Uptime percentage and check counts |

---

## Infra concepts demonstrated

- **Virtual threads (Java 21)** — high-concurrency health checks without a large thread pool
- **Scheduled tasks** — periodic monitoring (e.g. every 30s)
- **In-memory DB (H2)** — fast, ephemeral metrics; optional H2 console for inspection
- **REST + CORS** — decoupled frontend; CORS configured for the UI origin
- **Time-windowed metrics** — latency history and uptime over 15m / 1h / 4h

---

## License

MIT (or your choice).
