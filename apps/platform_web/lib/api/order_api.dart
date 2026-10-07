import 'dart:convert';
import 'package:shared_networking/shared_networking.dart';
import '../models/basket_item.dart';
import '../models/customer_order.dart';

class OrderApi {
  const OrderApi({required AuthenticatedApiClient apiClient})
    : _apiClient = apiClient; // ignore: prefer_initializing_formals

  final AuthenticatedApiClient _apiClient;

  Future<CustomerOrder> createOrder({
    required String restaurantId,
    required List<BasketItem> items,
  }) async {
    if (restaurantId.trim().isEmpty) {
      throw ArgumentError.value(
        restaurantId,
        'restaurantId',
        'Restaurant ID must not be empty.',
      );
    }

    if (items.isEmpty) {
      throw ArgumentError.value(
        items,
        'items',
        'An order must contain at least one item.',
      );
    }

    final body = jsonEncode({
      'restaurantId': restaurantId,
      'items': items
          .map(
            (item) => {
              'menuItemId': item.menuItem.id,
              'quantity': item.quantity,
            },
          )
          .toList(),
    });

    final response = await _apiClient.post(
      '/api/v1/customer/orders',
      body: body,
    );
    final json = jsonDecode(response.body) as Map<String, dynamic>;

    return CustomerOrder.fromJson(json['order'] as Map<String, dynamic>);
  }

  Future<List<CustomerOrder>> listOrders() async {
    final response = await _apiClient.get('/api/v1/customer/orders');
    final json = jsonDecode(response.body) as Map<String, dynamic>;
    final orders = json['orders'] as List<dynamic>? ?? [];

    return orders
        .map((item) => CustomerOrder.fromJson(item as Map<String, dynamic>))
        .toList();
  }

  Future<CustomerOrder> getOrder(String orderId) async {
    final response = await _apiClient.get('/api/v1/customer/orders/$orderId');
    final json = jsonDecode(response.body) as Map<String, dynamic>;

    return CustomerOrder.fromJson(json['order'] as Map<String, dynamic>);
  }

  Future<List<CustomerOrder>> listRestaurantOrders(String restaurantId) async {
    final response = await _apiClient.get(
      '/api/v1/restaurant/restaurants/$restaurantId/orders',
    );
    final json = jsonDecode(response.body) as Map<String, dynamic>;
    final orders = json['orders'] as List<dynamic>? ?? [];

    return orders
        .map((item) => CustomerOrder.fromJson(item as Map<String, dynamic>))
        .toList();
  }

  Future<CustomerOrder> getRestaurantOrder(String orderId) async {
    final response = await _apiClient.get('/api/v1/restaurant/orders/$orderId');
    final json = jsonDecode(response.body) as Map<String, dynamic>;

    return CustomerOrder.fromJson(json['order'] as Map<String, dynamic>);
  }

  Future<CustomerOrder> updateOrderStatus({
    required String orderId,
    required CustomerOrderStatus status,
  }) async {
    if (status == CustomerOrderStatus.placed) {
      throw ArgumentError.value(
        status,
        'status',
        'PLACED cannot be used as an update target.',
      );
    }

    final response = await _apiClient.patch(
      '/api/v1/restaurant/orders/$orderId/status',
      body: jsonEncode({'status': status.apiValue}),
    );
    final json = jsonDecode(response.body) as Map<String, dynamic>;

    return CustomerOrder.fromJson(json['order'] as Map<String, dynamic>);
  }
}
