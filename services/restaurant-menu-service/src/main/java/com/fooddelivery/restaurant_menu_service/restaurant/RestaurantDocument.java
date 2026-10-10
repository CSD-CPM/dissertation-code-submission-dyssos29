
package com.fooddelivery.restaurant_menu_service.restaurant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "restaurants")
public class RestaurantDocument {
    @Id
    private String id;
    private String ownerSub;
    private String name;
    private boolean active;

    public RestaurantDocument() {
        // Required for persistence mapping.
    }

    public RestaurantDocument(String ownerSub, String name) {
        this.ownerSub = ownerSub;
        this.name = name;
        this.active = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerSub() {
        return ownerSub;
    }

    public void setOwnerSub(String ownerSub) {
        this.ownerSub = ownerSub;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
