package com.fooddelivery.restaurant_menu_service.grpc;

import com.fooddelivery.foundation.v1.RestaurantFoundationServiceGrpc;
import com.fooddelivery.foundation.v1.RestaurantPingRequest;
import com.fooddelivery.foundation.v1.RestaurantPingResponse;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import com.fooddelivery.restaurant_menu_service.auth.RoleGuard;
import com.fooddelivery.restaurant_menu_service.auth.Role;

@GrpcService
public class RestaurantFoundationGrpcService
        extends RestaurantFoundationServiceGrpc.RestaurantFoundationServiceImplBase {

    @Override
    public void restaurantPing(
            RestaurantPingRequest request,
            StreamObserver<RestaurantPingResponse> responseObserver) {
        RoleGuard.requireRole(Role.RESTAURANT_OWNER);

        RestaurantPingResponse response =
                RestaurantPingResponse.newBuilder()
                        .setService("restaurant-menu-service")
                        .setMessage("Foundation probe reachable")
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
