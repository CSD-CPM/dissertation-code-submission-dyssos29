package com.fooddelivery.order_service.order;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class OrderItemSnapshot {
    @Column(name = "menu_item_id", nullable = false)
    private String menuItemId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "unit_price_minor_units", nullable = false)
    private long unitPriceMinorUnits;

    @Column(name = "line_total_minor_units", nullable = false)
    private long lineTotalMinorUnits;

    protected OrderItemSnapshot() {
    }

    public OrderItemSnapshot(
            String menuItemId,
            String name,
            int quantity,
            long unitPriceMinorUnits,
            long lineTotalMinorUnits) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.quantity = quantity;
        this.unitPriceMinorUnits = unitPriceMinorUnits;
        this.lineTotalMinorUnits = lineTotalMinorUnits;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getUnitPriceMinorUnits() {
        return unitPriceMinorUnits;
    }

    public long getLineTotalMinorUnits() {
        return lineTotalMinorUnits;
    }
}
