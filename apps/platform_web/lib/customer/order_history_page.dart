import 'package:flutter/material.dart';
import 'package:shared_networking/shared_networking.dart';
import '../api/order_api.dart';
import '../models/customer_order.dart';
import 'order_detail_page.dart';
import 'price_formatter.dart';

class OrderHistoryPage extends StatefulWidget {
  const OrderHistoryPage({required this.orderApi, super.key});

  final OrderApi orderApi;

  @override
  State<OrderHistoryPage> createState() => _OrderHistoryPageState();
}

class _OrderHistoryPageState extends State<OrderHistoryPage> {
  late Future<List<CustomerOrder>> _ordersFuture;

  @override
  void initState() {
    super.initState();
    _loadOrders();
  }

  void _loadOrders() {
    _ordersFuture = widget.orderApi.listOrders();
  }

  Future<void> _refresh() async {
    setState(_loadOrders);

    await _ordersFuture;
  }

  Future<void> _openOrder(CustomerOrder order) async {
    await Navigator.of(context).push(
      MaterialPageRoute<void>(
        builder: (_) =>
            OrderDetailPage(orderId: order.id, orderApi: widget.orderApi),
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

          return Center(
            child: Text(
              error is ApiException
                  ? error.message
                  : 'Orders could not be loaded.',
            ),
          );
        }

        final orders = snapshot.data ?? const [];

        if (orders.isEmpty) {
          return const Center(
            child: Text('You have not placed any orders yet.'),
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

              return Card(
                child: ListTile(
                  title: Text(
                    'Order ${order.id}',
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                  ),
                  subtitle: Text(
                    '${order.status.label} • '
                    '${formatPrice(order.totalMinorUnits)}',
                  ),
                  trailing: const Icon(Icons.chevron_right),
                  onTap: () => _openOrder(order),
                ),
              );
            },
          ),
        );
      },
    );
  }
}
