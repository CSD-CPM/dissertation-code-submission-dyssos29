import 'package:flutter/material.dart';
import 'package:shared_networking/shared_networking.dart';
import '../api/restaurant_api.dart';
import '../models/restaurant.dart';
import '../models/restaurant_menu_item.dart';
import '../utils/price_utils.dart';

class MenuManagementPage extends StatefulWidget {
  const MenuManagementPage({
    required this.restaurant,
    required this.restaurantApi,
    super.key,
  });

  final Restaurant restaurant;
  final RestaurantApi restaurantApi;

  @override
  State<MenuManagementPage> createState() => _MenuManagementPageState();
}

class _MenuManagementPageState extends State<MenuManagementPage> {
  late Future<List<RestaurantMenuItem>> _menuFuture;

  @override
  void initState() {
    super.initState();
    _loadMenu();
  }

  void _loadMenu() {
    _menuFuture = widget.restaurantApi.getOwnedMenu(widget.restaurant.id);
  }

  Future<void> _refresh() async {
    setState(_loadMenu);
    await _menuFuture;
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

  Future<_MenuItemFormResult?> _showMenuItemDialog({
    RestaurantMenuItem? item,
  }) async {
    var name = item?.name ?? '';

    var price = item == null
        ? ''
        : (item.priceMinorUnits / 100).toStringAsFixed(2);

    var available = item?.available ?? true;
    String? validationMessage;

    return showDialog<_MenuItemFormResult>(
      context: context,
      builder: (dialogContext) {
        return StatefulBuilder(
          builder: (context, setDialogState) {
            return AlertDialog(
              title: Text(item == null ? 'Create menu item' : 'Edit menu item'),
              content: SizedBox(
                width: 420,
                child: SingleChildScrollView(
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      TextFormField(
                        initialValue: name,
                        autofocus: true,
                        onChanged: (value) {
                          name = value;
                        },
                        decoration: const InputDecoration(labelText: 'Name'),
                      ),
                      const SizedBox(height: 16),
                      TextFormField(
                        initialValue: price,
                        onChanged: (value) {
                          price = value;
                        },
                        keyboardType: const TextInputType.numberWithOptions(
                          decimal: true,
                        ),
                        decoration: const InputDecoration(
                          labelText: 'Price (€)',
                          hintText: '7.50',
                        ),
                      ),
                      const SizedBox(height: 16),
                      SwitchListTile(
                        contentPadding: EdgeInsets.zero,
                        title: const Text('Available'),
                        value: available,
                        onChanged: (value) {
                          setDialogState(() {
                            available = value;
                          });
                        },
                      ),
                      if (validationMessage != null) ...[
                        const SizedBox(height: 8),
                        Align(
                          alignment: Alignment.centerLeft,
                          child: Text(
                            validationMessage!,
                            style: TextStyle(
                              color: Theme.of(context).colorScheme.error,
                            ),
                          ),
                        ),
                      ],
                    ],
                  ),
                ),
              ),
              actions: [
                TextButton(
                  onPressed: () => Navigator.pop(dialogContext),
                  child: const Text('Cancel'),
                ),
                FilledButton(
                  onPressed: () {
                    final validName = name.trim();

                    if (validName.isEmpty) {
                      setDialogState(() {
                        validationMessage = 'Name is required.';
                      });

                      return;
                    }

                    try {
                      final priceMinorUnits = parsePriceToMinorUnits(price);

                      Navigator.pop(
                        dialogContext,
                        _MenuItemFormResult(
                          name: validName,
                          priceMinorUnits: priceMinorUnits,
                          available: available,
                        ),
                      );
                    } on FormatException catch (error) {
                      setDialogState(() {
                        validationMessage = error.message.toString();
                      });
                    }
                  },
                  child: const Text('Save'),
                ),
              ],
            );
          },
        );
      },
    );
  }

  Future<void> _createMenuItem() async {
    final result = await _showMenuItemDialog();

    if (result == null) {
      return;
    }

    try {
      await widget.restaurantApi.createMenuItem(
        restaurantId: widget.restaurant.id,
        name: result.name,
        priceMinorUnits: result.priceMinorUnits,
        available: result.available,
      );

      await _refresh();
    } catch (error) {
      await _showError(error);
    }
  }

  Future<void> _editMenuItem(RestaurantMenuItem item) async {
    final result = await _showMenuItemDialog(item: item);

    if (result == null) {
      return;
    }

    try {
      await widget.restaurantApi.updateMenuItem(
        restaurantId: widget.restaurant.id,
        menuItemId: item.id,
        name: result.name,
        priceMinorUnits: result.priceMinorUnits,
        available: result.available,
      );

      await _refresh();
    } catch (error) {
      await _showError(error);
    }
  }

  Future<void> _deleteMenuItem(RestaurantMenuItem item) async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Delete menu item?'),
          content: Text(
            'Delete ${item.name}? '
            'The item will no longer appear in the active menu.',
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context, false),
              child: const Text('Cancel'),
            ),
            FilledButton(
              onPressed: () => Navigator.pop(context, true),
              child: const Text('Delete'),
            ),
          ],
        );
      },
    );

    if (confirmed != true) {
      return;
    }

    try {
      await widget.restaurantApi.deleteMenuItem(
        restaurantId: widget.restaurant.id,
        menuItemId: item.id,
      );

      await _refresh();
    } catch (error) {
      await _showError(error);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      floatingActionButton: FloatingActionButton.extended(
        onPressed: _createMenuItem,
        icon: const Icon(Icons.add),
        label: const Text('Menu item'),
      ),
      body: FutureBuilder<List<RestaurantMenuItem>>(
        future: _menuFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState != ConnectionState.done) {
            return const Center(child: CircularProgressIndicator());
          }

          if (snapshot.hasError) {
            final error = snapshot.error;

            final message = error is ApiException
                ? error.message
                : 'The menu could not be loaded.';

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

          final items = snapshot.data ?? const [];

          if (items.isEmpty) {
            return const Center(
              child: Text(
                'This restaurant does not currently '
                'have any active menu items.',
              ),
            );
          }

          return RefreshIndicator(
            onRefresh: _refresh,
            child: ListView.separated(
              padding: const EdgeInsets.fromLTRB(24, 24, 24, 96),
              itemCount: items.length,
              separatorBuilder: (_, _) => const SizedBox(height: 12),
              itemBuilder: (context, index) {
                final item = items[index];

                return Card(
                  child: Padding(
                    padding: const EdgeInsets.all(16),
                    child: Row(
                      children: [
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                item.name,
                                style: Theme.of(context).textTheme.titleMedium,
                              ),
                              const SizedBox(height: 8),
                              Text(formatPrice(item.priceMinorUnits)),
                              const SizedBox(height: 4),
                              Text(
                                item.available ? 'Available' : 'Unavailable',
                              ),
                            ],
                          ),
                        ),
                        IconButton(
                          tooltip: 'Edit menu item',
                          onPressed: () => _editMenuItem(item),
                          icon: const Icon(Icons.edit),
                        ),
                        IconButton(
                          tooltip: 'Delete menu item',
                          onPressed: () => _deleteMenuItem(item),
                          icon: const Icon(Icons.delete_outline),
                        ),
                      ],
                    ),
                  ),
                );
              },
            ),
          );
        },
      ),
    );
  }
}

class _MenuItemFormResult {
  const _MenuItemFormResult({
    required this.name,
    required this.priceMinorUnits,
    required this.available,
  });

  final String name;
  final int priceMinorUnits;
  final bool available;
}
