# Risk Register

Risk management is integrated into the Personal Kanban development process.
Risks are reviewed when relevant work begins and when each development cycle is completed.

Risk exposure is calculated as:

> Probability × Impact

| Exposure | Classification |
|---:|---|
| 15–25 | High |
| 8–14 | Medium |
| 1–7 | Low |

## Risks

| Risk | Probability | Impact | Exposure | Response | Mitigation / Contingency | Status |
|---|---:|---:|---:|---|---|---|
| Integration between services and technologies | 4 | 5 | 20 | Mitigate | Contract-first APIs, generated clients and integration testing; restore a compatible contract or simplify the interaction if necessary | Monitoring |
| Scope becomes too large | 4 | 4 | 16 | Avoid / Mitigate | MoSCoW prioritisation and deferral of non-essential functionality | Monitoring |
| Too many technologies increase development complexity | 4 | 4 | 16 | Mitigate | Reuse the existing stack and remove optional components where possible; de-scope non-essential integrations | Monitoring |
| Blockchain or RPC failure | 3 | 5 | 15 | Mitigate | Circuit breaker, bounded retries and settlement-pending state; retry settlement when the provider recovers | Not Started |
| Real-time tracking interruption | 3 | 4 | 12 | Mitigate | Reconnection and latest-location recovery; fall back to periodic polling or last-known location | Not Started |
| Cloud cost or quota limitations | 3 | 3 | 9 | Mitigate | Develop locally where practical and monitor cloud usage; reduce or pause non-critical resources | Monitoring |
