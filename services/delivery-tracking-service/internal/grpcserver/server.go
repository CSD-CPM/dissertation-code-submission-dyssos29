package grpcserver

import (
	foundationv1 "github.com/dyssos29/food-delivery-platform/contracts/gen/go/fooddelivery/foundation/v1"
	"google.golang.org/grpc"
)

func New() *grpc.Server {
	server := grpc.NewServer()

	foundationv1.RegisterDeliveryFoundationServiceServer(
		server,
		&DeliveryFoundationService{},
	)

	return server
}
