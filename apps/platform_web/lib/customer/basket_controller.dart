import 'package:flutter/foundation.dart';
import '../models/basket_item.dart';
import '../models/restaurant.dart';
import '../models/restaurant_menu_item.dart';

class BasketController extends ChangeNotifier {
  final List<BasketItem> _items = [];

  String? _restaurantId;
  String? _restaurantName;

  List<BasketItem> get items => List.unmodifiable(_items);

  String? get restaurantId => _restaurantId;

  String? get restaurantName => _restaurantName;

  bool get isEmpty => _items.isEmpty;

  bool get isNotEmpty => _items.isNotEmpty;

  int get itemCount => _items.fold(0, (total, item) => total + item.quantity);

  int get totalMinorUnits =>
      _items.fold(0, (total, item) => total + item.lineTotalMinorUnits);

  bool belongsToRestaurant(String restaurantId) {
    return _restaurantId == null || _restaurantId == restaurantId;
  }

  void addItem({
    required Restaurant restaurant,
    required RestaurantMenuItem menuItem,
  }) {
    if (!menuItem.available) {
      throw StateError('Unavailable items cannot be added to the basket.');
    }

    if (_restaurantId != null && _restaurantId != restaurant.id) {
      throw StateError(
        'The basket already contains items from another restaurant.',
      );
    }

    _restaurantId = restaurant.id;
    _restaurantName = restaurant.name;

    final index = _items.indexWhere((item) => item.menuItem.id == menuItem.id);

    if (index == -1) {
      _items.add(BasketItem(menuItem: menuItem, quantity: 1));
    } else {
      final current = _items[index];

      _items[index] = current.copyWith(quantity: current.quantity + 1);
    }

    notifyListeners();
  }

  void increment(String menuItemId) {
    final index = _items.indexWhere((item) => item.menuItem.id == menuItemId);

    if (index == -1) {
      return;
    }

    final current = _items[index];

    _items[index] = current.copyWith(quantity: current.quantity + 1);

    notifyListeners();
  }

  void decrement(String menuItemId) {
    final index = _items.indexWhere((item) => item.menuItem.id == menuItemId);

    if (index == -1) {
      return;
    }

    final current = _items[index];

    if (current.quantity == 1) {
      _items.removeAt(index);
    } else {
      _items[index] = current.copyWith(quantity: current.quantity - 1);
    }

    _resetRestaurantIfEmpty();

    notifyListeners();
  }

  void remove(String menuItemId) {
    _items.removeWhere((item) => item.menuItem.id == menuItemId);

    _resetRestaurantIfEmpty();

    notifyListeners();
  }

  void clear() {
    _items.clear();
    _restaurantId = null;
    _restaurantName = null;

    notifyListeners();
  }

  void _resetRestaurantIfEmpty() {
    if (_items.isEmpty) {
      _restaurantId = null;
      _restaurantName = null;
    }
  }
}
