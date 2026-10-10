import 'package:flutter/material.dart';
import 'package:shared_auth/shared_auth.dart';
import 'package:shared_models/shared_models.dart';
import 'package:shared_networking/shared_networking.dart';
import '../api/order_api.dart';
import '../api/restaurant_api.dart';
import '../models/restaurant.dart';
import 'restaurant_management_page.dart';

class RestaurantOwnerHomePage extends StatefulWidget {
  const RestaurantOwnerHomePage({
    required this.user,
    required this.authRepository,
    required this.apiClient,
    super.key,
  });

  final AuthenticatedUser user;
  final AuthRepository authRepository;
  final AuthenticatedApiClient apiClient;

  @override
  State<RestaurantOwnerHomePage> createState() =>
      _RestaurantOwnerHomePageState();
}

class _RestaurantOwnerHomePageState extends State<RestaurantOwnerHomePage> {
  late final RestaurantApi _restaurantApi;
  late final OrderApi _orderApi;
  late Future<List<Restaurant>> _restaurantsFuture;

  @override
  void initState() {
    super.initState();

    _restaurantApi = RestaurantApi(apiClient: widget.apiClient);
    _orderApi = OrderApi(apiClient: widget.apiClient);

    _loadRestaurants();
  }

  void _loadRestaurants() {
    _restaurantsFuture = _restaurantApi.listOwnedRestaurants();
  }

  Future<void> _refresh() async {
    setState(_loadRestaurants);
    await _restaurantsFuture;
  }

  Future<String?> _askForName({
    required String title,
    String initialValue = '',
  }) async {
    final controller = TextEditingController(text: initialValue);

    final result = await showDialog<String>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text(title),
          content: TextField(
            controller: controller,
            autofocus: true,
            decoration: const InputDecoration(labelText: 'Restaurant name'),
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('Cancel'),
            ),
            FilledButton(
              onPressed: () {
                final value = controller.text.trim();

                if (value.isNotEmpty) {
                  Navigator.pop(context, value);
                }
              },
              child: const Text('Save'),
            ),
          ],
        );
      },
    );

    controller.dispose();

    return result;
  }

  Future<void> _createRestaurant() async {
    final name = await _askForName(title: 'Create restaurant');

    if (name == null) {
      return;
    }

    await _restaurantApi.createRestaurant(name: name);
    await _refresh();
  }

  Future<void> _editRestaurant(Restaurant restaurant) async {
    final name = await _askForName(
      title: 'Edit restaurant',
      initialValue: restaurant.name,
    );

    if (name == null) {
      return;
    }

    await _restaurantApi.updateRestaurant(
      restaurantId: restaurant.id,
      name: name,
    );

    await _refresh();
  }

  Future<void> _deleteRestaurant(Restaurant restaurant) async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Delete restaurant?'),
        content: Text(
          'Delete ${restaurant.name}? '
          'This operation uses the backend soft-delete behaviour.',
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context, false),
            child: const Text('Cancel'),
          ),
          FilledButton(
            onPressed: () => Navigator.pop(context, true),
            child: const Text('Delete'),
          ),
        ],
      ),
    );

    if (confirmed != true) {
      return;
    }

    await _restaurantApi.deleteRestaurant(restaurant.id);
    await _refresh();
  }

  Future<void> _openRestaurant(Restaurant restaurant) async {
    await Navigator.of(context).push(
      MaterialPageRoute<void>(
        builder: (_) => RestaurantManagementPage(
          restaurant: restaurant,
          restaurantApi: _restaurantApi,
          orderApi: _orderApi,
        ),
      ),
    );

    if (mounted) {
      await _refresh();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Restaurant Dashboard'),
        actions: [
          IconButton(
            tooltip: 'Sign out',
            onPressed: widget.authRepository.signOut,
            icon: const Icon(Icons.logout),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: _createRestaurant,
        icon: const Icon(Icons.add),
        label: const Text('Restaurant'),
      ),
      body: FutureBuilder<List<Restaurant>>(
        future: _restaurantsFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState != ConnectionState.done) {
            return const Center(child: CircularProgressIndicator());
          }

          if (snapshot.hasError) {
            return Center(
              child: Text(
                snapshot.error is ApiException
                    ? (snapshot.error! as ApiException).message
                    : 'Restaurants could not be loaded.',
              ),
            );
          }

          final restaurants = snapshot.data ?? const [];

          if (restaurants.isEmpty) {
            return const Center(
              child: Text('You do not currently own any active restaurants.'),
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
                  subtitle: const Text('Manage restaurant'),
                  onTap: () => _openRestaurant(restaurant),
                  trailing: PopupMenuButton<String>(
                    onSelected: (value) {
                      if (value == 'edit') {
                        _editRestaurant(restaurant);
                      } else if (value == 'delete') {
                        _deleteRestaurant(restaurant);
                      }
                    },
                    itemBuilder: (_) => const [
                      PopupMenuItem(value: 'edit', child: Text('Edit')),
                      PopupMenuItem(value: 'delete', child: Text('Delete')),
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
