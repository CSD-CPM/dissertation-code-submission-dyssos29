package grpcserver

import (
	"context"

	foundationv1 "github.com/dyssos29/food-delivery-platform/contracts/gen/go/fooddelivery/foundation/v1"
	"github.com/dyssos29/food-delivery-platform/services/delivery-tracking-service/internal/auth"
)

type DeliveryFoundationService struct {
	foundationv1.UnimplementedDeliveryFoundationServiceServer
}

func (s *DeliveryFoundationService) CourierPing(
	ctx context.Context,
	req *foundationv1.CourierPingRequest,
) (*foundationv1.CourierPingResponse, error) {
	if err := auth.RequireRole(ctx, auth.RoleCourier); err != nil {
		return nil, err
	}

	return &foundationv1.CourierPingResponse{
		Service: "delivery-tracking-service",
		Message: "Foundation probe reachable",
	}, nil
}
