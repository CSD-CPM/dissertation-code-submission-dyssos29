# Cycle 1 — Foundation and Access Control

## Goal

Establish a reproducible platform foundation and implement secure authentication
and role-based access control across the Flutter clients, Google Cloud Identity
Platform, Envoy gateway, and backend services.

## Scope

### In Scope

- modular monorepo and local development foundation;
- Flutter web and Android workspaces;
- four backend service foundations;
- Docker Compose and Envoy;
- health and readiness endpoints;
- CI/CD and container-image publication;
- Google Cloud Identity Platform authentication;
- trusted application role claims;
- JWT validation;
- role-based access control;
- Flutter authentication and role-based navigation;
- protected backend foundation endpoints;
- request-ID propagation between Envoy and backend services;
- automated authentication and access-control testing;
- generated Protobuf artifact drift validation.

### Out of Scope

- restaurant and menu business functionality;
- order creation and processing;
- RabbitMQ Saga workflows;
- courier assignment and live tracking;
- WebSocket tracking;
- FCM notifications;
- blockchain escrow and settlement;
- Base Sepolia deployment;
- GKE deployment.

## Acceptance Criteria

Cycle 1 is complete when:

- the local platform starts successfully;
- all backend services expose working health endpoints;
- CI validation and continuous image publication succeed;
- users can authenticate through Google Cloud Identity Platform;
- authenticated users receive trusted application roles;
- Envoy validates JWTs;
- protected routes enforce the required roles;
- backend services retain service-level authorization checks;
- Flutter displays the appropriate role-specific interface;
- valid access returns `200 OK`;
- missing or invalid authentication returns `401 Unauthorized`;
- an authenticated user with the wrong role receives `403 Forbidden`;
- request IDs are propagated from Envoy to backend service logs;
- authentication and access-control tests pass.

## Cycle Review

Cycle 1 successfully established the platform foundation and completed the planned authentication and access-control vertical slice. The local microservice environment, Flutter clients, Identity Platform authentication, trusted role claims, Envoy JWT validation and RBAC, service-level authorization, request correlation, automated testing, and CI/CD validation were all implemented and verified.

Several integration issues required additional work during the cycle. These included local port conflicts, CORS configuration, Envoy JWKS retrieval, Android emulator connectivity, Flutter client networking, Protobuf descriptor generation, and CI differences caused by Buf versioning and breaking-change validation. All identified issues were resolved without reducing the intended Cycle 1 scope.

The final access-control matrix produced the expected `200`, `401`, and `403` responses, and all required CI checks passed.

## Retrospective

The cycle confirmed the value of implementing the platform as small, testable vertical slices. Authentication and authorization affected the gateway, clients, contracts, and multiple backend services, so validating each boundary independently made integration problems easier to isolate.

The main improvement for future cycles is to standardise development tooling and environment assumptions earlier. Pinning tool versions, defining deterministic generated-artifact commands, documenting platform-specific networking, and adding automated tests alongside implementation would have prevented several late CI and environment issues.

For Cycle 2, these practices should be applied from the beginning, with frequent integration checks rather than postponing full cross-service verification until the end of the cycle.
