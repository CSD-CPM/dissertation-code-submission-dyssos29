import 'package:flutter_test/flutter_test.dart';
import 'package:http/http.dart' as http;
import 'package:http/testing.dart';
import 'package:shared_auth/shared_auth.dart';
import 'package:shared_networking/shared_networking.dart';
import 'dart:convert';

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

  test('sends an authenticated POST request with a JSON body', () async {
    final authRepository = FakeAuthRepository(token: 'test-token');

    final requestBody = jsonEncode({
      'restaurantId': 'restaurant-1',
      'items': [
        {'menuItemId': 'soup-1', 'quantity': 2},
      ],
    });

    final httpClient = MockClient((request) async {
      expect(request.method, 'POST');
      expect(request.url.toString(), '$baseUri/api/v1/customer/orders');
      expect(request.headers['Accept'], 'application/json');
      expect(request.headers['Authorization'], 'Bearer test-token');
      expect(request.headers['Content-Type'], 'application/json');

      final body = jsonDecode(request.body) as Map<String, dynamic>;

      expect(body['restaurantId'], 'restaurant-1');

      final items = body['items'] as List<dynamic>;

      expect(items, hasLength(1));
      expect(items[0], {'menuItemId': 'soup-1', 'quantity': 2});
      expect(body.containsKey('totalMinorUnits'), isFalse);
      expect(body.containsKey('customerSub'), isFalse);
      expect(body.containsKey('status'), isFalse);

      return http.Response(
        '{"order":{"id":"order-1"}}',
        200,
        headers: {'content-type': 'application/json'},
      );
    });

    final client = AuthenticatedApiClient(
      baseUri: Uri.parse(baseUri),
      authRepository: authRepository,
      httpClient: httpClient,
    );

    final response = await client.post(
      '/api/v1/customer/orders',
      body: requestBody,
    );

    expect(response.statusCode, 200);
    final responseBody = jsonDecode(response.body) as Map<String, dynamic>;
    final order = responseBody['order'] as Map<String, dynamic>;
    expect(order['id'], 'order-1');

    client.close();
  });

  test('sends an authenticated PUT request with a JSON body', () async {
    final authRepository = FakeAuthRepository(token: 'test-token');

    final httpClient = MockClient((request) async {
      expect(request.method, 'PUT');
      expect(
        request.url.toString(),
        '$baseUri/api/v1/restaurant/restaurants/restaurant-1',
      );
      expect(request.headers['Authorization'], 'Bearer test-token');
      expect(request.headers['Content-Type'], 'application/json');
      expect(request.body, '{"name":"Updated Restaurant"}');

      return http.Response('{}', 200);
    });

    final client = AuthenticatedApiClient(
      baseUri: Uri.parse(baseUri),
      authRepository: authRepository,
      httpClient: httpClient,
    );

    final response = await client.put(
      '/api/v1/restaurant/restaurants/restaurant-1',
      body: '{"name":"Updated Restaurant"}',
    );

    expect(response.statusCode, 200);

    client.close();
  });

  test('sends an authenticated PATCH request with a JSON body', () async {
    final authRepository = FakeAuthRepository(token: 'test-token');

    final httpClient = MockClient((request) async {
      expect(request.method, 'PATCH');
      expect(
        request.url.toString(),
        '$baseUri/api/v1/restaurant/orders/order-1/status',
      );
      expect(request.headers['Authorization'], 'Bearer test-token');
      expect(request.headers['Content-Type'], 'application/json');
      expect(request.body, '{"status":"ORDER_STATUS_ACCEPTED"}');

      return http.Response('{}', 200);
    });

    final client = AuthenticatedApiClient(
      baseUri: Uri.parse(baseUri),
      authRepository: authRepository,
      httpClient: httpClient,
    );

    final response = await client.patch(
      '/api/v1/restaurant/orders/order-1/status',
      body: '{"status":"ORDER_STATUS_ACCEPTED"}',
    );

    expect(response.statusCode, 200);

    client.close();
  });

  test('sends an authenticated DELETE request', () async {
    final authRepository = FakeAuthRepository(token: 'test-token');

    final httpClient = MockClient((request) async {
      expect(request.method, 'DELETE');
      expect(
        request.url.toString(),
        '$baseUri/api/v1/restaurant/restaurants/restaurant-1',
      );
      expect(request.headers['Authorization'], 'Bearer test-token');

      return http.Response('{}', 200);
    });

    final client = AuthenticatedApiClient(
      baseUri: Uri.parse(baseUri),
      authRepository: authRepository,
      httpClient: httpClient,
    );

    final response = await client.delete(
      '/api/v1/restaurant/restaurants/restaurant-1',
    );

    expect(response.statusCode, 200);

    client.close();
  });

  test('converts another non-success response into an ApiException', () async {
    final authRepository = FakeAuthRepository(token: 'test-token');

    final client = AuthenticatedApiClient(
      baseUri: Uri.parse(baseUri),
      authRepository: authRepository,
      httpClient: MockClient((request) async {
        return http.Response('Bad request', 400);
      }),
    );

    expect(
      () => client.post('/api/v1/customer/orders', body: '{}'),
      throwsA(
        isA<ApiException>()
            .having((error) => error.statusCode, 'statusCode', 400)
            .having((error) => error.message, 'message', 'API request failed.'),
      ),
    );

    client.close();
  });
}
