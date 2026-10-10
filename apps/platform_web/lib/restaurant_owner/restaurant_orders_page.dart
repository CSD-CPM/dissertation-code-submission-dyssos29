import 'package:flutter/material.dart';
import 'package:shared_networking/shared_networking.dart';
import '../api/order_api.dart';
import '../models/customer_order.dart';
import '../models/restaurant.dart';
import '../utils/price_utils.dart';
import 'restaurant_order_detail_page.dart';

class RestaurantOrdersPage extends StatefulWidget {
  const RestaurantOrdersPage({
    required this.restaurant,
    required this.orderApi,
    super.key,
  });

  final Restaurant restaurant;
  final OrderApi orderApi;

  @override
  State<RestaurantOrdersPage> createState() => _RestaurantOrdersPageState();
}

class _RestaurantOrdersPageState extends State<RestaurantOrdersPage> {
  late Future<List<CustomerOrder>> _ordersFuture;

  final Map<String, CustomerOrderStatus> _pendingStatuses = {};

  @override
  void initState() {
    super.initState();
    _loadOrders();
  }

  void _loadOrders() {
    _ordersFuture = widget.orderApi.listRestaurantOrders(widget.restaurant.id);
  }

  Future<void> _refresh() async {
    setState(() {
      _loadOrders();
    });

    await _ordersFuture;
  }

  Future<void> _showError(Object error) async {
    if (!mounted) {
      return;
    }

    final message = error is ApiException
        ? error.message
        : 'The operation could not be completed.';

    await showDialog<void>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Operation failed'),
          content: Text(message),
          actions: [
            FilledButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('OK'),
            ),
          ],
        );
      },
    );
  }

  Future<bool> _confirmStatusChange({
    required CustomerOrderStatus status,
  }) async {
    final action = switch (status) {
      CustomerOrderStatus.accepted => 'accept',
      CustomerOrderStatus.rejected => 'reject',
      CustomerOrderStatus.placed => throw ArgumentError(
        'PLACED cannot be used as an update target.',
      ),
    };

    final confirmed = await showDialog<bool>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text(
            '${action[0].toUpperCase()}${action.substring(1)} order?',
          ),
          content: Text('Are you sure you want to $action this order?'),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context, false),
              child: const Text('Cancel'),
            ),
            FilledButton(
              onPressed: () => Navigator.pop(context, true),
              child: Text(action == 'accept' ? 'Accept' : 'Reject'),
            ),
          ],
        );
      },
    );

    return confirmed == true;
  }

  Future<void> _updateStatus({
    required CustomerOrder order,
    required CustomerOrderStatus status,
  }) async {
    if (_pendingStatuses.containsKey(order.id)) {
      return;
    }

    final confirmed = await _confirmStatusChange(status: status);

    if (!confirmed) {
      return;
    }

    setState(() {
      _pendingStatuses[order.id] = status;
    });

    try {
      await widget.orderApi.updateOrderStatus(
        orderId: order.id,
        status: status,
      );

      await _refresh();

      if (!mounted) {
        return;
      }

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(
            status == CustomerOrderStatus.accepted
                ? 'Order accepted.'
                : 'Order rejected.',
          ),
        ),
      );
    } catch (error) {
      await _showError(error);
    } finally {
      if (mounted) {
        setState(() {
          _pendingStatuses.remove(order.id);
        });
      }
    }
  }

  Future<void> _openOrder(CustomerOrder order) async {
    await Navigator.of(context).push(
      MaterialPageRoute<void>(
        builder: (_) => RestaurantOrderDetailPage(
          orderId: order.id,
          orderApi: widget.orderApi,
        ),
      ),
    );

    if (mounted) {
      await _refresh();
    }
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<CustomerOrder>>(
      future: _ordersFuture,
      builder: (context, snapshot) {
        if (snapshot.connectionState != ConnectionState.done) {
          return const Center(child: CircularProgressIndicator());
        }

        if (snapshot.hasError) {
          final error = snapshot.error;

          final message = error is ApiException
              ? error.message
              : 'Orders could not be loaded.';

          return Center(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(message),
                const SizedBox(height: 16),
                FilledButton(onPressed: _refresh, child: const Text('Retry')),
              ],
            ),
          );
        }

        final orders = snapshot.data ?? const [];

        if (orders.isEmpty) {
          return const Center(
            child: Text('This restaurant does not have any orders yet.'),
          );
        }

        return RefreshIndicator(
          onRefresh: _refresh,
          child: ListView.separated(
            padding: const EdgeInsets.all(24),
            itemCount: orders.length,
            separatorBuilder: (_, _) => const SizedBox(height: 12),
            itemBuilder: (context, index) {
              final order = orders[index];

              return _OrderCard(
                order: order,
                pendingStatus: _pendingStatuses[order.id],
                onOpen: () => _openOrder(order),
                onAccept: () => _updateStatus(
                  order: order,
                  status: CustomerOrderStatus.accepted,
                ),
                onReject: () => _updateStatus(
                  order: order,
                  status: CustomerOrderStatus.rejected,
                ),
              );
            },
          ),
        );
      },
    );
  }
}

class _OrderCard extends StatelessWidget {
  const _OrderCard({
    required this.order,
    required this.pendingStatus,
    required this.onOpen,
    required this.onAccept,
    required this.onReject,
  });

  final CustomerOrder order;
  final CustomerOrderStatus? pendingStatus;
  final VoidCallback onOpen;
  final VoidCallback onAccept;
  final VoidCallback onReject;

  @override
  Widget build(BuildContext context) {
    final canUpdate = order.status == CustomerOrderStatus.placed;
    final isUpdating = pendingStatus != null;

    return Card(
      child: InkWell(
        onTap: isUpdating ? null : onOpen,
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Order ${order.id}',
                style: Theme.of(context).textTheme.titleMedium,
              ),
              const SizedBox(height: 4),
              const Text('Click to view details'),
              const SizedBox(height: 8),
              Text(
                '${order.status.label} • '
                '${formatPrice(order.totalMinorUnits)}',
              ),
              const SizedBox(height: 16),

              for (final item in order.items)
                Padding(
                  padding: const EdgeInsets.only(bottom: 4),
                  child: Text('${item.name} × ${item.quantity}'),
                ),

              if (canUpdate) ...[
                const SizedBox(height: 16),
                const Divider(),
                const SizedBox(height: 8),
                Row(
                  mainAxisAlignment: MainAxisAlignment.end,
                  children: [
                    OutlinedButton(
                      onPressed: isUpdating ? null : onReject,
                      child: pendingStatus == CustomerOrderStatus.rejected
                          ? const SizedBox(
                              width: 18,
                              height: 18,
                              child: CircularProgressIndicator(strokeWidth: 2),
                            )
                          : const Text('Reject'),
                    ),
                    const SizedBox(width: 12),
                    FilledButton(
                      onPressed: isUpdating ? null : onAccept,
                      child: pendingStatus == CustomerOrderStatus.accepted
                          ? const SizedBox(
                              width: 18,
                              height: 18,
                              child: CircularProgressIndicator(strokeWidth: 2),
                            )
                          : const Text('Accept'),
                    ),
                  ],
                ),
              ],
            ],
          ),
        ),
      ),
    );
  }
}
