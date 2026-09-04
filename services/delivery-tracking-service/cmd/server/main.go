package main

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"log/slog"
	"net"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/dyssos29/food-delivery-platform/services/delivery-tracking-service/internal/grpcserver"
)

const (
	serviceName = "delivery-tracking-service"
	grpcPort    = "9090"
)

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

	grpcListener, err := net.Listen("tcp", ":"+grpcPort)
	if err != nil {
		return fmt.Errorf(
			"failed to create gRPC listener: %w",
			err,
		)
	}

	grpcServer := grpcserver.New()

	httpServerError := make(chan error, 1)
	grpcServerError := make(chan error, 1)

	go func() {
		slog.Info(
			"HTTP server started",
			"service", serviceName,
			"port", port,
		)

		httpServerError <- server.ListenAndServe()
	}()

	go func() {
		slog.Info(
			"gRPC server started",
			"service", serviceName,
			"port", grpcPort,
		)

		grpcServerError <- grpcServer.Serve(grpcListener)
	}()

	shutdownContext, stop := signal.NotifyContext(
		context.Background(),
		os.Interrupt,
		syscall.SIGTERM,
	)

	defer stop()

	var runError error

	select {
	case err := <-httpServerError:
		if !errors.Is(err, http.ErrServerClosed) {
			runError = fmt.Errorf(
				"HTTP server failed: %w",
				err,
			)
		}
	case err := <-grpcServerError:
		runError = fmt.Errorf(
			"gRPC server failed: %w",
			err,
		)
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
		if runError == nil {
			runError = fmt.Errorf(
				"HTTP graceful shutdown failed: %w",
				err,
			)
		}
	}

	grpcStopped := make(chan struct{})

	go func() {
		grpcServer.GracefulStop()
		close(grpcStopped)
	}()

	select {
	case <-grpcStopped:
		slog.Info(
			"gRPC server stopped",
			"service", serviceName,
		)

	case <-timeoutContext.Done():
		slog.Warn(
			"gRPC graceful shutdown timed out; forcing stop",
			"service", serviceName,
		)

		grpcServer.Stop()
	}

	return runError
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
