import 'package:shared_models/shared_models.dart';

abstract interface class AuthRepository {
  Future<AuthenticatedUser> signIn({
    required String email,
    required String password,
  });

  Future<void> signOut();

  AuthenticatedUser? get currentUser;

  Future<UserRole?> currentRole({bool forceRefresh = false});

  Future<String?> getIdToken({bool forceRefresh = false});

  Stream<AuthenticatedUser?> authStateChanges();

  Stream<AuthenticatedUser?> idTokenChanges();
}
