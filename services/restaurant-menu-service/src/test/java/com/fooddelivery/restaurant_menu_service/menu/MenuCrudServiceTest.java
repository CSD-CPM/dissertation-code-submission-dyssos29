
package com.fooddelivery.restaurant_menu_service.menu;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fooddelivery.restaurant_menu_service.restaurant.RestaurantCrudService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

@ExtendWith(MockitoExtension.class)
class MenuCrudServiceTest {
    @Mock
    private MenuItemRepository repository;

    @Mock
    private RestaurantCrudService restaurantService;

    @InjectMocks
    private MenuCrudService service;

    @Test
    void ownerCanCreateMenuItem() {
        when(repository.save(any(MenuItemDocument.class)))
                .thenAnswer(invocation -> {
                    MenuItemDocument item = invocation.getArgument(0);
                    item.setId("item-1");
                    return item;
                });

        MenuItemDocument result = service.create(
                "owner-1",
                "restaurant-1",
                " Pizza ",
                1250,
                true);

        assertEquals("item-1", result.getId());
        assertEquals("restaurant-1", result.getRestaurantId());
        assertEquals("Pizza", result.getName());
        assertEquals(1250, result.getPriceMinorUnits());
        assertTrue(result.isAvailable());
        assertTrue(result.isActive());

        verify(restaurantService)
                .getOwned("owner-1", "restaurant-1");
    }

    @Test
    void customerMenuUsesAvailableItemsQuery() {
        MenuItemDocument item = new MenuItemDocument(
                "restaurant-1",
                "Pizza",
                1250,
                true);

        when(repository
                .findByRestaurantIdAndActiveTrueAndAvailableTrueOrderByNameAsc(
                        "restaurant-1"))
                .thenReturn(List.of(item));

        List<MenuItemDocument> result = service.listPublic("restaurant-1");

        assertEquals(1, result.size());

        verify(restaurantService).getPublic("restaurant-1");
        verify(repository)
                .findByRestaurantIdAndActiveTrueAndAvailableTrueOrderByNameAsc(
                        "restaurant-1");
    }

    @Test
    void ownerMenuCanIncludeUnavailableItems() {
        MenuItemDocument item = new MenuItemDocument(
                "restaurant-1",
                "Soup",
                550,
                false);

        when(repository
                .findByRestaurantIdAndActiveTrueOrderByNameAsc(
                        "restaurant-1"))
                .thenReturn(List.of(item));

        List<MenuItemDocument> result = service.listOwned("owner-1", "restaurant-1");

        assertEquals(1, result.size());
        assertFalse(result.getFirst().isAvailable());

        verify(restaurantService)
                .getOwned("owner-1", "restaurant-1");
    }

    @Test
    void ownerCanUpdateMenuItem() {
        MenuItemDocument item = new MenuItemDocument(
                "restaurant-1",
                "Old Name",
                900,
                true);
        item.setId("item-1");

        when(repository.findByIdAndRestaurantIdAndActiveTrue(
                "item-1", "restaurant-1"))
                .thenReturn(Optional.of(item));
        when(repository.save(item)).thenReturn(item);

        MenuItemDocument result = service.update(
                "owner-1",
                "restaurant-1",
                "item-1",
                "New Name",
                1400,
                false);

        assertEquals("New Name", result.getName());
        assertEquals(1400, result.getPriceMinorUnits());
        assertFalse(result.isAvailable());

        verify(repository).save(item);
    }

    @Test
    void ownerCanSoftDeleteMenuItem() {
        MenuItemDocument item = new MenuItemDocument(
                "restaurant-1",
                "Pizza",
                1250,
                true);
        item.setId("item-1");

        when(repository.findByIdAndRestaurantIdAndActiveTrue(
                "item-1", "restaurant-1"))
                .thenReturn(Optional.of(item));

        service.delete("owner-1", "restaurant-1", "item-1");

        assertFalse(item.isActive());
        assertFalse(item.isAvailable());

        verify(repository).save(item);
        verify(repository, never()).delete(any(MenuItemDocument.class));
    }

    @Test
    void anotherOwnerCannotCreateMenuItem() {
        when(restaurantService.getOwned("owner-2", "restaurant-1"))
                .thenThrow(Status.PERMISSION_DENIED
                        .asRuntimeException());

        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> service.create(
                        "owner-2",
                        "restaurant-1",
                        "Unauthorized Item",
                        1000,
                        true));

        verify(restaurantService)
                .getOwned("owner-2", "restaurant-1");

        assertEquals(
                Status.Code.PERMISSION_DENIED,
                exception.getStatus().getCode());

        verifyNoInteractions(repository);
    }

    @Test
    void unknownMenuItemReturnsNotFound() {
        when(repository.findByIdAndRestaurantIdAndActiveTrue(
                "missing", "restaurant-1"))
                .thenReturn(Optional.empty());

        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> service.update(
                        "owner-1",
                        "restaurant-1",
                        "missing",
                        "Pizza",
                        1250,
                        true));

        assertEquals(
                Status.Code.NOT_FOUND,
                exception.getStatus().getCode());

        verify(repository, never()).save(any(MenuItemDocument.class));
    }

    @Test
    void zeroPriceIsRejected() {
        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> service.create(
                        "owner-1",
                        "restaurant-1",
                        "Pizza",
                        0,
                        true));

        assertEquals(
                Status.Code.INVALID_ARGUMENT,
                exception.getStatus().getCode());

        verifyNoInteractions(repository, restaurantService);
    }

    @Test
    void blankNameIsRejected() {
        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> service.create(
                        "owner-1",
                        "restaurant-1",
                        "  ",
                        1250,
                        true));

        assertEquals(
                Status.Code.INVALID_ARGUMENT,
                exception.getStatus().getCode());

        verifyNoInteractions(repository, restaurantService);
    }
}
