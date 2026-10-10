import 'dart:convert';

import 'package:flutter_test/flutter_test.dart';
import 'package:platform_web/api/order_api.dart';
import 'package:platform_web/api/restaurant_api.dart';
import 'package:platform_web/models/basket_item.dart';
import 'package:platform_web/models/customer_order.dart';
import 'package:platform_web/models/restaurant_menu_item.dart';
import 'package:shared_networking/shared_networking.dart';

class FakeAuthenticatedApiClient implements AuthenticatedApiClient {
  FakeAuthenticatedApiClient({this.getResponse, this.postResponse});

  ApiResponse? getResponse;
  ApiResponse? postResponse;

  String? lastMethod;
  String? lastPath;
  String? lastBody;

  @override
  Future<ApiResponse> get(String path) async {
    lastMethod = 'GET';
    lastPath = path;

    final response = getResponse;

    if (response == null) {
      throw StateError('No fake GET response configured.');
    }

    return response;
  }

  @override
  Future<ApiResponse> post(String path, {String? body}) async {
    lastMethod = 'POST';
    lastPath = path;
    lastBody = body;

    final response = postResponse;

    if (response == null) {
      throw StateError('No fake POST response configured.');
    }

    return response;
  }

  @override
  Future<ApiResponse> put(String path, {String? body}) {
    throw UnsupportedError('PUT is not used by these tests.');
  }

  @override
  Future<ApiResponse> patch(String path, {String? body}) {
    throw UnsupportedError('PATCH is not used by these tests.');
  }

  @override
  Future<ApiResponse> delete(String path) {
    throw UnsupportedError('DELETE is not used by these tests.');
  }

  @override
  void close() {}
}

void main() {
  group('RestaurantApi', () {
    test('parses the customer restaurant list', () async {
      final client = FakeAuthenticatedApiClient(
        getResponse: const ApiResponse(
          statusCode: 200,
          body: '''
            {
              "restaurants": [
                {
                  "id": "restaurant-1",
                  "name": "Test Restaurant",
                  "active": true
                }
              ]
            }
            ''',
        ),
      );

      final api = RestaurantApi(apiClient: client);
      final restaurants = await api.listRestaurants();

      expect(client.lastPath, '/api/v1/customer/restaurants');
      expect(restaurants, hasLength(1));
      expect(restaurants.first.id, 'restaurant-1');
      expect(restaurants.first.name, 'Test Restaurant');
      expect(restaurants.first.active, isTrue);
    });

    test('parses restaurant menu prices', () async {
      final client = FakeAuthenticatedApiClient(
        getResponse: const ApiResponse(
          statusCode: 200,
          body: '''
            {
              "items": [
                {
                  "id": "soup-1",
                  "restaurantId": "restaurant-1",
                  "name": "Soup of the Day",
                  "priceMinorUnits": "750",
                  "available": true
                }
              ]
            }
            ''',
        ),
      );

      final api = RestaurantApi(apiClient: client);
      final items = await api.getMenu('restaurant-1');

      expect(client.lastPath, '/api/v1/customer/restaurants/restaurant-1/menu');
      expect(items, hasLength(1));
      expect(items.first.id, 'soup-1');
      expect(items.first.name, 'Soup of the Day');
      expect(items.first.priceMinorUnits, 750);
      expect(items.first.available, isTrue);
    });
  });

  group('OrderApi', () {
    test('creates an order using only item IDs and quantities', () async {
      final client = FakeAuthenticatedApiClient(
        postResponse: const ApiResponse(
          statusCode: 200,
          body: '''
            {
              "order": {
                "id": "order-1",
                "customerSub": "customer-1",
                "restaurantId": "restaurant-1",
                "items": [
                  {
                    "menuItemId": "soup-1",
                    "name": "Soup of the Day",
                    "quantity": 2,
                    "unitPriceMinorUnits": "750"
                  }
                ],
                "totalMinorUnits": "1500",
                "status": "ORDER_STATUS_PLACED",
                "createdAt": "2026-10-06T17:00:00Z"
              }
            }
            ''',
        ),
      );

      final api = OrderApi(apiClient: client);

      const menuItem = RestaurantMenuItem(
        id: 'soup-1',
        restaurantId: 'restaurant-1',
        name: 'Soup of the Day',
        priceMinorUnits: 750,
        available: true,
      );

      final order = await api.createOrder(
        restaurantId: 'restaurant-1',
        items: const [BasketItem(menuItem: menuItem, quantity: 2)],
      );

      expect(client.lastMethod, 'POST');
      expect(client.lastPath, '/api/v1/customer/orders');
      final requestJson = jsonDecode(client.lastBody!) as Map<String, dynamic>;
      expect(requestJson['restaurantId'], 'restaurant-1');
      final items = requestJson['items'] as List<dynamic>;
      expect(items, hasLength(1));
      final item = items.single as Map<String, dynamic>;
      expect(item, {'menuItemId': 'soup-1', 'quantity': 2});
      expect(requestJson.containsKey('totalMinorUnits'), isFalse);
      expect(requestJson.containsKey('customerSub'), isFalse);
      expect(requestJson.containsKey('status'), isFalse);
      expect(order.id, 'order-1');
      expect(order.totalMinorUnits, 1500);
      expect(order.status, CustomerOrderStatus.placed);
    });

    test('rejects checkout with an empty basket', () async {
      final client = FakeAuthenticatedApiClient();
      final api = OrderApi(apiClient: client);

      expect(
        () => api.createOrder(restaurantId: 'restaurant-1', items: const []),
        throwsArgumentError,
      );
    });

    test('parses customer order history', () async {
      final client = FakeAuthenticatedApiClient(
        getResponse: const ApiResponse(
          statusCode: 200,
          body: '''
            {
              "orders": [
                {
                  "id": "order-1",
                  "customerSub": "customer-1",
                  "restaurantId": "restaurant-1",
                  "items": [
                    {
                      "menuItemId": "soup-1",
                      "name": "Soup of the Day",
                      "quantity": 2,
                      "unitPriceMinorUnits": "750"
                    }
                  ],
                  "totalMinorUnits": "1500",
                  "status": "ORDER_STATUS_ACCEPTED",
                  "createdAt": "2026-10-06T17:00:00Z"
                }
              ]
            }
            ''',
        ),
      );

      final api = OrderApi(apiClient: client);
      final orders = await api.listOrders();

      expect(client.lastPath, '/api/v1/customer/orders');
      expect(orders, hasLength(1));
      expect(orders.first.status, CustomerOrderStatus.accepted);
      expect(orders.first.totalMinorUnits, 1500);
      expect(orders.first.items.first.lineTotalMinorUnits, 1500);
    });
  });
}
