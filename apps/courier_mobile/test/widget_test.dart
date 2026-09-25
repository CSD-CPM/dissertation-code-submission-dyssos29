import 'dart:async';

import 'package:courier_mobile/auth_gate.dart';
import 'package:courier_mobile/login_page.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:shared_auth/shared_auth.dart';
import 'package:shared_models/shared_models.dart';
import 'package:shared_networking/shared_networking.dart';

class FakeAuthRepository implements AuthRepository {
  FakeAuthRepository({AuthenticatedUser? user, UserRole? role})
    : _currentUser = user,
      _role = role; // ignore: prefer_initializing_formals

  AuthenticatedUser? _currentUser;
  UserRole? _role;

  final StreamController<AuthenticatedUser?> _idTokenController =
      StreamController<AuthenticatedUser?>.broadcast();

  int signInCallCount = 0;
  int signOutCallCount = 0;

  @override
  AuthenticatedUser? get currentUser => _currentUser;

  @override
  Future<AuthenticatedUser> signIn({
    required String email,
    required String password,
  }) async {
    signInCallCount++;

    return _currentUser ?? AuthenticatedUser(uid: 'test-user', email: email);
  }

  @override
  Future<void> signOut() async {
    signOutCallCount++;

    _currentUser = null;
    _role = null;

    _idTokenController.add(null);
  }

  @override
  Future<UserRole?> currentRole({bool forceRefresh = false}) async {
    return _role;
  }

  @override
  Future<String?> getIdToken({bool forceRefresh = false}) async {
    if (_currentUser == null) {
      return null;
    }

    return 'test-token';
  }

  @override
  Stream<AuthenticatedUser?> authStateChanges() {
    return _idTokenController.stream;
  }

  @override
  Stream<AuthenticatedUser?> idTokenChanges() {
    return _idTokenController.stream;
  }

  void emitCurrentState() {
    _idTokenController.add(_currentUser);
  }

  Future<void> dispose() {
    return _idTokenController.close();
  }
}

AuthenticatedApiClient createApiClient(AuthRepository authRepository) {
  return AuthenticatedApiClient(
    baseUri: Uri.parse('http://example.test/'),
    authRepository: authRepository,
  );
}

Future<void> pumpAuthGate(
  WidgetTester tester,
  FakeAuthRepository authRepository,
  AuthenticatedApiClient apiClient,
) async {
  await tester.pumpWidget(
    MaterialApp(
      home: AuthGate(authRepository: authRepository, apiClient: apiClient),
    ),
  );

  authRepository.emitCurrentState();

  await tester.pumpAndSettle();
}

void main() {
  testWidgets('displays the courier login form', (tester) async {
    final authRepository = FakeAuthRepository();
    addTearDown(authRepository.dispose);

    await tester.pumpWidget(
      MaterialApp(home: LoginPage(authRepository: authRepository)),
    );

    expect(find.text('Courier App'), findsOneWidget);
    expect(find.text('Sign in to continue'), findsOneWidget);
    expect(find.byType(TextFormField), findsNWidgets(2));
    expect(find.text('Sign in'), findsOneWidget);
  });

  testWidgets('does not submit an empty login form', (tester) async {
    final authRepository = FakeAuthRepository();
    addTearDown(authRepository.dispose);

    await tester.pumpWidget(
      MaterialApp(home: LoginPage(authRepository: authRepository)),
    );

    await tester.tap(find.text('Sign in'));

    await tester.pump();

    expect(authRepository.signInCallCount, 0);
  });

  testWidgets('routes a logged-out user to the login page', (tester) async {
    final authRepository = FakeAuthRepository();

    final apiClient = createApiClient(authRepository);

    addTearDown(authRepository.dispose);
    addTearDown(apiClient.close);

    await pumpAuthGate(tester, authRepository, apiClient);

    expect(find.byType(LoginPage), findsOneWidget);
  });

  testWidgets('routes a courier to the courier home screen', (tester) async {
    final authRepository = FakeAuthRepository(
      user: const AuthenticatedUser(
        uid: 'courier-id',
        email: 'courier.test@example.com',
      ),
      role: UserRole.courier,
    );

    final apiClient = createApiClient(authRepository);

    addTearDown(authRepository.dispose);
    addTearDown(apiClient.close);

    await pumpAuthGate(tester, authRepository, apiClient);

    expect(find.text('Authenticated courier'), findsOneWidget);
  });

  testWidgets('rejects a non-courier role', (tester) async {
    final authRepository = FakeAuthRepository(
      user: const AuthenticatedUser(
        uid: 'customer-id',
        email: 'customer.test@example.com',
      ),
      role: UserRole.customer,
    );

    final apiClient = createApiClient(authRepository);

    addTearDown(authRepository.dispose);
    addTearDown(apiClient.close);

    await pumpAuthGate(tester, authRepository, apiClient);

    expect(
      find.text('This application is available only to courier accounts.'),
      findsOneWidget,
    );
  });

  testWidgets('logout returns to the courier login page', (tester) async {
    final authRepository = FakeAuthRepository(
      user: const AuthenticatedUser(
        uid: 'courier-id',
        email: 'courier.test@example.com',
      ),
      role: UserRole.courier,
    );

    final apiClient = createApiClient(authRepository);

    addTearDown(authRepository.dispose);
    addTearDown(apiClient.close);

    await pumpAuthGate(tester, authRepository, apiClient);

    expect(find.text('Authenticated courier'), findsOneWidget);

    await tester.tap(find.byIcon(Icons.logout));

    await tester.pumpAndSettle();

    expect(authRepository.signOutCallCount, 1);
    expect(find.byType(LoginPage), findsOneWidget);
  });
}
