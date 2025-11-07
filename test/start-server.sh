#!/bin/bash

echo "🚀 Starting Labee Backend Server..."
echo ""
echo "📍 Server will run on: http://localhost:8080"
echo "📝 Press Ctrl+C to stop the server"
echo ""
echo "⏳ Starting..."
echo ""

cd "$(dirname "$0")"
./mvnw spring-boot:run
