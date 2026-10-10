
package com.fooddelivery.restaurant_menu_service.quotation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fooddelivery.restaurant_menu_service.menu.MenuItemDocument;
import com.fooddelivery.restaurant_menu_service.menu.MenuItemRepository;
import com.fooddelivery.restaurant_menu_service.restaurant.RestaurantCrudService;
import com.fooddelivery.restaurant_menu_service.restaurant.RestaurantDocument;
import com.fooddelivery.restaurant_menu_service.quotation.OrderQuotationService.Line;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

@ExtendWith(MockitoExtension.class)
class OrderQuotationServiceTest {
    @Mock
    private RestaurantCrudService restaurantService;

    @Mock
    private MenuItemRepository menuRepository;

    @InjectMocks
    private OrderQuotationService service;

    // ==========================================================
    // Test helpers
    // ==========================================================
    private void givenActiveRestaurant() {
        RestaurantDocument restaurant = new RestaurantDocument("owner-1", "Test Restaurant");
        restaurant.setId("restaurant-1");

        when(restaurantService.getPublic("restaurant-1"))
                .thenReturn(restaurant);
    }

    private MenuItemDocument item(
            String id,
            String name,
            long price,
            boolean available) {
        MenuItemDocument item = new MenuItemDocument(
                "restaurant-1",
                name,
                price,
                available);
        item.setId(id);

        return item;
    }

    // ==========================================================
    // Successful quotation
    // ==========================================================
    @Test
    void calculatesAuthoritativePricesAndTotal() {
        givenActiveRestaurant();

        when(menuRepository.findByIdAndRestaurantIdAndActiveTrue(
                "pizza", "restaurant-1"))
                .thenReturn(Optional.of(
                        item("pizza", "Pizza", 1250, true)));
        when(menuRepository.findByIdAndRestaurantIdAndActiveTrue(
                "soup", "restaurant-1"))
                .thenReturn(Optional.of(
                        item("soup", "Soup", 750, true)));

        OrderQuotationService.Quote quote = service.quote(
                "restaurant-1",
                List.of(
                        new Line("pizza", 2),
                        new Line("soup", 3)));

        assertEquals("restaurant-1", quote.restaurantId());
        assertEquals("owner-1", quote.restaurantOwnerSub());
        assertEquals(2, quote.items().size());
        assertEquals(1250, quote.items().get(0).unitPriceMinorUnits());
        assertEquals(2500, quote.items().get(0).lineTotalMinorUnits());
        assertEquals(750, quote.items().get(1).unitPriceMinorUnits());
        assertEquals(2250, quote.items().get(1).lineTotalMinorUnits());
        assertEquals(4750, quote.totalMinorUnits());
    }

    // ==========================================================
    // Request validation
    // ==========================================================
    @Test
    void emptyOrderIsRejected() {
        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> service.quote("restaurant-1", List.of()));

        assertEquals(
                Status.Code.INVALID_ARGUMENT,
                exception.getStatus().getCode());

        verifyNoInteractions(restaurantService, menuRepository);
    }

    @Test
    void zeroQuantityIsRejected() {
        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> service.quote(
                        "restaurant-1",
                        List.of(new Line("pizza", 0))));

        assertEquals(
                Status.Code.INVALID_ARGUMENT,
                exception.getStatus().getCode());

        verifyNoInteractions(restaurantService, menuRepository);
    }

    @Test
    void duplicateMenuItemsAreRejected() {
        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> service.quote(
                        "restaurant-1",
                        List.of(
                                new Line("pizza", 1),
                                new Line("pizza", 2))));

        assertEquals(
                Status.Code.INVALID_ARGUMENT,
                exception.getStatus().getCode());

        verifyNoInteractions(restaurantService, menuRepository);
    }

    // ==========================================================
    // Restaurant and menu availability
    // ==========================================================
    @Test
    void unknownOrInactiveRestaurantIsRejected() {
        when(restaurantService.getPublic("restaurant-1"))
                .thenThrow(Status.NOT_FOUND.asRuntimeException());

        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> service.quote(
                        "restaurant-1",
                        List.of(new Line("pizza", 1))));

        assertEquals(
                Status.Code.NOT_FOUND,
                exception.getStatus().getCode());

        verifyNoInteractions(menuRepository);
    }

    @Test
    void unavailableMenuItemIsRejected() {
        givenActiveRestaurant();

        when(menuRepository.findByIdAndRestaurantIdAndActiveTrue(
                "pizza", "restaurant-1"))
                .thenReturn(Optional.of(
                        item("pizza", "Pizza", 1250, false)));

        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> service.quote(
                        "restaurant-1",
                        List.of(new Line("pizza", 1))));

        assertEquals(
                Status.Code.FAILED_PRECONDITION,
                exception.getStatus().getCode());
    }

    @Test
    void deletedOrUnknownMenuItemIsRejected() {
        givenActiveRestaurant();

        when(menuRepository.findByIdAndRestaurantIdAndActiveTrue(
                "missing", "restaurant-1"))
                .thenReturn(Optional.empty());

        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> service.quote(
                        "restaurant-1",
                        List.of(new Line("missing", 1))));

        assertEquals(
                Status.Code.NOT_FOUND,
                exception.getStatus().getCode());
    }

    // ==========================================================
    // Monetary validation
    // ==========================================================
    @Test
    void invalidStoredPriceIsRejected() {
        givenActiveRestaurant();

        when(menuRepository.findByIdAndRestaurantIdAndActiveTrue(
                "pizza", "restaurant-1"))
                .thenReturn(Optional.of(
                        item("pizza", "Pizza", 0, true)));

        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> service.quote(
                        "restaurant-1",
                        List.of(new Line("pizza", 1))));

        assertEquals(
                Status.Code.FAILED_PRECONDITION,
                exception.getStatus().getCode());
    }

    @Test
    void arithmeticOverflowIsRejected() {
        givenActiveRestaurant();

        when(menuRepository.findByIdAndRestaurantIdAndActiveTrue(
                "pizza", "restaurant-1"))
                .thenReturn(Optional.of(
                        item("pizza", "Pizza", Long.MAX_VALUE, true)));

        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> service.quote(
                        "restaurant-1",
                        List.of(new Line("pizza", 2))));

        assertEquals(
                Status.Code.OUT_OF_RANGE,
                exception.getStatus().getCode());
    }
}
