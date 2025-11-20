# Rebuild Backend Without Stopping It
# This script compiles Java changes while the backend is running in watch mode

Write-Host "Rebuilding backend (Java compilation only)..." -ForegroundColor Cyan

Push-Location "c:\Users\CITYTECH\projects\GroupManagement\docs\assignment-junior-developer\part3-backend-challenge"

# Compile without cleaning (preserves running state)
mvn compile

Pop-Location

Write-Host "`nDone! Backend should auto-reload the changes." -ForegroundColor Green
Write-Host "Test at: http://localhost:8080/api/v1/merchants/MCH-00001/transactions" -ForegroundColor Yellow
