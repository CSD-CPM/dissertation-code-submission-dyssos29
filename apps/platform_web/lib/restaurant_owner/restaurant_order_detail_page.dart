import 'package:flutter/material.dart';
import 'package:shared_networking/shared_networking.dart';
import '../api/order_api.dart';
import '../models/customer_order.dart';
import '../utils/price_utils.dart';

class RestaurantOrderDetailPage extends StatefulWidget {
  const RestaurantOrderDetailPage({
    required this.orderId,
    required this.orderApi,
    super.key,
  });

  final String orderId;
  final OrderApi orderApi;

  @override
  State<RestaurantOrderDetailPage> createState() =>
      _RestaurantOrderDetailPageState();
}

class _RestaurantOrderDetailPageState extends State<RestaurantOrderDetailPage> {
  late Future<CustomerOrder> _orderFuture;
  CustomerOrderStatus? _pendingStatus;

  @override
  void initState() {
    super.initState();
    _loadOrder();
  }

  void _loadOrder() {
    _orderFuture = widget.orderApi.getRestaurantOrder(widget.orderId);
  }

  Future<void> _refresh() async {
    setState(_loadOrder);
    await _orderFuture;
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

  Future<bool> _confirmStatusChange(CustomerOrderStatus status) async {
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

  Future<void> _updateStatus(CustomerOrderStatus status) async {
    if (_pendingStatus != null) {
      return;
    }

    final confirmed = await _confirmStatusChange(status);

    if (!confirmed) {
      return;
    }

    setState(() {
      _pendingStatus = status;
    });

    try {
      await widget.orderApi.updateOrderStatus(
        orderId: widget.orderId,
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
          _pendingStatus = null;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Order details')),
      body: FutureBuilder<CustomerOrder>(
        future: _orderFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState != ConnectionState.done) {
            return const Center(child: CircularProgressIndicator());
          }

          if (snapshot.hasError) {
            final error = snapshot.error;

            final message = error is ApiException
                ? error.message
                : 'The order could not be loaded.';

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

          final order = snapshot.data!;

          final canUpdate = order.status == CustomerOrderStatus.placed;

          return Center(
            child: ConstrainedBox(
              constraints: const BoxConstraints(maxWidth: 800),
              child: ListView(
                padding: const EdgeInsets.all(24),
                children: [
                  Text(
                    'Order ${order.id}',
                    style: Theme.of(context).textTheme.headlineSmall,
                  ),
                  const SizedBox(height: 16),

                  Text('Status: ${order.status.label}'),
                  const SizedBox(height: 8),

                  Text('Created: ${order.createdAt.toLocal()}'),

                  const SizedBox(height: 24),
                  const Divider(),
                  const SizedBox(height: 8),

                  Text('Items', style: Theme.of(context).textTheme.titleMedium),
                  const SizedBox(height: 8),

                  for (final item in order.items)
                    ListTile(
                      contentPadding: EdgeInsets.zero,
                      title: Text(item.name),
                      subtitle: Text(
                        '${formatPrice(item.unitPriceMinorUnits)}'
                        ' × ${item.quantity}',
                      ),
                      trailing: Text(formatPrice(item.lineTotalMinorUnits)),
                    ),

                  const Divider(),
                  const SizedBox(height: 8),

                  Align(
                    alignment: Alignment.centerRight,
                    child: Text(
                      'Total: '
                      '${formatPrice(order.totalMinorUnits)}',
                      style: Theme.of(context).textTheme.titleLarge,
                    ),
                  ),

                  if (canUpdate) ...[
                    const SizedBox(height: 32),

                    Row(
                      mainAxisAlignment: MainAxisAlignment.end,
                      children: [
                        OutlinedButton(
                          onPressed: _pendingStatus != null
                              ? null
                              : () =>
                                    _updateStatus(CustomerOrderStatus.rejected),
                          child: _pendingStatus == CustomerOrderStatus.rejected
                              ? const SizedBox(
                                  width: 18,
                                  height: 18,
                                  child: CircularProgressIndicator(
                                    strokeWidth: 2,
                                  ),
                                )
                              : const Text('Reject'),
                        ),

                        const SizedBox(width: 12),

                        FilledButton(
                          onPressed: _pendingStatus != null
                              ? null
                              : () =>
                                    _updateStatus(CustomerOrderStatus.accepted),
                          child: _pendingStatus == CustomerOrderStatus.accepted
                              ? const SizedBox(
                                  width: 18,
                                  height: 18,
                                  child: CircularProgressIndicator(
                                    strokeWidth: 2,
                                  ),
                                )
                              : const Text('Accept'),
                        ),
                      ],
                    ),
                  ],
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}
