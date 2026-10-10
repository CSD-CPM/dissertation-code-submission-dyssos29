# ADR-007 — Google Cloud Identity Platform for Authentication

## Context

The platform supports four application roles:

- `CUSTOMER`;
- `RESTAURANT_OWNER`;
- `COURIER`;
- `ADMIN`.

Authentication must support the platform's client applications while allowing backend services to rely on verified user identities and application roles.

Implementing and maintaining a custom identity system would add significant security and development complexity.

## Decision

Use **Google Cloud Identity Platform** with the **Firebase Authentication SDKs** for user authentication.

Application roles are assigned through privileged administrative tooling and stored as trusted custom claims. **Envoy** validates Identity Platform ID tokens and propagates verified identity metadata to backend services through the `x-authenticated-sub` and `x-authenticated-role` headers.

Backend services independently enforce role-based authorization using trusted identity information. Client-supplied identity headers must be removed or overwritten at the gateway, and protected backend endpoints must not be directly accessible in ways that bypass authentication controls. Direct internal service-to-service communication requires a separately controlled trust boundary and must not treat arbitrary caller-supplied identity headers as verified.

## Rationale

Identity Platform provides managed authentication and CIAM capabilities, integrates with client applications through Firebase Authentication SDKs, and fits within the project's existing Google Cloud infrastructure.

Using signed ID tokens allows authentication to remain external to the business microservices while providing verified identity and role information for authorization. Assigning custom claims exclusively through privileged administrative operations prevents clients from independently granting themselves elevated privileges.

Two alternatives were considered:

- **Firebase Authentication** — provides the core authentication capabilities required by the prototype, including signed ID tokens, custom claims, and client SDKs, while integrating with the Google Cloud ecosystem. Identity Platform extends these capabilities with enterprise identity federation, multi-tenancy, additional authentication controls, and an enterprise SLA. Although these features are not all required by the prototype, they provide greater flexibility for future identity-management requirements at the expense of potential additional costs.
- **Self-hosted identity provider** — offers greater control over authentication infrastructure and configuration, but introduces additional deployment, maintenance, and security responsibilities that are unnecessary for this prototype.

Identity Platform was therefore preferred for its managed CIAM capabilities, additional identity-management flexibility, and reduced operational overhead.

## Consequences

Benefits include:

- managed user authentication;
- client integration through Firebase Authentication SDKs;
- signed JWT ID tokens;
- centralized identity management;
- trusted application role claims;
- gateway-level token validation with service-level authorization;
- reduced need for custom authentication infrastructure.

Trade-offs include:

- dependency on an external identity provider;
- privileged tooling is required to assign and manage roles;
- gateway and backend services must securely validate and handle trusted identity information;
- direct service-to-service communication requires separate trust controls;
- role changes may require token refresh before becoming effective;
- potential provider-specific costs and configuration dependencies.
