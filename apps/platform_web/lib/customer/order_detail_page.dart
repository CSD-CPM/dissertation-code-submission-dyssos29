import 'package:flutter/material.dart';
import 'package:shared_networking/shared_networking.dart';
import '../api/order_api.dart';
import '../models/customer_order.dart';
import '../utils/price_utils.dart';

class OrderDetailPage extends StatefulWidget {
  const OrderDetailPage({
    required this.orderId,
    required this.orderApi,
    super.key,
  });

  final String orderId;
  final OrderApi orderApi;

  @override
  State<OrderDetailPage> createState() => _OrderDetailPageState();
}

class _OrderDetailPageState extends State<OrderDetailPage> {
  late Future<CustomerOrder> _orderFuture;

  @override
  void initState() {
    super.initState();

    _orderFuture = widget.orderApi.getOrder(widget.orderId);
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

            return Center(
              child: Text(
                error is ApiException
                    ? error.message
                    : 'The order could not be loaded.',
              ),
            );
          }

          final order = snapshot.data!;

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
                  Text(
                    'Created: '
                    '${order.createdAt.toLocal()}',
                  ),
                  const SizedBox(height: 24),
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
                  Align(
                    alignment: Alignment.centerRight,
                    child: Text(
                      'Total: '
                      '${formatPrice(order.totalMinorUnits)}',
                      style: Theme.of(context).textTheme.titleLarge,
                    ),
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
