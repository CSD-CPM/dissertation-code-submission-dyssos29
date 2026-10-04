
package com.fooddelivery.restaurant_menu_service.quotation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import com.fooddelivery.restaurant_menu_service.menu.MenuItemDocument;
import com.fooddelivery.restaurant_menu_service.menu.MenuItemRepository;
import com.fooddelivery.restaurant_menu_service.restaurant.RestaurantCrudService;
import com.fooddelivery.restaurant_menu_service.restaurant.RestaurantDocument;
import io.grpc.Status;

@Service
public class OrderQuotationService {
    private final RestaurantCrudService restaurantService;
    private final MenuItemRepository menuRepository;

    public OrderQuotationService(
            RestaurantCrudService restaurantService,
            MenuItemRepository menuRepository) {
        this.restaurantService = restaurantService;
        this.menuRepository = menuRepository;
    }

    // ==========================================================
    // Internal quotation models
    // ==========================================================
    public record Line(
            String menuItemId,
            int quantity) {}

    public record QuotedLine(
            String menuItemId,
            String name,
            int quantity,
            long unitPriceMinorUnits,
            long lineTotalMinorUnits) {}

    public record Quote(
            String restaurantId,
            String restaurantOwnerSub,
            List<QuotedLine> items,
            long totalMinorUnits) {}

    private record QuotationCalculation(
        List<QuotedLine> items,
        long totalMinorUnits) {}

    // ==========================================================
    // Generate an authoritative quotation
    // ==========================================================
    public Quote quote(String restaurantId, List<Line> lines) {
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

        if (lines.size() > 100) {
            throw Status.INVALID_ARGUMENT
                    .withDescription(
                            "Order must not exceed 100 distinct items.")
                    .asRuntimeException();
        }

        List<Line> normalizedLines = normalizeLines(lines);

        String normalizedRestaurantId = restaurantId.trim();

        // The existing CRUD service ensures that the restaurant
        // exists and has not been soft-deleted.
        RestaurantDocument restaurant = restaurantService.getPublic(normalizedRestaurantId);

        String ownerSub = restaurant.getOwnerSub();

        if (ownerSub == null || ownerSub.isBlank()) {
            throw Status.INTERNAL
                    .withDescription(
                            "Restaurant ownership information is missing.")
                    .asRuntimeException();
        }

        QuotationCalculation calculation = calculateQuotation(normalizedRestaurantId, normalizedLines);

        return new Quote(
                normalizedRestaurantId,
                ownerSub,
                calculation.items(),
                calculation.totalMinorUnits());
    }

    // ==========================================================
    // Shared helpers
    // ==========================================================
    private static List<Line> normalizeLines(List<Line> lines) {
        // Validate the entire request before accessing MongoDB.
        Set<String> seenItemIds = new HashSet<>();
        List<Line> normalizedLines = new ArrayList<>();

        for (Line line : lines) {
            if (line == null ||
                    line.menuItemId() == null ||
                    line.menuItemId().isBlank()) {
                throw Status.INVALID_ARGUMENT
                        .withDescription("Menu item ID is required.")
                        .asRuntimeException();
            }

            if (line.quantity() <= 0) {
                throw Status.INVALID_ARGUMENT
                        .withDescription(
                                "Item quantity must be greater than zero.")
                        .asRuntimeException();
            }

            String itemId = line.menuItemId().trim();

            if (!seenItemIds.add(itemId)) {
                throw Status.INVALID_ARGUMENT
                        .withDescription(
                                "Duplicate menu item IDs are not allowed.")
                        .asRuntimeException();
            }

            normalizedLines.add(new Line(itemId, line.quantity()));
        }

        return normalizedLines;
    }

    private QuotationCalculation calculateQuotation(String restaurantId, List<Line> lines) {
        List<QuotedLine> quotedItems = new ArrayList<>();
        long orderTotal = 0;

        for (Line line : lines) {
            MenuItemDocument item = menuRepository
                    .findByIdAndRestaurantIdAndActiveTrue(
                            line.menuItemId(),
                            restaurantId)
                    .orElseThrow(() -> Status.NOT_FOUND
                            .withDescription(
                                    "Menu item not found for this restaurant.")
                            .asRuntimeException());

            if (!item.isAvailable()) {
                throw Status.FAILED_PRECONDITION
                        .withDescription(
                                "A requested menu item is unavailable.")
                        .asRuntimeException();
            }

            long unitPrice = item.getPriceMinorUnits();

            if (unitPrice <= 0) {
                throw Status.FAILED_PRECONDITION
                        .withDescription(
                                "A requested menu item has an invalid price.")
                        .asRuntimeException();
            }

            long lineTotal;

            try {
                lineTotal = Math.multiplyExact(unitPrice, (long) line.quantity());
                orderTotal = Math.addExact(orderTotal, lineTotal);
            } catch (ArithmeticException exception) {
                throw Status.OUT_OF_RANGE
                        .withDescription(
                                "Calculated order total exceeds the supported range.")
                        .asRuntimeException();
            }

            quotedItems.add(new QuotedLine(
                    item.getId(),
                    item.getName(),
                    line.quantity(),
                    unitPrice,
                    lineTotal));
        }

        return new QuotationCalculation(
                List.copyOf(quotedItems),
                orderTotal);
    }
}
