package com.fooddelivery.order_service.grpc;

import java.time.Instant;
import org.springframework.grpc.server.service.GrpcService;
import com.fooddelivery.order.v1.CreateOrderRequest;
import com.fooddelivery.order.v1.CreateOrderResponse;
import com.fooddelivery.order.v1.GetOrderRequest;
import com.fooddelivery.order.v1.GetOrderResponse;
import com.fooddelivery.order.v1.ListCustomerOrdersRequest;
import com.fooddelivery.order.v1.ListCustomerOrdersResponse;
import com.fooddelivery.order.v1.ListRestaurantOrdersRequest;
import com.fooddelivery.order.v1.ListRestaurantOrdersResponse;
import com.fooddelivery.order.v1.OrderItem;
import com.fooddelivery.order.v1.OrderServiceGrpc;
import com.fooddelivery.order.v1.OrderStatus;
import com.fooddelivery.order.v1.UpdateOrderStatusRequest;
import com.fooddelivery.order.v1.UpdateOrderStatusResponse;
import com.fooddelivery.order.v1.Order;
import com.fooddelivery.order_service.auth.Role;
import com.fooddelivery.order_service.auth.RoleGuard;
import com.fooddelivery.order_service.auth.TrustedIdentityInterceptor;
import com.fooddelivery.order_service.order.OrderEntity;
import com.fooddelivery.order_service.order.OrderState;
import com.fooddelivery.order_service.order.OrderService;
import com.google.protobuf.Timestamp;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

@GrpcService
public class OrderGrpcService extends OrderServiceGrpc.OrderServiceImplBase {
    private final OrderService orderService;

    public OrderGrpcService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void createOrder(CreateOrderRequest request, StreamObserver<CreateOrderResponse> observer) {
        respond(observer, () -> {
            RoleGuard.requireRole(Role.CUSTOMER);

            OrderEntity order = orderService.create(
                    authenticatedSubject(),
                    request.getRestaurantId(),
                    request.getItemsList()
                            .stream()
                            .map(item ->
                                    new OrderService.CreateLine(
                                            item.getMenuItemId(),
                                            item.getQuantity()))
                            .toList());

            return CreateOrderResponse.newBuilder()
                    .setOrder(toProto(order))
                    .build();
        });
    }

    @Override
    public void getOrder(GetOrderRequest request, StreamObserver<GetOrderResponse> observer) {
        respond(observer, () -> {
            String role = TrustedIdentityInterceptor.ROLE.get();

            OrderEntity order;

            if (Role.CUSTOMER.name().equals(role)) {
                RoleGuard.requireRole(Role.CUSTOMER);
                order = orderService.getForCustomer(authenticatedSubject(), request.getOrderId());
            } else {
                RoleGuard.requireRole(Role.RESTAURANT_OWNER);
                order = orderService.getForRestaurant(authenticatedSubject(), request.getOrderId());
            }

            return GetOrderResponse.newBuilder()
                    .setOrder(toProto(order))
                    .build();
        });
    }

    @Override
    public void listCustomerOrders(ListCustomerOrdersRequest request, StreamObserver<ListCustomerOrdersResponse> observer) {
        respond(observer, () -> {
            RoleGuard.requireRole(Role.CUSTOMER);

            var orders = orderService.listForCustomer(authenticatedSubject());
            var response = ListCustomerOrdersResponse.newBuilder();

            orders.stream()
                    .map(OrderGrpcService::toProto)
                    .forEach(response::addOrders);

            return response.build();
        });
    }

    @Override
    public void listRestaurantOrders(ListRestaurantOrdersRequest request, StreamObserver<ListRestaurantOrdersResponse> observer) {
        respond(observer, () -> {
            RoleGuard.requireRole(Role.RESTAURANT_OWNER);

            var orders = orderService.listForRestaurant(authenticatedSubject(), request.getRestaurantId());
            var response = ListRestaurantOrdersResponse.newBuilder();

            orders.stream()
                    .map(OrderGrpcService::toProto)
                    .forEach(response::addOrders);

            return response.build();
        });
    }

    @Override
    public void updateOrderStatus(UpdateOrderStatusRequest request, StreamObserver<UpdateOrderStatusResponse> observer) {
        respond(observer, () -> {
            RoleGuard.requireRole(Role.RESTAURANT_OWNER);

            OrderState target = switch (request.getStatus()) {
                case ORDER_STATUS_ACCEPTED ->
                        OrderState.ACCEPTED;
                case ORDER_STATUS_REJECTED ->
                        OrderState.REJECTED;
                default -> throw Status.INVALID_ARGUMENT
                        .withDescription(
                                "Order can only be accepted or rejected.")
                        .asRuntimeException();
            };

            OrderEntity order = orderService.updateStatus(
                    authenticatedSubject(),
                    request.getOrderId(),
                    target);

            return UpdateOrderStatusResponse.newBuilder()
                    .setOrder(toProto(order))
                    .build();
        });
    }

    // ==========================================================
    // Shared helpers
    // ==========================================================
    private static Order toProto(OrderEntity entity) {
        var order = Order.newBuilder()
                        .setId(entity.getId())
                        .setCustomerSub(entity.getCustomerSub())
                        .setRestaurantId(entity.getRestaurantId())
                        .setTotalMinorUnits(entity.getTotalMinorUnits())
                        .setStatus(toProtoStatus(entity.getStatus()))
                        .setCreatedAt(toTimestamp(entity.getCreatedAt()));

        entity.getItems().stream()
                .map(item -> OrderItem.newBuilder()
                        .setMenuItemId(item.getMenuItemId())
                        .setName(item.getName())
                        .setQuantity(item.getQuantity())
                        .setUnitPriceMinorUnits(
                                item.getUnitPriceMinorUnits())
                        .build())
                .forEach(order::addItems);

        return order.build();
    }

    private static OrderStatus toProtoStatus(OrderState status) {
        return switch (status) {
            case PLACED -> OrderStatus.ORDER_STATUS_PLACED;
            case ACCEPTED -> OrderStatus.ORDER_STATUS_ACCEPTED;
            case REJECTED -> OrderStatus.ORDER_STATUS_REJECTED;
        };
    }

    private static Timestamp toTimestamp(Instant instant) {
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }

    private static String authenticatedSubject() {
        String subject = TrustedIdentityInterceptor.SUBJECT.get();

        if (subject == null || subject.isBlank()) {
            throw Status.UNAUTHENTICATED
                    .withDescription(
                            "Authenticated subject is missing.")
                    .asRuntimeException();
        }

        return subject;
    }

    private static <T> void respond(StreamObserver<T> observer, ThrowingSupplier<T> operation) {
        try {
            observer.onNext(operation.get());
            observer.onCompleted();
        } catch (StatusRuntimeException exception) {
            observer.onError(exception);
        } catch (RuntimeException exception) {
            observer.onError(
                    Status.INTERNAL
                            .withDescription(
                                    "Internal service error.")
                            .withCause(exception)
                            .asRuntimeException());
        }
    }

    @FunctionalInterface
    private interface ThrowingSupplier<T> {
        T get();
    }
}
