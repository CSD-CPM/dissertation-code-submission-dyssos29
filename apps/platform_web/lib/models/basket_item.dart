import 'restaurant_menu_item.dart';

class BasketItem {
  const BasketItem({required this.menuItem, required this.quantity});

  final RestaurantMenuItem menuItem;
  final int quantity;

  int get lineTotalMinorUnits => menuItem.priceMinorUnits * quantity;

  BasketItem copyWith({int? quantity}) {
    return BasketItem(menuItem: menuItem, quantity: quantity ?? this.quantity);
  }
}
