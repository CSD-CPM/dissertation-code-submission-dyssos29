# Risk Register

Risk management is integrated into the Solo Scrumban development process.
Risks are reviewed when relevant work begins and when each development cycle
is completed.

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
| Integration between services and technologies | 4 | 5 | 20 | Mitigate | Contract-first APIs/events, generated clients and integration testing; restore a compatible contract or simplify the interaction if necessary | In Progress |
| Scope becomes too large | | | | Avoid / Mitigate | MoSCoW prioritisation and removal of optional functionality; defer non-essential features | In Progress |
| Blockchain or RPC failure | | | | Mitigate | Circuit breaker, bounded retries and settlement-pending state; retry settlement when the provider recovers | Not Started |
| Real-time tracking interruption | | | | Mitigate | Reconnection and latest-location recovery; show last-known state and use fallback notifications | Not Started |
| Cloud cost or quota limitations | | | | Mitigate | Develop locally where practical and monitor cloud usage; reduce resources or pause non-critical components | Not Started |
