# Asistec UI (Frontend)

Angular client for the school attendance system.

## Prerequisites

- Node.js 22+
- Backend running at `http://localhost:8080`

## Run

```bash
npm install
npm start
```

App: http://localhost:4200

## Tests

```bash
npm test
```

## Modules

- **teacher** (`/teacher`): register and update today's attendance by section.
- **coordinator** (`/coordinator`): daily summary, pending sections, live updates via SSE.
- **history** (`/coordinator/history`): student attendance in a date range.

## API integration

The UI calls the backend REST API. For coordinator summary and pending sections, if report endpoints are unavailable it aggregates data from `GET /api/sections/{id}/attendance/today` for seeded sections 1–4.

Expected report endpoints (when available on backend):

- `GET /api/reports/today/summary`
- `GET /api/reports/pending-sections`
- `GET /api/students/{id}/attendance?startDate=&endDate=`
- `GET /api/events/attendance` (SSE)
