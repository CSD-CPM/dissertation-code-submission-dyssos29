package com.fooddelivery.order_service.order;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    private String id;

    @Column(name = "customer_sub", nullable = false)
    private String customerSub;

    @Column(name = "restaurant_id", nullable = false)
    private String restaurantId;

    @Column(name = "restaurant_owner_sub", nullable = false)
    private String restaurantOwnerSub;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "order_items",
            joinColumns = @JoinColumn(name = "order_id"))
    @OrderColumn(name = "line_number")
    private List<OrderItemSnapshot> items = new ArrayList<>();

    @Column(name = "total_minor_units", nullable = false)
    private long totalMinorUnits;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderState status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Version
    private long version;

    protected OrderEntity() {
    }

    public OrderEntity(
            String customerSub,
            String restaurantId,
            String restaurantOwnerSub,
            List<OrderItemSnapshot> items,
            long totalMinorUnits) {
        this.id = UUID.randomUUID().toString();
        this.customerSub = customerSub;
        this.restaurantId = restaurantId;
        this.restaurantOwnerSub = restaurantOwnerSub;
        this.items = new ArrayList<>(items);
        this.totalMinorUnits = totalMinorUnits;
        this.status = OrderState.PLACED;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getCustomerSub() {
        return customerSub;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantOwnerSub() {
        return restaurantOwnerSub;
    }

    public List<OrderItemSnapshot> getItems() {
        return List.copyOf(items);
    }

    public long getTotalMinorUnits() {
        return totalMinorUnits;
    }

    public OrderState getStatus() {
        return status;
    }

    public void setStatus(OrderState status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
