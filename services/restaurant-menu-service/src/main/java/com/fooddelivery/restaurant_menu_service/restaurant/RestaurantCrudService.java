
package com.fooddelivery.restaurant_menu_service.restaurant;

import java.util.List;
import org.springframework.stereotype.Service;
import io.grpc.Status;

@Service
public class RestaurantCrudService {
    private final RestaurantRepository repository;

    public RestaurantCrudService(RestaurantRepository repository) {
        this.repository = repository;
    }

    // ==========================================================
    // Create
    // ==========================================================
    public RestaurantDocument create(String ownerSub, String name) {
        String validOwnerSub = requireSubject(ownerSub);
        String validName = requireName(name);

        RestaurantDocument restaurant = new RestaurantDocument(validOwnerSub, validName);

        return repository.save(restaurant);
    }

    // ==========================================================
    // Read
    // ==========================================================
    public RestaurantDocument getPublic(String restaurantId) {
        return findActiveRestaurant(restaurantId);
    }

    public RestaurantDocument getOwned(String ownerSub, String restaurantId) {
        return findOwnedRestaurant(ownerSub, restaurantId);
    }

    public List<RestaurantDocument> listPublic() {
        return repository.findByActiveTrueOrderByNameAsc();
    }

    public List<RestaurantDocument> listOwned(String ownerSub) {
        return repository.findByOwnerSubAndActiveTrueOrderByNameAsc(requireSubject(ownerSub));
    }

    // ==========================================================
    // Update
    // ==========================================================
    public RestaurantDocument update(String ownerSub, String restaurantId, String name) {
        RestaurantDocument restaurant = findOwnedRestaurant(ownerSub, restaurantId);
        restaurant.setName(requireName(name));

        return repository.save(restaurant);
    }

    // ==========================================================
    // Soft delete
    // ==========================================================
    public void delete(String ownerSub, String restaurantId) {
        RestaurantDocument restaurant = findOwnedRestaurant(ownerSub, restaurantId);
        restaurant.setActive(false);

        repository.save(restaurant);
    }

    // ==========================================================
    // Internal validation and ownership checks
    // ==========================================================
    private RestaurantDocument findActiveRestaurant(String restaurantId) {
        RestaurantDocument restaurant = findRestaurant(restaurantId);

        if (!restaurant.isActive()) {
            throw Status.NOT_FOUND
                    .withDescription("Restaurant not found.")
                    .asRuntimeException();
        }

        return restaurant;
    }

    private RestaurantDocument findOwnedRestaurant(String ownerSub, String restaurantId) {
        String subject = requireSubject(ownerSub);
        RestaurantDocument restaurant = findRestaurant(restaurantId);

        if (!subject.equals(restaurant.getOwnerSub())) {
            throw Status.PERMISSION_DENIED
                    .withDescription(
                            "You do not own this restaurant.")
                    .asRuntimeException();
        }

        if (!restaurant.isActive()) {
            throw Status.NOT_FOUND
                    .withDescription("Restaurant not found.")
                    .asRuntimeException();
        }

        return restaurant;
    }

    private RestaurantDocument findRestaurant(String restaurantId) {
        if (restaurantId == null || restaurantId.isBlank()) {
            throw Status.INVALID_ARGUMENT
                    .withDescription("Restaurant ID is required.")
                    .asRuntimeException();
        }

        return repository.findById(restaurantId.trim())
                .orElseThrow(() -> Status.NOT_FOUND
                        .withDescription("Restaurant not found.")
                        .asRuntimeException());
    }

    private String requireName(String name) {
        if (name == null || name.isBlank()) {
            throw Status.INVALID_ARGUMENT
                    .withDescription("Restaurant name is required.")
                    .asRuntimeException();
        }

        String normalized = name.trim();

        if (normalized.length() > 120) {
            throw Status.INVALID_ARGUMENT
                    .withDescription(
                            "Restaurant name must not exceed 120 characters.")
                    .asRuntimeException();
        }

        return normalized;
    }

    private String requireSubject(String subject) {
        if (subject == null || subject.isBlank()) {
            throw Status.UNAUTHENTICATED
                    .withDescription("Authenticated subject is missing.")
                    .asRuntimeException();
        }

        return subject.trim();
    }
}
