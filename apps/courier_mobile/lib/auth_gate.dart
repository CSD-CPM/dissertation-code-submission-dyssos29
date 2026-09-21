import 'package:flutter/material.dart';
import 'package:shared_auth/shared_auth.dart';
import 'package:shared_models/shared_models.dart';
import 'package:shared_networking/shared_networking.dart';

import 'courier_home_page.dart';
import 'login_page.dart';

class AuthGate extends StatelessWidget {
  const AuthGate({
    required this.authRepository,
    required this.apiClient,
    super.key,
  });

  final AuthRepository authRepository;
  final AuthenticatedApiClient apiClient;

  @override
  Widget build(BuildContext context) {
    return StreamBuilder<AuthenticatedUser?>(
      stream: authRepository.idTokenChanges(),
      initialData: authRepository.currentUser,
      builder: (context, authSnapshot) {
        final user = authSnapshot.data;

        if (user == null) {
          return LoginPage(authRepository: authRepository);
        }

        return FutureBuilder<UserRole?>(
          future: authRepository.currentRole(),
          builder: (context, roleSnapshot) {
            if (roleSnapshot.connectionState != ConnectionState.done) {
              return const Scaffold(
                body: Center(child: CircularProgressIndicator()),
              );
            }

            if (roleSnapshot.hasError) {
              return InvalidCourierRolePage(
                authRepository: authRepository,
                message: 'This account does not have a valid application role.',
              );
            }

            final role = roleSnapshot.data;

            if (role == null) {
              return InvalidCourierRolePage(
                authRepository: authRepository,
                message: 'This account does not have a valid application role.',
              );
            }

            if (role != UserRole.courier) {
              return InvalidCourierRolePage(
                authRepository: authRepository,
                message:
                    'This application is available only to courier accounts.',
              );
            }

            return CourierHomePage(
              user: user,
              role: role,
              authRepository: authRepository,
              apiClient: apiClient,
            );
          },
        );
      },
    );
  }
}

class InvalidCourierRolePage extends StatelessWidget {
  const InvalidCourierRolePage({
    required this.authRepository,
    required this.message,
    super.key,
  });

  final AuthRepository authRepository;
  final String message;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(24),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(message, textAlign: TextAlign.center),
              const SizedBox(height: 24),
              FilledButton(
                onPressed: authRepository.signOut,
                child: const Text('Sign out'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
