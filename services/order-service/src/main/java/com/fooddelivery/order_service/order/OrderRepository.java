package com.fooddelivery.order_service.order;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
    List<OrderEntity>
    findByCustomerSubOrderByCreatedAtDesc(String customerSub);

    List<OrderEntity>
    findByRestaurantIdAndRestaurantOwnerSubOrderByCreatedAtDesc(String restaurantId, String restaurantOwnerSub);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select o
            from OrderEntity o
            where o.id = :orderId
            """)
    Optional<OrderEntity> findByIdForUpdate(@Param("orderId") String orderId);
}
