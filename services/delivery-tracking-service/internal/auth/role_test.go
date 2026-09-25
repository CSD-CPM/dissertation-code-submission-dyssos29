package auth

import (
	"context"
	"testing"

	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/metadata"
	"google.golang.org/grpc/status"
)

func TestRequireRoleAcceptsCourier(t *testing.T) {
	ctx := metadata.NewIncomingContext(
		context.Background(),
		metadata.Pairs(
			"x-authenticated-sub", "test-user",
			"x-authenticated-role", RoleCourier,
		),
	)

	if err := RequireRole(ctx, RoleCourier); err != nil {
		t.Fatalf("RequireRole() returned unexpected error: %v", err)
	}
}

func TestRequireRoleRejectsWrongRole(t *testing.T) {
	ctx := metadata.NewIncomingContext(
		context.Background(),
		metadata.Pairs(
			"x-authenticated-sub", "test-user",
			"x-authenticated-role", RoleCustomer,
		),
	)

	err := RequireRole(ctx, RoleCourier)

	if status.Code(err) != codes.PermissionDenied {
		t.Fatalf(
			"expected PermissionDenied, got %v",
			status.Code(err),
		)
	}
}

func TestRequireRoleRejectsMissingIdentity(t *testing.T) {
	err := RequireRole(context.Background(), RoleCourier)

	if status.Code(err) != codes.Unauthenticated {
		t.Fatalf(
			"expected Unauthenticated, got %v",
			status.Code(err),
		)
	}
}

func TestRequireRoleRejectsMissingRole(t *testing.T) {
	ctx := metadata.NewIncomingContext(
		context.Background(),
		metadata.Pairs(
			"x-authenticated-sub", "test-user",
		),
	)

	err := RequireRole(ctx, RoleCourier)

	if status.Code(err) != codes.PermissionDenied {
		t.Fatalf(
			"expected PermissionDenied, got %v",
			status.Code(err),
		)
	}
}
