package grpcserver

import (
	"context"
	"testing"

	"google.golang.org/grpc"
	"google.golang.org/grpc/metadata"
)

func TestContextWithRequestIDUsesIncomingRequestID(t *testing.T) {
	ctx := metadata.NewIncomingContext(
		context.Background(),
		metadata.Pairs(
			requestIDMetadataKey,
			"  request-123  ",
		),
	)

	correlatedContext, requestID := contextWithRequestID(ctx)

	if requestID != "request-123" {
		t.Fatalf(
			"expected request ID %q, got %q",
			"request-123",
			requestID,
		)
	}

	contextRequestID, ok := correlatedContext.Value(
		requestIDContextKey{},
	).(string)

	if !ok {
		t.Fatal("expected request ID to be stored in the context")
	}

	if contextRequestID != "request-123" {
		t.Fatalf(
			"expected context request ID %q, got %q",
			"request-123",
			contextRequestID,
		)
	}
}

func TestContextWithRequestIDUsesFallbackWhenMissing(t *testing.T) {
	correlatedContext, requestID := contextWithRequestID(
		context.Background(),
	)

	if requestID != "-" {
		t.Fatalf(
			"expected fallback request ID %q, got %q",
			"-",
			requestID,
		)
	}

	contextRequestID, ok := correlatedContext.Value(
		requestIDContextKey{},
	).(string)

	if !ok {
		t.Fatal("expected fallback request ID to be stored in the context")
	}

	if contextRequestID != "-" {
		t.Fatalf(
			"expected context request ID %q, got %q",
			"-",
			contextRequestID,
		)
	}
}

func TestContextWithRequestIDUsesFallbackWhenBlank(t *testing.T) {
	ctx := metadata.NewIncomingContext(
		context.Background(),
		metadata.Pairs(
			requestIDMetadataKey,
			"   ",
		),
	)

	_, requestID := contextWithRequestID(ctx)

	if requestID != "-" {
		t.Fatalf(
			"expected fallback request ID %q, got %q",
			"-",
			requestID,
		)
	}
}

func TestUnaryRequestCorrelationInterceptorAddsRequestIDToContext(
	t *testing.T,
) {
	ctx := metadata.NewIncomingContext(
		context.Background(),
		metadata.Pairs(
			requestIDMetadataKey,
			"request-unary-123",
		),
	)

	info := &grpc.UnaryServerInfo{
		FullMethod: "/test.Service/TestUnary",
	}

	handler := func(
		handlerContext context.Context,
		request any,
	) (any, error) {
		requestID, ok := handlerContext.Value(
			requestIDContextKey{},
		).(string)

		if !ok {
			t.Fatal(
				"expected request ID to be available in handler context",
			)
		}

		if requestID != "request-unary-123" {
			t.Fatalf(
				"expected request ID %q, got %q",
				"request-unary-123",
				requestID,
			)
		}

		return "ok", nil
	}

	response, err := unaryRequestCorrelationInterceptor(
		ctx,
		nil,
		info,
		handler,
	)

	if err != nil {
		t.Fatalf(
			"unexpected interceptor error: %v",
			err,
		)
	}

	if response != "ok" {
		t.Fatalf(
			"expected response %q, got %v",
			"ok",
			response,
		)
	}
}

type testServerStream struct {
	grpc.ServerStream
	ctx context.Context
}

func (stream *testServerStream) Context() context.Context {
	return stream.ctx
}

func TestStreamRequestCorrelationInterceptorAddsRequestIDToContext(
	t *testing.T,
) {
	ctx := metadata.NewIncomingContext(
		context.Background(),
		metadata.Pairs(
			requestIDMetadataKey,
			"request-stream-123",
		),
	)

	stream := &testServerStream{
		ctx: ctx,
	}

	info := &grpc.StreamServerInfo{
		FullMethod: "/test.Service/TestStream",
	}

	handler := func(
		service any,
		serverStream grpc.ServerStream,
	) error {
		requestID, ok := serverStream.Context().Value(
			requestIDContextKey{},
		).(string)

		if !ok {
			t.Fatal(
				"expected request ID to be available in stream context",
			)
		}

		if requestID != "request-stream-123" {
			t.Fatalf(
				"expected request ID %q, got %q",
				"request-stream-123",
				requestID,
			)
		}

		return nil
	}

	err := streamRequestCorrelationInterceptor(
		nil,
		stream,
		info,
		handler,
	)

	if err != nil {
		t.Fatalf(
			"unexpected stream interceptor error: %v",
			err,
		)
	}
}
