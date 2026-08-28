# Cloud-Native Food Delivery Platform

Prototype developed for the dissertation:

> **Design and Implementation of a Cloud-Native Microservices-Based Food Delivery Platform with Blockchain Payment Integration**

The platform combines cloud-native microservices for food-delivery operations with a Solidity-based blockchain escrow and multi-party settlement mechanism.

This repository contains an academic prototype. It uses test-network cryptocurrency only and does not provide production cryptocurrency custody or process real financial transactions.

---

## Project Status

Development follows a lightweight **Solo Scrumban** process using iterative, incremental vertical slices.

The current foundation establishes:

- the modular monorepo;
- four independently deployable backend services;
- Flutter web/mobile workspaces;
- shared contracts;
- Envoy as the edge gateway;
- local Docker Compose infrastructure;
- health and readiness endpoints;
- CI/CD workflows;
- container-image publication and release versioning.

---

## Architecture

The backend consists of four business microservices:

| Service | Implementation | Data store |
|---|---|---|
| Restaurant and Menu Service | Java 25 + Spring Boot | MongoDB |
| Order Service | Java 25 + Spring Boot | PostgreSQL |
| Delivery and Tracking Service | Go | Redis |
| Payment Service | Go | PostgreSQL |

Google Cloud Identity Platform provides managed authentication and identity management.

Envoy Proxy is the centralized edge gateway.

RabbitMQ provides asynchronous communication and Saga choreography between the four business services.

Only the Payment Service communicates directly with the blockchain RPC.

```text
Flutter Clients
      |
      v
    Envoy
      |
      +-------------------------------+
      |          |          |         |
      v          v          v         v
 Restaurant    Order     Delivery   Payment
 & Menu        Service   & Tracking Service
 Service                  Service
      |          |          |         |
   MongoDB   PostgreSQL    Redis   PostgreSQL
      \          |          |         /
       +---------+ RabbitMQ +--------+
                                   |
                                   v
                              Base Sepolia
```

Each service exclusively owns its datastore. Cross-service database access is prohibited.

RabbitMQ connects only to the four business microservices. It does not connect directly to databases, Envoy, clients, Identity Platform, Firebase Cloud Messaging, or the blockchain.

---

## Technology Stack

| Area | Technology |
|---|---|
| Frontend | Flutter / Dart |
| Customer | Flutter Web / PWA |
| Restaurant Owner | Flutter Web |
| Administrator | Flutter Web |
| Courier | Flutter mobile |
| Edge gateway | Envoy Proxy |
| Authentication / CIAM | Google Cloud Identity Platform, OIDC, JWT |
| Java services | Java 25 LTS, Spring Boot |
| Go services | Go |
| Internal synchronous communication | gRPC |
| Web client communication | REST/HTTPS through Envoy |
| Courier location ingestion | Native gRPC streaming |
| Customer live tracking | WebSockets |
| Messaging | RabbitMQ / AMQP 0-9-1 |
| Notifications | Firebase Cloud Messaging |
| Relational storage | PostgreSQL / Cloud SQL |
| Document storage | MongoDB Atlas |
| Tracking storage | Redis |
| Java containerisation | Jib |
| Go containerisation | `ko` |
| Local orchestration | Docker Compose |
| Container registry | Docker Hub |
| Cloud orchestration | Google Kubernetes Engine |
| Smart contract | Solidity |
| Smart-contract tooling | Foundry |
| Test blockchain | Base Sepolia |

---

## Repository Structure

```text
food-delivery-platform/
├── .github/
│   ├── workflows/
│   │   ├── ci.yml
│   │   ├── cd.yml
│   │   └── release.yml
│   ├── dependabot.yml
│   └── pull_request_template.md
│
├── apps/
│   ├── platform_web/
│   ├── courier_mobile/
│   └── packages/
│       ├── shared_auth/
│       ├── shared_models/
│       └── shared_networking/
│
├── services/
│   ├── restaurant-menu-service/
│   ├── order-service/
│   ├── delivery-tracking-service/
│   └── payment-service/
│
├── contracts/
│   ├── proto/
│   ├── events/
│   ├── websocket/
│   └── blockchain/
│
├── gateway/
│   └── envoy/
│
├── infrastructure/
│   ├── compose/
│   ├── postgres/
│   ├── mongodb/
│   ├── rabbitmq/
│   └── kubernetes/
│
├── smart-contracts/
│   └── escrow/
│
├── tools/
│   └── identity-admin/
│
├── docs/
│   ├── decisions/
│   ├── diagrams/
│   ├── cycle-1/
│   ├── cycle-evidence/
│   ├── test-results/
│   └── evaluation-data/
│
├── scripts/
│   └── build-local-images.sh
│
├── .editorconfig
├── .env.example
├── .gitattributes
├── .gitignore
├── .ko.yaml
├── compose.yaml
├── go.work
├── pubspec.yaml
└── README.md
```

The repository is a **modular monorepo**. Each microservice remains independently buildable, testable, containerised, and deployable.

---

## Communication Model

External web traffic enters through Envoy.

```text
Flutter Web
    → REST/HTTPS
    → Envoy
    → backend services
```

Courier location updates use native gRPC streaming:

```text
Courier Mobile
    → gRPC
    → Envoy
    → Delivery and Tracking Service
```

Customer live tracking uses WebSockets:

```text
Delivery and Tracking Service
    → Envoy
    → WebSocket
    → Customer
```

Internal synchronous service communication uses gRPC only where required. The main planned synchronous business-service interaction is:

```text
Order Service
    → gRPC
    → Restaurant and Menu Service
```

Distributed workflow coordination uses RabbitMQ events and choreography-based Sagas.

Individual GPS updates are not sent through RabbitMQ.

---

## Data Ownership

The Order and Payment services share one PostgreSQL server locally and one Cloud SQL instance in the target deployment, but they use separate logical databases and credentials:

```text
Order Service
    → order_service_db
    → order_service_user

Payment Service
    → payment_service_db
    → payment_service_user
```

Neither service may access the other's database.

The Restaurant and Menu Service owns MongoDB data, while the Delivery and Tracking Service owns Redis tracking state.

---

## Containerisation

No Dockerfiles are maintained for the four application microservices.

### Java

The two Spring Boot services use Jib:

```text
Java source
    → Maven
    → Jib
    → OCI image
```

For local development, Jib loads the resulting images directly into the local Docker daemon.

### Go

The two Go services use `ko`:

```text
Go source
    → ko
    → OCI image
```

For reproducible local builds, `build-local-images.sh` executes the pinned `ko` version inside a Linux `golang` container.

The script:

1. bind-mounts the repository into the temporary Go container;
2. builds each Go application with `ko`;
3. writes the resulting image to a temporary tar archive;
4. loads the archive into the local Docker daemon with `docker load`;
5. tags the image as `food-delivery/<service>:local`;
6. removes the temporary archive and temporary image tag.

A Docker named volume caches `ko`, downloaded Go modules, and the Go build cache between executions.

This also avoids relying on a native Windows `ko.exe`.

### Local Image Names

```text
food-delivery/restaurant-menu-service:local
food-delivery/order-service:local
food-delivery/delivery-tracking-service:local
food-delivery/payment-service:local
```

The `local` tag is used only for local development.

---

## Prerequisites

Install:

- Git;
- Docker Desktop with Docker Compose;
- Java 25 LTS;
- Go 1.26.6;
- Flutter 3.44.7;
- Dart;
- Buf.

A native `ko` installation is not required for the local image-build script because the pinned `ko` version runs inside Docker.

Verify the main tools:

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
```

### Windows

Repository `.sh` files should use **LF** line endings and be executed from **Git Bash**.

In VS Code, Git Bash can be selected as the default terminal profile. Then the same command used on Linux/macOS works on Windows:

```bash
./scripts/build-local-images.sh
```

---

## Initial Setup

Clone the repository:

```bash
git clone https://github.com/YOUR_USERNAME/food-delivery-platform.git
cd food-delivery-platform
```

Create local environment configuration:

```bash
cp .env.example .env
```

PowerShell equivalent:

```powershell
Copy-Item .env.example .env
```

Resolve Flutter dependencies:

```bash
flutter pub get
dart pub workspace list
```

Synchronise the Go workspace:

```bash
go work sync
```

---

## Build Local Images

From Git Bash, Linux, or macOS:

```bash
./scripts/build-local-images.sh
```

The script builds all four application images and prints the resulting `:local` images.

Verify manually with:

```bash
docker image ls
```

---

## Local Docker Compose Environment

Validate the Compose configuration:

```bash
docker compose config --quiet
```

Start the platform:

```bash
docker compose up -d
```

Inspect it:

```bash
docker compose ps
```

Stop it:

```bash
docker compose down
```

Remove persistent local volumes only when intentionally resetting the environment:

```bash
docker compose down -v
```

### Startup Dependencies

Infrastructure services use Compose health checks.

```text
Restaurant and Menu → MongoDB + RabbitMQ
Order               → PostgreSQL + RabbitMQ
Delivery            → Redis + RabbitMQ
Payment             → PostgreSQL + RabbitMQ
```

These dependencies use `service_healthy`.

Envoy depends on all four application containers using `service_started`.

Application readiness is handled separately through application health endpoints and, later, Kubernetes probes.

---

## Local Network Exposure

Only selected services are published to the host.

| Port | Purpose |
|---|---|
| `8080` | Envoy public development listener |
| `9901` | Envoy administration interface |
| `15672` | RabbitMQ management interface |

Backend microservices and databases remain private to the Docker network.

Containers can communicate internally using Compose DNS names such as:

```text
order-service:8080
payment-service:8080
postgres:5432
mongodb:27017
redis:6379
rabbitmq:5672
```

A blank `PORTS` entry in `docker compose ps` does not mean a service is not listening internally.

The Envoy administration interface on port `9901` is intended for local inspection only and should not be publicly exposed in the future GKE deployment.

---

## Health Endpoints

Through Envoy:

| Endpoint | Target |
|---|---|
| `/health` | Envoy |
| `/health/restaurant` | Restaurant and Menu Service |
| `/health/order` | Order Service |
| `/health/delivery` | Delivery and Tracking Service |
| `/health/payment` | Payment Service |

Example:

```bash
curl -fsS http://localhost:8080/health
```

On PowerShell, use the actual curl executable:

```powershell
curl.exe -fsS http://localhost:8080/health
```

Smoke-test all services:

```powershell
curl.exe -fsS http://localhost:8080/health
curl.exe -fsS http://localhost:8080/health/restaurant
curl.exe -fsS http://localhost:8080/health/order
curl.exe -fsS http://localhost:8080/health/delivery
curl.exe -fsS http://localhost:8080/health/payment
```

The root URL:

```text
http://localhost:8080/
```

currently returns `404` by design because no root route is configured.

The Spring Boot services expose Actuator liveness/readiness endpoints, while the Go services expose:

```text
/health/live
/health/ready
```

Future GKE deployments will use native Kubernetes HTTP readiness and liveness probes.

---

## Local Verification

### Java

```bash
cd services/restaurant-menu-service
./mvnw -B -ntp verify
cd ../..

cd services/order-service
./mvnw -B -ntp verify
cd ../..
```

### Go

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

If Windows Application Control blocks generated Go test executables, the tests can be run in the same Linux Go version through Docker:

```powershell
docker run --rm `
  -v "${PWD}:/workspace" `
  -w /workspace `
  golang:1.26.6 `
  go test ./services/delivery-tracking-service/... ./services/payment-service/...
```

### Flutter

```bash
flutter pub get
dart pub workspace list
dart format --output=none --set-exit-if-changed apps
```

Run `flutter analyze` for the relevant workspace members.

Flutter tests are also executed by the Ubuntu-based CI workflow.

Build the web application:

```bash
cd apps/platform_web
flutter build web --release
cd ../..
```

### Protobuf

```bash
cd contracts/proto
buf format --diff --exit-code
buf lint
buf build
cd ../..
```

### Infrastructure

```bash
docker compose config --quiet
bash -n infrastructure/postgres/init/create-service-databases.sh
```

---

## CI/CD

The repository contains three GitHub Actions workflows:

| Workflow | Trigger | Responsibility |
|---|---|---|
| `ci.yml` | Pull request targeting `main` | Full validation and testing |
| `cd.yml` | Push / merge commit to `main` | Publish SHA-tagged continuous images |
| `release.yml` | Git tag `vX.Y.Z` | Promote an existing build to a formal release |

### CI

Full CI runs once on pull requests targeting `main`.

It validates:

- Protobuf contracts;
- Java builds, tests, and Jib image construction;
- Go formatting, vetting, compilation, tests, and `ko` image construction;
- Flutter formatting, analysis, tests, and web build;
- Docker Compose;
- PostgreSQL initialization-script syntax;
- Envoy configuration.

The aggregate required status check is:

```text
CI / Required
```

The complete CI suite is not repeated after a merge.

### Continuous Delivery

After a successful PR is merged, the resulting push to `main` triggers `cd.yml`.

All four service images are published to Docker Hub using the complete Git commit SHA:

```text
restaurant-menu-service:sha-<git-sha>
order-service:sha-<git-sha>
delivery-tracking-service:sha-<git-sha>
payment-service:sha-<git-sha>
```

These are continuous builds, not formal releases.

---

## Versioning and Releases

The repository uses one **platform-level Semantic Version** rather than independent service versions.

During initial development:

```text
0.MINOR.0     meaningful platform milestone
0.MINOR.PATCH correction to that milestone
```

Planned milestone progression is approximately:

```text
v0.1.0  Foundation and Access Control
v0.2.0  Restaurant and Order Workflow
v0.3.0  Messaging and Blockchain Payment
v0.4.0  Delivery and Real-Time Tracking
v0.5.0  Hardening, Deployment and Evaluation
v1.0.0  Final evaluated prototype
```

Versions are created when milestones are actually complete, not simply according to elapsed time.

Annotated Git tags are the release source of truth:

```bash
git tag -a v0.1.0 -m "Food Delivery Platform v0.1.0"
git push origin v0.1.0
```

The release workflow does **not rebuild** the applications. It promotes the already-published SHA image corresponding to the tagged commit:

```text
:sha-<git-sha>
      |
      +---- :0.1.0
      |
      +---- :latest
```

`latest` therefore means **latest formal release**, not latest merge.

The release workflow also creates the corresponding GitHub Release.

### Version Identities

| Identifier | Purpose |
|---|---|
| `v0.3.0` | Platform release |
| Git SHA | Exact source revision |
| `:sha-<git-sha>` | Continuous-build traceability |
| `:0.3.0` | Release image tag |
| `:latest` | Latest formal release |
| `@sha256:...` | Immutable container identity |

Future GKE manifests should deploy images by immutable digest:

```text
image@sha256:...
```

Platform Semantic Versions are separate from Protobuf API versions, RabbitMQ event-schema versions, and smart-contract ABI evolution.

---

## Git Workflow

Development uses short-lived branches such as:

```text
feature/<name>
fix/<name>
chore/<name>
docs/<name>
test/<name>
```

Normal flow:

```text
main
  ↓
feature branch
  ↓
Pull Request
  ↓
Full CI
  ↓
CI / Required
  ↓
Merge commit
  ↓
main
  ↓
SHA-tagged container publication
```

The repository uses **merge commits** rather than squash or rebase merging.

The `main` branch should require a pull request and `CI / Required`, while blocking force pushes and deletion.

Because this is a solo-developed dissertation project, another person's approval, merge queues, forced up-to-date branches, and linear-history enforcement are not required.

Commit messages follow a Conventional Commit-style convention, for example:

```text
feat(auth): integrate Identity Platform
fix(gateway): correct service routing
build: improve cross-platform local image builds
ci: configure continuous delivery
docs: update architecture documentation
```

---

## Dependency Management

Dependabot checks the following ecosystems weekly:

```text
GitHub Actions
Maven
Go modules
Flutter / Dart
```

Dependabot pull requests pass through the same `CI / Required` quality gate as other changes.

---

## Security

Never commit:

```text
.env
service-account credentials
JWTs
Docker Hub tokens
database passwords
blockchain private keys
wallet seed phrases
Firebase Admin credentials
production credentials
```

Only test accounts, test funds, and test-network blockchain assets are used in the prototype.

---

## Development Methodology

Development follows **Solo Scrumban**:

```text
Backlog
   ↓
Ready
   ↓
In Progress
   ↓
Testing / Review
   ↓
Done
```

Blocked work is tracked separately.

The project uses vertical slices, continuous testing, MoSCoW prioritisation, and a work-in-progress limit of one primary implementation task.

Architecture decisions and development evidence are stored under `docs/`.

---

## Licence

This repository currently contains an academic dissertation prototype.

No open-source licence has been selected.
