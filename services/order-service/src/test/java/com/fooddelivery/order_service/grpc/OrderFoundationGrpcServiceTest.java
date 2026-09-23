package com.fooddelivery.order_service.grpc;

import com.fooddelivery.foundation.v1.AdminPingRequest;
import com.fooddelivery.foundation.v1.AdminPingResponse;
import com.fooddelivery.foundation.v1.CustomerPingRequest;
import com.fooddelivery.foundation.v1.CustomerPingResponse;
import com.fooddelivery.order_service.auth.TrustedIdentityInterceptor;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderFoundationGrpcServiceTest {
    private final OrderFoundationGrpcService service =
            new OrderFoundationGrpcService();

    @Test
    void customerPingAllowsCustomer() {
        Context context = Context.current()
                .withValue(
                        TrustedIdentityInterceptor.SUBJECT,
                        "test-customer"
                )
                .withValue(
                        TrustedIdentityInterceptor.ROLE,
                        "CUSTOMER"
                );

        RecordingObserver<CustomerPingResponse> observer =
                new RecordingObserver<>();

        context.run(() ->
                service.customerPing(
                        CustomerPingRequest.getDefaultInstance(),
                        observer
                )
        );

        assertNull(observer.error);
        assertTrue(observer.completed);
        assertNotNull(observer.response);

        assertEquals(
                "order-service",
                observer.response.getService()
        );

        assertEquals(
                "Foundation probe reachable",
                observer.response.getMessage()
        );
    }

    @Test
    void customerPingRejectsWrongRole() {
        Context context = Context.current()
                .withValue(
                        TrustedIdentityInterceptor.SUBJECT,
                        "test-admin"
                )
                .withValue(
                        TrustedIdentityInterceptor.ROLE,
                        "ADMIN"
                );

        context.run(() -> {
            StatusRuntimeException exception =
                    assertThrows(
                            StatusRuntimeException.class,
                            () -> service.customerPing(
                                    CustomerPingRequest.getDefaultInstance(),
                                    new RecordingObserver<>()
                            )
                    );

            assertEquals(
                    Status.Code.PERMISSION_DENIED,
                    exception.getStatus().getCode()
            );
        });
    }

    @Test
    void adminPingAllowsAdmin() {
        Context context = Context.current()
                .withValue(
                        TrustedIdentityInterceptor.SUBJECT,
                        "test-admin"
                )
                .withValue(
                        TrustedIdentityInterceptor.ROLE,
                        "ADMIN"
                );

        RecordingObserver<AdminPingResponse> observer =
                new RecordingObserver<>();

        context.run(() ->
                service.adminPing(
                        AdminPingRequest.getDefaultInstance(),
                        observer
                )
        );

        assertNull(observer.error);
        assertTrue(observer.completed);
        assertNotNull(observer.response);

        assertEquals(
                "order-service",
                observer.response.getService()
        );

        assertEquals(
                "Foundation probe reachable",
                observer.response.getMessage()
        );
    }

    @Test
    void adminPingRejectsWrongRole() {
        Context context = Context.current()
                .withValue(
                        TrustedIdentityInterceptor.SUBJECT,
                        "test-customer"
                )
                .withValue(
                        TrustedIdentityInterceptor.ROLE,
                        "CUSTOMER"
                );

        context.run(() -> {
            StatusRuntimeException exception =
                    assertThrows(
                            StatusRuntimeException.class,
                            () -> service.adminPing(
                                    AdminPingRequest.getDefaultInstance(),
                                    new RecordingObserver<>()
                            )
                    );

            assertEquals(
                    Status.Code.PERMISSION_DENIED,
                    exception.getStatus().getCode()
            );
        });
    }

    private static final class RecordingObserver<T>
            implements StreamObserver<T> {

        private T response;
        private Throwable error;
        private boolean completed;

        @Override
        public void onNext(T value) {
            response = value;
        }

        @Override
        public void onError(Throwable throwable) {
            error = throwable;
        }

        @Override
        public void onCompleted() {
            completed = true;
        }
    }
}
