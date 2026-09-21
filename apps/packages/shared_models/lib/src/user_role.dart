enum UserRole {
  customer('CUSTOMER'),
  restaurantOwner('RESTAURANT_OWNER'),
  courier('COURIER'),
  admin('ADMIN');

  const UserRole(this.claimValue);

  final String claimValue;

  static UserRole? fromClaim(Object? value) {
    if (value is! String) {
      return null;
    }

    for (final role in UserRole.values) {
      if (role.claimValue == value) {
        return role;
      }
    }

    return null;
  }
}
