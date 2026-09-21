import 'package:flutter/material.dart';
import 'package:shared_auth/shared_auth.dart';
import 'package:shared_models/shared_models.dart';
import 'package:shared_networking/shared_networking.dart';

class CourierHomePage extends StatefulWidget {
  const CourierHomePage({
    required this.user,
    required this.role,
    required this.authRepository,
    required this.apiClient,
    super.key,
  });

  final AuthenticatedUser user;
  final UserRole role;
  final AuthRepository authRepository;
  final AuthenticatedApiClient apiClient;

  @override
  State<CourierHomePage> createState() => _CourierHomePageState();
}

class _CourierHomePageState extends State<CourierHomePage> {
  static const _protectedEndpoint = '/api/v1/courier/ping';

  bool _isTestingEndpoint = false;
  String? _endpointResult;
  String? _endpointError;

  Future<void> _testProtectedEndpoint() async {
    if (_isTestingEndpoint) {
      return;
    }

    setState(() {
      _isTestingEndpoint = true;
      _endpointResult = null;
      _endpointError = null;
    });

    try {
      final response = await widget.apiClient.get(_protectedEndpoint);

      if (!mounted) {
        return;
      }

      setState(() {
        _endpointResult = '${response.statusCode}\n${response.body}';
      });
    } on ApiException catch (error) {
      if (!mounted) {
        return;
      }

      setState(() {
        _endpointError = error.statusCode == null
            ? error.message
            : '${error.statusCode}: ${error.message}';
      });
    } catch (_) {
      if (!mounted) {
        return;
      }

      setState(() {
        _endpointError = 'An unexpected API error occurred.';
      });
    } finally {
      if (mounted) {
        setState(() {
          _isTestingEndpoint = false;
        });
      }
    }
  }

  Future<void> _signOut() async {
    await widget.authRepository.signOut();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Courier'),
        actions: [
          IconButton(
            onPressed: _signOut,
            tooltip: 'Sign out',
            icon: const Icon(Icons.logout),
          ),
        ],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Text(
              'Authenticated courier',
              style: Theme.of(context).textTheme.headlineSmall,
            ),
            const SizedBox(height: 24),
            Text('UID: ${widget.user.uid}'),
            const SizedBox(height: 12),
            Text('Email: ${widget.user.email ?? 'Not available'}'),
            const SizedBox(height: 12),
            Text('Role: ${widget.role.claimValue}'),
            const SizedBox(height: 12),
            const Text('Protected endpoint: $_protectedEndpoint'),
            const SizedBox(height: 32),
            FilledButton(
              onPressed: _isTestingEndpoint ? null : _testProtectedEndpoint,
              child: _isTestingEndpoint
                  ? const SizedBox(
                      width: 20,
                      height: 20,
                      child: CircularProgressIndicator(strokeWidth: 2),
                    )
                  : const Text('Test protected endpoint'),
            ),
            if (_endpointResult != null) ...[
              const SizedBox(height: 24),
              Text(
                'API response',
                style: Theme.of(context).textTheme.titleMedium,
              ),
              const SizedBox(height: 8),
              SelectableText(_endpointResult!),
            ],
            if (_endpointError != null) ...[
              const SizedBox(height: 24),
              Text(
                _endpointError!,
                style: TextStyle(color: Theme.of(context).colorScheme.error),
              ),
            ],
          ],
        ),
      ),
    );
  }
}
