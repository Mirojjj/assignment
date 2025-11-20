# Backend Restart Instructions

## Swagger UI Support Added

New dependencies have been added to enable Swagger UI. To apply these changes:

### Option 1: Quick Restart (If backend is running in a terminal you can access)
1. Go to the backend terminal
2. Press `Ctrl+C` to stop the server
3. Run: `mvn clean compile`
4. Run: `mvn mn:run`

### Option 2: Kill and Restart (If backend is running in background)
1. Open PowerShell
2. Run: `Get-Process | Where-Object { $_.ProcessName -like "*java*" } | Stop-Process -Force`
3. Navigate to backend directory: `cd "c:\Users\CITYTECH\projects\GroupManagement\docs\assignment-junior-developer\part3-backend-challenge"`
4. Rebuild: `mvn clean compile`
5. Start: `mvn mn:run`

### After Restart

Swagger UI will be available at:
- **Swagger UI**: http://localhost:8080/swagger-ui
- **OpenAPI Spec**: http://localhost:8080/swagger/payment-platform-api-1.0.yml
- **API Status**: http://localhost:8080/api/v1/status
- **Transactions**: http://localhost:8080/api/v1/merchants/{merchantId}/transactions

## Changes Made
- Added `micronaut-openapi-annotations` dependency
- Enabled OpenAPI views in application.yml
- Configured Swagger UI static resources
