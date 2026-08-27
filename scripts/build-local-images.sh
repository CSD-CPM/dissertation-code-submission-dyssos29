#!/usr/bin/env bash

set -Eeuo pipefail


# ============================================================
# Configuration
# ============================================================

GO_VERSION="1.26.6"
KO_VERSION="v0.19.1"

REPOSITORY_ROOT="$(
  cd "$(dirname "${BASH_SOURCE[0]}")/.." &&
  pwd
)"

TEMP_DIR="$REPOSITORY_ROOT/tmp/local-image-builds"

# Version-specific cache so upgrading Go or ko automatically
# creates a fresh cache instead of accidentally reusing another
# tool version.
KO_CACHE_VOLUME="food-delivery-ko-${KO_VERSION#v}-go-${GO_VERSION}-cache"

cd "$REPOSITORY_ROOT"


# ============================================================
# Cleanup
# ============================================================

cleanup() {
  rm -rf "$TEMP_DIR"
}

trap cleanup EXIT

rm -rf "$TEMP_DIR"
mkdir -p "$TEMP_DIR"


# ============================================================
# Preconditions
# ============================================================

if ! command -v docker >/dev/null 2>&1; then
  echo "Error: Docker is not available."
  exit 1
fi

if ! docker info >/dev/null 2>&1; then
  echo "Error: Docker is not running."
  exit 1
fi


# ============================================================
# Java images
# ============================================================

build_java_image() {
  local service="$1"
  local image="$2"

  echo
  echo "Building ${service} with Jib..."

  (
    cd "services/${service}"

    ./mvnw \
      -B \
      -ntp \
      -DskipTests \
      package \
      jib:dockerBuild \
      "-Djib.to.image=${image}"
  )
}


echo "Building Java images with Jib..."

build_java_image \
  "restaurant-menu-service" \
  "food-delivery/restaurant-menu-service:local"

build_java_image \
  "order-service" \
  "food-delivery/order-service:local"


# ============================================================
# Prepare cross-platform Docker mount
# ============================================================

case "$(uname -s)" in
  MINGW*|MSYS*|CYGWIN*)
    MOUNT_SOURCE="$(cygpath -w "$REPOSITORY_ROOT")"
    DOCKER_COMMAND=(env MSYS_NO_PATHCONV=1 docker)
    ;;

  *)
    MOUNT_SOURCE="$REPOSITORY_ROOT"
    DOCKER_COMMAND=(docker)
    ;;
esac


# ============================================================
# Go images
# ============================================================

echo
echo "Building Go images with ko..."

# Persist:
#   - ko v0.19.1 executable
#   - downloaded Go modules
#   - Go build cache
#
# This makes the first execution slower, while subsequent
# executions reuse the cached tool and dependencies.

docker volume create "$KO_CACHE_VOLUME" >/dev/null


"${DOCKER_COMMAND[@]}" run --rm \
  --mount "type=bind,source=${MOUNT_SOURCE},target=/workspace" \
  --mount "type=volume,source=${KO_CACHE_VOLUME},target=/ko-cache" \
  -w /workspace \
  -e "KO_VERSION=${KO_VERSION}" \
  "golang:${GO_VERSION}" \
  bash -c '
    set -Eeuo pipefail

    export GOMODCACHE="/ko-cache/go-mod"
    export GOCACHE="/ko-cache/go-build"

    mkdir -p \
      "/ko-cache/bin" \
      "$GOMODCACHE" \
      "$GOCACHE"

    if [ ! -x "/ko-cache/bin/ko" ]; then
      echo "Installing ko ${KO_VERSION}..."

      GOBIN="/ko-cache/bin" \
        /usr/local/go/bin/go install \
        "github.com/google/ko@${KO_VERSION}"
    else
      echo "Using cached ko ${KO_VERSION}..."
    fi

    echo
    /ko-cache/bin/ko version

    echo
    echo "Building Delivery and Tracking Service..."

    export KO_DOCKER_REPO="local.invalid/food-delivery/delivery-tracking-service"

    /ko-cache/bin/ko build \
      --push=false \
      --bare \
      --tarball=/workspace/tmp/local-image-builds/delivery-tracking-service.tar \
      ./services/delivery-tracking-service/cmd/server

    echo
    echo "Building Payment Service..."

    export KO_DOCKER_REPO="local.invalid/food-delivery/payment-service"

    /ko-cache/bin/ko build \
      --push=false \
      --bare \
      --tarball=/workspace/tmp/local-image-builds/payment-service.tar \
      ./services/payment-service/cmd/server
  '


# ============================================================
# Load Go images into local Docker daemon
# ============================================================

load_go_image() {
  local service="$1"

  local tar_file="$TEMP_DIR/${service}.tar"
  local temporary_image="local.invalid/food-delivery/${service}:latest"
  local local_image="food-delivery/${service}:local"

  echo
  echo "Loading ${service} into Docker..."

  docker load -i "$tar_file"

  if ! docker image inspect "$temporary_image" >/dev/null 2>&1; then
    echo "Error: Expected image was not loaded:"
    echo "  ${temporary_image}"
    exit 1
  fi

  docker tag \
    "$temporary_image" \
    "$local_image"

  docker image rm \
    "$temporary_image" \
    >/dev/null

  echo "Created ${local_image}"
}


load_go_image "delivery-tracking-service"
load_go_image "payment-service"


# ============================================================
# Final verification
# ============================================================

echo
echo "Local service images:"
echo

docker image ls \
  --filter "reference=food-delivery/*:local"

echo
echo "All local service images built successfully."
