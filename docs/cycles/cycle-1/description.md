# Cycle 1 — Foundation and Access Control

## Goal

Establish a reproducible platform foundation and implement secure authentication
and role-based access control across the Flutter clients, Google Cloud Identity
Platform, Envoy gateway, and backend services.

## Scope

### In Scope

- modular monorepo and local development foundation;
- Flutter web and mobile workspaces;
- four backend service foundations;
- Docker Compose and Envoy;
- health and readiness endpoints;
- CI/CD and container-image publication;
- Google Cloud Identity Platform authentication;
- JWT validation;
- application roles and role-based access control;
- Flutter authentication and role-based navigation;
- protected backend endpoints;
- automated access-control testing.

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
- Flutter displays the appropriate interface for the authenticated role;
- valid access returns `200 OK`;
- missing or invalid authentication returns `401 Unauthorized`;
- an authenticated user with the wrong role receives `403 Forbidden`;
- authentication and access-control tests pass.

## Cycle Review

To be completed at the end of Cycle 1.

## Retrospective

To be completed at the end of Cycle 1.
