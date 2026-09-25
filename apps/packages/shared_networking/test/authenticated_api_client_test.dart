import 'package:flutter_test/flutter_test.dart';
import 'package:http/http.dart' as http;
import 'package:http/testing.dart';
import 'package:shared_auth/shared_auth.dart';
import 'package:shared_networking/shared_networking.dart';

class FakeAuthRepository implements AuthRepository {
  FakeAuthRepository({this.token});

  final String? token;

  @override
  Future<String?> getIdToken({bool forceRefresh = false}) async {
    return token;
  }

  @override
  dynamic noSuchMethod(Invocation invocation) {
    return super.noSuchMethod(invocation);
  }
}

void main() {
  const baseUri = 'http://example.test';

  test('attaches the current ID token as a Bearer token', () async {
    final authRepository = FakeAuthRepository(token: 'test-token');

    final httpClient = MockClient((request) async {
      expect(request.method, 'GET');
      expect(request.url.toString(), '$baseUri/api/v1/customer/ping');
      expect(request.headers['Accept'], 'application/json');
      expect(request.headers['Authorization'], 'Bearer test-token');

      return http.Response(
        '{"message":"ok"}',
        200,
        headers: {'content-type': 'application/json'},
      );
    });

    final client = AuthenticatedApiClient(
      baseUri: Uri.parse(baseUri),
      authRepository: authRepository,
      httpClient: httpClient,
    );

    final response = await client.get('/api/v1/customer/ping');

    expect(response.statusCode, 200);
    expect(response.body, '{"message":"ok"}');

    client.close();
  });

  test('rejects a request when no ID token is available', () async {
    final authRepository = FakeAuthRepository(token: null);

    final client = AuthenticatedApiClient(
      baseUri: Uri.parse(baseUri),
      authRepository: authRepository,
      httpClient: MockClient((request) async {
        fail('HTTP request should not be sent when the token is missing.');
      }),
    );

    expect(
      () => client.get('/api/v1/customer/ping'),
      throwsA(
        isA<ApiException>()
            .having((error) => error.statusCode, 'statusCode', 401)
            .having(
              (error) => error.message,
              'message',
              'Authentication is required.',
            ),
      ),
    );

    client.close();
  });

  test('rejects a request when the ID token is empty', () async {
    final authRepository = FakeAuthRepository(token: '');

    final client = AuthenticatedApiClient(
      baseUri: Uri.parse(baseUri),
      authRepository: authRepository,
      httpClient: MockClient((request) async {
        fail('HTTP request should not be sent when the token is empty.');
      }),
    );

    expect(
      () => client.get('/api/v1/customer/ping'),
      throwsA(
        isA<ApiException>().having(
          (error) => error.statusCode,
          'statusCode',
          401,
        ),
      ),
    );

    client.close();
  });

  test('converts an HTTP 401 response into an ApiException', () async {
    final authRepository = FakeAuthRepository(token: 'test-token');

    final client = AuthenticatedApiClient(
      baseUri: Uri.parse(baseUri),
      authRepository: authRepository,
      httpClient: MockClient((request) async {
        return http.Response('Unauthorized', 401);
      }),
    );

    expect(
      () => client.get('/api/v1/customer/ping'),
      throwsA(
        isA<ApiException>()
            .having((error) => error.statusCode, 'statusCode', 401)
            .having(
              (error) => error.message,
              'message',
              'Authentication is no longer valid.',
            ),
      ),
    );

    client.close();
  });

  test('converts an HTTP 403 response into an ApiException', () async {
    final authRepository = FakeAuthRepository(token: 'test-token');

    final client = AuthenticatedApiClient(
      baseUri: Uri.parse(baseUri),
      authRepository: authRepository,
      httpClient: MockClient((request) async {
        return http.Response('Forbidden', 403);
      }),
    );

    expect(
      () => client.get('/api/v1/admin/ping'),
      throwsA(
        isA<ApiException>()
            .having((error) => error.statusCode, 'statusCode', 403)
            .having(
              (error) => error.message,
              'message',
              'Access is forbidden for this account.',
            ),
      ),
    );

    client.close();
  });

  test('returns ApiResponse for a successful HTTP response', () async {
    final authRepository = FakeAuthRepository(token: 'test-token');

    const responseBody =
        '{"service":"order-service","message":"Foundation probe reachable"}';

    final client = AuthenticatedApiClient(
      baseUri: Uri.parse(baseUri),
      authRepository: authRepository,
      httpClient: MockClient((request) async {
        return http.Response(
          responseBody,
          200,
          headers: {'content-type': 'application/json'},
        );
      }),
    );

    final response = await client.get('/api/v1/customer/ping');

    expect(response.statusCode, 200);
    expect(response.body, responseBody);

    client.close();
  });
}
