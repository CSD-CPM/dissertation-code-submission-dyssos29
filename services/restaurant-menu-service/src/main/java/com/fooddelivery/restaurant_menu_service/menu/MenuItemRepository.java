
package com.fooddelivery.restaurant_menu_service.menu;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MenuItemRepository extends MongoRepository<MenuItemDocument, String> {
    // Customer view: only active and available items.
    List<MenuItemDocument>
    findByRestaurantIdAndActiveTrueAndAvailableTrueOrderByNameAsc(String restaurantId);

    // Owner view: active items, including unavailable ones.
    List<MenuItemDocument>
    findByRestaurantIdAndActiveTrueOrderByNameAsc(String restaurantId);

    // Ensure the requested item belongs to the specified restaurant.
    Optional<MenuItemDocument>
    findByIdAndRestaurantIdAndActiveTrue(String itemId, String restaurantId);
}
