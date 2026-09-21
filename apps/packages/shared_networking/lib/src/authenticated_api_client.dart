import 'package:http/http.dart' as http;
import 'package:shared_auth/shared_auth.dart';

import 'api_exception.dart';
import 'api_response.dart';

class AuthenticatedApiClient {
  AuthenticatedApiClient({
    required Uri baseUri,
    required AuthRepository authRepository,
    http.Client? httpClient,
  }) : _baseUri = baseUri, // ignore: prefer_initializing_formals
       _authRepository = authRepository, // ignore: prefer_initializing_formals
       _httpClient = httpClient ?? http.Client();

  final Uri _baseUri;
  final AuthRepository _authRepository;
  final http.Client _httpClient;

  Future<ApiResponse> get(String path) async {
    if (!path.startsWith('/')) {
      throw ArgumentError.value(path, 'path', 'API path must start with "/".');
    }

    final token = await _authRepository.getIdToken();

    if (token == null || token.isEmpty) {
      throw const ApiException(
        message: 'Authentication is required.',
        statusCode: 401,
      );
    }

    final uri = _baseUri.resolve(path);

    final response = await _httpClient.get(
      uri,
      headers: {'Accept': 'application/json', 'Authorization': 'Bearer $token'},
    );

    if (response.statusCode == 401) {
      throw const ApiException(
        message: 'Authentication is no longer valid.',
        statusCode: 401,
      );
    }

    if (response.statusCode == 403) {
      throw const ApiException(
        message: 'Access is forbidden for this account.',
        statusCode: 403,
      );
    }

    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw ApiException(
        message: 'API request failed.',
        statusCode: response.statusCode,
      );
    }

    return ApiResponse(statusCode: response.statusCode, body: response.body);
  }

  void close() {
    _httpClient.close();
  }
}
