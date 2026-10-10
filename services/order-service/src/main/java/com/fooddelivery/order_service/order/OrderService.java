package com.fooddelivery.order_service.order;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fooddelivery.order_service.grpc.client.RestaurantQuotationGrpcClient;
import com.fooddelivery.order_service.grpc.client.RestaurantQuotationGrpcClient.RequestedLine;
import io.grpc.Status;

@Service
public class OrderService {
    private final OrderRepository repository;
    private final RestaurantQuotationGrpcClient quotationClient;

    public OrderService(OrderRepository repository, RestaurantQuotationGrpcClient quotationClient) {
        this.repository = repository;
        this.quotationClient = quotationClient;
    }

    @Transactional
    public OrderEntity create(
            String customerSub,
            String restaurantId,
            List<CreateLine> lines) {
        requireSubject(customerSub);

        if (restaurantId == null || restaurantId.isBlank()) {
            throw Status.INVALID_ARGUMENT
                    .withDescription("Restaurant ID is required.")
                    .asRuntimeException();
        }

        if (lines == null || lines.isEmpty()) {
            throw Status.INVALID_ARGUMENT
                    .withDescription("Order must contain at least one item.")
                    .asRuntimeException();
        }

        var quote = quotationClient.quote(
                restaurantId.trim(),
                lines.stream()
                        .map(line -> new RequestedLine(
                                line.menuItemId(),
                                line.quantity()))
                        .toList());

        validateQuote(restaurantId.trim(), quote);

        List<OrderItemSnapshot> snapshots =
                quote.items()
                        .stream()
                        .map(item -> new OrderItemSnapshot(
                                item.menuItemId(),
                                item.name(),
                                item.quantity(),
                                item.unitPriceMinorUnits(),
                                item.lineTotalMinorUnits()))
                        .toList();

        OrderEntity order = new OrderEntity(
                customerSub.trim(),
                quote.restaurantId(),
                quote.restaurantOwnerSub(),
                snapshots,
                quote.totalMinorUnits());

        return repository.save(order);
    }

    @Transactional(readOnly = true)
    public OrderEntity getForCustomer(String customerSub, String orderId) {
        requireSubject(customerSub);

        OrderEntity order = find(orderId);

        if (!customerSub.trim().equals(order.getCustomerSub())) {
            throw Status.NOT_FOUND
                    .withDescription("Order not found.")
                    .asRuntimeException();
        }

        return order;
    }

    @Transactional(readOnly = true)
    public List<OrderEntity> listForCustomer(String customerSub) {
        requireSubject(customerSub);

        return repository
                .findByCustomerSubOrderByCreatedAtDesc(
                        customerSub.trim());
    }

    @Transactional(readOnly = true)
    public OrderEntity getForRestaurant(String ownerSub, String orderId) {
        requireSubject(ownerSub);

        OrderEntity order = find(orderId);

        requireRestaurantOwner(ownerSub, order);

        return order;
    }

    @Transactional(readOnly = true)
    public List<OrderEntity> listForRestaurant(String ownerSub, String restaurantId) {
        requireSubject(ownerSub);

        if (restaurantId == null || restaurantId.isBlank()) {
            throw Status.INVALID_ARGUMENT
                    .withDescription("Restaurant ID is required.")
                    .asRuntimeException();
        }

        return repository
                .findByRestaurantIdAndRestaurantOwnerSubOrderByCreatedAtDesc(
                        restaurantId.trim(),
                        ownerSub.trim());
    }

    @Transactional
    public OrderEntity updateStatus(
            String ownerSub,
            String orderId,
            OrderState targetStatus) {
        requireSubject(ownerSub);

        if (targetStatus != OrderState.ACCEPTED &&
                targetStatus != OrderState.REJECTED) {
            throw Status.INVALID_ARGUMENT
                    .withDescription(
                            "Order can only be accepted or rejected.")
                    .asRuntimeException();
        }

        if (orderId == null || orderId.isBlank()) {
            throw Status.INVALID_ARGUMENT
                    .withDescription("Order ID is required.")
                    .asRuntimeException();
        }

        OrderEntity order = repository
                .findByIdForUpdate(orderId.trim())
                .orElseThrow(() -> Status.NOT_FOUND
                        .withDescription("Order not found.")
                        .asRuntimeException());

        requireRestaurantOwner(ownerSub, order);

        if (order.getStatus() != OrderState.PLACED) {
            throw Status.FAILED_PRECONDITION
                    .withDescription(
                            "Only placed orders can change status.")
                    .asRuntimeException();
        }

        order.setStatus(targetStatus);

        return repository.save(order);
    }

    // ==========================================================
    // Shared helpers
    // ==========================================================
    private OrderEntity find(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw Status.INVALID_ARGUMENT
                    .withDescription("Order ID is required.")
                    .asRuntimeException();
        }

        return repository.findById(orderId.trim())
                .orElseThrow(() -> Status.NOT_FOUND
                        .withDescription("Order not found.")
                        .asRuntimeException());
    }

    private static void requireSubject(String subject) {
        if (subject == null || subject.isBlank()) {
            throw Status.UNAUTHENTICATED
                    .withDescription(
                            "Authenticated subject is required.")
                    .asRuntimeException();
        }
    }

    private static void requireRestaurantOwner(String ownerSub, OrderEntity order) {
        if (!ownerSub.trim().equals(order.getRestaurantOwnerSub())) {
            throw Status.NOT_FOUND
                    .withDescription("Order not found.")
                    .asRuntimeException();
        }
    }

    private static void validateQuote(String requestedRestaurantId, RestaurantQuotationGrpcClient.Quote quote) {
        if (!requestedRestaurantId.equals(quote.restaurantId())) {
            throw Status.INTERNAL
                    .withDescription(
                            "Quotation restaurant mismatch.")
                    .asRuntimeException();
        }

        if (quote.restaurantOwnerSub() == null ||
                quote.restaurantOwnerSub().isBlank() ||
                quote.items().isEmpty() ||
                quote.totalMinorUnits() <= 0) {
            throw Status.INTERNAL
                    .withDescription(
                            "Restaurant quotation is invalid.")
                    .asRuntimeException();
        }
    }

    public record CreateLine(String menuItemId, int quantity) {}
}
