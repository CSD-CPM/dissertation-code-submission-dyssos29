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

  Future<ApiResponse> get(String path) {
    return _send(method: 'GET', path: path);
  }

  Future<ApiResponse> post(String path, {String? body}) {
    return _send(method: 'POST', path: path, body: body);
  }

  Future<ApiResponse> put(String path, {String? body}) {
    return _send(method: 'PUT', path: path, body: body);
  }

  Future<ApiResponse> patch(String path, {String? body}) {
    return _send(method: 'PATCH', path: path, body: body);
  }

  Future<ApiResponse> delete(String path) {
    return _send(method: 'DELETE', path: path);
  }

  Future<ApiResponse> _send({
    required String method,
    required String path,
    String? body,
  }) async {
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
    final request = http.Request(method, uri);

    request.headers.addAll({
      'Accept': 'application/json',
      'Authorization': 'Bearer $token',
    });

    if (body != null) {
      request.headers['Content-Type'] = 'application/json';
      request.body = body;
    }

    final streamedResponse = await _httpClient.send(request);
    final responseBody = await streamedResponse.stream.bytesToString();
    final statusCode = streamedResponse.statusCode;

    if (statusCode == 401) {
      throw const ApiException(
        message: 'Authentication is no longer valid.',
        statusCode: 401,
      );
    }

    if (statusCode == 403) {
      throw const ApiException(
        message: 'Access is forbidden for this account.',
        statusCode: 403,
      );
    }

    if (statusCode < 200 || statusCode >= 300) {
      throw ApiException(
        message: 'API request failed.',
        statusCode: statusCode,
      );
    }

    return ApiResponse(statusCode: statusCode, body: responseBody);
  }

  void close() {
    _httpClient.close();
  }
}
