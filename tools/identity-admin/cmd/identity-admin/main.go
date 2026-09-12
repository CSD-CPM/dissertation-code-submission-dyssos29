package main

import (
	"context"
	"fmt"
	"log"
	"os"
	"strings"

	firebase "firebase.google.com/go/v4"
)

const projectID = "food-delivery-platform-msc"

var allowedRoles = map[string]struct{}{
	"CUSTOMER":         {},
	"RESTAURANT_OWNER": {},
	"COURIER":          {},
	"ADMIN":            {},
}

func main() {
	if len(os.Args) < 2 {
		printUsage()
		os.Exit(2)
	}

	ctx := context.Background()

	app, err := firebase.NewApp(ctx, &firebase.Config{
		ProjectID: projectID,
	})
	if err != nil {
		log.Fatalf("initialize Firebase Admin SDK: %v", err)
	}

	client, err := app.Auth(ctx)
	if err != nil {
		log.Fatalf("create Firebase Auth client: %v", err)
	}

	switch os.Args[1] {
	case "set-role":
		if len(os.Args) != 4 {
			printUsage()
			os.Exit(2)
		}

		uid := strings.TrimSpace(os.Args[2])
		role := strings.ToUpper(strings.TrimSpace(os.Args[3]))

		if uid == "" {
			log.Fatal("UID must not be empty")
		}

		if _, ok := allowedRoles[role]; !ok {
			log.Fatalf(
				"invalid role %q; allowed roles: CUSTOMER, RESTAURANT_OWNER, COURIER, ADMIN",
				role,
			)
		}

		user, err := client.GetUser(ctx, uid)
		if err != nil {
			log.Fatalf("get user %q: %v", uid, err)
		}

		claims := make(map[string]interface{}, len(user.CustomClaims)+1)
		for key, value := range user.CustomClaims {
			claims[key] = value
		}

		claims["role"] = role

		if err := client.SetCustomUserClaims(ctx, uid, claims); err != nil {
			log.Fatalf("set role for user %q: %v", uid, err)
		}

		fmt.Printf(
			"Role assigned successfully.\nuid=%s\nemail=%s\nrole=%s\n",
			user.UID,
			user.Email,
			role,
		)

	case "get-user":
		if len(os.Args) != 3 {
			printUsage()
			os.Exit(2)
		}

		uid := strings.TrimSpace(os.Args[2])

		user, err := client.GetUser(ctx, uid)
		if err != nil {
			log.Fatalf("get user %q: %v", uid, err)
		}

		fmt.Printf("uid=%s\n", user.UID)
		fmt.Printf("email=%s\n", user.Email)
		fmt.Printf("disabled=%t\n", user.Disabled)

		if role, ok := user.CustomClaims["role"]; ok {
			fmt.Printf("role=%v\n", role)
		} else {
			fmt.Println("role=<not assigned>")
		}

	default:
		printUsage()
		os.Exit(2)
	}
}

func printUsage() {
	fmt.Println("Usage:")
	fmt.Println("  identity-admin set-role <uid> <role>")
	fmt.Println("  identity-admin get-user <uid>")
	fmt.Println()
	fmt.Println("Allowed roles:")
	fmt.Println("  CUSTOMER")
	fmt.Println("  RESTAURANT_OWNER")
	fmt.Println("  COURIER")
	fmt.Println("  ADMIN")
}
