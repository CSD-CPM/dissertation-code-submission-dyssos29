package com.fooddelivery.restaurant_menu_service.auth;

import io.grpc.Context;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.fooddelivery.restaurant_menu_service.auth.Role;

class RoleGuardTest {
    @Test
    void acceptsExpectedRole() {
        Context context = Context.current()
                .withValue(TrustedIdentityInterceptor.SUBJECT, "test-user")
                .withValue(TrustedIdentityInterceptor.ROLE, "CUSTOMER");

        context.run(() ->
                assertDoesNotThrow(
                        () -> RoleGuard.requireRole(Role.CUSTOMER)
                )
        );
    }

    @Test
    void rejectsIncorrectRole() {
        Context context = Context.current()
                .withValue(TrustedIdentityInterceptor.SUBJECT, "test-user")
                .withValue(TrustedIdentityInterceptor.ROLE, "CUSTOMER");

        context.run(() -> {
            StatusRuntimeException exception =
                    assertThrows(
                            StatusRuntimeException.class,
                            () -> RoleGuard.requireRole(Role.ADMIN)
                    );

            assertEquals(
                    Status.Code.PERMISSION_DENIED,
                    exception.getStatus().getCode()
            );
        });
    }

    @Test
    void rejectsMissingIdentity() {
        StatusRuntimeException exception =
                assertThrows(
                        StatusRuntimeException.class,
                        () -> RoleGuard.requireRole(Role.CUSTOMER)
                );

        assertEquals(
                Status.Code.UNAUTHENTICATED,
                exception.getStatus().getCode()
        );
    }

    @Test
    void rejectsMissingRole() {
        Context context = Context.current()
                .withValue(TrustedIdentityInterceptor.SUBJECT, "test-user");

        context.run(() -> {
            StatusRuntimeException exception =
                    assertThrows(
                            StatusRuntimeException.class,
                            () -> RoleGuard.requireRole(Role.CUSTOMER));

            assertEquals(
                    Status.Code.PERMISSION_DENIED,
                    exception.getStatus().getCode());
        });
    }
}
