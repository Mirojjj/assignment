#!/bin/bash

# Post-start script for GitHub Codespaces
# Runs every time the container starts

set -e

echo "🔄 Running post-start tasks..."

# Ensure Docker services are running
echo "🐳 Checking Docker services..."
docker-compose -f .devcontainer/docker-compose.yml up -d

# Wait for services to be healthy
echo "⏳ Waiting for services to be healthy..."
sleep 5

# Check PostgreSQL
if docker exec payment-platform-db pg_isready -U postgres > /dev/null 2>&1; then
    echo "✅ PostgreSQL is ready"
else
    echo "⚠️  PostgreSQL is not ready yet"
fi

# Check Redis
if docker exec payment-platform-cache redis-cli ping > /dev/null 2>&1; then
    echo "✅ Redis is ready"
else
    echo "⚠️  Redis is not ready yet"
fi

echo "✨ Post-start tasks completed!"
