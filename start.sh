#!/usr/bin/env bash
set -e

PORT="$1"
PROJECT="stockmarket"
DB_NAME="stockmarket-db"

docker build -t stockmarket:latest .

# Reuse existing DB container if it already exists.
if docker ps -a --format '{{.Names}}' | grep -Fxq "$DB_NAME"; then
  if [ "$(docker inspect -f '{{.State.Running}}' "$DB_NAME")" != "true" ]; then
    docker start "$DB_NAME" >/dev/null
  fi
  echo "Using existing DB container: $DB_NAME"
else
  docker compose -p "$PROJECT" up -d db
fi

# Use the DB container network dynamically to avoid hardcoded network-name issues.
APP_NETWORK="$(docker inspect -f '{{range $name, $_ := .NetworkSettings.Networks}}{{println $name}}{{end}}' "$DB_NAME" | head -n1 | tr -d '[:space:]')"
if [ -z "$APP_NETWORK" ]; then
  echo "Could not resolve network for $DB_NAME"
  exit 1
fi
echo "Using network: $APP_NETWORK"

docker rm -f "stockmarket-app-$PORT" 2>/dev/null || true
docker run -d \
  --name "stockmarket-app-$PORT" \
  --network "$APP_NETWORK" \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://stockmarket-db:5432/stockmarket \
  -e SPRING_DATASOURCE_USERNAME=sa \
  -e SPRING_DATASOURCE_PASSWORD=secret \
  -e SERVER_PORT=8080 \
  -p "${PORT}:8080" \
  stockmarket:latest

echo "App is starting on port $PORT..."
while true; do
  status=$(curl -s "http://localhost:$PORT/actuator/health" | sed -n 's/.*"status":"\([^"]*\)".*/\1/p')
  [ "$status" = "UP" ] && break
  sleep 2
done
echo "App running at http://localhost:$PORT"