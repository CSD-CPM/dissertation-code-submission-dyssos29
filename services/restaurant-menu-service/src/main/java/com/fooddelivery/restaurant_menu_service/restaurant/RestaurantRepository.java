
package com.fooddelivery.restaurant_menu_service.restaurant;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestaurantRepository extends MongoRepository<RestaurantDocument, String> {
    List<RestaurantDocument> findByActiveTrueOrderByNameAsc();
    List<RestaurantDocument> findByOwnerSubAndActiveTrueOrderByNameAsc(String ownerSub);
}
