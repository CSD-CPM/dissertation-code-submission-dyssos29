package com.fooddelivery.restaurant_menu_service.grpc;

import com.fooddelivery.foundation.v1.RestaurantPingRequest;
import com.fooddelivery.foundation.v1.RestaurantPingResponse;
import com.fooddelivery.restaurant_menu_service.auth.TrustedIdentityInterceptor;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantFoundationGrpcServiceTest {

    private final RestaurantFoundationGrpcService service =
            new RestaurantFoundationGrpcService();

    @Test
    void restaurantPingAllowsRestaurantOwner() {
        Context context = Context.current()
                .withValue(
                        TrustedIdentityInterceptor.SUBJECT,
                        "test-restaurant-owner"
                )
                .withValue(
                        TrustedIdentityInterceptor.ROLE,
                        "RESTAURANT_OWNER"
                );

        RecordingObserver<RestaurantPingResponse> observer =
                new RecordingObserver<>();

        context.run(() ->
                service.restaurantPing(
                        RestaurantPingRequest.getDefaultInstance(),
                        observer
                )
        );

        assertNull(observer.error);
        assertTrue(observer.completed);
        assertNotNull(observer.response);

        assertEquals(
                "restaurant-menu-service",
                observer.response.getService()
        );

        assertEquals(
                "Foundation probe reachable",
                observer.response.getMessage()
        );
    }

    @Test
    void restaurantPingRejectsWrongRole() {
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
                            () -> service.restaurantPing(
                                    RestaurantPingRequest.getDefaultInstance(),
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
