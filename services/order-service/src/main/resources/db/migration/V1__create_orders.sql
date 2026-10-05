CREATE TABLE orders (
    id VARCHAR(36) PRIMARY KEY,
    customer_sub VARCHAR(128) NOT NULL,
    restaurant_id VARCHAR(128) NOT NULL,
    restaurant_owner_sub VARCHAR(128) NOT NULL,
    total_minor_units BIGINT NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT chk_orders_total_positive
        CHECK (total_minor_units > 0),

    CONSTRAINT chk_orders_status
        CHECK (status IN ('PLACED', 'ACCEPTED', 'REJECTED'))
);

CREATE TABLE order_items (
    order_id VARCHAR(36) NOT NULL,
    line_number INTEGER NOT NULL,
    menu_item_id VARCHAR(128) NOT NULL,
    name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price_minor_units BIGINT NOT NULL,
    line_total_minor_units BIGINT NOT NULL,

    PRIMARY KEY (order_id, line_number),

    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_order_items_menu_item
        UNIQUE (order_id, menu_item_id),

    CONSTRAINT chk_order_items_quantity_positive
        CHECK (quantity > 0),

    CONSTRAINT chk_order_items_unit_price_positive
        CHECK (unit_price_minor_units > 0),

    CONSTRAINT chk_order_items_line_total_positive
        CHECK (line_total_minor_units > 0)
);

CREATE INDEX idx_orders_customer_created
    ON orders(customer_sub, created_at DESC);

CREATE INDEX idx_orders_restaurant_created
    ON orders(restaurant_id, created_at DESC);

CREATE INDEX idx_orders_owner_created
    ON orders(restaurant_owner_sub, created_at DESC);
