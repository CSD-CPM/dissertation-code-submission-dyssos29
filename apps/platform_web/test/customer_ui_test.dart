import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:platform_web/api/order_api.dart';
import 'package:platform_web/api/restaurant_api.dart';
import 'package:platform_web/customer/basket_controller.dart';
import 'package:platform_web/customer/basket_page.dart';
import 'package:platform_web/customer/order_history_page.dart';
import 'package:platform_web/customer/restaurant_list_page.dart';
import 'package:platform_web/customer/restaurant_menu_page.dart';
import 'package:platform_web/models/basket_item.dart';
import 'package:platform_web/models/customer_order.dart';
import 'package:platform_web/models/restaurant.dart';
import 'package:platform_web/models/restaurant_menu_item.dart';

class FakeRestaurantApi implements RestaurantApi {
  FakeRestaurantApi({this.restaurants = const [], this.menu = const []});

  final List<Restaurant> restaurants;
  final List<RestaurantMenuItem> menu;

  @override
  Future<List<Restaurant>> listRestaurants() async {
    return restaurants;
  }

  @override
  Future<Restaurant> getRestaurant(String restaurantId) async {
    return restaurants.firstWhere(
      (restaurant) => restaurant.id == restaurantId,
    );
  }

  @override
  Future<List<RestaurantMenuItem>> getMenu(String restaurantId) async {
    return menu.where((item) => item.restaurantId == restaurantId).toList();
  }
}

class FakeOrderApi implements OrderApi {
  FakeOrderApi({this.createResult, this.orders = const []});

  final CustomerOrder? createResult;
  final List<CustomerOrder> orders;

  int createCalls = 0;
  String? lastRestaurantId;
  List<BasketItem>? lastItems;

  @override
  Future<CustomerOrder> createOrder({
    required String restaurantId,
    required List<BasketItem> items,
  }) async {
    createCalls += 1;
    lastRestaurantId = restaurantId;
    lastItems = List<BasketItem>.from(items);

    final result = createResult;

    if (result == null) {
      throw StateError('No fake create-order response configured.');
    }

    return result;
  }

  @override
  Future<List<CustomerOrder>> listOrders() async {
    return orders;
  }

  @override
  Future<CustomerOrder> getOrder(String orderId) async {
    return orders.firstWhere((order) => order.id == orderId);
  }
}

const restaurant = Restaurant(
  id: 'restaurant-1',
  name: 'Test Restaurant',
  active: true,
);

const soup = RestaurantMenuItem(
  id: 'soup-1',
  restaurantId: 'restaurant-1',
  name: 'Soup of the Day',
  priceMinorUnits: 750,
  available: true,
);

CustomerOrder testOrder({
  CustomerOrderStatus status = CustomerOrderStatus.placed,
}) {
  return CustomerOrder(
    id: 'order-1',
    customerSub: 'customer-1',
    restaurantId: 'restaurant-1',
    items: const [
      CustomerOrderItem(
        menuItemId: 'soup-1',
        name: 'Soup of the Day',
        quantity: 2,
        unitPriceMinorUnits: 750,
      ),
    ],
    totalMinorUnits: 1500,
    status: status,
    createdAt: DateTime.utc(2026, 10, 6, 17),
  );
}

void main() {
  testWidgets('restaurant catalogue displays available restaurants', (
    tester,
  ) async {
    final restaurantApi = FakeRestaurantApi(restaurants: const [restaurant]);
    final basket = BasketController();

    await tester.pumpWidget(
      MaterialApp(
        home: Scaffold(
          body: RestaurantListPage(
            restaurantApi: restaurantApi,
            basketController: basket,
          ),
        ),
      ),
    );

    await tester.pumpAndSettle();

    expect(find.text('Test Restaurant'), findsOneWidget);
    expect(find.text('View restaurant menu'), findsOneWidget);

    basket.dispose();
  });

  testWidgets('menu displays price and adds an item to the basket', (
    tester,
  ) async {
    final restaurantApi = FakeRestaurantApi(
      restaurants: const [restaurant],
      menu: const [soup],
    );
    final basket = BasketController();

    await tester.pumpWidget(
      MaterialApp(
        home: RestaurantMenuPage(
          restaurant: restaurant,
          restaurantApi: restaurantApi,
          basketController: basket,
        ),
      ),
    );

    await tester.pumpAndSettle();

    expect(find.text('Soup of the Day'), findsOneWidget);
    expect(find.text('€7.50'), findsOneWidget);
    expect(find.text('Add'), findsOneWidget);

    await tester.tap(find.widgetWithText(FilledButton, 'Add'));
    await tester.pump();

    expect(basket.itemCount, 1);
    expect(basket.items.single.menuItem.id, 'soup-1');
    expect(basket.items.single.quantity, 1);

    basket.dispose();
  });

  testWidgets('basket displays quantities and calculated display total', (
    tester,
  ) async {
    final basket = BasketController();

    basket.addItem(restaurant: restaurant, menuItem: soup);
    basket.addItem(restaurant: restaurant, menuItem: soup);

    final orderApi = FakeOrderApi(createResult: testOrder());

    await tester.pumpWidget(
      MaterialApp(
        home: BasketPage(basketController: basket, orderApi: orderApi),
      ),
    );

    expect(find.text('€7.50 × 2 = €15.00'), findsOneWidget);
    expect(find.text('Display total: €15.00'), findsOneWidget);

    await tester.tap(find.byTooltip('Decrease quantity'));
    await tester.pump();

    expect(basket.itemCount, 1);
    expect(find.text('€7.50 × 1 = €7.50'), findsOneWidget);
    expect(find.text('Display total: €7.50'), findsOneWidget);

    basket.dispose();
  });

  testWidgets('successful checkout submits basket and clears it', (
    tester,
  ) async {
    final basket = BasketController();

    basket.addItem(restaurant: restaurant, menuItem: soup);
    basket.addItem(restaurant: restaurant, menuItem: soup);

    final orderApi = FakeOrderApi(createResult: testOrder());

    await tester.pumpWidget(
      MaterialApp(
        home: Builder(
          builder: (context) {
            return Scaffold(
              body: Center(
                child: FilledButton(
                  onPressed: () {
                    Navigator.of(context).push<CustomerOrder>(
                      MaterialPageRoute<CustomerOrder>(
                        builder: (_) => BasketPage(
                          basketController: basket,
                          orderApi: orderApi,
                        ),
                      ),
                    );
                  },
                  child: const Text('Open basket'),
                ),
              ),
            );
          },
        ),
      ),
    );

    await tester.tap(find.text('Open basket'));
    await tester.pumpAndSettle();

    expect(find.text('Display total: €15.00'), findsOneWidget);

    await tester.tap(find.text('Place order'));
    await tester.pumpAndSettle();

    expect(orderApi.createCalls, 1);
    expect(orderApi.lastRestaurantId, 'restaurant-1');
    expect(orderApi.lastItems, hasLength(1));
    expect(orderApi.lastItems!.single.menuItem.id, 'soup-1');
    expect(orderApi.lastItems!.single.quantity, 2);
    expect(basket.isEmpty, isTrue);

    // BasketPage should have returned to the
    // previous route after successful checkout.
    expect(find.text('Open basket'), findsOneWidget);

    basket.dispose();
  });

  testWidgets('order history displays status and authoritative total', (
    tester,
  ) async {
    final orderApi = FakeOrderApi(
      orders: [testOrder(status: CustomerOrderStatus.accepted)],
    );

    await tester.pumpWidget(
      MaterialApp(
        home: Scaffold(body: OrderHistoryPage(orderApi: orderApi)),
      ),
    );

    await tester.pumpAndSettle();

    expect(find.text('Order order-1'), findsOneWidget);
    expect(find.text('Accepted • €15.00'), findsOneWidget);
  });
}
