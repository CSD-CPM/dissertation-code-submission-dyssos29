# Cycle 1 Access-Control Evidence

## Protected Endpoints

| Role | Endpoint |
|---|---|
| `CUSTOMER` | `/api/v1/customer/ping` |
| `RESTAURANT_OWNER` | `/api/v1/restaurant/ping` |
| `COURIER` | `/api/v1/courier/ping` |
| `ADMIN` | `/api/v1/admin/ping` |

## Access Matrix

The complete access-control matrix was tested against Envoy and the backend services.

| Token / Role | Customer | Restaurant | Courier | Admin |
|---|---:|---:|---:|---:|
| No token | 401 | 401 | 401 | 401 |
| Invalid token | 401 | 401 | 401 | 401 |
| `CUSTOMER` | 200 | 403 | 403 | 403 |
| `RESTAURANT_OWNER` | 403 | 200 | 403 | 403 |
| `COURIER` | 403 | 403 | 200 | 403 |
| `ADMIN` | 403 | 403 | 403 | 200 |

All **24 cases passed** with the expected HTTP status code.

## Result Interpretation

- `200 OK` — authenticated user has the required role.
- `401 Unauthorized` — authentication is missing or invalid.
- `403 Forbidden` — authentication succeeded, but the user does not have the required role.

The tests confirm that authenticated role claims are enforced at the gateway and that protected backend endpoints reject unauthorized access.
