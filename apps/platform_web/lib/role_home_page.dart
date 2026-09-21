import 'package:flutter/material.dart';
import 'package:shared_auth/shared_auth.dart';
import 'package:shared_models/shared_models.dart';
import 'package:shared_networking/shared_networking.dart';

class RoleHomePage extends StatefulWidget {
  const RoleHomePage({
    required this.user,
    required this.role,
    required this.endpoint,
    required this.title,
    required this.authRepository,
    required this.apiClient,
    super.key,
  });

  final AuthenticatedUser user;
  final UserRole role;
  final String endpoint;
  final String title;
  final AuthRepository authRepository;
  final AuthenticatedApiClient apiClient;

  @override
  State<RoleHomePage> createState() => _RoleHomePageState();
}

class _RoleHomePageState extends State<RoleHomePage> {
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
      final response = await widget.apiClient.get(widget.endpoint);

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
        title: Text(widget.title),
        actions: [
          TextButton(onPressed: _signOut, child: const Text('Sign out')),
          const SizedBox(width: 8),
        ],
      ),
      body: Center(
        child: ConstrainedBox(
          constraints: const BoxConstraints(maxWidth: 700),
          child: Padding(
            padding: const EdgeInsets.all(24),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                Text(
                  'Authenticated session',
                  style: Theme.of(context).textTheme.headlineMedium,
                ),
                const SizedBox(height: 24),

                _InformationRow(label: 'UID', value: widget.user.uid),
                const SizedBox(height: 12),

                _InformationRow(
                  label: 'Email',
                  value: widget.user.email ?? 'Not available',
                ),
                const SizedBox(height: 12),

                _InformationRow(label: 'Role', value: widget.role.claimValue),
                const SizedBox(height: 12),

                _InformationRow(
                  label: 'Protected endpoint',
                  value: widget.endpoint,
                ),
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
                    style: TextStyle(
                      color: Theme.of(context).colorScheme.error,
                    ),
                  ),
                ],
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class _InformationRow extends StatelessWidget {
  const _InformationRow({required this.label, required this.value});

  final String label;
  final String value;

  @override
  Widget build(BuildContext context) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        SizedBox(
          width: 160,
          child: Text(label, style: Theme.of(context).textTheme.titleSmall),
        ),
        Expanded(child: SelectableText(value)),
      ],
    );
  }
}
