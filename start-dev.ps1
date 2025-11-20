# Start all services for local development (Windows PowerShell)

Write-Host "🚀 Starting Payment Platform Development Environment" -ForegroundColor Green
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Check if Docker is running
try {
    docker info | Out-Null
} catch {
    Write-Host "❌ Docker is not running. Please start Docker Desktop and try again." -ForegroundColor Red
    exit 1
}

# Navigate to assignment directory
Set-Location $PSScriptRoot

# Start PostgreSQL
Write-Host ""
Write-Host "📦 Starting PostgreSQL database..." -ForegroundColor Cyan
docker-compose up -d postgres

# Wait for PostgreSQL to be healthy
Write-Host "⏳ Waiting for PostgreSQL to be ready..." -ForegroundColor Yellow
for ($i = 1; $i -le 30; $i++) {
    try {
        docker exec payment-platform-db pg_isready -U postgres 2>&1 | Out-Null
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ PostgreSQL is ready!" -ForegroundColor Green
            break
        }
    } catch {
        # Continue waiting
    }
    Write-Host "   Waiting... ($i/30)"
    Start-Sleep -Seconds 2
}

# Display database info
Write-Host ""
Write-Host "🗄️  Database Information:" -ForegroundColor Cyan
Write-Host "   Host: localhost:5432"
Write-Host "   Database: payment_platform"
Write-Host "   Username: postgres"
Write-Host "   Password: postgres"

# Check if backend should be started
Write-Host ""
$startBackend = Read-Host "Start Backend API? (Y/n)"
if ($startBackend -eq "" -or $startBackend -eq "Y" -or $startBackend -eq "y") {
    Write-Host "☕ Starting Backend API (this may take a few minutes)..." -ForegroundColor Cyan
    Set-Location part3-backend-challenge
    Start-Process -NoNewWindow -FilePath "./mvnw.cmd" -ArgumentList "mn:run"
    Set-Location ..
}

# Check if frontend should be started
Write-Host ""
$startFrontend = Read-Host "Start Frontend? (Y/n)"
if ($startFrontend -eq "" -or $startFrontend -eq "Y" -or $startFrontend -eq "y") {
    Write-Host "⚛️  Starting Frontend..." -ForegroundColor Cyan
    Set-Location part4-frontend-challenge
    
    # Install dependencies if node_modules doesn't exist
    if (-not (Test-Path "node_modules")) {
        Write-Host "   Installing dependencies..."
        npm install
    }
    
    # Copy env file if it doesn't exist
    if (-not (Test-Path ".env.local")) {
        Write-Host "   Creating .env.local..."
        Copy-Item .env.example .env.local
    }
    
    Start-Process -NoNewWindow -FilePath "npm" -ArgumentList "run", "dev"
    Set-Location ..
}

Write-Host ""
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Green
Write-Host "✨ Development environment is starting!" -ForegroundColor Green
Write-Host ""
Write-Host "📚 Services:" -ForegroundColor Cyan
Write-Host "   🗄️  PostgreSQL:    http://localhost:5432"
Write-Host "   ☕ Backend API:    http://localhost:8080"
Write-Host "   📖 API Docs:       http://localhost:8080/swagger-ui"
Write-Host "   ⚛️  Frontend:       http://localhost:3000"
Write-Host ""
Write-Host "🛠️  Quick Commands:" -ForegroundColor Cyan
Write-Host "   Connect to DB:   docker exec -it payment-platform-db psql -U postgres -d payment_platform"
Write-Host "   Stop all:        docker-compose down"
Write-Host "   View logs:       docker-compose logs -f postgres"
Write-Host ""
Write-Host "📝 Press Ctrl+C to stop, or close terminal windows" -ForegroundColor Yellow
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Green
