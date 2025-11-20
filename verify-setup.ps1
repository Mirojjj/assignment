# Verify Assessment Setup - PowerShell Script
# This script checks that all components are ready to run

Write-Host "Verifying Junior Developer Assessment Setup" -ForegroundColor Cyan
Write-Host "============================================`n"

$passCount = 0
$failCount = 0
$warnCount = 0

function Test-Requirement {
    param(
        [string]$Name,
        [scriptblock]$Test,
        [string]$FailMessage,
        [bool]$Required = $true
    )
    
    Write-Host "Testing: $Name..." -NoNewline
    try {
        $result = & $Test
        if ($result) {
            Write-Host " PASS" -ForegroundColor Green
            $script:passCount++
            return $true
        } else {
            if ($Required) {
                Write-Host " FAIL" -ForegroundColor Red
                Write-Host "  -> $FailMessage" -ForegroundColor Yellow
                $script:failCount++
            } else {
                Write-Host " WARN" -ForegroundColor Yellow
                Write-Host "  -> $FailMessage" -ForegroundColor Gray
                $script:warnCount++
            }
            return $false
        }
    } catch {
        if ($Required) {
            Write-Host " ERROR" -ForegroundColor Red
            Write-Host "  -> $($_.Exception.Message)" -ForegroundColor Yellow
            $script:failCount++
        } else {
            Write-Host " WARN" -ForegroundColor Yellow
            $script:warnCount++
        }
        return $false
    }
}

# Navigate to assignment directory
Set-Location $PSScriptRoot

Write-Host "`nCHECKING FILE STRUCTURE" -ForegroundColor Cyan
Write-Host "=======================`n"

Test-Requirement -Name "README.md exists" -Test {
    Test-Path "README.md"
} -FailMessage "Main README is missing"

Test-Requirement -Name "docker-compose.yml exists" -Test {
    Test-Path "docker-compose.yml"
} -FailMessage "Docker Compose configuration is missing"

Test-Requirement -Name "Part 1 (Database) files" -Test {
    (Test-Path "part1-database-challenge/schema.sql") -and
    (Test-Path "part1-database-challenge/sample-data.sql") -and
    (Test-Path "part1-database-challenge/original-query.sql")
} -FailMessage "Database challenge files incomplete"

Test-Requirement -Name "Part 3 (Backend) structure" -Test {
    (Test-Path "part3-backend-challenge/pom.xml") -and
    (Test-Path "part3-backend-challenge/src/main/java/com/payment/Application.java")
} -FailMessage "Backend boilerplate incomplete"

Test-Requirement -Name "Part 4 (Frontend) structure" -Test {
    (Test-Path "part4-frontend-challenge/package.json") -and
    (Test-Path "part4-frontend-challenge/src/App.tsx")
} -FailMessage "Frontend boilerplate incomplete"

Test-Requirement -Name "Part 5 (Debugging) files" -Test {
    (Test-Path "part5-debugging-challenge/buggy-backend/PaymentProcessingService.java") -and
    (Test-Path "part5-debugging-challenge/buggy-frontend/TransactionList.tsx") -and
    (Test-Path "part5-debugging-challenge/buggy-sql/settlement-report.sql")
} -FailMessage "Debugging challenge files incomplete"

Write-Host "`nCHECKING PREREQUISITES" -ForegroundColor Cyan
Write-Host "======================`n"

Test-Requirement -Name "Docker Desktop installed" -Test {
    Get-Command docker -ErrorAction SilentlyContinue
} -FailMessage "Docker is not installed"

Test-Requirement -Name "Docker is running" -Test {
    docker info 2>$null
    $LASTEXITCODE -eq 0
} -FailMessage "Docker Desktop is not running" -Required $true

Test-Requirement -Name "Java 17+ installed" -Test {
    $javaVersion = & java -version 2>&1 | Select-String "version"
    if ($javaVersion -match '"(\d+)\.') {
        [int]$matches[1] -ge 17
    } else { $false }
} -FailMessage "Java 17+ is required" -Required $false

Test-Requirement -Name "Node.js 18+ installed" -Test {
    $nodeVersion = & node --version 2>$null
    if ($nodeVersion -match 'v(\d+)\.') {
        [int]$matches[1] -ge 18
    } else { $false }
} -FailMessage "Node.js 18+ is required" -Required $false

Write-Host "`nCHECKING CODE STRUCTURE" -ForegroundColor Cyan
Write-Host "=======================`n"

Test-Requirement -Name "Backend entities exist" -Test {
    (Test-Path "part3-backend-challenge/src/main/java/com/payment/entity/TransactionMaster.java") -and
    (Test-Path "part3-backend-challenge/src/main/java/com/payment/entity/Member.java")
} -FailMessage "Backend entity classes missing"

Test-Requirement -Name "Backend repositories exist" -Test {
    (Test-Path "part3-backend-challenge/src/main/java/com/payment/repository/TransactionRepository.java")
} -FailMessage "Backend repository interfaces missing"

Test-Requirement -Name "Frontend types exist" -Test {
    Test-Path "part4-frontend-challenge/src/types/transaction.ts"
} -FailMessage "Frontend type definitions missing"

Test-Requirement -Name "Frontend components exist" -Test {
    (Test-Path "part4-frontend-challenge/src/components/common/Button.tsx") -and
    (Test-Path "part4-frontend-challenge/src/components/common/Table.tsx")
} -FailMessage "Frontend UI components missing"

Write-Host "`nVERIFICATION SUMMARY" -ForegroundColor Cyan
Write-Host "====================`n"

Write-Host "Total Tests: $($passCount + $failCount + $warnCount)"
Write-Host "  Passed: $passCount" -ForegroundColor Green
if ($warnCount -gt 0) {
    Write-Host "  Warnings: $warnCount" -ForegroundColor Yellow
}
if ($failCount -gt 0) {
    Write-Host "  Failed: $failCount" -ForegroundColor Red
}

Write-Host ""

if ($failCount -eq 0) {
    Write-Host "SUCCESS! Setup verification PASSED. Ready for candidates." -ForegroundColor Green
    Write-Host ""
    Write-Host "Next steps:" -ForegroundColor Cyan
    Write-Host "  1. Review GETTING_STARTED.md"
    Write-Host "  2. Test with: .\start-dev.ps1"
    Write-Host "  3. Verify database connectivity"
    Write-Host ""
    exit 0
} else {
    Write-Host "WARNING: Setup verification found issues." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Troubleshooting:" -ForegroundColor Cyan
    Write-Host "  - Check Docker Desktop is running"
    Write-Host "  - Verify prerequisites are installed"
    Write-Host "  - Review SETUP.md for requirements"
    Write-Host ""
    exit 1
}
