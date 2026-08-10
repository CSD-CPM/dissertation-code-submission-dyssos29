package main

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"log/slog"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"
)

const serviceName = "payment-service"

type healthResponse struct {
	Service string `json:"service"`
	Status  string `json:"status"`
}

func main() {
	if err := run(); err != nil {
		slog.Error(
			"service stopped with an error",
			"service", serviceName,
			"error", err,
		)

		os.Exit(1)
	}
}

func run() error {
	port := os.Getenv("SERVER_PORT")

	if port == "" {
		port = "8080"
	}

	server := &http.Server{
		Addr:              ":" + port,
		Handler:           newHandler(),
		ReadHeaderTimeout: 5 * time.Second,
		ReadTimeout:       10 * time.Second,
		WriteTimeout:      10 * time.Second,
		IdleTimeout:       60 * time.Second,
	}

	serverError := make(chan error, 1)

	go func() {
		slog.Info(
			"service started",
			"service", serviceName,
			"port", port,
		)

		serverError <- server.ListenAndServe()
	}()

	shutdownContext, stop := signal.NotifyContext(
		context.Background(),
		os.Interrupt,
		syscall.SIGTERM,
	)

	defer stop()

	select {
	case err := <-serverError:
		if errors.Is(err, http.ErrServerClosed) {
			return nil
		}

		return fmt.Errorf("HTTP server failed: %w", err)

	case <-shutdownContext.Done():
		slog.Info(
			"shutdown requested",
			"service", serviceName,
		)
	}

	timeoutContext, cancel := context.WithTimeout(
		context.Background(),
		10*time.Second,
	)

	defer cancel()

	if err := server.Shutdown(timeoutContext); err != nil {
		return fmt.Errorf(
			"graceful shutdown failed: %w",
			err,
		)
	}

	return nil
}

func newHandler() http.Handler {
	mux := http.NewServeMux()

	mux.HandleFunc("/health/live", writeHealth)
	mux.HandleFunc("/health/ready", writeHealth)

	return mux
}

func writeHealth(
	responseWriter http.ResponseWriter,
	_ *http.Request,
) {
	responseWriter.Header().
		Set("Content-Type", "application/json")

	responseWriter.WriteHeader(http.StatusOK)

	response := healthResponse{
		Service: serviceName,
		Status:  "UP",
	}

	if err := json.NewEncoder(responseWriter).
		Encode(response); err != nil {

		slog.Error(
			"failed to encode health response",
			"error", err,
		)
	}
}
