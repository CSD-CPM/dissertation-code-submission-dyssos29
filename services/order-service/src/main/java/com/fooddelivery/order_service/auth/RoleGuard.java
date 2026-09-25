package com.fooddelivery.order_service.auth;

import io.grpc.Status;
import java.util.Objects;

public final class RoleGuard {

    private RoleGuard() {
    }

    public static void requireRole(Role expectedRole) {
        Objects.requireNonNull(expectedRole, "expectedRole must not be null");

        String subject = TrustedIdentityInterceptor.SUBJECT.get();
        String actualRole = TrustedIdentityInterceptor.ROLE.get();

        if (subject == null || subject.isBlank()) {
            throw Status.UNAUTHENTICATED
                    .withDescription("Authenticated subject is missing or invalid")
                    .asRuntimeException();
        }

        if (actualRole == null || actualRole.isBlank()) {
            throw Status.PERMISSION_DENIED
                    .withDescription("Authenticated role is missing or invalid")
                    .asRuntimeException();
        }

        if (!expectedRole.name().equals(actualRole)) {
            throw Status.PERMISSION_DENIED
                    .withDescription("Insufficient role.")
                    .asRuntimeException();
        }
    }
}
