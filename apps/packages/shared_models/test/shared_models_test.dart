import 'package:flutter_test/flutter_test.dart';
import 'package:shared_models/shared_models.dart';

void main() {
  group('UserRole.fromClaim', () {
    test('parses CUSTOMER', () {
      expect(UserRole.fromClaim('CUSTOMER'), UserRole.customer);
    });

    test('parses RESTAURANT_OWNER', () {
      expect(UserRole.fromClaim('RESTAURANT_OWNER'), UserRole.restaurantOwner);
    });

    test('parses COURIER', () {
      expect(UserRole.fromClaim('COURIER'), UserRole.courier);
    });

    test('parses ADMIN', () {
      expect(UserRole.fromClaim('ADMIN'), UserRole.admin);
    });

    test('returns null for unknown role', () {
      expect(UserRole.fromClaim('UNKNOWN'), isNull);
    });

    test('returns null for non-string claim', () {
      expect(UserRole.fromClaim(123), isNull);
    });

    test('returns null for null claim', () {
      expect(UserRole.fromClaim(null), isNull);
    });
  });
}
