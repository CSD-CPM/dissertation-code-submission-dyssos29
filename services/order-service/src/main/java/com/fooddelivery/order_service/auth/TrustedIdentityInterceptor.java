package com.fooddelivery.order_service.auth;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.stereotype.Component;
import java.util.Iterator;

@Component
@GlobalServerInterceptor
public final class TrustedIdentityInterceptor implements ServerInterceptor {
    public static final Context.Key<String> SUBJECT =
            Context.key("authenticated-sub");

    public static final Context.Key<String> ROLE =
            Context.key("authenticated-role");

    private static final Metadata.Key<String> SUBJECT_HEADER =
            Metadata.Key.of(
                    "x-authenticated-sub",
                    Metadata.ASCII_STRING_MARSHALLER);

    private static final Metadata.Key<String> ROLE_HEADER =
            Metadata.Key.of(
                    "x-authenticated-role",
                    Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {
        String subject = singleMetadataValue(headers, SUBJECT_HEADER);
        String role = singleMetadataValue(headers, ROLE_HEADER);

        Context context = Context.current();

        if (subject != null) {
            context = context.withValue(SUBJECT, subject);
        }

        if (role != null) {
            context = context.withValue(ROLE, role);
        }

        return Contexts.interceptCall(
                context,
                call,
                headers,
                next);
    }

    private static String singleMetadataValue(
            Metadata metadata,
            Metadata.Key<String> key) {
        Iterable<String> values = metadata.getAll(key);

        if (values == null) {
            return null;
        }

        Iterator<String> iterator = values.iterator();

        if (!iterator.hasNext()) {
            return null;
        }

        String value = iterator.next();

        if (iterator.hasNext()) {
            return null;
        }

        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
