import 'package:flutter_test/flutter_test.dart';
import 'package:platform_web/customer/basket_controller.dart';
import 'package:platform_web/models/restaurant.dart';
import 'package:platform_web/models/restaurant_menu_item.dart';

void main() {
  const restaurant = Restaurant(
    id: 'restaurant-1',
    name: 'Test Restaurant',
    active: true,
  );

  const otherRestaurant = Restaurant(
    id: 'restaurant-2',
    name: 'Other Restaurant',
    active: true,
  );

  const soup = RestaurantMenuItem(
    id: 'soup-1',
    restaurantId: 'restaurant-1',
    name: 'Soup of the Day',
    priceMinorUnits: 750,
    available: true,
  );

  const otherItem = RestaurantMenuItem(
    id: 'pizza-1',
    restaurantId: 'restaurant-2',
    name: 'Pizza',
    priceMinorUnits: 1200,
    available: true,
  );

  test('adds an item and increments its quantity', () {
    final basket = BasketController();

    basket.addItem(restaurant: restaurant, menuItem: soup);
    basket.addItem(restaurant: restaurant, menuItem: soup);

    expect(basket.items, hasLength(1));
    expect(basket.items.single.quantity, 2);
    expect(basket.itemCount, 2);
    expect(basket.totalMinorUnits, 1500);
    expect(basket.restaurantId, 'restaurant-1');
    expect(basket.restaurantName, 'Test Restaurant');

    basket.dispose();
  });

  test(
    'decrementing the last quantity removes the item and resets restaurant',
    () {
      final basket = BasketController();

      basket.addItem(restaurant: restaurant, menuItem: soup);
      basket.decrement('soup-1');

      expect(basket.isEmpty, isTrue);
      expect(basket.itemCount, 0);
      expect(basket.totalMinorUnits, 0);
      expect(basket.restaurantId, isNull);
      expect(basket.restaurantName, isNull);

      basket.dispose();
    },
  );

  test('clear removes all basket state', () {
    final basket = BasketController();

    basket.addItem(restaurant: restaurant, menuItem: soup);
    basket.clear();

    expect(basket.isEmpty, isTrue);
    expect(basket.restaurantId, isNull);
    expect(basket.restaurantName, isNull);

    basket.dispose();
  });

  test('does not allow items from another restaurant in the same basket', () {
    final basket = BasketController();

    basket.addItem(restaurant: restaurant, menuItem: soup);

    expect(
      () => basket.addItem(restaurant: otherRestaurant, menuItem: otherItem),
      throwsA(isA<StateError>()),
    );

    expect(basket.items, hasLength(1));
    expect(basket.restaurantId, 'restaurant-1');

    basket.dispose();
  });
}
