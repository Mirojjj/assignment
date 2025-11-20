#!/bin/bash

# Post-create script for GitHub Codespaces
# Runs once when the container is created

set -e

echo "🚀 Running post-create setup..."

# Update package lists
echo "📦 Updating packages..."
sudo apt-get update

# Install additional tools
echo "🔧 Installing additional tools..."
sudo apt-get install -y \
    postgresql-client \
    curl \
    wget \
    jq \
    tree

# Set up Maven settings
echo "⚙️  Configuring Maven..."
mkdir -p ~/.m2
cat > ~/.m2/settings.xml <<EOF
<settings>
  <localRepository>/home/vscode/.m2/repository</localRepository>
</settings>
EOF

# Backend: Install dependencies
if [ -d "part3-backend-challenge" ]; then
    echo "☕ Installing backend dependencies..."
    cd part3-backend-challenge
    ./mvnw dependency:go-offline -DskipTests || true
    cd ..
fi

# Frontend: Install dependencies
if [ -d "part4-frontend-challenge" ]; then
    echo "⚛️  Installing frontend dependencies..."
    cd part4-frontend-challenge
    npm ci || npm install
    cd ..
fi

# Create database initialization script
echo "🗄️  Setting up database initialization..."
mkdir -p .devcontainer/init-scripts

cat > .devcontainer/init-scripts/01-init.sql <<'EOF'
-- Create schemas
CREATE SCHEMA IF NOT EXISTS operators;

-- Grant permissions
GRANT ALL ON SCHEMA operators TO postgres;

-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- Log initialization
DO $$
BEGIN
    RAISE NOTICE 'Database payment_platform initialized successfully';
END $$;
EOF

# Start database services
echo "🐘 Starting PostgreSQL..."
docker-compose -f .devcontainer/docker-compose.yml up -d

# Wait for database to be ready
echo "⏳ Waiting for database to be ready..."
for i in {1..30}; do
    if docker exec payment-platform-db pg_isready -U postgres > /dev/null 2>&1; then
        echo "✅ Database is ready!"
        break
    fi
    echo "   Waiting... ($i/30)"
    sleep 2
done

# Run database migrations (if using Flyway)
if [ -d "part3-backend-challenge/src/main/resources/db/migration" ]; then
    echo "🔄 Running database migrations..."
    cd part3-backend-challenge
    ./mvnw flyway:migrate || echo "⚠️  Migrations will run on application startup"
    cd ..
fi

# Create .env files if they don't exist
echo "📝 Creating environment files..."

if [ ! -f "part3-backend-challenge/.env" ]; then
    cat > part3-backend-challenge/.env <<EOF
DATASOURCES_DEFAULT_URL=jdbc:postgresql://localhost:5432/payment_platform
DATASOURCES_DEFAULT_USERNAME=postgres
DATASOURCES_DEFAULT_PASSWORD=postgres
EOF
fi

if [ ! -f "part4-frontend-challenge/.env.local" ]; then
    cat > part4-frontend-challenge/.env.local <<EOF
VITE_API_BASE_URL=http://localhost:8080/api/v1
VITE_API_TIMEOUT=30000
VITE_ENABLE_MOCK_API=false
EOF
fi

# Set up Git configuration
echo "🔧 Configuring Git..."
git config --global core.autocrlf input
git config --global pull.rebase false

# Display helpful information
echo ""
echo "✨ Setup complete! ✨"
echo ""
echo "📚 Quick Start Guide:"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "🗄️  Database:"
echo "   • PostgreSQL is running on port 5432"
echo "   • Connection: postgresql://postgres:postgres@localhost:5432/payment_platform"
echo ""
echo "☕ Backend (Micronaut):"
echo "   cd part3-backend-challenge"
echo "   ./mvnw mn:run"
echo "   • API: http://localhost:8080"
echo "   • Swagger: http://localhost:8080/swagger-ui"
echo ""
echo "⚛️  Frontend (React):"
echo "   cd part4-frontend-challenge"
echo "   npm run dev"
echo "   • UI: http://localhost:3000"
echo ""
echo "🐳 Docker:"
echo "   docker-compose -f .devcontainer/docker-compose.yml ps"
echo ""
echo "📖 Read README.md for detailed instructions"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Create a welcome message file
cat > ~/.welcome <<EOF
╔═══════════════════════════════════════════════════════════════╗
║                                                               ║
║   🎯 Payment Platform - Junior Developer Assessment          ║
║                                                               ║
║   Environment: GitHub Codespaces                              ║
║   Ready to code! 🚀                                           ║
║                                                               ║
╚═══════════════════════════════════════════════════════════════╝

Quick Commands:
  • start-backend    - Start Micronaut API
  • start-frontend   - Start React app
  • start-all        - Start both services
  • db-console       - Connect to PostgreSQL
  • check-services   - View running services
EOF

# Create helper scripts
echo "🔨 Creating helper scripts..."

# Backend start script
cat > ~/start-backend.sh <<'EOF'
#!/bin/bash
cd /workspaces/*/part3-backend-challenge
echo "🚀 Starting Backend API..."
./mvnw mn:run
EOF
chmod +x ~/start-backend.sh

# Frontend start script
cat > ~/start-frontend.sh <<'EOF'
#!/bin/bash
cd /workspaces/*/part4-frontend-challenge
echo "🚀 Starting Frontend..."
npm run dev
EOF
chmod +x ~/start-frontend.sh

# Database console script
cat > ~/db-console.sh <<'EOF'
#!/bin/bash
docker exec -it payment-platform-db psql -U postgres -d payment_platform
EOF
chmod +x ~/db-console.sh

# Check services script
cat > ~/check-services.sh <<'EOF'
#!/bin/bash
echo "🔍 Checking Services..."
echo ""
echo "Docker Containers:"
docker-compose -f /workspaces/*/.devcontainer/docker-compose.yml ps
echo ""
echo "Port Status:"
curl -s http://localhost:8080/health > /dev/null 2>&1 && echo "✅ Backend API (8080): Running" || echo "❌ Backend API (8080): Not running"
curl -s http://localhost:3000 > /dev/null 2>&1 && echo "✅ Frontend (3000): Running" || echo "❌ Frontend (3000): Not running"
EOF
chmod +x ~/check-services.sh

# Add aliases to bashrc
cat >> ~/.bashrc <<'EOF'

# Payment Platform Aliases
alias start-backend='~/start-backend.sh'
alias start-frontend='~/start-frontend.sh'
alias db-console='~/db-console.sh'
alias check-services='~/check-services.sh'
alias start-all='start-backend & start-frontend'

# Show welcome message
if [ -f ~/.welcome ]; then
    cat ~/.welcome
fi
EOF

echo "✅ Post-create setup completed successfully!"
