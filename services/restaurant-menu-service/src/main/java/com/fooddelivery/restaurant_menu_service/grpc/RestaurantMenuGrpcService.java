
package com.fooddelivery.restaurant_menu_service.grpc;

import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;
import com.fooddelivery.restaurant.v1.*;
import com.fooddelivery.restaurant_menu_service.auth.Role;
import com.fooddelivery.restaurant_menu_service.auth.RoleGuard;
import com.fooddelivery.restaurant_menu_service.auth.TrustedIdentityInterceptor;
import com.fooddelivery.restaurant_menu_service.restaurant.RestaurantCrudService;
import com.fooddelivery.restaurant_menu_service.restaurant.RestaurantDocument;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

@GrpcService
public class RestaurantMenuGrpcService extends RestaurantMenuServiceGrpc.RestaurantMenuServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(RestaurantMenuGrpcService.class);
    private final RestaurantCrudService restaurantService;

    public RestaurantMenuGrpcService(RestaurantCrudService restaurantService) {
        this.restaurantService = restaurantService;
    }

    // ==========================================================
    // Create restaurant
    // ==========================================================
    @Override
    public void createRestaurant(CreateRestaurantRequest request, StreamObserver<CreateRestaurantResponse> observer) {
        respond(observer, () -> {
            RoleGuard.requireRole(Role.RESTAURANT_OWNER);

            RestaurantDocument restaurant = restaurantService.create(authenticatedSubject(), request.getName());

            return CreateRestaurantResponse.newBuilder()
                    .setRestaurant(toProto(restaurant))
                    .build();
        });
    }

    // ==========================================================
    // Get restaurant
    // ==========================================================
    @Override
    public void getRestaurant(GetRestaurantRequest request, StreamObserver<GetRestaurantResponse> observer) {
        respond(observer, () -> {
            RestaurantDocument restaurant;

            String role = TrustedIdentityInterceptor.ROLE.get();

            if ("CUSTOMER".equals(role)) {
                RoleGuard.requireRole(Role.CUSTOMER);
                restaurant = restaurantService.getPublic(request.getRestaurantId());
            } else {
                RoleGuard.requireRole(Role.RESTAURANT_OWNER);
                restaurant = restaurantService.getOwned(authenticatedSubject(), request.getRestaurantId());
            }

            return GetRestaurantResponse.newBuilder()
                    .setRestaurant(toProto(restaurant))
                    .build();
        });
    }

    // ==========================================================
    // List public restaurants
    // ==========================================================
    @Override
    public void listRestaurants(ListRestaurantsRequest request, StreamObserver<ListRestaurantsResponse> observer) {
        respond(observer, () -> {
            RoleGuard.requireRole(Role.CUSTOMER);
            ListRestaurantsResponse.Builder response = ListRestaurantsResponse.newBuilder();

            restaurantService.listPublic().stream()
                    .map(RestaurantMenuGrpcService::toProto)
                    .forEach(response::addRestaurants);

            return response.build();
        });
    }

    // ==========================================================
    // List owned restaurants
    // ==========================================================
    @Override
    public void listOwnedRestaurants(ListOwnedRestaurantsRequest request, StreamObserver<ListOwnedRestaurantsResponse> observer) {
        respond(observer, () -> {
            RoleGuard.requireRole(Role.RESTAURANT_OWNER);
            ListOwnedRestaurantsResponse.Builder response = ListOwnedRestaurantsResponse.newBuilder();

            restaurantService.listOwned(authenticatedSubject()).stream()
                    .map(RestaurantMenuGrpcService::toProto)
                    .forEach(response::addRestaurants);

            return response.build();
        });
    }

    // ==========================================================
    // Update restaurant
    // ==========================================================
    @Override
    public void updateRestaurant(UpdateRestaurantRequest request, StreamObserver<UpdateRestaurantResponse> observer) {
        respond(observer, () -> {
            RoleGuard.requireRole(Role.RESTAURANT_OWNER);

            RestaurantDocument restaurant = restaurantService.update(
                    authenticatedSubject(),
                    request.getRestaurantId(),
                    request.getName());

            return UpdateRestaurantResponse.newBuilder()
                    .setRestaurant(toProto(restaurant))
                    .build();
        });
    }

    // ==========================================================
    // Soft delete restaurant
    // ==========================================================
    @Override
    public void deleteRestaurant(DeleteRestaurantRequest request, StreamObserver<DeleteRestaurantResponse> observer) {
        respond(observer, () -> {
            RoleGuard.requireRole(Role.RESTAURANT_OWNER);
            restaurantService.delete(authenticatedSubject(), request.getRestaurantId());

            return DeleteRestaurantResponse.newBuilder().build();
        });
    }

    // ==========================================================
    // Shared helpers
    // ==========================================================
    private static String authenticatedSubject() {
        return TrustedIdentityInterceptor.SUBJECT.get();
    }

    private static Restaurant toProto(RestaurantDocument document) {
        return Restaurant.newBuilder()
                .setId(document.getId())
                .setName(document.getName())
                .setActive(document.isActive())
                .build();
    }

    private static <T> void respond(StreamObserver<T> observer, Supplier<T> operation) {
        try {
            T response = operation.get();

            observer.onNext(response);
            observer.onCompleted();
        } catch (StatusRuntimeException exception) {
            observer.onError(exception);
        } catch (RuntimeException exception) {
            log.error("Restaurant operation failed.", exception);

            observer.onError(
                    Status.INTERNAL
                            .withDescription("Restaurant operation failed.")
                            .asRuntimeException());
        }
    }
}
