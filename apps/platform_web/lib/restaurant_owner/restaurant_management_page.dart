import 'package:flutter/material.dart';
import '../api/order_api.dart';
import '../api/restaurant_api.dart';
import '../models/restaurant.dart';
import 'menu_management_page.dart';
import 'restaurant_orders_page.dart';

class RestaurantManagementPage extends StatefulWidget {
  const RestaurantManagementPage({
    required this.restaurant,
    required this.restaurantApi,
    required this.orderApi,
    super.key,
  });

  final Restaurant restaurant;
  final RestaurantApi restaurantApi;
  final OrderApi orderApi;

  @override
  State<RestaurantManagementPage> createState() =>
      _RestaurantManagementPageState();
}

class _RestaurantManagementPageState extends State<RestaurantManagementPage> {
  int _selectedIndex = 0;

  @override
  Widget build(BuildContext context) {
    final body = switch (_selectedIndex) {
      0 => MenuManagementPage(
        restaurant: widget.restaurant,
        restaurantApi: widget.restaurantApi,
      ),
      1 => RestaurantOrdersPage(
        restaurant: widget.restaurant,
        orderApi: widget.orderApi,
      ),
      _ => throw StateError('Unsupported management index.'),
    };

    return Scaffold(
      appBar: AppBar(title: Text(widget.restaurant.name)),
      body: body,
      bottomNavigationBar: NavigationBar(
        selectedIndex: _selectedIndex,
        onDestinationSelected: (index) {
          setState(() {
            _selectedIndex = index;
          });
        },
        destinations: const [
          NavigationDestination(
            icon: Icon(Icons.restaurant_menu),
            label: 'Menu',
          ),
          NavigationDestination(
            icon: Icon(Icons.receipt_long),
            label: 'Orders',
          ),
        ],
      ),
    );
  }
}
