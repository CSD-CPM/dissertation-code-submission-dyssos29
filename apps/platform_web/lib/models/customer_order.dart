enum CustomerOrderStatus {
  placed('Placed'),
  accepted('Accepted'),
  rejected('Rejected');

  const CustomerOrderStatus(this.label);

  final String label;

  static CustomerOrderStatus fromJson(String value) {
    return switch (value) {
      'ORDER_STATUS_PLACED' => CustomerOrderStatus.placed,
      'ORDER_STATUS_ACCEPTED' => CustomerOrderStatus.accepted,
      'ORDER_STATUS_REJECTED' => CustomerOrderStatus.rejected,
      _ => throw FormatException('Unsupported order status: $value'),
    };
  }
}

class CustomerOrder {
  const CustomerOrder({
    required this.id,
    required this.customerSub,
    required this.restaurantId,
    required this.items,
    required this.totalMinorUnits,
    required this.status,
    required this.createdAt,
  });

  final String id;
  final String customerSub;
  final String restaurantId;
  final List<CustomerOrderItem> items;
  final int totalMinorUnits;
  final CustomerOrderStatus status;
  final DateTime createdAt;

  factory CustomerOrder.fromJson(Map<String, dynamic> json) {
    return CustomerOrder(
      id: json['id'] as String,
      customerSub: json['customerSub'] as String,
      restaurantId: json['restaurantId'] as String,
      items: (json['items'] as List<dynamic>)
          .map(
            (item) => CustomerOrderItem.fromJson(item as Map<String, dynamic>),
          )
          .toList(),
      totalMinorUnits: int.parse(json['totalMinorUnits'].toString()),
      status: CustomerOrderStatus.fromJson(json['status'] as String),
      createdAt: DateTime.parse(json['createdAt'] as String),
    );
  }
}

class CustomerOrderItem {
  const CustomerOrderItem({
    required this.menuItemId,
    required this.name,
    required this.quantity,
    required this.unitPriceMinorUnits,
  });

  final String menuItemId;
  final String name;
  final int quantity;
  final int unitPriceMinorUnits;

  int get lineTotalMinorUnits => unitPriceMinorUnits * quantity;

  factory CustomerOrderItem.fromJson(Map<String, dynamic> json) {
    return CustomerOrderItem(
      menuItemId: json['menuItemId'] as String,
      name: json['name'] as String,
      quantity: json['quantity'] as int,
      unitPriceMinorUnits: int.parse(json['unitPriceMinorUnits'].toString()),
    );
  }
}
