#!/usr/bin/env bash

set -Eeuo pipefail

REPOSITORY_ROOT="$(
  cd "$(dirname "${BASH_SOURCE[0]}")/.." &&
  pwd
)"

cd "$REPOSITORY_ROOT"

echo "Building Java images with Jib..."

(
  cd services/restaurant-menu-service

  ./mvnw \
    -B \
    -ntp \
    package \
    jib:dockerBuild \
    -Djib.to.image=food-delivery/restaurant-menu-service:local
)

(
  cd services/order-service

  ./mvnw \
    -B \
    -ntp \
    package \
    jib:dockerBuild \
    -Djib.to.image=food-delivery/order-service:local
)

echo "Building Go images with ko..."

export KO_DOCKER_REPO="ko.local"

DELIVERY_REFS="$(mktemp)"

ko build \
  --local \
  --image-refs="$DELIVERY_REFS" \
  ./services/delivery-tracking-service/cmd/delivery-tracking-service

DELIVERY_IMAGE="$(tail -n 1 "$DELIVERY_REFS")"

docker tag \
  "$DELIVERY_IMAGE" \
  food-delivery/delivery-tracking-service:local

rm -f "$DELIVERY_REFS"

PAYMENT_REFS="$(mktemp)"

ko build \
  --local \
  --image-refs="$PAYMENT_REFS" \
  ./services/payment-service/cmd/payment-service

PAYMENT_IMAGE="$(tail -n 1 "$PAYMENT_REFS")"

docker tag \
  "$PAYMENT_IMAGE" \
  food-delivery/payment-service:local

rm -f "$PAYMENT_REFS"

unset KO_DOCKER_REPO

echo
echo "All local service images built successfully."