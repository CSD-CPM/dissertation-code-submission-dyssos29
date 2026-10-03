
package com.fooddelivery.restaurant_menu_service.restaurant;

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
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

@ExtendWith(MockitoExtension.class)
class RestaurantCrudServiceTest {
    @Mock
    private RestaurantRepository repository;

    @InjectMocks
    private RestaurantCrudService service;

    @Test
    void createAssignsAuthenticatedOwnerAndActiveStatus() {
        when(repository.save(any(RestaurantDocument.class)))
                .thenAnswer(invocation -> {
                    RestaurantDocument document = invocation.getArgument(0);
                    document.setId("restaurant-1");
                    return document;
                });

        RestaurantDocument result = service.create("owner-1", " Test Restaurant ");

        assertEquals("restaurant-1", result.getId());
        assertEquals("owner-1", result.getOwnerSub());
        assertEquals("Test Restaurant", result.getName());
        assertTrue(result.isActive());
    }

    @Test
    void publicListUsesActiveRestaurantQuery() {
        RestaurantDocument restaurant = new RestaurantDocument("owner-1", "Test Restaurant");

        when(repository.findByActiveTrueOrderByNameAsc())
                .thenReturn(List.of(restaurant));

        List<RestaurantDocument> result = service.listPublic();

        assertEquals(1, result.size());
        assertTrue(result.getFirst().isActive());

        verify(repository).findByActiveTrueOrderByNameAsc();
    }

    @Test
    void ownerCanUpdateOwnRestaurant() {
        RestaurantDocument restaurant = new RestaurantDocument("owner-1", "Old Name");
        restaurant.setId("restaurant-1");

        when(repository.findById("restaurant-1"))
                .thenReturn(Optional.of(restaurant));
        when(repository.save(restaurant))
                .thenReturn(restaurant);

        RestaurantDocument result = service.update(
                "owner-1",
                "restaurant-1",
                "New Name");

        assertEquals("New Name", result.getName());

        verify(repository).save(restaurant);
    }

    @Test
    void differentOwnerCannotUpdateRestaurant() {
        RestaurantDocument restaurant = new RestaurantDocument("owner-2", "Test Restaurant");
        restaurant.setId("restaurant-1");

        when(repository.findById("restaurant-1"))
                .thenReturn(Optional.of(restaurant));

        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> service.update(
                        "owner-1",
                        "restaurant-1",
                        "Unauthorized Change"));

        assertEquals(Status.Code.PERMISSION_DENIED, exception.getStatus().getCode());

        verify(repository, never()).save(any(RestaurantDocument.class));
    }

    @Test
    void deleteMarksRestaurantInactive() {
        RestaurantDocument restaurant = new RestaurantDocument("owner-1", "Test Restaurant");
        restaurant.setId("restaurant-1");

        when(repository.findById("restaurant-1"))
                .thenReturn(Optional.of(restaurant));

        service.delete("owner-1", "restaurant-1");

        assertFalse(restaurant.isActive());

        verify(repository).save(restaurant);
        verify(repository, never()).delete(any(RestaurantDocument.class));
    }

    @Test
    void unknownRestaurantReturnsNotFound() {
        when(repository.findById("missing"))
                .thenReturn(Optional.empty());

        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> service.getPublic("missing"));

        assertEquals(Status.Code.NOT_FOUND, exception.getStatus().getCode());
    }

    @Test
    void blankRestaurantNameIsRejected() {
        StatusRuntimeException exception = assertThrows(
                StatusRuntimeException.class,
                () -> service.create("owner-1", "  "));

        assertEquals(Status.Code.INVALID_ARGUMENT, exception.getStatus().getCode());

        verifyNoInteractions(repository);
    }
}
