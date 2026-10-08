
package com.fooddelivery.restaurant_menu_service.grpc;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.stereotype.Component;
import com.fooddelivery.restaurant.v1.RestaurantMenuServiceGrpc;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import org.springframework.context.annotation.Profile;

@Component
@GlobalServerInterceptor
@Profile("local")
public final class InternalQuoteAuthenticationInterceptor implements ServerInterceptor {
    private static final String QUOTE_METHOD =
            RestaurantMenuServiceGrpc
                    .getQuoteOrderMethod()
                    .getFullMethodName();
    private static final Metadata.Key<String> TOKEN_HEADER =
            Metadata.Key.of(
                    "x-internal-quote-token",
                    Metadata.ASCII_STRING_MARSHALLER);
    private final String expectedToken;

    public InternalQuoteAuthenticationInterceptor(@Value("${INTERNAL_QUOTE_TOKEN:}") String expectedToken) {
        this.expectedToken = expectedToken;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {
        // Do not change authentication behaviour for existing RPCs.
        if (!QUOTE_METHOD.equals(call.getMethodDescriptor().getFullMethodName())) {
            return next.startCall(call, headers);
        }

        String suppliedToken = singleToken(headers);

        if (!isAuthorized(suppliedToken)) {
            call.close(
                    Status.UNAUTHENTICATED.withDescription(
                            "Valid internal quotation credentials are required."),
                    new Metadata());

            return new ServerCall.Listener<>() {};
        }

        return next.startCall(call, headers);
    }

    private String singleToken(Metadata headers) {
        Iterable<String> values = headers.getAll(TOKEN_HEADER);

        if (values == null) {
            return null;
        }

        Iterator<String> iterator = values.iterator();

        if (!iterator.hasNext()) {
            return null;
        }

        String token = iterator.next();

        // Reject multiple token values.
        if (iterator.hasNext()) {
            return null;
        }

        return token;
    }

    private boolean isAuthorized(String suppliedToken) {
        // Fail closed if the service was started without a token.
        if (expectedToken == null ||
                expectedToken.isBlank() ||
                suppliedToken == null) {
            return false;
        }

        return MessageDigest.isEqual(
                expectedToken.getBytes(StandardCharsets.UTF_8),
                suppliedToken.getBytes(StandardCharsets.UTF_8));
    }
}
