# Cloud-Native Food Delivery Platform

This repository contains the prototype developed for the dissertation:

> **Design and Implementation of a Cloud-Native Microservices-Based Food Delivery Platform with Blockchain Payment Integration**

The platform combines cloud-native operational microservices with a Solidity-based escrow and multi-party settlement mechanism.

## Project Status

The project is currently being developed using a lightweight Solo Scrumban process and vertical slices.

The current foundation focuses on:

- establishing the modular monorepo;
- creating independently deployable microservice skeletons;
- configuring shared API contracts;
- creating a reproducible local environment;
- establishing authentication and access-control foundations;
- adding health and readiness endpoints;
- configuring continuous integration;
- establishing container-image publication.

This repository contains an academic prototype. It does not process real payments, provide cryptocurrency custody, or use production funds.

---

## Architecture Overview

The platform consists of four business microservices and an externally managed identity provider:

1. Restaurant and Menu Service
2. Order Service
3. Delivery and Tracking Service
4. Payment Service
5. Google Cloud Identity Platform for CIAM

Envoy Proxy provides the centralized edge gateway.

RabbitMQ provides asynchronous event-driven communication between the four business microservices.

Each microservice owns its own datastore and other services are prohibited from accessing that datastore directly.

---

## Technology Stack

| Component | Selected technology |
|---|---|
| Frontend workspace | Flutter and Dart |
| Customer client | Flutter Web / PWA |
| Restaurant-owner client | Flutter Web |
| Courier client | Flutter mobile |
| Administrator client | Flutter Web |
| API gateway | Envoy Proxy |
| Authentication and CIAM | Google Cloud Identity Platform using OIDC and JWT |
| Restaurant and Menu Service | Java 25 LTS and Spring Boot |
| Order Service | Java 25 LTS and Spring Boot |
| Delivery and Tracking Service | Go |
| Payment Service | Go |
| Internal synchronous communication | gRPC |
| External web communication | REST/HTTPS through Envoy |
| Courier telemetry | Native gRPC streaming |
| Customer live tracking | WSS WebSockets |
| Asynchronous messaging | RabbitMQ using AMQP 0-9-1 |
| Customer fallback notifications | Firebase Cloud Messaging |
| Restaurant datastore | MongoDB Atlas |
| Order datastore | Cloud SQL PostgreSQL |
| Payment datastore | Cloud SQL PostgreSQL |
| Tracking datastore | Redis |
| Java containerisation | Jib Maven Plugin |
| Go containerisation | ko |
| Container registry | Docker Hub |
| Smart contract | Solidity |
| Smart-contract toolchain | Foundry |
| Local blockchain | Anvil |
| Blockchain test network | Base Sepolia |
| Local orchestration | Docker Compose |
| Cloud orchestration | Google Kubernetes Engine |

---

## Container Image Strategy

The project uses language-specific container-image tooling rather than service-specific Dockerfiles.

### Spring Boot services

```text
Java source
    ↓
Maven
    ↓
Jib
    ↓
OCI image
    ↓
Docker Hub
```

Jib is used by:

- Restaurant and Menu Service
- Order Service

Jib can construct and publish the Java service images directly from Maven without requiring a Dockerfile.

### Go services

```text
Go source
    ↓
ko
    ↓
OCI image + SPDX SBOM
    ↓
Docker Hub
```

`ko` is used by:

- Delivery and Tracking Service
- Payment Service

The Go service images do not require manually maintained Dockerfiles.

### Local development

Local service images are loaded into the Docker daemon before Docker Compose starts the platform:

```text
Jib / ko
    ↓
local Docker images
    ↓
Docker Compose
```

The canonical helper script is:

```bash
./scripts/build-local-images.sh
```

---

## Service Data Ownership

| Service | Owned datastore |
|---|---|
| Restaurant and Menu Service | MongoDB |
| Order Service | `order_service_db` |
| Delivery and Tracking Service | Redis |
| Payment Service | `payment_service_db` |

The Order Service and Payment Service use separate logical PostgreSQL databases and credentials.

Cross-service database access is prohibited.

---

## Local Compose Dependency Policy

Docker Compose is used only for the local development environment.

Infrastructure dependencies use health-aware startup conditions:

| Dependent service | Dependency | Condition |
|---|---|---|
| Restaurant and Menu Service | MongoDB | `service_healthy` |
| Restaurant and Menu Service | RabbitMQ | `service_healthy` |
| Order Service | PostgreSQL | `service_healthy` |
| Order Service | RabbitMQ | `service_healthy` |
| Delivery and Tracking Service | Redis | `service_healthy` |
| Delivery and Tracking Service | RabbitMQ | `service_healthy` |
| Payment Service | PostgreSQL | `service_healthy` |
| Payment Service | RabbitMQ | `service_healthy` |
| Envoy | Restaurant and Menu Service | `service_started` |
| Envoy | Order Service | `service_started` |
| Envoy | Delivery and Tracking Service | `service_started` |
| Envoy | Payment Service | `service_started` |

The application containers deliberately do not define Docker Compose health checks.

Their HTTP liveness and readiness endpoints are retained for manual verification, automated tests, monitoring, and future Kubernetes probes.

---

## Kubernetes Health Strategy

When deployed to GKE, Kubernetes will use native HTTP probes.

Spring Boot services expose:

```text
/actuator/health/liveness
/actuator/health/readiness
```

Go services expose:

```text
/health/live
/health/ready
```

Kubernetes will query these endpoints directly using `livenessProbe` and `readinessProbe`.

Docker Compose startup ordering is therefore treated only as a local-development convenience and not as the platform's runtime resilience mechanism.

---

## Repository Structure

```text
apps/
  platform_web/
  courier_mobile/

  packages/
    shared_auth/
    shared_models/
    shared_networking/

services/
  restaurant-menu-service/
  order-service/
  delivery-tracking-service/
  payment-service/

contracts/
  proto/
  events/
  websocket/
  blockchain/

gateway/
  envoy/

infrastructure/
  compose/
  postgres/
  mongodb/
  rabbitmq/
  kubernetes/

smart-contracts/
  escrow/

tools/
  identity-admin/

docs/
  decisions/
  diagrams/
  cycle-1/
  cycle-evidence/
  test-results/
  evaluation-data/

scripts/
  build-local-images.sh
```

---

## Modular Monorepo

The project uses a modular monorepo.

All application code and shared contracts are stored in one Git repository while each business microservice remains independently:

- buildable;
- testable;
- containerised;
- configured;
- deployable;
- responsible for its own datastore.

The repository structure reduces coordination overhead while preserving runtime microservice boundaries.

---

## Prerequisites

Install:

- Git
- Docker Desktop with Docker Compose
- Java 25 LTS
- Go 1.26.6
- Flutter 3.44.7
- Dart
- Buf
- ko 0.19.1

Verify:

```bash
git --version
docker --version
docker compose version
java -version
javac -version
go version
flutter --version
dart --version
buf --version
ko version
```

On Windows, Bash commands can be executed using Git Bash or WSL.

---

## Initial Setup

Clone the repository:

```bash
git clone https://github.com/YOUR_USERNAME/food-delivery-platform.git
cd food-delivery-platform
```

Create the local environment configuration:

```bash
cp .env.example .env
```

When using PowerShell:

```powershell
Copy-Item .env.example .env
```

Resolve Flutter workspace dependencies:

```bash
flutter pub get
dart pub workspace list
```

Synchronise the Go workspace:

```bash
go work sync
```

---

## Build Local Service Images

Run:

```bash
./scripts/build-local-images.sh
```

From PowerShell with Bash available:

```powershell
bash ./scripts/build-local-images.sh
```

The script uses:

- Jib for both Spring Boot services;
- `ko` for both Go services.

Verify:

```bash
docker image ls
```

Expected local images:

```text
food-delivery/restaurant-menu-service:local
food-delivery/order-service:local
food-delivery/delivery-tracking-service:local
food-delivery/payment-service:local
```

---

## Validate Docker Compose

```bash
docker compose config --quiet
```

No output indicates that the Compose configuration is valid.

---

## Start the Local Platform

```bash
docker compose up -d
```

Inspect:

```bash
docker compose ps
```

Docker Compose waits for PostgreSQL, MongoDB, Redis, and RabbitMQ health checks before starting their dependent application services.

Envoy starts after all four application containers have started. The application services may require a short additional period before their readiness endpoints respond successfully.

---

## Local Endpoints

| Endpoint | Purpose |
|---|---|
| `http://localhost:8080/health` | Envoy health |
| `http://localhost:8080/health/restaurant` | Restaurant and Menu Service |
| `http://localhost:8080/health/order` | Order Service |
| `http://localhost:8080/health/delivery` | Delivery and Tracking Service |
| `http://localhost:8080/health/payment` | Payment Service |
| `http://localhost:9901` | Envoy administration |
| `http://localhost:15672` | RabbitMQ management |

---

## Stop the Local Platform

```bash
docker compose down
```

Persistent development volumes are retained.

To deliberately remove the local volumes:

```bash
docker compose down -v
```

---

## Java Verification

Restaurant and Menu Service:

```bash
cd services/restaurant-menu-service
./mvnw -B -ntp verify
cd ../..
```

Order Service:

```bash
cd services/order-service
./mvnw -B -ntp verify
cd ../..
```

---

## Go Verification

Formatting:

```bash
gofmt -w services/delivery-tracking-service
gofmt -w services/payment-service
```

Static analysis:

```bash
go vet ./services/delivery-tracking-service/...
go vet ./services/payment-service/...
```

Tests:

```bash
go test ./services/delivery-tracking-service/...
go test ./services/payment-service/...
```

---

## Flutter Verification

Resolve dependencies:

```bash
flutter pub get
```

Check formatting:

```bash
dart format --output=none --set-exit-if-changed apps
```

Run analysis and tests from the appropriate workspace members.

Build the web application:

```bash
cd apps/platform_web
flutter build web --release
cd ../..
```

---

## Contract Verification

```bash
cd contracts/proto

buf format --diff --exit-code
buf lint
buf build

cd ../..
```

---

## Continuous Integration

GitHub Actions verifies:

- Protobuf formatting;
- Protobuf linting;
- Protobuf compilation;
- breaking contract changes on pull requests;
- Spring Boot builds and tests using Java 25;
- Java OCI-image construction using Jib;
- Go formatting;
- Go static analysis;
- Go race-enabled tests;
- static Go compilation;
- Go OCI-image construction using `ko`;
- Flutter formatting;
- Flutter analysis;
- Flutter tests;
- Flutter Web compilation;
- Docker Compose syntax;
- PostgreSQL initialization-script syntax;
- Envoy configuration.

Pull-request CI never publishes container images.

---

## Container Image Publication

Image publication is separated from normal CI.

The pipeline is:

```text
Feature branch
    ↓
Pull Request to main
    ↓
Full Continuous Integration
    ↓
Merge commit
    ↓
main
    ↓
Container image publication
    ↓
Docker Hub
```

Full continuous integration runs on pull requests targeting `main`. A pull request must pass the required CI quality gate before merging. Once the pull request is merged using a merge commit, the resulting push to `main` triggers container-image construction and publication.

The four Docker Hub repositories are:

```text
restaurant-menu-service
order-service
delivery-tracking-service
payment-service
```

Jib publishes the Java images directly to Docker Hub.

`ko` publishes the Go images directly to Docker Hub.

Published image digests are recorded by the publication workflow.

Future Kubernetes deployments should reference immutable SHA-256 image digests rather than relying solely on mutable tags such as `latest`.

---

## Branch Strategy

The `main` branch contains verified work.

Development branches follow:

```text
feature/<name>
fix/<name>
chore/<name>
docs/<name>
test/<name>
```

Examples:

```text
feature/identity-platform-auth
feature/order-service
chore/configure-jib-and-ko
docs/document-api-gateway
test/access-control
```

---

## Commit Convention

Examples:

```text
feat(auth): integrate Identity Platform
fix(gateway): correct service routing
test(payment): add payment state tests
ci: add container verification
docs: document monorepo decision
chore: configure local infrastructure
```

---

## Security

Never commit:

- `.env`
- service-account credentials
- JWTs
- Docker Hub access tokens
- database passwords
- blockchain private keys
- wallet seed phrases
- Firebase Admin credentials

Only test cryptocurrency and test-network accounts are permitted.

---

## Dissertation Evidence

Architecture decisions are stored under:

```text
docs/decisions/
```

Development evidence is stored under:

```text
docs/cycle-evidence/
```

Each completed vertical slice should retain selected evidence such as:

- screenshots;
- diagrams;
- test results;
- relevant logs;
- measurements;
- identified limitations;
- retrospective notes.

---

## Development Methodology

Development follows a lightweight Solo Scrumban workflow:

```text
Backlog → Ready → In Progress → Testing/Review → Done
                    ↓
                  Blocked
```

Work is implemented incrementally using vertical slices.

---

## Licence

This repository currently contains an academic dissertation prototype.

No open-source licence has been selected.
