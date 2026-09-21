import 'package:courier_mobile/login_page.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:shared_auth/shared_auth.dart';
import 'package:shared_models/shared_models.dart';

class FakeAuthRepository implements AuthRepository {
  @override
  AuthenticatedUser? get currentUser => null;

  @override
  Future<AuthenticatedUser> signIn({
    required String email,
    required String password,
  }) async {
    return const AuthenticatedUser(
      uid: 'test-courier',
      email: 'courier.test@example.com',
    );
  }

  @override
  Future<void> signOut() async {}

  @override
  Future<UserRole?> currentRole({bool forceRefresh = false}) async {
    return null;
  }

  @override
  Future<String?> getIdToken({bool forceRefresh = false}) async {
    return null;
  }

  @override
  Stream<AuthenticatedUser?> authStateChanges() {
    return const Stream.empty();
  }

  @override
  Stream<AuthenticatedUser?> idTokenChanges() {
    return const Stream.empty();
  }
}

void main() {
  testWidgets('displays the courier login form', (tester) async {
    final authRepository = FakeAuthRepository();

    await tester.pumpWidget(
      MaterialApp(home: LoginPage(authRepository: authRepository)),
    );

    expect(find.text('Courier App'), findsOneWidget);

    expect(find.text('Sign in to continue'), findsOneWidget);

    expect(find.byType(TextFormField), findsNWidgets(2));

    expect(find.text('Sign in'), findsOneWidget);
  });
}
