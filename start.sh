#!/usr/bin/env bash

PORT="$1"

docker build -t stockmarket:latest .
docker compose up -d db

docker rm -f "stockmarket-app-$PORT" 2>/dev/null
docker run -d \
  --name "stockmarket-app-$PORT" \
  --network stockmarket_default \
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