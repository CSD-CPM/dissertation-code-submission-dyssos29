# ADR-009 — Flutter Client Layer and Real-Time Communication

## Context

The platform requires four client interfaces:

- a web application for customers;
- a web application for restaurant owners;
- a web application for administrators;
- an Android application for couriers.

Customers, restaurant owners, and administrators primarily perform conventional request-response operations such as browsing restaurants, placing or managing orders, editing menus, and performing administrative tasks.

The courier has a different requirement. During an active delivery, the courier continuously produces location updates while physically moving. Tracking should therefore remain dependable when the courier changes applications, uses navigation software, or locks the device while travelling.

A web-based courier client could technically obtain location updates through the browser Geolocation API and transmit them through a persistent WebSocket. However, browser background execution and page lifecycle management make continuous tracking less reliable when the browser is no longer active or visible.

A dedicated mobile application is therefore more appropriate for the courier workflow.

Using React for the web clients and native Android technologies for the courier would, however, introduce separate languages, frameworks, build systems, networking implementations, and testing approaches.

## Decision

Use **Flutter and Dart** as the common client technology across web and Android.

The platform uses:

- **Flutter Web** for customer, restaurant-owner, and administrator interfaces;
- **Flutter Android** for the courier application;
- **REST through Envoy** for conventional commands and queries;
- **native gRPC client streaming** from the courier Android application to the Delivery and Tracking Service;
- **JSON WebSockets** from the Delivery and Tracking Service through Envoy to the customer web application.

Shared authentication, networking, domain-model, validation, and application functionality is reused across the Flutter applications where appropriate.

## Rationale

### Courier Mobile Application

The main reason for implementing the courier interface as a mobile application is the requirement for reliable continuous location tracking throughout an active delivery.

A Flutter Web courier client would be technically feasible:

```text
Courier Flutter Web
    → Browser Geolocation
    → WebSocket
    → Envoy
    → Delivery and Tracking Service
```

However, this approach depends on the lifecycle of the browser page. During a delivery, the courier may switch to a navigation application, lock the device, answer a call, or otherwise move the browser into the background. Browser pages can be throttled, suspended, or discarded in these situations, reducing the reliability of continuous location acquisition and transmission. A dedicated Android application, on the other hand, provides greater control over device location access and application lifecycle behaviour and therefore better matches the courier's operational requirements.

Furthermore, an iOS courier application is deliberately excluded from the scope of this prototype. Although Flutter supports iOS, supporting it would require additional platform-specific location permissions and background-execution handling, application signing and provisioning, and device testing. The aim is to demonstrate dependable mobile location acquisition and native gRPC client streaming, not to validate the same workflow on every mobile operating system. The Flutter Android application is sufficient to demonstrate these capabilities, whereas adding an iOS implementation would increase development and testing effort without providing significant additional value to the architectural evaluation.

Reliable location tracking when the application is not visible requires appropriate Android location permissions, foreground-service configuration, and reconnection handling. Although a dedicated mobile application provides greater control over these mechanisms than a browser-based client, operating-system restrictions and network interruptions must still be considered.

The other roles do not require continuous device location access and can use web applications.

### Native gRPC Streaming for Courier Tracking

Once the courier is implemented as an Android application, **native gRPC client streaming** is used for continuous location transmission.

During an active delivery, the application establishes a long-lived stream with the Delivery and Tracking Service and sends successive location messages through it:

```text
Courier Android
    → native gRPC client stream
    → Envoy
    → Delivery and Tracking Service
    → Redis
```

gRPC is suitable because it provides:

- client-streaming RPCs for continuous upstream transmission;
- efficient Protocol Buffer serialization;
- strongly typed messages;
- generated client and server contracts;
- long-lived HTTP/2 communication;
- reuse of the platform's existing Protobuf and gRPC service model.

This avoids issuing an independent request for every location update and keeps the tracking interface consistent with the platform's contract-first communication approach.

MQTT was considered as an alternative for courier location telemetry because its lightweight publish/subscribe model, session management, and quality-of-service mechanisms are suited to intermittently connected mobile and IoT clients. A peer-reviewed comparative study reported that gRPC consistently achieved lower latency and more compact payloads than MQTT under the conditions it evaluated. MQTT maintained simpler overhead in its unsecured configuration, but enabling security resulted in larger and less predictable message sizes. These findings further support gRPC for the prototype's continuous location updates, while recognising that measured performance depends on the specific workload and configuration.

Beyond these reported performance characteristics, MQTT would introduce a broker-mediated client communication path and additional topic, authentication, and connection-management concerns. Native gRPC client streaming allows the Android application to communicate with the Delivery and Tracking Service through Envoy while reusing the platform's existing Protobuf contracts, generated clients, and strongly typed service model. It therefore provides a closer architectural fit without introducing a separate client-facing messaging protocol.

### Web Interfaces for Other Roles

Customers, restaurant owners, and administrators do not continuously produce location telemetry.

Their main responsibilities are:

- **Customers** — browse restaurants, place orders, make payments, and observe delivery progress;
- **Restaurant owners** — manage restaurants, menus, and incoming orders;
- **Administrators** — perform platform-management and monitoring operations.

These workflows are primarily request-response based and can be handled through REST APIs exposed through Envoy.

Dedicated mobile applications would therefore add implementation and deployment overhead without providing a significant architectural benefit for these roles.

### Cross-Platform Client Technology

Using separate React and native Android implementations would introduce two substantially different client technology stacks.

Despite their different interfaces, the applications share concerns such as:

- authentication and role handling;
- backend communication;
- domain models;
- validation;
- error handling;
- common application behaviour.

Flutter allows both Android and web clients to use the same Dart language, framework, package ecosystem, and development tooling while retaining role-specific interfaces.

This reduces implementation and maintenance overhead, which is particularly important for a prototype developed by a single developer.

### Flutter instead of React Native or Kotlin Multiplatform

Two major cross-platform alternatives were considered.

**React Native** provides a mature cross-platform mobile framework, but web support is provided through the separate React Native Web ecosystem. Using React separately for the web clients would also reintroduce differences between the mobile and web stacks.

Flutter provides Android and web within the same SDK and development model.

**Kotlin Multiplatform (KMP)** provides strong code-sharing capabilities and can share UI through Compose Multiplatform. However, its project model introduces additional concepts such as platform-specific source sets and entry points, Gradle multiplatform configuration, and platform-specific implementations where APIs cannot be shared.

That flexibility is useful where greater platform-specific integration is required, but this project benefits more from a simpler common application framework for Android and web.

Flutter therefore provides the lower-overhead solution for the prototype while satisfying the required client platforms.

### Customer Real-Time Tracking

The customer's real-time requirement differs from the courier's.

The courier continuously **produces** location information, whereas the customer primarily **consumes** updated courier positions.

Normal customer operations continue to use REST, while live tracking uses a persistent connection:

```text
Delivery and Tracking Service
    → Envoy
    → JSON WebSocket
    → Customer Web
```

WebSockets allow the Delivery and Tracking Service to push courier-position updates immediately without repeated HTTP polling.

JSON is sufficient for these relatively small tracking messages and is straightforward for the Flutter Web client to consume and render.

Customer WebSocket connections must be authenticated using a browser-compatible mechanism consistent with the platform's identity-management architecture. The Delivery and Tracking Service must also verify that the authenticated customer is authorized to access the requested delivery's tracking information.

### WebSocket instead of Server-Sent Events

**Server-Sent Events (SSE)** were also considered because the current customer tracking requirement is primarily server-to-client.

SSE would be capable of delivering courier-location updates to the customer and provides a simple event-streaming model. However, WebSockets were preferred for several reasons.

First, SSE is unidirectional at the application level. It provides a server-to-client event stream, whereas WebSockets provide a full-duplex connection. Although customer tracking is currently downstream-oriented, WebSockets retain the flexibility to introduce client-to-server real-time messages later without changing the transport mechanism.

Second, SSE has an additional limitation when operating over **HTTP/1.1**. Browsers commonly restrict concurrent HTTP connections to approximately six per browser and domain. Because SSE connections are long-lived, multiple tabs or other persistent connections can consume this limited connection pool. This restriction is substantially alleviated when HTTP/2 is used because concurrent streams can be multiplexed over the same connection, so it is considered a secondary rather than decisive limitation.

Finally, WebSockets provide a general-purpose persistent communication mechanism that can be handled directly within the Flutter/Dart networking layer without introducing an additional SSE-specific communication approach.

The choice is therefore not based on SSE being incapable of supporting customer tracking. Both technologies satisfy the current downstream requirement, but WebSockets provide greater communication flexibility while remaining sufficiently simple for the prototype.

The resulting client communication model is:

```text
Courier
    → continuous location producer
    → Flutter Android
    → native gRPC client streaming

Customer
    → conventional commands and queries
    → Flutter Web
    → REST

Customer
    → live tracking consumer
    → Flutter Web
    → JSON WebSocket
```

RabbitMQ is not involved in the customer WebSocket path. It remains responsible for asynchronous domain-event communication between backend services, while the WebSocket provides direct real-time communication between the Delivery and Tracking Service and the connected customer.

## Consequences

Benefits include:

- more reliable mobile location tracking for the courier workflow;
- native gRPC client streaming for efficient continuous location transmission;
- strongly typed Protobuf contracts for courier tracking;
- browser-compatible real-time customer tracking through WebSockets;
- REST for conventional client operations;
- one primary language and framework across the client layer;
- reuse of shared authentication, networking, models, and application functionality;
- reduced implementation and maintenance overhead for a solo-developed prototype.

Trade-offs include:

- the courier application must be installed on an Android device;
- Android location tracking requires platform-specific permissions and lifecycle handling;
- persistent gRPC and WebSocket connections require reconnection and failure-handling logic;
- Flutter Web may have a larger initial application size than a conventional JavaScript web application;
- some platform-specific configuration remains necessary despite using a shared framework.

These trade-offs are acceptable because the architecture provides more reliable courier tracking while retaining a largely unified client technology stack.
