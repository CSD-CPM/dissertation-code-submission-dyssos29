# ADR-002 — Java and Go for Backend Microservices

## Context

The proposed food-delivery platform requires independently deployable services with different operational characteristics. Restaurant and menu management and order processing primarily involve business rules, validation, persistent data, and transactional workflows. Delivery and tracking must handle frequent location updates and concurrent connections, while payment processing requires reliable communication with external blockchain services, payment-state management, and failure handling.

The backend technology stack must support these workloads while remaining maintainable and manageable for a single-developer prototype.

## Decision

Use **Java with Spring Boot** for the **Restaurant and Menu Service** and **Order Service**, and **Go** for the **Delivery and Tracking Service** and **Payment Service**.

Limit the backend to these two programming languages. Specific communication protocols, persistence technologies, container-building tools, and deployment infrastructure will be addressed in subsequent ADRs.

## Rationale

### Java and Spring Boot for Business-Oriented Services

Java is an established and widely adopted language for enterprise backend development, with mature tooling, extensive libraries, and a strong ecosystem for long-lived applications. **Spring Boot** provides a productive framework for implementing business services through dependency injection, validation, data access, transaction management, testing, and production-oriented monitoring capabilities.

These characteristics make Java particularly suitable for the Restaurant and Menu and Order services, where domain rules, data integrity, and maintainable business workflows are important. Prior familiarity with Java is an additional practical reason for its selection, reducing the learning curve and implementation risk within the dissertation's limited development time.

### Go for Cloud-Native and Network-Oriented Services

Go is widely used in cloud-native infrastructure and microservice development. It combines a relatively small language and strong standard-library networking support with compiled deployment and built-in concurrency through goroutines. These characteristics are useful for services dominated by concurrent I/O and external network communication.

For the **Delivery and Tracking Service**, frequent location updates and potentially many long-lived connections create a connection-oriented workload. Go's lightweight goroutines and networking ecosystem support concurrent streams, real-time connections, and integrations such as gRPC, WebSockets, and Redis. Connection recovery and stale-location handling must still be implemented explicitly; language concurrency support does not provide reliability automatically.

For the **Payment Service**, Go supports HTTP-based blockchain RPC interactions, concurrent external requests, timeouts, retries, and persistence operations. The **go-ethereum** ecosystem also provides established Ethereum client libraries. Reusing Go for this service avoids introducing a separate Node.js/TypeScript backend solely for blockchain integration, while allowing the service to manage payment-state transitions and failures independently.

### Technology Alternatives and Trade-offs

A **Java-only backend** would simplify the language stack and could satisfy all four services' requirements. However, using Go for the more network-oriented services offers a straightforward concurrency model and lightweight compiled deployment suited to their expected workloads. Conversely, **Go for every service** would forgo the advantages of Java's mature enterprise framework ecosystem and the existing Java experience for business-oriented services.

**Node.js/TypeScript** was also considered for payment integration because of its extensive blockchain ecosystem. Nevertheless, introducing a third backend runtime would increase tooling and maintenance overhead without a sufficiently strong advantage for the prototype. The selection of Java and Go is therefore based on workload fit, ecosystem maturity, developer productivity, and limiting the technology stack—not on a claim that either language is universally faster.

## Consequences

Benefits include:

- mature Java and Spring Boot tooling for business-oriented services;
- efficient concurrency and networking support for tracking and external RPC workloads;
- straightforward deployment of compiled Go services;
- reuse of Go tooling across payment and tracking implementations;
- only two backend languages, balancing workload specialization with manageable complexity.

Trade-offs include:

- maintaining separate Java and Go build, testing, and debugging toolchains;
- additional cross-language integration and contract-testing effort;
- explicit concurrency, reconnection, timeout, and failure-handling logic in network-oriented services;
- a potentially smaller selection of blockchain-specific libraries in Go than in the JavaScript/TypeScript ecosystem.

These trade-offs are accepted because the selected languages fit the services' differing responsibilities while keeping the backend technology stack limited for a single-developer prototype.
