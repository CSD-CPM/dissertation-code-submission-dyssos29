package grpcserver

import (
	"context"
	"testing"

	"github.com/dyssos29/food-delivery-platform/services/delivery-tracking-service/internal/auth"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/metadata"
	"google.golang.org/grpc/status"
)

func TestCourierPingAcceptsCourier(t *testing.T) {
	ctx := metadata.NewIncomingContext(
		context.Background(),
		metadata.Pairs(
			"x-authenticated-sub", "test-courier",
			"x-authenticated-role", auth.RoleCourier,
		),
	)

	service := &DeliveryFoundationService{}

	response, err := service.CourierPing(ctx, nil)

	if err != nil {
		t.Fatalf("CourierPing() returned unexpected error: %v", err)
	}

	if response.GetService() != "delivery-tracking-service" {
		t.Fatalf(
			"expected service %q, got %q",
			"delivery-tracking-service",
			response.GetService(),
		)
	}

	if response.GetMessage() != "Foundation probe reachable" {
		t.Fatalf(
			"expected message %q, got %q",
			"Foundation probe reachable",
			response.GetMessage(),
		)
	}
}

func TestCourierPingRejectsWrongRole(t *testing.T) {
	ctx := metadata.NewIncomingContext(
		context.Background(),
		metadata.Pairs(
			"x-authenticated-sub", "test-customer",
			"x-authenticated-role", auth.RoleCustomer,
		),
	)

	service := &DeliveryFoundationService{}

	_, err := service.CourierPing(ctx, nil)

	if status.Code(err) != codes.PermissionDenied {
		t.Fatalf(
			"expected PermissionDenied, got %v",
			status.Code(err),
		)
	}
}
