package grpcserver

import (
	"context"
	"log/slog"
	"strings"

	"google.golang.org/grpc"
	"google.golang.org/grpc/metadata"
)

const requestIDMetadataKey = "x-request-id"

type requestIDContextKey struct{}

func contextWithRequestID(
	ctx context.Context,
) (context.Context, string) {
	requestID := "-"

	values := metadata.ValueFromIncomingContext(
		ctx,
		requestIDMetadataKey,
	)

	if len(values) > 0 {
		value := strings.TrimSpace(values[0])

		if value != "" {
			requestID = value
		}
	}

	return context.WithValue(
		ctx,
		requestIDContextKey{},
		requestID,
	), requestID
}

func unaryRequestCorrelationInterceptor(
	ctx context.Context,
	request any,
	info *grpc.UnaryServerInfo,
	handler grpc.UnaryHandler,
) (any, error) {
	ctx, requestID := contextWithRequestID(ctx)

	response, err := handler(ctx, request)

	if err != nil {
		slog.ErrorContext(
			ctx,
			"gRPC request failed",
			"service", "delivery-tracking-service",
			"method", info.FullMethod,
			"requestId", requestID,
			"error", err,
		)

		return response, err
	}

	slog.InfoContext(
		ctx,
		"gRPC request completed",
		"service", "delivery-tracking-service",
		"method", info.FullMethod,
		"requestId", requestID,
	)

	return response, nil
}

type correlatedServerStream struct {
	grpc.ServerStream
	ctx context.Context
}

func (stream *correlatedServerStream) Context() context.Context {
	return stream.ctx
}

func streamRequestCorrelationInterceptor(
	service any,
	stream grpc.ServerStream,
	info *grpc.StreamServerInfo,
	handler grpc.StreamHandler,
) error {
	ctx, requestID := contextWithRequestID(stream.Context())

	correlatedStream := &correlatedServerStream{
		ServerStream: stream,
		ctx:          ctx,
	}

	err := handler(service, correlatedStream)

	if err != nil {
		slog.ErrorContext(
			ctx,
			"gRPC stream failed",
			"service", "delivery-tracking-service",
			"method", info.FullMethod,
			"requestId", requestID,
			"error", err,
		)

		return err
	}

	slog.InfoContext(
		ctx,
		"gRPC stream completed",
		"service", "delivery-tracking-service",
		"method", info.FullMethod,
		"requestId", requestID,
	)

	return nil
}
