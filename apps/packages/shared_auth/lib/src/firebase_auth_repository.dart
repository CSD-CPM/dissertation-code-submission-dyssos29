import 'package:firebase_auth/firebase_auth.dart' as firebase;
import 'package:shared_models/shared_models.dart';

import 'auth_exception.dart';
import 'auth_repository.dart';

class FirebaseAuthRepository implements AuthRepository {
  FirebaseAuthRepository({firebase.FirebaseAuth? firebaseAuth})
    : _firebaseAuth = firebaseAuth ?? firebase.FirebaseAuth.instance;

  final firebase.FirebaseAuth _firebaseAuth;

  @override
  AuthenticatedUser? get currentUser {
    final user = _firebaseAuth.currentUser;

    if (user == null) {
      return null;
    }

    return _mapUser(user);
  }

  @override
  Future<AuthenticatedUser> signIn({
    required String email,
    required String password,
  }) async {
    try {
      final credential = await _firebaseAuth.signInWithEmailAndPassword(
        email: email.trim(),
        password: password,
      );

      final user = credential.user;

      if (user == null) {
        throw const AuthException(
          'Authentication completed without an authenticated user.',
        );
      }

      return _mapUser(user);
    } on firebase.FirebaseAuthException catch (error) {
      throw _mapFirebaseException(error);
    }
  }

  @override
  Future<void> signOut() {
    return _firebaseAuth.signOut();
  }

  @override
  Future<UserRole?> currentRole({bool forceRefresh = false}) async {
    final user = _firebaseAuth.currentUser;

    if (user == null) {
      return null;
    }

    final tokenResult = await user.getIdTokenResult(forceRefresh);

    final role = UserRole.fromClaim(tokenResult.claims?['role']);

    if (role == null) {
      throw const AuthException('Authenticated user has no valid role.');
    }

    return role;
  }

  @override
  Future<String?> getIdToken({bool forceRefresh = false}) async {
    final user = _firebaseAuth.currentUser;

    if (user == null) {
      return null;
    }

    return user.getIdToken(forceRefresh);
  }

  @override
  Stream<AuthenticatedUser?> authStateChanges() {
    return _firebaseAuth.authStateChanges().map(
      (user) => user == null ? null : _mapUser(user),
    );
  }

  @override
  Stream<AuthenticatedUser?> idTokenChanges() {
    return _firebaseAuth.idTokenChanges().map(
      (user) => user == null ? null : _mapUser(user),
    );
  }

  AuthenticatedUser _mapUser(firebase.User user) {
    return AuthenticatedUser(uid: user.uid, email: user.email);
  }

  AuthException _mapFirebaseException(firebase.FirebaseAuthException error) {
    return switch (error.code) {
      'invalid-email' => const AuthException('The email address is invalid.'),
      'user-disabled' => const AuthException('This account has been disabled.'),
      'invalid-credential' ||
      'wrong-password' ||
      'user-not-found' => const AuthException('Invalid email or password.'),
      'too-many-requests' => const AuthException(
        'Too many sign-in attempts. Please try again later.',
      ),
      'network-request-failed' => const AuthException(
        'Authentication could not reach the network.',
      ),
      _ => const AuthException('Authentication failed. Please try again.'),
    };
  }
}
