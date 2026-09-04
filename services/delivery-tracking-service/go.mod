module github.com/dyssos29/food-delivery-platform/services/delivery-tracking-service

go 1.26.6

require google.golang.org/grpc v1.83.2

require google.golang.org/genproto/googleapis/api v0.0.0-20260831171406-18b4a7587f8a // indirect

require (
	github.com/dyssos29/food-delivery-platform/contracts/gen v0.0.0
	golang.org/x/net v0.58.0 // indirect
	golang.org/x/sys v0.47.0 // indirect
	golang.org/x/text v0.41.0 // indirect
	google.golang.org/genproto/googleapis/rpc v0.0.0-20260825221802-da73d73af1c5 // indirect
	google.golang.org/protobuf v1.36.12 // indirect
)

replace github.com/dyssos29/food-delivery-platform/contracts/gen => ../../contracts/gen
