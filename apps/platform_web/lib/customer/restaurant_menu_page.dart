import 'package:flutter/material.dart';
import 'package:shared_networking/shared_networking.dart';
import '../api/restaurant_api.dart';
import '../models/restaurant.dart';
import '../models/restaurant_menu_item.dart';
import 'basket_controller.dart';
import 'price_formatter.dart';

class RestaurantMenuPage extends StatefulWidget {
  const RestaurantMenuPage({
    required this.restaurant,
    required this.restaurantApi,
    required this.basketController,
    super.key,
  });

  final Restaurant restaurant;
  final RestaurantApi restaurantApi;
  final BasketController basketController;

  @override
  State<RestaurantMenuPage> createState() => _RestaurantMenuPageState();
}

class _RestaurantMenuPageState extends State<RestaurantMenuPage> {
  late Future<List<RestaurantMenuItem>> _menuFuture;

  @override
  void initState() {
    super.initState();
    _loadMenu();
  }

  void _loadMenu() {
    _menuFuture = widget.restaurantApi.getMenu(widget.restaurant.id);
  }

  Future<void> _addItem(RestaurantMenuItem item) async {
    if (!widget.basketController.belongsToRestaurant(widget.restaurant.id)) {
      final replaceBasket = await showDialog<bool>(
        context: context,
        builder: (context) {
          return AlertDialog(
            title: const Text('Replace basket?'),
            content: const Text(
              'Your basket contains items from another restaurant. '
              'Clear it and start a new basket?',
            ),
            actions: [
              TextButton(
                onPressed: () => Navigator.pop(context, false),
                child: const Text('Cancel'),
              ),
              FilledButton(
                onPressed: () => Navigator.pop(context, true),
                child: const Text('Clear basket'),
              ),
            ],
          );
        },
      );

      if (replaceBasket != true) {
        return;
      }

      widget.basketController.clear();
    }

    widget.basketController.addItem(
      restaurant: widget.restaurant,
      menuItem: item,
    );

    if (!mounted) {
      return;
    }

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text('${item.name} added to basket.'),
        duration: const Duration(seconds: 1),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(widget.restaurant.name)),
      body: FutureBuilder<List<RestaurantMenuItem>>(
        future: _menuFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState != ConnectionState.done) {
            return const Center(child: CircularProgressIndicator());
          }

          if (snapshot.hasError) {
            final error = snapshot.error;

            final message = error is ApiException
                ? error.message
                : 'The menu could not be loaded.';

            return Center(child: Text(message));
          }

          final items = snapshot.data ?? const [];

          if (items.isEmpty) {
            return const Center(
              child: Text(
                'This restaurant currently has no available menu items.',
              ),
            );
          }

          return ListView.separated(
            padding: const EdgeInsets.all(24),
            itemCount: items.length,
            separatorBuilder: (_, _) => const SizedBox(height: 12),
            itemBuilder: (context, index) {
              final item = items[index];

              return Card(
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Row(
                    children: [
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              item.name,
                              style: Theme.of(context).textTheme.titleMedium,
                            ),
                            const SizedBox(height: 8),
                            Text(formatPrice(item.priceMinorUnits)),
                          ],
                        ),
                      ),
                      FilledButton(
                        onPressed: item.available ? () => _addItem(item) : null,
                        child: const Text('Add'),
                      ),
                    ],
                  ),
                ),
              );
            },
          );
        },
      ),
    );
  }
}
