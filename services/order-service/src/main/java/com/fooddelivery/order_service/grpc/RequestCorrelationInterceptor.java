package com.fooddelivery.order_service.grpc;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.stereotype.Component;

@Component
@GlobalServerInterceptor
public final class RequestCorrelationInterceptor implements ServerInterceptor {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(RequestCorrelationInterceptor.class);

    private static final Metadata.Key<String> REQUEST_ID_HEADER =
            Metadata.Key.of(
                    "x-request-id",
                    Metadata.ASCII_STRING_MARSHALLER);

    public static final Context.Key<String> REQUEST_ID_CONTEXT =
            Context.key("requestId");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String requestId = headers.get(REQUEST_ID_HEADER);

        if (requestId == null || requestId.isBlank()) {
            requestId = "-";
        }

        String correlationId = requestId;

        Context context = Context.current()
                .withValue(REQUEST_ID_CONTEXT, correlationId);

        ServerCall.Listener<ReqT> delegate;

        try (MDC.MDCCloseable ignored =
                    MDC.putCloseable("requestId", correlationId)) {

            LOGGER.info(
                    "gRPC request received method={}",
                    call.getMethodDescriptor().getFullMethodName());

            delegate = Contexts.interceptCall(
                    context,
                    call,
                    headers,
                    next);
        }

        return new ForwardingServerCallListener
                .SimpleForwardingServerCallListener<>(delegate) {

            @Override
            public void onMessage(ReqT message) {
                try (MDC.MDCCloseable ignored =
                            MDC.putCloseable("requestId", correlationId)) {
                    super.onMessage(message);
                }
            }

            @Override
            public void onHalfClose() {
                try (MDC.MDCCloseable ignored =
                            MDC.putCloseable("requestId", correlationId)) {
                    super.onHalfClose();
                }
            }

            @Override
            public void onCancel() {
                try (MDC.MDCCloseable ignored =
                            MDC.putCloseable("requestId", correlationId)) {
                    super.onCancel();
                }
            }

            @Override
            public void onComplete() {
                try (MDC.MDCCloseable ignored =
                            MDC.putCloseable("requestId", correlationId)) {
                    super.onComplete();
                }
            }

            @Override
            public void onReady() {
                try (MDC.MDCCloseable ignored =
                            MDC.putCloseable("requestId", correlationId)) {
                    super.onReady();
                }
            }
        };
    }
}
