
package com.fooddelivery.restaurant_menu_service.menu;

import java.util.List;
import org.springframework.stereotype.Service;
import com.fooddelivery.restaurant_menu_service.restaurant.RestaurantCrudService;
import io.grpc.Status;

@Service
public class MenuCrudService {
    private final MenuItemRepository repository;
    private final RestaurantCrudService restaurantService;

    public MenuCrudService(
            MenuItemRepository repository,
            RestaurantCrudService restaurantService) {
        this.repository = repository;
        this.restaurantService = restaurantService;
    }

    // ==========================================================
    // Create
    // ==========================================================
    public MenuItemDocument create(
            String ownerSub,
            String restaurantId,
            String name,
            long priceMinorUnits,
            boolean available) {
        String validName = requireName(name);
        requirePositivePrice(priceMinorUnits);

        // Ensures the restaurant exists, is active, and belongs to the owner.
        restaurantService.getOwned(ownerSub, restaurantId);

        MenuItemDocument item = new MenuItemDocument(
                restaurantId,
                validName,
                priceMinorUnits,
                available);

        return repository.save(item);
    }

    // ==========================================================
    // Customer menu
    // ==========================================================
    public List<MenuItemDocument> listPublic(String restaurantId) {
        // Inactive or unknown restaurants must not expose their menus.
        restaurantService.getPublic(restaurantId);

        return repository
                .findByRestaurantIdAndActiveTrueAndAvailableTrueOrderByNameAsc(
                        restaurantId);
    }

    // ==========================================================
    // Restaurant owner menu
    // ==========================================================
    public List<MenuItemDocument> listOwned(String ownerSub, String restaurantId) {
        restaurantService.getOwned(ownerSub, restaurantId);

        return repository
                .findByRestaurantIdAndActiveTrueOrderByNameAsc(
                        restaurantId);
    }

    // ==========================================================
    // Update
    // ==========================================================
    public MenuItemDocument update(
            String ownerSub,
            String restaurantId,
            String itemId,
            String name,
            long priceMinorUnits,
            boolean available) {
        restaurantService.getOwned(ownerSub, restaurantId);
        String validName = requireName(name);
        requirePositivePrice(priceMinorUnits);

        MenuItemDocument item = findActiveItem(restaurantId, itemId);

        item.setName(validName);
        item.setPriceMinorUnits(priceMinorUnits);
        item.setAvailable(available);

        return repository.save(item);
    }

    // ==========================================================
    // Soft delete
    // ==========================================================
    public void delete(
            String ownerSub,
            String restaurantId,
            String itemId) {
        restaurantService.getOwned(ownerSub, restaurantId);

        MenuItemDocument item = findActiveItem(restaurantId, itemId);

        item.setAvailable(false);
        item.setActive(false);

        repository.save(item);
    }

    // ==========================================================
    // Internal helpers
    // ==========================================================
    private MenuItemDocument findActiveItem(String restaurantId, String itemId) {
        if (itemId == null || itemId.isBlank()) {
            throw Status.INVALID_ARGUMENT
                    .withDescription("Menu item ID is required.")
                    .asRuntimeException();
        }

        return repository.findByIdAndRestaurantIdAndActiveTrue(
                        itemId.trim(),
                        restaurantId.trim())
                .orElseThrow(() -> Status.NOT_FOUND
                        .withDescription("Menu item not found.")
                        .asRuntimeException());
    }

    private String requireName(String name) {
        if (name == null || name.isBlank()) {
            throw Status.INVALID_ARGUMENT
                    .withDescription("Menu item name is required.")
                    .asRuntimeException();
        }

        String normalized = name.trim();

        if (normalized.length() > 120) {
            throw Status.INVALID_ARGUMENT
                    .withDescription(
                            "Menu item name must not exceed 120 characters.")
                    .asRuntimeException();
        }

        return normalized;
    }

    private void requirePositivePrice(long priceMinorUnits) {
        if (priceMinorUnits <= 0) {
            throw Status.INVALID_ARGUMENT
                    .withDescription(
                            "Menu item price must be greater than zero.")
                    .asRuntimeException();
        }
    }
}
