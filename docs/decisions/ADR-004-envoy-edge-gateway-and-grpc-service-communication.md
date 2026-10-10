# ADR-004 — Envoy Edge API Gateway and gRPC Service Communication

## Context

The proposed platform follows a microservices architecture in which independently deployable services must be accessible to external clients and communicate synchronously with one another.

A consistent public entry point is required to manage request routing, authentication, access-control policies, Cross-Origin Resource Sharing (CORS), and observability while limiting direct exposure of internal services. The approach should support different client communication requirements without introducing unnecessary gateway layers or operational overhead.

The platform also requires a language-independent mechanism for synchronous service-to-service communication, with well-defined interfaces, efficient data exchange, and support for possible streaming interactions. At the same time, browser-facing operations should remain accessible through conventional HTTP/JSON APIs without requiring duplicate business API implementations.

## Decision

Adopt the **Edge API Gateway** architectural pattern, using **Envoy Proxy** as the centralized entry point for external API traffic.

Use **gRPC with Protocol Buffers (Protobuf)** for synchronous service-to-service communication. Internal RPCs communicate directly between services rather than passing through the public edge gateway.

Expose selected client-facing operations as **REST/JSON through Envoy's gRPC-JSON transcoding**, using HTTP mappings associated with the relevant Protobuf service definitions. Internal-only operations remain accessible through gRPC. Envoy may also proxy native gRPC, including streaming RPCs, for clients that require and support it.

## Rationale

### Edge API Gateway Pattern

A single edge gateway establishes a clear public boundary, keeps backend services private, and centralizes cross-cutting request-processing concerns without adding unnecessary infrastructure tiers.

Three architectural alternatives were considered:

- **Direct client-to-service access** — would avoid a separate gateway but require individual services to be exposed externally and to implement or duplicate concerns such as client authentication, cross-origin policies, routing, and request monitoring. This increases coupling between clients and backend service locations.
- **Backend-for-Frontend (BFF)** — provides client-specific entry points and can be valuable when different client types require substantially different aggregation or presentation logic. For the anticipated scope, maintaining multiple gateway applications would add complexity without a corresponding benefit.
- **Two-tier gateway** — separates the external edge from an additional internal gateway. It can help large deployments enforce complex internal traffic policies, but adds another component, routing hop, and configuration surface without a demonstrated requirement at this stage.

The single Edge API Gateway therefore provides the best balance of service isolation, centralized controls, and architectural simplicity for the prototype.

### Envoy Proxy and Gateway Alternatives

**Envoy Proxy** is a CNCF-graduated, high-performance proxy designed for edge and service communication. Its relevant capabilities include:

- first-class HTTP/1.1 and HTTP/2 handling, including multiplexed HTTP/2 connections;
- native gRPC proxying, including streaming and correct handling of gRPC status trailers;
- a built-in gRPC-JSON transcoder for mapping selected HTTP/JSON requests to Protobuf-defined RPCs;
- JWT verification, role-based access-control (RBAC) filters, CORS, and routing policies;
- connection pooling, load balancing, request tracing, and observability;
- configurable HTTP compression for eligible payloads, subject to protocol, content type, payload size, and CPU trade-offs.

The combination of HTTP/2-aware proxying, gRPC streaming, and direct gRPC-JSON transcoding is particularly relevant. Envoy can accept conventional HTTP/JSON requests at the public boundary and forward corresponding gRPC requests to backend services, while also allowing native gRPC traffic where appropriate. HTTP/2 multiplexing supports concurrent streams over a connection, but does not by itself guarantee sub-millisecond application latency.

Three gateway-product alternatives were evaluated:

- **Kong API Gateway** — is built on NGINX/OpenResty and offers mature API management, native gRPC proxying, extensive plugins, and JSON-to-gRPC conversion through its **gRPC-Gateway plugin**. It is technically capable of meeting many of the platform's requirements. However, its broader API-management features are not central to this prototype, and JSON-to-gRPC conversion is provided through an additional plugin. Envoy offers the required proxying and transcoding directly through its established HTTP filter architecture.
- **KrakenD** — is a high-performance gateway with strong HTTP API aggregation and response-transformation capabilities. Its documented Enterprise gRPC backend integration supports **unary RPCs**, but not gRPC client-server, or bidirectional-streaming connections. This makes it a less direct fit for an architecture that may require native gRPC streaming at the gateway, while its aggregation strengths are not a primary requirement.
- **Google Cloud API Gateway** — reduces the need to operate a self-managed gateway and supports HTTP/JSON-to-gRPC transcoding. However, its documented gRPC backend support is limited to **Cloud Run gRPC services**, and its gRPC integration does not support **payload compression**. Also, a self-managed Envoy gateway provides greater control over gRPC routing, filters, and deployment portability.

Envoy is therefore preferred for its direct fit with the required **HTTP/JSON edge, native gRPC routing, streaming support, and configurable security and traffic controls**, rather than for unverified performance superiority over other gateways.

### gRPC for Synchronous Service Communication

**gRPC** provides a consistent remote-procedure-call model based on explicitly defined service methods and Protobuf message types. Its main advantages for the proposed platform are:

- **Contract-first development:** service and message interfaces are declared in language-neutral `.proto` files, from which client and server interfaces can be generated.
- **Compact serialization:** Protobuf uses a binary wire format that is generally more compact than equivalent textual JSON messages, reducing data-transfer and parsing overhead in many workloads.
- **HTTP/2 transport:** persistent connections and multiplexed streams support concurrent requests without requiring a separate TCP connection for each call. This is a benefit of HTTP/2, not something unique to gRPC.
- **Integrated RPC streaming:** unary, client-streaming, server-streaming, and bidirectional-streaming calls are defined within the same service model.
- **Explicit interface evolution:** generated types help expose interface mismatches during development, while Protobuf's field-numbering and compatibility rules support controlled schema evolution.

The contract-first model reduces manually maintained client code and the risk of mismatched request and response definitions. Compile-time checking can detect some interface inconsistencies, but it does **not** guarantee that every schema change is backward-compatible or prevent all runtime integration failures. Compatibility checks and testing remain necessary.

### Alternatives to gRPC

Two principal alternatives were considered:

- **REST/JSON over HTTP** — is widely supported, human-readable, and especially suitable for public-facing APIs. It can also use HTTP/2 multiplexing and formal OpenAPI contracts with generated clients, so neither connection reuse nor type definitions are exclusive to gRPC. Nevertheless, JSON generally carries more textual serialization overhead than Protobuf for comparable structured messages. Conventional REST/JSON request-response APIs also lack gRPC's built-in, contract-defined client- and bidirectional-streaming model; comparable streaming workflows require additional mechanisms or conventions. For controlled internal RPCs, gRPC therefore provides a more cohesive combination of typed contracts, compact serialization, and streaming support.
- **Apache Thrift** — is a credible cross-language RPC alternative, offering its own IDL, generated client/server code, and multiple binary or textual protocols and transport options. It can meet the need for strongly typed internal calls. However, gRPC provides a more directly standardized HTTP/2-based streaming RPC model and integrates natively with Envoy's gRPC proxying and gRPC-JSON transcoding. Using Thrift would require a separate transport and HTTP/JSON exposure strategy without an identified architectural advantage for this project.

### REST/JSON at the Public Boundary

Standard native gRPC cannot generally be called directly from browser applications without a browser-compatible intermediary or adaptation such as gRPC-Web. REST/JSON offers broad browser and HTTP tooling compatibility and is straightforward to inspect and debug.

Providing HTTP/JSON access through Envoy's **gRPC-JSON transcoder** therefore allows public operations to remain accessible using conventional HTTP requests while reusing their underlying gRPC service definitions. This does not imply that REST is the only browser-compatible approach or that all streaming RPCs can be exposed as equivalent REST endpoints. Client-facing mappings will be selected explicitly, with native streaming or other browser-compatible transports evaluated separately where required.

## Consequences

Benefits include:

- one public gateway boundary, reducing direct exposure of backend services;
- centralized request routing, JWT validation, access-control filtering, and observability;
- native support for gRPC routing and streaming alongside REST/JSON access;
- reuse of Protobuf service contracts for internal RPCs and selected external HTTP endpoints;
- generated, language-independent client and server interfaces;
- generally compact message serialization and a common unary/streaming RPC model;
- portable gateway configuration without dependence on a managed API-gateway product.

Trade-offs include:

- Envoy is a critical infrastructure component requiring reliable configuration, deployment, and monitoring;
- gateway configuration errors can affect multiple public operations;
- Protobuf contracts and generated artifacts must be updated and versioned carefully;
- gRPC-JSON transcoding requires HTTP annotations, descriptor generation, and integration testing;
- binary RPC traffic is less human-readable during debugging than ordinary JSON;
- payload compression, where enabled, has CPU and configuration costs and is not automatically beneficial for small messages;
- edge-level checks do not replace authorization of business operations within individual services.

These trade-offs are acceptable because the combined decision provides a clear external boundary and a coherent synchronous communication model without duplicating public API implementations or adding unnecessary gateway layers.
