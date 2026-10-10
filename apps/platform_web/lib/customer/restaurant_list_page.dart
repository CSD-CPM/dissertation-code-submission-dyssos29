import 'package:flutter/material.dart';
import 'package:shared_networking/shared_networking.dart';
import '../api/restaurant_api.dart';
import '../models/restaurant.dart';
import 'basket_controller.dart';
import 'restaurant_menu_page.dart';

class RestaurantListPage extends StatefulWidget {
  const RestaurantListPage({
    required this.restaurantApi,
    required this.basketController,
    super.key,
  });

  final RestaurantApi restaurantApi;
  final BasketController basketController;

  @override
  State<RestaurantListPage> createState() => _RestaurantListPageState();
}

class _RestaurantListPageState extends State<RestaurantListPage> {
  late Future<List<Restaurant>> _restaurantsFuture;

  @override
  void initState() {
    super.initState();
    _loadRestaurants();
  }

  void _loadRestaurants() {
    _restaurantsFuture = widget.restaurantApi.listRestaurants();
  }

  Future<void> _retry() async {
    setState(_loadRestaurants);
  }

  Future<void> _openRestaurant(Restaurant restaurant) async {
    await Navigator.of(context).push(
      MaterialPageRoute<void>(
        builder: (_) => RestaurantMenuPage(
          restaurant: restaurant,
          restaurantApi: widget.restaurantApi,
          basketController: widget.basketController,
        ),
      ),
    );

    if (mounted) {
      setState(() {});
    }
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<Restaurant>>(
      future: _restaurantsFuture,
      builder: (context, snapshot) {
        if (snapshot.connectionState != ConnectionState.done) {
          return const Center(child: CircularProgressIndicator());
        }

        if (snapshot.hasError) {
          final error = snapshot.error;

          final message = error is ApiException
              ? error.message
              : 'Restaurants could not be loaded.';

          return Center(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(message),
                const SizedBox(height: 16),
                FilledButton(onPressed: _retry, child: const Text('Retry')),
              ],
            ),
          );
        }

        final restaurants = snapshot.data ?? const [];

        if (restaurants.isEmpty) {
          return const Center(
            child: Text('No restaurants are currently available.'),
          );
        }

        return ListView.separated(
          padding: const EdgeInsets.all(24),
          itemCount: restaurants.length,
          separatorBuilder: (_, _) => const SizedBox(height: 12),
          itemBuilder: (context, index) {
            final restaurant = restaurants[index];

            return Card(
              child: ListTile(
                title: Text(restaurant.name),
                subtitle: const Text('View restaurant menu'),
                trailing: const Icon(Icons.chevron_right),
                onTap: () => _openRestaurant(restaurant),
              ),
            );
          },
        );
      },
    );
  }
}
