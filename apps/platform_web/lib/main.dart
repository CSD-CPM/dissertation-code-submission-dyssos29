import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'package:shared_auth/shared_auth.dart';
import 'package:shared_networking/shared_networking.dart';

import 'firebase_options.dart';
import 'auth_gate.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await Firebase.initializeApp(options: DefaultFirebaseOptions.currentPlatform);

  final authRepository = FirebaseAuthRepository();

  final apiClient = AuthenticatedApiClient(
    baseUri: Uri.parse(
      const String.fromEnvironment(
        'API_BASE_URL',
        defaultValue: 'http://localhost:8080',
      ),
    ),
    authRepository: authRepository,
  );

  runApp(PlatformWebApp(authRepository: authRepository, apiClient: apiClient));
}

class PlatformWebApp extends StatelessWidget {
  const PlatformWebApp({
    required this.authRepository,
    required this.apiClient,
    super.key,
  });

  final AuthRepository authRepository;
  final AuthenticatedApiClient apiClient;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Food Delivery Platform',
      debugShowCheckedModeBanner: false,
      home: AuthGate(authRepository: authRepository, apiClient: apiClient),
    );
  }
}
