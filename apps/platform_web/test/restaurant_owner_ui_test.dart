import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:platform_web/api/order_api.dart';
import 'package:platform_web/api/restaurant_api.dart';
import 'package:platform_web/models/customer_order.dart';
import 'package:platform_web/models/restaurant.dart';
import 'package:platform_web/models/restaurant_menu_item.dart';
import 'package:platform_web/restaurant_owner/menu_management_page.dart';
import 'package:platform_web/restaurant_owner/restaurant_orders_page.dart';
import 'package:platform_web/restaurant_owner/restaurant_order_detail_page.dart';

const restaurant = Restaurant(
  id: 'restaurant-1',
  name: 'Test Restaurant',
  active: true,
);

const soup = RestaurantMenuItem(
  id: 'item-1',
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
        menuItemId: 'item-1',
        name: 'Soup of the Day',
        quantity: 2,
        unitPriceMinorUnits: 750,
      ),
    ],
    totalMinorUnits: 1500,
    status: status,
    createdAt: DateTime.utc(2026, 10, 7, 17),
  );
}

class FakeRestaurantApi implements RestaurantApi {
  FakeRestaurantApi({List<RestaurantMenuItem>? menu})
    : _menu = List.of(menu ?? const []);

  final List<RestaurantMenuItem> _menu;

  int createMenuItemCalls = 0;
  int updateMenuItemCalls = 0;
  int deleteMenuItemCalls = 0;

  String? lastName;
  int? lastPriceMinorUnits;
  bool? lastAvailable;

  @override
  Future<List<RestaurantMenuItem>> getOwnedMenu(String restaurantId) async {
    return List.unmodifiable(_menu);
  }

  @override
  Future<RestaurantMenuItem> createMenuItem({
    required String restaurantId,
    required String name,
    required int priceMinorUnits,
    required bool available,
  }) async {
    createMenuItemCalls += 1;
    lastName = name;
    lastPriceMinorUnits = priceMinorUnits;
    lastAvailable = available;

    final item = RestaurantMenuItem(
      id: 'created-item',
      restaurantId: restaurantId,
      name: name,
      priceMinorUnits: priceMinorUnits,
      available: available,
    );

    _menu.add(item);

    return item;
  }

  @override
  Future<RestaurantMenuItem> updateMenuItem({
    required String restaurantId,
    required String menuItemId,
    required String name,
    required int priceMinorUnits,
    required bool available,
  }) async {
    updateMenuItemCalls += 1;
    lastName = name;
    lastPriceMinorUnits = priceMinorUnits;
    lastAvailable = available;

    final index = _menu.indexWhere((item) => item.id == menuItemId);

    final updated = RestaurantMenuItem(
      id: menuItemId,
      restaurantId: restaurantId,
      name: name,
      priceMinorUnits: priceMinorUnits,
      available: available,
    );

    if (index >= 0) {
      _menu[index] = updated;
    }

    return updated;
  }

  @override
  Future<void> deleteMenuItem({
    required String restaurantId,
    required String menuItemId,
  }) async {
    deleteMenuItemCalls += 1;

    _menu.removeWhere((item) => item.id == menuItemId);
  }

  @override
  dynamic noSuchMethod(Invocation invocation) {
    return super.noSuchMethod(invocation);
  }
}

class FakeOrderApi implements OrderApi {
  FakeOrderApi({List<CustomerOrder>? orders})
    : _orders = List.of(orders ?? const []);

  final List<CustomerOrder> _orders;

  int listCalls = 0;
  int updateCalls = 0;
  String? lastOrderId;
  CustomerOrderStatus? lastStatus;

  @override
  Future<List<CustomerOrder>> listRestaurantOrders(String restaurantId) async {
    listCalls += 1;

    return List.unmodifiable(
      _orders.where((order) => order.restaurantId == restaurantId),
    );
  }

  @override
  Future<CustomerOrder> updateOrderStatus({
    required String orderId,
    required CustomerOrderStatus status,
  }) async {
    updateCalls += 1;
    lastOrderId = orderId;
    lastStatus = status;

    final index = _orders.indexWhere((order) => order.id == orderId);

    if (index < 0) {
      throw StateError('Order not found.');
    }

    final current = _orders[index];
    final updated = CustomerOrder(
      id: current.id,
      customerSub: current.customerSub,
      restaurantId: current.restaurantId,
      items: current.items,
      totalMinorUnits: current.totalMinorUnits,
      status: status,
      createdAt: current.createdAt,
    );

    _orders[index] = updated;

    return updated;
  }

  @override
  Future<CustomerOrder> getRestaurantOrder(String orderId) async {
    return _orders.firstWhere((order) => order.id == orderId);
  }

  @override
  dynamic noSuchMethod(Invocation invocation) {
    return super.noSuchMethod(invocation);
  }
}

void main() {
  testWidgets('owner menu renders menu items', (tester) async {
    final api = FakeRestaurantApi(menu: const [soup]);

    await tester.pumpWidget(
      MaterialApp(
        home: Scaffold(
          body: MenuManagementPage(restaurant: restaurant, restaurantApi: api),
        ),
      ),
    );
    await tester.pumpAndSettle();

    expect(find.text('Soup of the Day'), findsOneWidget);
    expect(find.text('Available'), findsOneWidget);
  });

  testWidgets('owner menu renders price as euro value', (tester) async {
    final api = FakeRestaurantApi(menu: const [soup]);

    await tester.pumpWidget(
      MaterialApp(
        home: Scaffold(
          body: MenuManagementPage(restaurant: restaurant, restaurantApi: api),
        ),
      ),
    );

    await tester.pumpAndSettle();

    expect(find.text('€7.50'), findsOneWidget);
  });

  testWidgets('owner can create a menu item through the dialog', (
    tester,
  ) async {
    final api = FakeRestaurantApi();

    await tester.pumpWidget(
      MaterialApp(
        home: Scaffold(
          body: MenuManagementPage(restaurant: restaurant, restaurantApi: api),
        ),
      ),
    );

    await tester.pumpAndSettle();
    await tester.tap(find.text('Menu item'));
    await tester.pumpAndSettle();

    expect(find.text('Create menu item'), findsOneWidget);

    final fields = find.byType(TextField);

    expect(fields, findsNWidgets(2));

    await tester.enterText(fields.at(0), 'New Item');
    await tester.enterText(fields.at(1), '4.25');
    await tester.tap(find.widgetWithText(FilledButton, 'Save'));
    await tester.pumpAndSettle();

    expect(api.createMenuItemCalls, 1);
    expect(api.lastName, 'New Item');
    expect(api.lastPriceMinorUnits, 425);
    expect(api.lastAvailable, isTrue);
    expect(find.text('New Item'), findsOneWidget);
    expect(find.text('€4.25'), findsOneWidget);
  });

  testWidgets('owner can edit a menu item through the dialog', (tester) async {
    final api = FakeRestaurantApi(menu: const [soup]);

    await tester.pumpWidget(
      MaterialApp(
        home: Scaffold(
          body: MenuManagementPage(restaurant: restaurant, restaurantApi: api),
        ),
      ),
    );

    await tester.pumpAndSettle();
    await tester.tap(find.byTooltip('Edit menu item'));
    await tester.pumpAndSettle();

    expect(find.text('Edit menu item'), findsOneWidget);

    final fields = find.byType(TextField);

    expect(fields, findsNWidgets(2));

    await tester.enterText(fields.at(0), 'Updated Soup');
    await tester.enterText(fields.at(1), '8.00');
    await tester.tap(find.widgetWithText(FilledButton, 'Save'));
    await tester.pumpAndSettle();

    expect(api.updateMenuItemCalls, 1);
    expect(api.lastName, 'Updated Soup');
    expect(api.lastPriceMinorUnits, 800);
    expect(find.text('Updated Soup'), findsOneWidget);
    expect(find.text('€8.00'), findsOneWidget);
  });

  testWidgets('PLACED order shows Accept and Reject actions', (tester) async {
    final api = FakeOrderApi(orders: [testOrder()]);

    await tester.pumpWidget(
      MaterialApp(
        home: Scaffold(
          body: RestaurantOrdersPage(restaurant: restaurant, orderApi: api),
        ),
      ),
    );

    await tester.pumpAndSettle();

    expect(find.text('Placed • €15.00'), findsOneWidget);
    expect(find.widgetWithText(FilledButton, 'Accept'), findsOneWidget);
    expect(find.widgetWithText(OutlinedButton, 'Reject'), findsOneWidget);
  });

  testWidgets('ACCEPTED order does not show transition actions', (
    tester,
  ) async {
    final api = FakeOrderApi(
      orders: [testOrder(status: CustomerOrderStatus.accepted)],
    );

    await tester.pumpWidget(
      MaterialApp(
        home: Scaffold(
          body: RestaurantOrdersPage(restaurant: restaurant, orderApi: api),
        ),
      ),
    );

    await tester.pumpAndSettle();

    expect(find.text('Accepted • €15.00'), findsOneWidget);
    expect(find.widgetWithText(FilledButton, 'Accept'), findsNothing);
    expect(find.widgetWithText(OutlinedButton, 'Reject'), findsNothing);
  });

  testWidgets('successful Accept refreshes the persisted status', (
    tester,
  ) async {
    final api = FakeOrderApi(orders: [testOrder()]);

    await tester.pumpWidget(
      MaterialApp(
        home: Scaffold(
          body: RestaurantOrdersPage(restaurant: restaurant, orderApi: api),
        ),
      ),
    );

    await tester.pumpAndSettle();
    await tester.tap(find.widgetWithText(FilledButton, 'Accept'));
    await tester.pumpAndSettle();

    expect(find.text('Accept order?'), findsOneWidget);

    final confirmButton = find.descendant(
      of: find.byType(AlertDialog),
      matching: find.widgetWithText(FilledButton, 'Accept'),
    );

    await tester.tap(confirmButton);
    await tester.pumpAndSettle();

    expect(api.updateCalls, 1);
    expect(api.lastOrderId, 'order-1');
    expect(api.lastStatus, CustomerOrderStatus.accepted);
    expect(find.text('Accepted • €15.00'), findsOneWidget);
    expect(find.widgetWithText(FilledButton, 'Accept'), findsNothing);
    expect(find.widgetWithText(OutlinedButton, 'Reject'), findsNothing);
  });

  testWidgets('successful Reject refreshes the persisted status', (
    tester,
  ) async {
    final api = FakeOrderApi(orders: [testOrder()]);

    await tester.pumpWidget(
      MaterialApp(
        home: Scaffold(
          body: RestaurantOrdersPage(restaurant: restaurant, orderApi: api),
        ),
      ),
    );

    await tester.pumpAndSettle();
    await tester.tap(find.widgetWithText(OutlinedButton, 'Reject'));
    await tester.pumpAndSettle();

    expect(find.text('Reject order?'), findsOneWidget);

    final confirmButton = find.descendant(
      of: find.byType(AlertDialog),
      matching: find.widgetWithText(FilledButton, 'Reject'),
    );

    await tester.tap(confirmButton);
    await tester.pumpAndSettle();

    expect(api.updateCalls, 1);
    expect(api.lastOrderId, 'order-1');
    expect(api.lastStatus, CustomerOrderStatus.rejected);
    expect(find.text('Rejected • €15.00'), findsOneWidget);
    expect(find.widgetWithText(FilledButton, 'Accept'), findsNothing);
    expect(find.widgetWithText(OutlinedButton, 'Reject'), findsNothing);
  });

  testWidgets('restaurant order detail renders the persisted order snapshot', (
    tester,
  ) async {
    final api = FakeOrderApi(orders: [testOrder()]);

    await tester.pumpWidget(
      MaterialApp(
        home: RestaurantOrderDetailPage(orderId: 'order-1', orderApi: api),
      ),
    );

    await tester.pumpAndSettle();

    expect(find.text('Order order-1'), findsOneWidget);
    expect(find.text('Status: Placed'), findsOneWidget);
    expect(find.text('Soup of the Day'), findsOneWidget);
    expect(find.text('€7.50 × 2'), findsOneWidget);
    expect(find.text('Total: €15.00'), findsOneWidget);
  });
}
