import 'dart:convert';
import 'package:shared_networking/shared_networking.dart';
import '../models/restaurant.dart';
import '../models/restaurant_menu_item.dart';

class RestaurantApi {
  const RestaurantApi({required AuthenticatedApiClient apiClient})
    : _apiClient = apiClient; // ignore: prefer_initializing_formals

  final AuthenticatedApiClient _apiClient;

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
}
