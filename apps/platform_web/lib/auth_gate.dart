import 'package:flutter/material.dart';
import 'package:shared_auth/shared_auth.dart';
import 'package:shared_models/shared_models.dart';
import 'package:shared_networking/shared_networking.dart';
import 'login_page.dart';
import 'role_home_page.dart';
import 'customer/customer_home_page.dart';
import 'restaurant_owner/restaurant_owner_home_page.dart';

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
      builder: (context, snapshot) {
        final user = snapshot.data;

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

            final role = roleSnapshot.data;

            if (role == null) {
              return InvalidRolePage(authRepository: authRepository);
            }

            return switch (role) {
              UserRole.customer => CustomerHomePage(
                user: user,
                authRepository: authRepository,
                apiClient: apiClient,
              ),
              UserRole.restaurantOwner => RestaurantOwnerHomePage(
                user: user,
                authRepository: authRepository,
                apiClient: apiClient,
              ),
              UserRole.admin => RoleHomePage(
                user: user,
                role: role,
                endpoint: '/api/v1/admin/ping',
                title: 'Administrator Dashboard',
                authRepository: authRepository,
                apiClient: apiClient,
              ),
              UserRole.courier => InvalidRolePage(
                authRepository: authRepository,
                message: 'Courier accounts must use the courier application.',
              ),
            };
          },
        );
      },
    );
  }
}

class InvalidRolePage extends StatelessWidget {
  const InvalidRolePage({
    required this.authRepository,
    this.message = 'This account does not have a valid application role.',
    super.key,
  });

  final AuthRepository authRepository;
  final String message;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(message),
            const SizedBox(height: 16),
            ElevatedButton(
              onPressed: authRepository.signOut,
              child: const Text('Sign out'),
            ),
          ],
        ),
      ),
    );
  }
}
