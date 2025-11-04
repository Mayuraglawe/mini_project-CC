# Quick Start Script for TAC to Assembly Converter
Write-Host "Starting TAC to Assembly Converter..." -ForegroundColor Cyan
Write-Host ""

# Check if already built
if (-not (Test-Path "target\LowLevel_Language-0.0.1-SNAPSHOT.jar")) {
    Write-Host "Building project (first time)..." -ForegroundColor Yellow
    .\mvnw.cmd clean package -DskipTests
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Build failed!" -ForegroundColor Red
        exit 1
    }
    Write-Host "Build complete!" -ForegroundColor Green
    Write-Host ""
}

Write-Host "Starting Spring Boot application..." -ForegroundColor Cyan
Write-Host ""
Write-Host "========================================" -ForegroundColor Gray
Write-Host "Application will be available at:" -ForegroundColor Green
Write-Host "  http://localhost:8080" -ForegroundColor White
Write-Host "  http://localhost:8080/index.html" -ForegroundColor White
Write-Host ""
Write-Host "API Endpoints:" -ForegroundColor Green
Write-Host "  http://localhost:8080/api/compile/health" -ForegroundColor White
Write-Host "  http://localhost:8080/api/compile/tac" -ForegroundColor White
Write-Host "  http://localhost:8080/api/compile/v2/complete" -ForegroundColor White
Write-Host ""
Write-Host "Press Ctrl+C to stop the application" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Gray
Write-Host ""

# Start the application
java -jar target\LowLevel_Language-0.0.1-SNAPSHOT.jar
