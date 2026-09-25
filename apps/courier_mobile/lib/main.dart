import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'package:shared_auth/shared_auth.dart';
import 'package:shared_networking/shared_networking.dart';

import 'auth_gate.dart';
import 'firebase_options.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await Firebase.initializeApp(options: DefaultFirebaseOptions.currentPlatform);

  final authRepository = FirebaseAuthRepository();

  final apiClient = AuthenticatedApiClient(
    baseUri: Uri.parse(
      const String.fromEnvironment(
        'API_BASE_URL',
        defaultValue: 'http://10.0.2.2:8080',
      ),
    ),
    authRepository: authRepository,
  );

  runApp(
    CourierMobileApp(authRepository: authRepository, apiClient: apiClient),
  );
}

class CourierMobileApp extends StatelessWidget {
  const CourierMobileApp({
    required this.authRepository,
    required this.apiClient,
    super.key,
  });

  final AuthRepository authRepository;
  final AuthenticatedApiClient apiClient;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Food Delivery Courier',
      debugShowCheckedModeBanner: false,
      home: AuthGate(authRepository: authRepository, apiClient: apiClient),
    );
  }
}
