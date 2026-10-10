# ADR-005 — Database-per-Service and Polyglot Persistence

## Context

The platform's independently deployable microservices manage different types of data. Orders and payments require durable transactional records, restaurant menus benefit from flexible document structures, and delivery tracking requires frequent, low-latency updates to current locations.

A persistence strategy is needed that preserves service autonomy and data ownership while balancing consistency, availability, operational effort, and cloud deployment costs.

## Decision

Adopt the **Database-per-Service** pattern with **polyglot persistence**. Each service exclusively owns its data and exposes it to other services through defined interfaces; direct cross-service database access is prohibited.

Use the following persistence technologies:

| Service | Datastore | Hosting and ownership |
|---|---|---|
| Restaurant and Menu | **MongoDB** | Managed **MongoDB Atlas** on Google Cloud |
| Order | **PostgreSQL** | Dedicated logical database in **Google Cloud SQL** |
| Payment | **PostgreSQL** | Separate logical database in the **same Cloud SQL instance** |
| Delivery and Tracking | **Redis** | Self-managed in **GKE**, using a compatible Kubernetes operator |

Order and Payment will use separate database credentials and permissions despite sharing a Cloud SQL instance. Cloud-hosted application services will access Cloud SQL through the **Cloud SQL Auth Proxy**, with appropriate database authentication; Atlas connectivity will use authenticated, encrypted connections, with private networking configured when supported by the chosen cluster tier. Redis will be accessible through internal Kubernetes networking.

## Rationale

### Database-per-Service and Polyglot Persistence

A shared database schema would couple independently deployable services through common tables and schema changes. Service-owned data preserves clear responsibility boundaries and allows persistence models to evolve independently. Separate logical databases within one PostgreSQL instance retain ownership and access isolation while avoiding the expense of an additional managed instance for this prototype; they do not provide physical or infrastructure-level isolation.

Polyglot persistence allows each service to use storage suited to its workload rather than imposing one database technology across the platform.

### Database Technology Choices

- **PostgreSQL / Cloud SQL** — provides ACID transactions and relational constraints for orders, payment metadata, refunds, and settlement state. Cloud SQL removes much of the work of operating PostgreSQL directly. Google Cloud Spanner offers distributed transactional scalability but adds capabilities unnecessary for the prototype's anticipated workload.
- **MongoDB Atlas** — supports flexible, nested documents suited to restaurant profiles, menu categories, items, and availability information. PostgreSQL with JSON support is a viable alternative, but Atlas provides a dedicated managed document database without requiring a self-operated MongoDB cluster.
- **Redis** — offers low-latency in-memory operations and geospatial data structures for active courier positions and tracking sessions. Its current-state workload does not require the same durability guarantees as orders or payments. Redis is not the authoritative store for financial records or immutable delivery history.

### Managed and Kubernetes-Hosted Datastores

Running critical transactional and document databases as self-managed Kubernetes workloads would add responsibility for database-specific replication, backups, upgrades, and recovery. **Cloud SQL and MongoDB Atlas** are therefore preferred as managed services, with availability and backup features configured according to the deployment tier and requirements. Managed hosting reduces administration but does not guarantee uninterrupted access or eliminate recovery planning.

**Redis is hosted within GKE** because current location data is frequently refreshed and benefits from straightforward cluster-local access. A compatible operator can simplify Redis lifecycle management, but does not eliminate the need to configure persistence, high availability, monitoring, and recovery. Redis Open Source operators must not be confused with Redis's official **Redis Enterprise** Kubernetes operator. Temporary loss of tracking state may be tolerated, but accurate location display resumes only when fresh updates arrive.

## Consequences

Benefits include:

- explicit service ownership and reduced coupling through shared database schemas;
- workload-appropriate transactional, document, and geospatial storage;
- reduced operational responsibility for persistent business records through managed services;
- lower infrastructure cost than provisioning separate Cloud SQL instances for Order and Payment;
- isolation of high-frequency tracking updates from transactional databases.

Trade-offs include:

- distributed workflows require coordination and eventual-consistency handling across service boundaries;
- using multiple database engines increases tooling, monitoring, and operational complexity;
- Order and Payment share a Cloud SQL instance-level failure and resource boundary;
- managed database pricing and network configuration require attention, while self-managed Redis retains operational risk;
- Redis failures can interrupt live tracking until the service recovers and new location updates arrive.

These trade-offs are accepted because data ownership and workload-specific persistence support the platform's microservice objectives while keeping the prototype's most durable records in managed databases.
