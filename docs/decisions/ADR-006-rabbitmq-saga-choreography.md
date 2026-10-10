# ADR-006 — RabbitMQ and Choreography-Based Saga

## Context

Order processing, payment, delivery, and settlement span multiple independently owned microservices and datastores.

A distributed database transaction would tightly couple these services and is unsuitable for the platform. A mechanism is therefore needed to coordinate operations and handle failures without relying on a shared transaction.

## Decision

Use a **choreography-based Saga** with **RabbitMQ** and AMQP 0-9-1 for asynchronous domain-event communication.

Each participating service reacts to relevant events, performs its local operation, and publishes the resulting event for other interested services.

No dedicated Saga orchestrator is introduced.

## Rationale

### Choreography instead of orchestration

Choreography is more suitable for this prototype because the distributed workflows involve a relatively small number of services with clear responsibilities.

Each service can react to domain events such as payment success, payment failure, restaurant acceptance, or delivery completion while retaining ownership of its own state and business logic.

An orchestrated Saga would require a central coordinator responsible for controlling the workflow. Although this coordinator need not be a separate deployable service, it would introduce additional coordination logic, workflow state management, and a central dependency. Orchestration provides clearer workflow visibility for large or highly complex workflows, but adds responsibilities that are unnecessary for this prototype.

Choreography therefore better matches the project's goals of:

- loose coupling between microservices;
- event-driven communication;
- independent service ownership;
- avoiding an additional orchestration component;
- keeping the prototype architecture relatively simple.

The trade-off is that workflow dependencies become harder to observe as the number of participants grows, so orchestration could become preferable if the workflow becomes substantially more complex.

### RabbitMQ instead of alternative brokers

RabbitMQ was selected because its queue-oriented messaging model closely matches the requirements of Saga-based business workflows.

It provides:

- exchanges and flexible event routing;
- durable queues;
- consumer acknowledgements;
- publisher confirms;
- message redelivery;
- dead-letter exchanges;
- straightforward publish/subscribe communication;
- mature Java and Go client support.

Two major alternatives were considered:

- **Apache Kafka** — highly suitable for large-scale event streaming, durable event logs, replay, and very high-throughput data pipelines. However, the prototype primarily requires reliable business-event delivery, routing, retries, and failure handling rather than long-term event-stream storage or large-scale stream processing. Kafka would therefore introduce additional operational and conceptual complexity without using many of its main advantages.

- **NATS with JetStream** — offers lightweight, subject-based messaging with persistent streams, durable consumers, acknowledgements, redelivery, and replay. It could support the planned Saga workflows. However, handling messages that exhaust their delivery attempts typically requires processing JetStream advisories and implementing additional recovery or dead-letter handling, whereas RabbitMQ supports configurable dead-letter exchanges for routing failed messages. Together with its explicit exchange-to-queue bindings, this makes RabbitMQ a more direct fit for the prototype's business-event routing and failure-handling needs, without implying that JetStream is less reliable.

RabbitMQ therefore provides the best balance between reliability, routing flexibility, operational simplicity, and the requirements of the prototype.

## Consequences

Benefits include:

- loose coupling between Saga participants;
- no dedicated workflow-orchestrator service;
- explicit retry and dead-letter handling;
- flexible routing of domain events;
- straightforward integration with both Java and Go services.

Trade-offs include:

- the complete workflow is less visible than with orchestration;
- consumers must be idempotent;
- event contracts require versioning;
- compensating actions must be implemented explicitly;
- eventual consistency must be accepted;
- choreography becomes harder to manage if the number of participants and workflow branches increases;
- a service may commit a local database transaction but fail before publishing its corresponding event, leaving a dual-write failure window that is accepted within the prototype's scope.
