import 'dart:convert';
import 'package:flutter_test/flutter_test.dart';
import 'package:platform_web/api/order_api.dart';
import 'package:platform_web/api/restaurant_api.dart';
import 'package:platform_web/models/customer_order.dart';
import 'package:shared_networking/shared_networking.dart';

class FakeAuthenticatedApiClient implements AuthenticatedApiClient {
  ApiResponse? getResponse;
  ApiResponse? postResponse;
  ApiResponse? putResponse;
  ApiResponse? patchResponse;
  ApiResponse? deleteResponse;

  String? lastMethod;
  String? lastPath;
  String? lastBody;

  @override
  Future<ApiResponse> get(String path) async {
    lastMethod = 'GET';
    lastPath = path;
    lastBody = null;

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
  Future<ApiResponse> put(String path, {String? body}) async {
    lastMethod = 'PUT';
    lastPath = path;
    lastBody = body;

    final response = putResponse;

    if (response == null) {
      throw StateError('No fake PUT response configured.');
    }

    return response;
  }

  @override
  Future<ApiResponse> patch(String path, {String? body}) async {
    lastMethod = 'PATCH';
    lastPath = path;
    lastBody = body;

    final response = patchResponse;

    if (response == null) {
      throw StateError('No fake PATCH response configured.');
    }

    return response;
  }

  @override
  Future<ApiResponse> delete(String path) async {
    lastMethod = 'DELETE';
    lastPath = path;
    lastBody = null;

    final response = deleteResponse;

    if (response == null) {
      throw StateError('No fake DELETE response configured.');
    }

    return response;
  }

  @override
  void close() {}
}

const restaurantResponse = '''
{
  "restaurant": {
    "id": "restaurant-1",
    "name": "Test Restaurant",
    "active": true
  }
}
''';

const menuItemResponse = '''
{
  "item": {
    "id": "item-1",
    "restaurantId": "restaurant-1",
    "name": "Soup of the Day",
    "priceMinorUnits": "750",
    "available": true
  }
}
''';

const orderResponse = '''
{
  "order": {
    "id": "order-1",
    "customerSub": "customer-1",
    "restaurantId": "restaurant-1",
    "items": [
      {
        "menuItemId": "item-1",
        "name": "Soup of the Day",
        "quantity": 2,
        "unitPriceMinorUnits": "750"
      }
    ],
    "totalMinorUnits": "1500",
    "status": "ORDER_STATUS_PLACED",
    "createdAt": "2026-10-07T17:00:00Z"
  }
}
''';

void main() {
  group('RestaurantApi owner operations', () {
    test('listOwnedRestaurants uses the restaurant-owner endpoint', () async {
      final client = FakeAuthenticatedApiClient()
        ..getResponse = const ApiResponse(
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
        );

      final api = RestaurantApi(apiClient: client);
      final restaurants = await api.listOwnedRestaurants();

      expect(client.lastMethod, 'GET');
      expect(client.lastPath, '/api/v1/restaurant/restaurants');
      expect(restaurants, hasLength(1));
      expect(restaurants.single.id, 'restaurant-1');
      expect(restaurants.single.name, 'Test Restaurant');
    });

    test('createRestaurant sends only the restaurant name', () async {
      final client = FakeAuthenticatedApiClient()
        ..postResponse = const ApiResponse(
          statusCode: 200,
          body: restaurantResponse,
        );
      final api = RestaurantApi(apiClient: client);

      await api.createRestaurant(name: 'Test Restaurant');

      expect(client.lastMethod, 'POST');
      expect(client.lastPath, '/api/v1/restaurant/restaurants');

      final body = jsonDecode(client.lastBody!) as Map<String, dynamic>;

      expect(body, {'name': 'Test Restaurant'});
      expect(body.containsKey('ownerSub'), isFalse);
      expect(body.containsKey('active'), isFalse);
    });

    test('updateRestaurant sends only the new name', () async {
      final client = FakeAuthenticatedApiClient()
        ..putResponse = const ApiResponse(
          statusCode: 200,
          body: restaurantResponse,
        );
      final api = RestaurantApi(apiClient: client);

      await api.updateRestaurant(
        restaurantId: 'restaurant-1',
        name: 'Updated Restaurant',
      );

      expect(client.lastMethod, 'PUT');
      expect(client.lastPath, '/api/v1/restaurant/restaurants/restaurant-1');

      final body = jsonDecode(client.lastBody!) as Map<String, dynamic>;

      expect(body, {'name': 'Updated Restaurant'});
      expect(body.containsKey('ownerSub'), isFalse);
    });

    test('deleteRestaurant uses DELETE', () async {
      final client = FakeAuthenticatedApiClient()
        ..deleteResponse = const ApiResponse(statusCode: 200, body: '{}');
      final api = RestaurantApi(apiClient: client);

      await api.deleteRestaurant('restaurant-1');

      expect(client.lastMethod, 'DELETE');
      expect(client.lastPath, '/api/v1/restaurant/restaurants/restaurant-1');
      expect(client.lastBody, isNull);
    });

    test('createMenuItem sends name price and availability', () async {
      final client = FakeAuthenticatedApiClient()
        ..postResponse = const ApiResponse(
          statusCode: 200,
          body: menuItemResponse,
        );
      final api = RestaurantApi(apiClient: client);

      await api.createMenuItem(
        restaurantId: 'restaurant-1',
        name: 'Soup of the Day',
        priceMinorUnits: 750,
        available: true,
      );

      expect(client.lastMethod, 'POST');
      expect(
        client.lastPath,
        '/api/v1/restaurant/restaurants/restaurant-1/menu/items',
      );

      final body = jsonDecode(client.lastBody!) as Map<String, dynamic>;

      expect(body, {
        'name': 'Soup of the Day',
        'priceMinorUnits': '750',
        'available': true,
      });
      expect(body.containsKey('restaurantId'), isFalse);
    });

    test('updateMenuItem uses PUT with editable menu fields', () async {
      final client = FakeAuthenticatedApiClient()
        ..putResponse = const ApiResponse(
          statusCode: 200,
          body: menuItemResponse,
        );
      final api = RestaurantApi(apiClient: client);

      await api.updateMenuItem(
        restaurantId: 'restaurant-1',
        menuItemId: 'item-1',
        name: 'Updated Soup',
        priceMinorUnits: 800,
        available: false,
      );

      expect(client.lastMethod, 'PUT');
      expect(
        client.lastPath,
        '/api/v1/restaurant/restaurants/restaurant-1/menu/items/item-1',
      );

      final body = jsonDecode(client.lastBody!) as Map<String, dynamic>;

      expect(body, {
        'name': 'Updated Soup',
        'priceMinorUnits': '800',
        'available': false,
      });
      expect(body.containsKey('restaurantId'), isFalse);
      expect(body.containsKey('id'), isFalse);
    });

    test('deleteMenuItem uses DELETE', () async {
      final client = FakeAuthenticatedApiClient()
        ..deleteResponse = const ApiResponse(statusCode: 200, body: '{}');
      final api = RestaurantApi(apiClient: client);

      await api.deleteMenuItem(
        restaurantId: 'restaurant-1',
        menuItemId: 'item-1',
      );

      expect(client.lastMethod, 'DELETE');
      expect(
        client.lastPath,
        '/api/v1/restaurant/restaurants/restaurant-1/menu/items/item-1',
      );
      expect(client.lastBody, isNull);
    });
  });

  group('OrderApi restaurant-owner operations', () {
    test('listRestaurantOrders uses the restaurant-owner endpoint', () async {
      final client = FakeAuthenticatedApiClient()
        ..getResponse = const ApiResponse(
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
                      "menuItemId": "item-1",
                      "name": "Soup of the Day",
                      "quantity": 2,
                      "unitPriceMinorUnits": "750"
                    }
                  ],
                  "totalMinorUnits": "1500",
                  "status": "ORDER_STATUS_PLACED",
                  "createdAt": "2026-10-07T17:00:00Z"
                }
              ]
            }
            ''',
        );

      final api = OrderApi(apiClient: client);
      final orders = await api.listRestaurantOrders('restaurant-1');

      expect(client.lastMethod, 'GET');
      expect(
        client.lastPath,
        '/api/v1/restaurant/restaurants/restaurant-1/orders',
      );
      expect(orders, hasLength(1));
      expect(orders.single.status, CustomerOrderStatus.placed);
      expect(orders.single.totalMinorUnits, 1500);
    });

    test('accept sends ORDER_STATUS_ACCEPTED', () async {
      final acceptedResponse = orderResponse.replaceFirst(
        'ORDER_STATUS_PLACED',
        'ORDER_STATUS_ACCEPTED',
      );
      final client = FakeAuthenticatedApiClient()
        ..patchResponse = ApiResponse(statusCode: 200, body: acceptedResponse);
      final api = OrderApi(apiClient: client);
      final order = await api.updateOrderStatus(
        orderId: 'order-1',
        status: CustomerOrderStatus.accepted,
      );

      expect(client.lastMethod, 'PATCH');
      expect(client.lastPath, '/api/v1/restaurant/orders/order-1/status');

      final body = jsonDecode(client.lastBody!) as Map<String, dynamic>;

      expect(body, {'status': 'ORDER_STATUS_ACCEPTED'});
      expect(order.status, CustomerOrderStatus.accepted);
    });

    test('reject sends ORDER_STATUS_REJECTED', () async {
      final rejectedResponse = orderResponse.replaceFirst(
        'ORDER_STATUS_PLACED',
        'ORDER_STATUS_REJECTED',
      );
      final client = FakeAuthenticatedApiClient()
        ..patchResponse = ApiResponse(statusCode: 200, body: rejectedResponse);
      final api = OrderApi(apiClient: client);
      final order = await api.updateOrderStatus(
        orderId: 'order-1',
        status: CustomerOrderStatus.rejected,
      );

      expect(client.lastMethod, 'PATCH');
      expect(client.lastPath, '/api/v1/restaurant/orders/order-1/status');

      final body = jsonDecode(client.lastBody!) as Map<String, dynamic>;

      expect(body, {'status': 'ORDER_STATUS_REJECTED'});
      expect(order.status, CustomerOrderStatus.rejected);
    });

    test('PLACED cannot be supplied as an update target', () async {
      final client = FakeAuthenticatedApiClient();
      final api = OrderApi(apiClient: client);

      expect(
        () => api.updateOrderStatus(
          orderId: 'order-1',
          status: CustomerOrderStatus.placed,
        ),
        throwsArgumentError,
      );

      expect(client.lastMethod, isNull);
      expect(client.lastPath, isNull);
      expect(client.lastBody, isNull);
    });

    test('getRestaurantOrder uses the owner detail endpoint', () async {
      final client = FakeAuthenticatedApiClient()
        ..getResponse = const ApiResponse(statusCode: 200, body: orderResponse);
      final api = OrderApi(apiClient: client);
      final order = await api.getRestaurantOrder('order-1');

      expect(client.lastMethod, 'GET');
      expect(client.lastPath, '/api/v1/restaurant/orders/order-1');
      expect(order.id, 'order-1');
      expect(order.status, CustomerOrderStatus.placed);
    });
  });
}
