# ADR-003 — Containerisation, Orchestration and Cloud Platform

## Context

The proposed platform adopts a microservices architecture, requiring independently deployable application services and supporting infrastructure. A consistent containerisation and orchestration strategy is needed to package, deploy, scale, and manage these components across development and cloud environments.

A consistent containerisation approach is required for local development and CI/CD. The target cloud deployment also requires orchestration for service lifecycle management, scaling, networking, health monitoring, and failure recovery.

A cloud platform and suitable image-building strategy are therefore required.

## Decision

Use:

- **Docker** and Docker Compose for local containerisation and development;
- **Kubernetes** through Google Kubernetes Engine (GKE) for cloud orchestration;
- **Google Cloud Platform (GCP)** as the target cloud provider;
- **Jib** for building OCI images for the Java services;
- **ko** for building OCI images for the Go services.

Application microservices therefore do not require manually maintained Dockerfiles.

## Rationale

### Docker for Containerisation

Docker was selected because it provides a mature developer-oriented workflow for building, running, networking, and composing OCI-compatible application containers.

Three alternatives were considered:

- **containerd** — an industry-standard container runtime and an important part of the Kubernetes ecosystem, but intentionally provides lower-level container primitives rather than a complete developer-oriented container platform.
- **Podman** — a strong OCI-compatible alternative with daemonless and rootless operation, but Docker provides a well-established Docker Desktop and Compose workflow that better matches the project's local multi-service development environment.
- **LXC/LXD** — focuses on system containers that behave more like lightweight virtual machines, whereas this project requires application containers for independently packaged microservices.

Docker therefore provides the most practical local development environment for the prototype while producing portable OCI-compatible images.

### Kubernetes instead of Docker Swarm

Both Kubernetes and Docker Swarm provide declarative container orchestration and desired-state management.

Docker Swarm is simpler to configure and supports replicated services, service discovery, rolling updates, and manual scaling. However, its built-in scaling model primarily requires the desired replica count to be configured explicitly.

Kubernetes provides additional capabilities that better match the project's cloud-native and scalability requirements:

- **self-healing**, including restarting failed containers and replacing failed Pods;
- **Horizontal Pod Autoscaling (HPA)** based on resource or custom metrics;
- declarative desired-state management;
- health and readiness probes;
- rolling deployments;
- configuration and secret management;
- an extensive cloud-native ecosystem.

Horizontal autoscaling is particularly relevant to a food-delivery platform because workloads can vary significantly during periods of increased ordering activity. Kubernetes can adjust the number of service replicas automatically according to configured metrics rather than requiring the replica count to be changed manually.

Kubernetes therefore introduces greater complexity than Docker Swarm, but its automated scaling, recovery mechanisms, and broader cloud-native ecosystem make it more suitable for the project's goal of evaluating a scalable microservices architecture.

### Google Cloud Platform

Once Kubernetes had been selected as the target orchestration technology, a suitable managed Kubernetes platform was required.

The three main cloud alternatives considered were:

- **Google Kubernetes Engine (GKE)** on Google Cloud Platform;
- **Elastic Kubernetes Service (EKS)** on Amazon Web Services;
- **Azure Kubernetes Service (AKS)** on Microsoft Azure.

All three provide mature managed Kubernetes environments and are technically capable of hosting the platform.

GCP was selected primarily because **GKE provides a strongly Kubernetes-focused managed environment**. Google originally developed Kubernetes before contributing it to the Cloud Native Computing Foundation, and GKE has remained closely aligned with Kubernetes throughout its development.

GKE manages the Kubernetes control plane and provides integrated support for cluster upgrades, node management, autoscaling, monitoring, networking, and security. It also offers **Autopilot**, where Google manages worker nodes, scaling, upgrades, and several infrastructure and security settings, reducing operational administration.

The alternatives remain viable:

- **AWS EKS** provides mature managed Kubernetes and extensive AWS integration. However, in its standard configuration it can require more explicit setup and lifecycle management across areas such as node groups, networking, load balancing, storage, and cluster add-ons. This can increase operational overhead compared with GKE's more integrated managed experience. AWS has introduced **EKS Auto Mode**, which automates many of these responsibilities and substantially reduces this difference, so this is a relative complexity consideration rather than an inherent limitation of EKS.
- **Azure AKS** similarly provides mature Kubernetes orchestration and strong Azure integration, but does not provide a significant advantage for the requirements of this project.

GCP was therefore selected because **GKE offered the most suitable balance of Kubernetes integration and reduced operational overhead for the project's Kubernetes-first deployment strategy**.

Later decisions to use services such as Google Cloud Identity Platform and Cloud SQL were made within the already-selected GCP environment and were not reasons for the original cloud-platform choice.

### Jib for Java Images

The Java Spring Boot services use **Jib** rather than manually maintained Dockerfiles.

A conventional Dockerfile would provide full control over every image-building step, but the developer would also be responsible for selecting build stages, copying application artifacts, maintaining the runtime image, and designing efficient image layers.

Jib integrates directly with Maven and understands the structure of Java applications. It automatically separates dependencies, resources, and application classes into layers so that changes do not necessarily require rebuilding and transferring the complete application image.

Jib also provides:

- direct integration with the existing Maven build;
- reproducible image generation;
- daemonless image builds;
- direct publication to container registries;
- automatic Java-specific image layering;
- OCI-compatible output.

For this project, the additional low-level flexibility of manually maintained Dockerfiles does not outweigh their maintenance overhead. Jib provides the required container images while keeping image construction closely integrated with the Java build system and reducing repetitive infrastructure code.

### ko for Go Images

The Go services use **ko** rather than manually maintained Dockerfiles.

Go applications compile to self-contained binaries and generally require little runtime infrastructure. `ko` is designed specifically for this model: it compiles Go applications and packages them directly into OCI-compatible container images.

Using `ko`:

- reduces Dockerfile maintenance;
- produces minimal application images;
- integrates naturally with the Go toolchain;
- supports local and CI image publication.

Jib and `ko` therefore allow each language ecosystem to use specialised image-building tooling while producing standard OCI images that can be run locally through Docker and deployed through Kubernetes.

## Consequences

Benefits include:

- consistent OCI-compatible containers across Java and Go services;
- reproducible local environments through Docker Compose;
- no manually maintained Dockerfiles for application microservices;
- language-aware image construction using Jib and `ko`;
- Kubernetes self-healing and horizontal autoscaling;
- managed Kubernetes through GKE;
- a cloud platform selected specifically around the Kubernetes deployment requirement.

Trade-offs include:

- Kubernetes introduces substantially more complexity than Docker Swarm;
- choosing GCP introduces dependence on a specific managed Kubernetes provider;
- Docker Desktop is an additional local development dependency;
- Jib and `ko` provide less low-level image customisation than manually written Dockerfiles;
- developers must understand both the local Docker Compose environment and the Kubernetes deployment model.
