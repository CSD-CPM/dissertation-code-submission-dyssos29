String formatPrice(int minorUnits) {
  return '€${(minorUnits / 100).toStringAsFixed(2)}';
}

int parsePriceToMinorUnits(String value) {
  final normalized = value.trim().replaceAll(',', '.');

  if (!RegExp(r'^\d+(\.\d{1,2})?$').hasMatch(normalized)) {
    throw const FormatException('Invalid price.');
  }

  final parts = normalized.split('.');
  final whole = int.parse(parts[0]);
  final cents = parts.length == 1 ? 0 : int.parse(parts[1].padRight(2, '0'));
  final result = whole * 100 + cents;

  if (result <= 0) {
    throw const FormatException('Price must be greater than zero.');
  }

  return result;
}
