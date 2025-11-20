#!/bin/bash
# Start all services for local development

set -e

echo "🚀 Starting Payment Platform Development Environment"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker and try again."
    exit 1
fi

# Navigate to assignment directory
cd "$(dirname "$0")"

# Start PostgreSQL
echo ""
echo "📦 Starting PostgreSQL database..."
docker-compose up -d postgres

# Wait for PostgreSQL to be healthy
echo "⏳ Waiting for PostgreSQL to be ready..."
for i in {1..30}; do
    if docker exec payment-platform-db pg_isready -U postgres > /dev/null 2>&1; then
        echo "✅ PostgreSQL is ready!"
        break
    fi
    echo "   Waiting... ($i/30)"
    sleep 2
done

# Display database info
echo ""
echo "🗄️  Database Information:"
echo "   Host: localhost:5432"
echo "   Database: payment_platform"
echo "   Username: postgres"
echo "   Password: postgres"

# Check if backend should be started
echo ""
read -p "Start Backend API? (Y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]] || [[ -z $REPLY ]]; then
    echo "☕ Starting Backend API (this may take a few minutes)..."
    cd part3-backend-challenge
    ./mvnw mn:run &
    BACKEND_PID=$!
    echo "   Backend PID: $BACKEND_PID"
    cd ..
fi

# Check if frontend should be started
echo ""
read -p "Start Frontend? (Y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]] || [[ -z $REPLY ]]; then
    echo "⚛️  Starting Frontend..."
    cd part4-frontend-challenge
    
    # Install dependencies if node_modules doesn't exist
    if [ ! -d "node_modules" ]; then
        echo "   Installing dependencies..."
        npm install
    fi
    
    # Copy env file if it doesn't exist
    if [ ! -f ".env.local" ]; then
        echo "   Creating .env.local..."
        cp .env.example .env.local
    fi
    
    npm run dev &
    FRONTEND_PID=$!
    echo "   Frontend PID: $FRONTEND_PID"
    cd ..
fi

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "✨ Development environment is starting!"
echo ""
echo "📚 Services:"
echo "   🗄️  PostgreSQL:    http://localhost:5432"
echo "   ☕ Backend API:    http://localhost:8080"
echo "   📖 API Docs:       http://localhost:8080/swagger-ui"
echo "   ⚛️  Frontend:       http://localhost:3000"
echo ""
echo "🛠️  Quick Commands:"
echo "   Connect to DB:   psql -U postgres -h localhost -d payment_platform"
echo "   Stop all:        ./stop-dev.sh"
echo "   View logs:       docker-compose logs -f postgres"
echo ""
echo "📝 To stop services, press Ctrl+C or run: ./stop-dev.sh"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Wait for user to stop
wait
