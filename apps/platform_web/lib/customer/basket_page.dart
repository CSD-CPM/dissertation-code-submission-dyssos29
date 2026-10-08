import 'package:flutter/material.dart';
import 'package:shared_networking/shared_networking.dart';
import '../api/order_api.dart';
import '../models/customer_order.dart';
import 'basket_controller.dart';
import '../utils/price_utils.dart';

class BasketPage extends StatefulWidget {
  const BasketPage({
    required this.basketController,
    required this.orderApi,
    super.key,
  });

  final BasketController basketController;
  final OrderApi orderApi;

  @override
  State<BasketPage> createState() => _BasketPageState();
}

class _BasketPageState extends State<BasketPage> {
  bool _isSubmitting = false;
  String? _error;

  Future<void> _placeOrder() async {
    if (_isSubmitting || widget.basketController.isEmpty) {
      return;
    }

    final restaurantId = widget.basketController.restaurantId;

    if (restaurantId == null) {
      return;
    }

    setState(() {
      _isSubmitting = true;
      _error = null;
    });

    try {
      final CustomerOrder order = await widget.orderApi.createOrder(
        restaurantId: restaurantId,
        items: widget.basketController.items,
      );

      widget.basketController.clear();

      if (!mounted) {
        return;
      }

      Navigator.of(context).pop(order);
    } on ApiException catch (error) {
      if (!mounted) {
        return;
      }

      setState(() {
        _error = error.statusCode == null
            ? error.message
            : '${error.statusCode}: ${error.message}';
      });
    } catch (_) {
      if (!mounted) {
        return;
      }

      setState(() {
        _error = 'The order could not be created.';
      });
    } finally {
      if (mounted) {
        setState(() {
          _isSubmitting = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Basket')),
      body: ListenableBuilder(
        listenable: widget.basketController,
        builder: (context, _) {
          final basket = widget.basketController;

          if (basket.isEmpty) {
            return const Center(child: Text('Your basket is empty.'));
          }

          return Center(
            child: ConstrainedBox(
              constraints: const BoxConstraints(maxWidth: 800),
              child: ListView(
                padding: const EdgeInsets.all(24),
                children: [
                  if (basket.restaurantName != null)
                    Text(
                      basket.restaurantName!,
                      style: Theme.of(context).textTheme.headlineSmall,
                    ),
                  const SizedBox(height: 16),
                  for (final item in basket.items)
                    Card(
                      child: ListTile(
                        title: Text(item.menuItem.name),
                        subtitle: Text(
                          '${formatPrice(item.menuItem.priceMinorUnits)}'
                          ' × ${item.quantity}'
                          ' = ${formatPrice(item.lineTotalMinorUnits)}',
                        ),
                        leading: IconButton(
                          tooltip: 'Decrease quantity',
                          onPressed: () => basket.decrement(item.menuItem.id),
                          icon: const Icon(Icons.remove),
                        ),
                        trailing: IconButton(
                          tooltip: 'Increase quantity',
                          onPressed: () => basket.increment(item.menuItem.id),
                          icon: const Icon(Icons.add),
                        ),
                      ),
                    ),
                  const SizedBox(height: 24),
                  Text(
                    'Display total: '
                    '${formatPrice(basket.totalMinorUnits)}',
                    style: Theme.of(context).textTheme.titleLarge,
                  ),
                  const SizedBox(height: 8),
                  const Text(
                    'The final total is recalculated by the server '
                    'using the current authoritative menu prices.',
                  ),
                  if (_error != null) ...[
                    const SizedBox(height: 16),
                    Text(
                      _error!,
                      style: TextStyle(
                        color: Theme.of(context).colorScheme.error,
                      ),
                    ),
                  ],
                  const SizedBox(height: 24),
                  FilledButton(
                    onPressed: _isSubmitting ? null : _placeOrder,
                    child: _isSubmitting
                        ? const SizedBox(
                            width: 20,
                            height: 20,
                            child: CircularProgressIndicator(strokeWidth: 2),
                          )
                        : const Text('Place order'),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}
