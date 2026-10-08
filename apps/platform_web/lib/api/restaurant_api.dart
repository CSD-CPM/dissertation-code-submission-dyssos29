import 'dart:convert';
import 'package:shared_networking/shared_networking.dart';
import '../models/restaurant.dart';
import '../models/restaurant_menu_item.dart';

class RestaurantApi {
  const RestaurantApi({required AuthenticatedApiClient apiClient})
    : _apiClient = apiClient; // ignore: prefer_initializing_formals

  final AuthenticatedApiClient _apiClient;

  // ==========================================================
  // Customer endpoints (for customers)
  // ==========================================================
  Future<List<Restaurant>> listRestaurants() async {
    final response = await _apiClient.get('/api/v1/customer/restaurants');
    final json = jsonDecode(response.body) as Map<String, dynamic>;
    final restaurants = json['restaurants'] as List<dynamic>? ?? [];

    return restaurants
        .map((item) => Restaurant.fromJson(item as Map<String, dynamic>))
        .toList();
  }

  Future<Restaurant> getRestaurant(String restaurantId) async {
    final response = await _apiClient.get(
      '/api/v1/customer/restaurants/$restaurantId',
    );
    final json = jsonDecode(response.body) as Map<String, dynamic>;

    return Restaurant.fromJson(json['restaurant'] as Map<String, dynamic>);
  }

  Future<List<RestaurantMenuItem>> getMenu(String restaurantId) async {
    final response = await _apiClient.get(
      '/api/v1/customer/restaurants/$restaurantId/menu',
    );
    final json = jsonDecode(response.body) as Map<String, dynamic>;
    final items = json['items'] as List<dynamic>? ?? [];

    return items
        .map(
          (item) => RestaurantMenuItem.fromJson(item as Map<String, dynamic>),
        )
        .toList();
  }

  // ==========================================================
  // Owned restaurant endpoints (for restaurant owners)
  // ==========================================================
  Future<List<Restaurant>> listOwnedRestaurants() async {
    final response = await _apiClient.get('/api/v1/restaurant/restaurants');
    final json = jsonDecode(response.body) as Map<String, dynamic>;
    final restaurants = json['restaurants'] as List<dynamic>? ?? [];

    return restaurants
        .map((item) => Restaurant.fromJson(item as Map<String, dynamic>))
        .toList();
  }

  Future<Restaurant> getOwnedRestaurant(String restaurantId) async {
    final response = await _apiClient.get(
      '/api/v1/restaurant/restaurants/$restaurantId',
    );
    final json = jsonDecode(response.body) as Map<String, dynamic>;

    return Restaurant.fromJson(json['restaurant'] as Map<String, dynamic>);
  }

  Future<Restaurant> createRestaurant({required String name}) async {
    final response = await _apiClient.post(
      '/api/v1/restaurant/restaurants',
      body: jsonEncode({'name': name}),
    );
    final json = jsonDecode(response.body) as Map<String, dynamic>;

    return Restaurant.fromJson(json['restaurant'] as Map<String, dynamic>);
  }

  Future<Restaurant> updateRestaurant({
    required String restaurantId,
    required String name,
  }) async {
    final response = await _apiClient.put(
      '/api/v1/restaurant/restaurants/$restaurantId',
      body: jsonEncode({'name': name}),
    );

    final json = jsonDecode(response.body) as Map<String, dynamic>;

    return Restaurant.fromJson(json['restaurant'] as Map<String, dynamic>);
  }

  Future<void> deleteRestaurant(String restaurantId) async {
    await _apiClient.delete('/api/v1/restaurant/restaurants/$restaurantId');
  }

  Future<List<RestaurantMenuItem>> getOwnedMenu(String restaurantId) async {
    final response = await _apiClient.get(
      '/api/v1/restaurant/restaurants/$restaurantId/menu',
    );

    final json = jsonDecode(response.body) as Map<String, dynamic>;
    final items = json['items'] as List<dynamic>? ?? [];

    return items
        .map(
          (item) => RestaurantMenuItem.fromJson(item as Map<String, dynamic>),
        )
        .toList();
  }

  Future<RestaurantMenuItem> createMenuItem({
    required String restaurantId,
    required String name,
    required int priceMinorUnits,
    required bool available,
  }) async {
    final response = await _apiClient.post(
      '/api/v1/restaurant/restaurants/$restaurantId/menu/items',
      body: jsonEncode({
        'name': name,
        'priceMinorUnits': priceMinorUnits.toString(),
        'available': available,
      }),
    );

    final json = jsonDecode(response.body) as Map<String, dynamic>;

    return RestaurantMenuItem.fromJson(json['item'] as Map<String, dynamic>);
  }

  Future<RestaurantMenuItem> updateMenuItem({
    required String restaurantId,
    required String menuItemId,
    required String name,
    required int priceMinorUnits,
    required bool available,
  }) async {
    final response = await _apiClient.put(
      '/api/v1/restaurant/restaurants/$restaurantId/menu/items/$menuItemId',
      body: jsonEncode({
        'name': name,
        'priceMinorUnits': priceMinorUnits.toString(),
        'available': available,
      }),
    );

    final json = jsonDecode(response.body) as Map<String, dynamic>;

    return RestaurantMenuItem.fromJson(json['item'] as Map<String, dynamic>);
  }

  Future<void> deleteMenuItem({
    required String restaurantId,
    required String menuItemId,
  }) async {
    await _apiClient.delete(
      '/api/v1/restaurant/restaurants/$restaurantId/menu/items/$menuItemId',
    );
  }
}
