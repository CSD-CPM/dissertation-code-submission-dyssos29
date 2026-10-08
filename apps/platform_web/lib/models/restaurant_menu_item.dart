class RestaurantMenuItem {
  const RestaurantMenuItem({
    required this.id,
    required this.restaurantId,
    required this.name,
    required this.priceMinorUnits,
    required this.available,
  });

  final String id;
  final String restaurantId;
  final String name;
  final int priceMinorUnits;
  final bool available;

  factory RestaurantMenuItem.fromJson(Map<String, dynamic> json) {
    return RestaurantMenuItem(
      id: json['id'] as String,
      restaurantId: json['restaurantId'] as String,
      name: json['name'] as String,
      priceMinorUnits: int.parse(json['priceMinorUnits'].toString()),
      available: json['available'] as bool? ?? false,
    );
  }
}
