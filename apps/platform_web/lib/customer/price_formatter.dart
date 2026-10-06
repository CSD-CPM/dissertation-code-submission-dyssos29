String formatPrice(int minorUnits) {
  return '€${(minorUnits / 100).toStringAsFixed(2)}';
}
