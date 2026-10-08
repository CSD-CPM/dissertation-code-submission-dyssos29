
package com.fooddelivery.restaurant_menu_service.menu;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "menu_items")
public class MenuItemDocument {
    @Id
    private String id;
    private String restaurantId;
    private String name;
    private long priceMinorUnits;
    private boolean available;
    private boolean active;

    public MenuItemDocument() {
        // Required for MongoDB persistence mapping.
    }

    public MenuItemDocument(
            String restaurantId,
            String name,
            long priceMinorUnits,
            boolean available) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.priceMinorUnits = priceMinorUnits;
        this.available = available;
        this.active = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPriceMinorUnits() {
        return priceMinorUnits;
    }

    public void setPriceMinorUnits(long priceMinorUnits) {
        this.priceMinorUnits = priceMinorUnits;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
