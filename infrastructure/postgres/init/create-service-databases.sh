#!/usr/bin/env bash

set -Eeuo pipefail

psql \
  --username "$POSTGRES_USER" \
  --dbname "$POSTGRES_DB" \
  --set=order_user="$ORDER_DB_USER" \
  --set=order_password="$ORDER_DB_PASSWORD" \
  --set=order_db="$ORDER_DB_NAME" \
  --set=payment_user="$PAYMENT_DB_USER" \
  --set=payment_password="$PAYMENT_DB_PASSWORD" \
  --set=payment_db="$PAYMENT_DB_NAME" \
  --set=ON_ERROR_STOP=1 <<'EOSQL'

SELECT format(
    'CREATE ROLE %I LOGIN PASSWORD %L',
    :'order_user',
    :'order_password'
)
WHERE NOT EXISTS (
    SELECT 1
    FROM pg_roles
    WHERE rolname = :'order_user'
)
\gexec

SELECT format(
    'CREATE ROLE %I LOGIN PASSWORD %L',
    :'payment_user',
    :'payment_password'
)
WHERE NOT EXISTS (
    SELECT 1
    FROM pg_roles
    WHERE rolname = :'payment_user'
)
\gexec

SELECT format(
    'CREATE DATABASE %I OWNER %I',
    :'order_db',
    :'order_user'
)
WHERE NOT EXISTS (
    SELECT 1
    FROM pg_database
    WHERE datname = :'order_db'
)
\gexec

SELECT format(
    'CREATE DATABASE %I OWNER %I',
    :'payment_db',
    :'payment_user'
)
WHERE NOT EXISTS (
    SELECT 1
    FROM pg_database
    WHERE datname = :'payment_db'
)
\gexec

SELECT format(
    'REVOKE CONNECT ON DATABASE %I FROM PUBLIC',
    :'order_db'
)
\gexec

SELECT format(
    'GRANT CONNECT ON DATABASE %I TO %I',
    :'order_db',
    :'order_user'
)
\gexec

SELECT format(
    'REVOKE CONNECT ON DATABASE %I FROM PUBLIC',
    :'payment_db'
)
\gexec

SELECT format(
    'GRANT CONNECT ON DATABASE %I TO %I',
    :'payment_db',
    :'payment_user'
)
\gexec

EOSQL