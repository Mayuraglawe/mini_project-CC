# Rebuild and Start Script
Write-Host "Rebuilding TAC to Assembly Converter..." -ForegroundColor Cyan
Write-Host ""

# Clean and build
Write-Host "Running Maven build..." -ForegroundColor Yellow
.\mvnw.cmd clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "Build failed! Please check the error messages above." -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Build successful!" -ForegroundColor Green
Write-Host ""
Write-Host "Starting application..." -ForegroundColor Cyan
Write-Host ""
Write-Host "========================================" -ForegroundColor Gray
Write-Host "Application will be available at:" -ForegroundColor Green
Write-Host "  http://localhost:8080" -ForegroundColor White
Write-Host ""
Write-Host "Press Ctrl+C to stop the application" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Gray
Write-Host ""

# Start the application
java -jar target\LowLevel_Language-0.0.1-SNAPSHOT.jar
