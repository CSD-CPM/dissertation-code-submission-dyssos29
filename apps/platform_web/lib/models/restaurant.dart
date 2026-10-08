class Restaurant {
  const Restaurant({
    required this.id,
    required this.name,
    required this.active,
  });

  final String id;
  final String name;
  final bool active;

  factory Restaurant.fromJson(Map<String, dynamic> json) {
    return Restaurant(
      id: json['id'] as String,
      name: json['name'] as String,
      active: json['active'] as bool,
    );
  }
}
