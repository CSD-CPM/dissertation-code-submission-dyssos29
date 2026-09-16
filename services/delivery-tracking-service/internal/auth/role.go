package auth

import (
	"context"
	"strings"

	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/metadata"
	"google.golang.org/grpc/status"
)

const (
	RoleCustomer        = "CUSTOMER"
	RoleRestaurantOwner = "RESTAURANT_OWNER"
	RoleCourier         = "COURIER"
	RoleAdmin           = "ADMIN"

	subjectMetadataKey = "x-authenticated-sub"
	roleMetadataKey    = "x-authenticated-role"
)

func RequireRole(ctx context.Context, expectedRole string) error {
	md, ok := metadata.FromIncomingContext(ctx)
	if !ok {
		return status.Error(
			codes.Unauthenticated,
			"authenticated subject is missing",
		)
	}

	_, subjectOK := singleMetadataValue(md, subjectMetadataKey)
	if !subjectOK {
		return status.Error(
			codes.Unauthenticated,
			"authenticated subject is missing or invalid",
		)
	}

	role, roleOK := singleMetadataValue(md, roleMetadataKey)
	if !roleOK {
		return status.Error(
			codes.PermissionDenied,
			"authenticated role is missing or invalid",
		)
	}

	if role != expectedRole {
		return status.Error(
			codes.PermissionDenied,
			"insufficient role",
		)
	}

	return nil
}

func singleMetadataValue(md metadata.MD, key string) (string, bool) {
	values := md.Get(key)

	if len(values) != 1 {
		return "", false
	}

	value := strings.TrimSpace(values[0])
	if value == "" {
		return "", false
	}

	return value, true
}
