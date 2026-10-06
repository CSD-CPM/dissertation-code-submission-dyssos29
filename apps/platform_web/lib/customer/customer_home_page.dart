import 'package:flutter/material.dart';
import 'package:shared_auth/shared_auth.dart';
import 'package:shared_models/shared_models.dart';
import 'package:shared_networking/shared_networking.dart';
import '../api/order_api.dart';
import '../api/restaurant_api.dart';
import '../models/customer_order.dart';
import 'basket_controller.dart';
import 'basket_page.dart';
import 'order_detail_page.dart';
import 'order_history_page.dart';
import 'restaurant_list_page.dart';

class CustomerHomePage extends StatefulWidget {
  const CustomerHomePage({
    required this.user,
    required this.authRepository,
    required this.apiClient,
    super.key,
  });

  final AuthenticatedUser user;
  final AuthRepository authRepository;
  final AuthenticatedApiClient apiClient;

  @override
  State<CustomerHomePage> createState() => _CustomerHomePageState();
}

class _CustomerHomePageState extends State<CustomerHomePage> {
  late final RestaurantApi _restaurantApi;
  late final OrderApi _orderApi;
  late final BasketController _basketController;

  int _selectedIndex = 0;

  @override
  void initState() {
    super.initState();

    _restaurantApi = RestaurantApi(apiClient: widget.apiClient);
    _orderApi = OrderApi(apiClient: widget.apiClient);
    _basketController = BasketController();
  }

  @override
  void dispose() {
    _basketController.dispose();
    super.dispose();
  }

  Future<void> _openBasket() async {
    final order = await Navigator.of(context).push<CustomerOrder>(
      MaterialPageRoute<CustomerOrder>(
        builder: (_) => BasketPage(
          basketController: _basketController,
          orderApi: _orderApi,
        ),
      ),
    );

    if (order == null || !mounted) {
      return;
    }

    setState(() {
      _selectedIndex = 1;
    });

    await Navigator.of(context).push(
      MaterialPageRoute<void>(
        builder: (_) => OrderDetailPage(orderId: order.id, orderApi: _orderApi),
      ),
    );
  }

  Future<void> _signOut() {
    return widget.authRepository.signOut();
  }

  @override
  Widget build(BuildContext context) {
    final body = switch (_selectedIndex) {
      0 => RestaurantListPage(
        restaurantApi: _restaurantApi,
        basketController: _basketController,
      ),
      1 => OrderHistoryPage(orderApi: _orderApi),
      _ => throw StateError('Unsupported customer navigation index.'),
    };

    return Scaffold(
      appBar: AppBar(
        title: Text(_selectedIndex == 0 ? 'Restaurants' : 'My Orders'),
        actions: [
          ListenableBuilder(
            listenable: _basketController,
            builder: (context, _) {
              return Badge(
                isLabelVisible: _basketController.itemCount > 0,
                label: Text('${_basketController.itemCount}'),
                child: IconButton(
                  tooltip: 'Basket',
                  onPressed: _openBasket,
                  icon: const Icon(Icons.shopping_basket_outlined),
                ),
              );
            },
          ),
          IconButton(
            tooltip: 'Sign out',
            onPressed: _signOut,
            icon: const Icon(Icons.logout),
          ),
          const SizedBox(width: 8),
        ],
      ),
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
            icon: Icon(Icons.restaurant_outlined),
            selectedIcon: Icon(Icons.restaurant),
            label: 'Restaurants',
          ),
          NavigationDestination(
            icon: Icon(Icons.receipt_long_outlined),
            selectedIcon: Icon(Icons.receipt_long),
            label: 'My Orders',
          ),
        ],
      ),
    );
  }
}
