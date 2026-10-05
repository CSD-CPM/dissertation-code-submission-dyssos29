package com.fooddelivery.order_service.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fooddelivery.order_service.grpc.client.RestaurantQuotationGrpcClient;
import com.fooddelivery.order_service.grpc.client.RestaurantQuotationGrpcClient.Quote;
import com.fooddelivery.order_service.grpc.client.RestaurantQuotationGrpcClient.QuotedLine;
import com.fooddelivery.order_service.grpc.client.RestaurantQuotationGrpcClient.RequestedLine;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository repository;

    @Mock
    private RestaurantQuotationGrpcClient quotationClient;

    private OrderService service() {
        return new OrderService(repository, quotationClient);
    }

    // ==========================================================
    // Helpers
    // ==========================================================
    private Quote validQuote() {
        return new Quote(
                "restaurant-1",
                "owner-1",
                List.of(
                        new QuotedLine(
                                "soup-1",
                                "Soup of the Day",
                                2,
                                750,
                                1500)),
                1500);
    }

    private OrderEntity persistedOrder(String customerSub, String ownerSub) {
        return new OrderEntity(
                customerSub,
                "restaurant-1",
                ownerSub,
                List.of(
                        new OrderItemSnapshot(
                                "soup-1",
                                "Soup of the Day",
                                2,
                                750,
                                1500)),
                1500);
    }

    private void givenSuccessfulQuotation() {
        when(quotationClient.quote(
                "restaurant-1",
                List.of(
                        new RequestedLine(
                                "soup-1",
                                2))))
                .thenReturn(validQuote());

        when(repository.save(any(OrderEntity.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));
    }

    // ==========================================================
    // Order creation
    // ==========================================================
    @Test
    void customerCreationPersistsAuthoritativeQuotation() {
        givenSuccessfulQuotation();

        OrderEntity result = service().create(
                "customer-1",
                "restaurant-1",
                List.of(
                        new OrderService.CreateLine(
                                "soup-1",
                                2)));

        assertEquals(
                "restaurant-1",
                result.getRestaurantId());

        assertEquals(
                "owner-1",
                result.getRestaurantOwnerSub());

        assertEquals(
                1500,
                result.getTotalMinorUnits());

        assertEquals(
                1,
                result.getItems().size());

        OrderItemSnapshot item = result.getItems().get(0);

        assertEquals(
                "soup-1",
                item.getMenuItemId());

        assertEquals(
                "Soup of the Day",
                item.getName());

        assertEquals(
                2,
                item.getQuantity());

        assertEquals(
                750,
                item.getUnitPriceMinorUnits());

        assertEquals(
                1500,
                item.getLineTotalMinorUnits());

        verify(quotationClient).quote(
                "restaurant-1",
                List.of(
                        new RequestedLine(
                                "soup-1",
                                2)));

        verify(repository)
                .save(any(OrderEntity.class));
    }

    @Test
    void newlyCreatedOrderHasPlacedStatus() {
        givenSuccessfulQuotation();

        OrderEntity result = service().create(
                "customer-1",
                "restaurant-1",
                List.of(
                        new OrderService.CreateLine(
                                "soup-1",
                                2)));

        assertEquals(
                OrderState.PLACED,
                result.getStatus());
    }

    @Test
    void customerSubjectComesFromTrustedCaller() {
        givenSuccessfulQuotation();

        OrderEntity result = service().create(
                "trusted-customer-sub",
                "restaurant-1",
                List.of(
                        new OrderService.CreateLine(
                                "soup-1",
                                2)));

        assertEquals(
                "trusted-customer-sub",
                result.getCustomerSub());
    }

    // ==========================================================
    // Customer access
    // ==========================================================
    @Test
    void anotherCustomerCannotRetrieveOrder() {
        OrderEntity order = persistedOrder(
                        "customer-1",
                        "owner-1");

        when(repository.findById(order.getId()))
                .thenReturn(Optional.of(order));

        StatusRuntimeException exception =
                assertThrows(
                        StatusRuntimeException.class,
                        () -> service().getForCustomer(
                                "customer-2",
                                order.getId()));

        assertEquals(
                Status.Code.NOT_FOUND,
                exception.getStatus().getCode());
    }

    // ==========================================================
    // Restaurant-owner access
    // ==========================================================
    @Test
    void restaurantOwnerCanRetrieveOwnOrder() {
        OrderEntity order = persistedOrder(
                        "customer-1",
                        "owner-1");

        when(repository.findById(order.getId()))
                .thenReturn(Optional.of(order));

        OrderEntity result =
                service().getForRestaurant(
                        "owner-1",
                        order.getId());

        assertSame(
                order,
                result);
    }

    @Test
    void anotherRestaurantOwnerCannotRetrieveOrder() {
        OrderEntity order = persistedOrder(
                        "customer-1",
                        "owner-1");

        when(repository.findById(order.getId()))
                .thenReturn(Optional.of(order));

        StatusRuntimeException exception =
                assertThrows(
                        StatusRuntimeException.class,
                        () -> service().getForRestaurant(
                                "owner-2",
                                order.getId()));

        assertEquals(
                Status.Code.NOT_FOUND,
                exception.getStatus().getCode());
    }

    // ==========================================================
    // Valid status transitions
    // ==========================================================
    @Test
    void placedOrderCanBeAccepted() {
        OrderEntity order = persistedOrder(
                        "customer-1",
                        "owner-1");

        when(repository.findByIdForUpdate(
                order.getId()))
                .thenReturn(Optional.of(order));
        when(repository.save(order))
                .thenReturn(order);

        OrderEntity result =
                service().updateStatus(
                        "owner-1",
                        order.getId(),
                        OrderState.ACCEPTED);

        assertEquals(
                OrderState.ACCEPTED,
                result.getStatus());

        verify(repository).save(order);
    }

    @Test
    void placedOrderCanBeRejected() {
        OrderEntity order = persistedOrder(
                        "customer-1",
                        "owner-1");

        when(repository.findByIdForUpdate(
                order.getId()))
                .thenReturn(Optional.of(order));
        when(repository.save(order))
                .thenReturn(order);

        OrderEntity result =
                service().updateStatus(
                        "owner-1",
                        order.getId(),
                        OrderState.REJECTED);

        assertEquals(
                OrderState.REJECTED,
                result.getStatus());

        verify(repository).save(order);
    }

    // ==========================================================
    // Invalid status transitions
    // ==========================================================
    @Test
    void acceptedOrderCannotBeRejected() {
        OrderEntity order = persistedOrder(
                        "customer-1",
                        "owner-1");

        order.setStatus(OrderState.ACCEPTED);

        when(repository.findByIdForUpdate(
                order.getId()))
                .thenReturn(Optional.of(order));

        StatusRuntimeException exception =
                assertThrows(
                        StatusRuntimeException.class,
                        () -> service().updateStatus(
                                "owner-1",
                                order.getId(),
                                OrderState.REJECTED));

        assertEquals(
                Status.Code.FAILED_PRECONDITION,
                exception.getStatus().getCode());

        verify(repository, never())
                .save(any(OrderEntity.class));
    }

    @Test
    void rejectedOrderCannotBeAccepted() {
        OrderEntity order = persistedOrder(
                        "customer-1",
                        "owner-1");

        order.setStatus(OrderState.REJECTED);

        when(repository.findByIdForUpdate(
                order.getId()))
                .thenReturn(Optional.of(order));

        StatusRuntimeException exception =
                assertThrows(
                        StatusRuntimeException.class,
                        () -> service().updateStatus(
                                "owner-1",
                                order.getId(),
                                OrderState.ACCEPTED));

        assertEquals(
                Status.Code.FAILED_PRECONDITION,
                exception.getStatus().getCode());

        verify(repository, never())
                .save(any(OrderEntity.class));
    }

    @Test
    void placedCannotBeSuppliedAsUpdateTarget() {
        StatusRuntimeException exception =
                assertThrows(
                        StatusRuntimeException.class,
                        () -> service().updateStatus(
                                "owner-1",
                                "order-1",
                                OrderState.PLACED));

        assertEquals(
                Status.Code.INVALID_ARGUMENT,
                exception.getStatus().getCode());

        verifyNoInteractions(repository);
    }

    // ==========================================================
    // Quotation integrity
    // ==========================================================
    @Test
    void quotationRestaurantMismatchIsRejected() {
        Quote mismatchedQuote =
                new Quote(
                        "different-restaurant",
                        "owner-1",
                        List.of(
                                new QuotedLine(
                                        "soup-1",
                                        "Soup of the Day",
                                        2,
                                        750,
                                        1500)),
                        1500);

        when(quotationClient.quote(
                "restaurant-1",
                List.of(
                        new RequestedLine(
                                "soup-1",
                                2))))
                .thenReturn(mismatchedQuote);

        StatusRuntimeException exception =
                assertThrows(
                        StatusRuntimeException.class,
                        () -> service().create(
                                "customer-1",
                                "restaurant-1",
                                List.of(
                                        new OrderService.CreateLine(
                                                "soup-1",
                                                2))));

        assertEquals(
                Status.Code.INTERNAL,
                exception.getStatus().getCode());

        verify(repository, never())
                .save(any(OrderEntity.class));
    }
}
