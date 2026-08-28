package main

import (
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestLivenessEndpoint(t *testing.T) {
	request := httptest.NewRequest(
		http.MethodGet,
		"/health/live",
		nil,
	)

	recorder := httptest.NewRecorder()

	newHandler().ServeHTTP(
		recorder,
		request,
	)

	if recorder.Code != http.StatusOK {
		t.Fatalf(
			"expected status %d, received %d",
			http.StatusOK,
			recorder.Code,
		)
	}

	contentType :=
		recorder.Header().Get("Content-Type")

	if contentType != "application/json" {
		t.Fatalf(
			"expected application/json, received %q",
			contentType,
		)
	}
}
