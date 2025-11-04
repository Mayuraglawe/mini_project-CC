# Deployment Readiness Check Script
Write-Host "Checking Render Deployment Readiness..." -ForegroundColor Cyan
Write-Host ""

$allGood = $true

# Check if Dockerfile exists
Write-Host "Checking for Dockerfile..." -NoNewline
if (Test-Path "Dockerfile") {
    Write-Host " Found" -ForegroundColor Green
} else {
    Write-Host " Missing" -ForegroundColor Red
    $allGood = $false
}

# Check if render.yaml exists
Write-Host "Checking for render.yaml..." -NoNewline
if (Test-Path "render.yaml") {
    Write-Host " Found" -ForegroundColor Green
} else {
    Write-Host " Missing" -ForegroundColor Red
    $allGood = $false
}

# Check if .gitignore exists
Write-Host "Checking for .gitignore..." -NoNewline
if (Test-Path ".gitignore") {
    Write-Host " Found" -ForegroundColor Green
} else {
    Write-Host " Not found (recommended)" -ForegroundColor Yellow
}

# Check if pom.xml exists
Write-Host "Checking for pom.xml..." -NoNewline
if (Test-Path "pom.xml") {
    Write-Host " Found" -ForegroundColor Green
} else {
    Write-Host " Missing" -ForegroundColor Red
    $allGood = $false
}

# Check if src directory exists
Write-Host "Checking for src directory..." -NoNewline
if (Test-Path "src") {
    Write-Host " Found" -ForegroundColor Green
} else {
    Write-Host " Missing" -ForegroundColor Red
    $allGood = $false
}

# Check if Java files exist
Write-Host "Checking for Java files..." -NoNewline
$javaFiles = Get-ChildItem -Path "src" -Filter "*.java" -Recurse -ErrorAction SilentlyContinue
if ($javaFiles.Count -gt 0) {
    Write-Host " Found $($javaFiles.Count) files" -ForegroundColor Green
} else {
    Write-Host " No Java files found" -ForegroundColor Red
    $allGood = $false
}

# Check if git is initialized
Write-Host "Checking if git is initialized..." -NoNewline
if (Test-Path ".git") {
    Write-Host " Git repository exists" -ForegroundColor Green
} else {
    Write-Host " Not initialized" -ForegroundColor Yellow
    Write-Host "   Run: git init" -ForegroundColor Gray
}

# Check Docker installation
Write-Host "Checking if Docker is installed..." -NoNewline
try {
    $dockerCmd = Get-Command docker -ErrorAction SilentlyContinue
    if ($dockerCmd) {
        Write-Host " Installed" -ForegroundColor Green
    } else {
        Write-Host " Not found (optional)" -ForegroundColor Yellow
    }
} catch {
    Write-Host " Not found (optional)" -ForegroundColor Yellow
}

# Check Maven installation
Write-Host "Checking if Maven is installed..." -NoNewline
try {
    $mvnCmd = Get-Command mvn -ErrorAction SilentlyContinue
    if ($mvnCmd) {
        Write-Host " Installed" -ForegroundColor Green
    } else {
        Write-Host " Not found (wrapper available)" -ForegroundColor Yellow
    }
} catch {
    Write-Host " Not found (wrapper available)" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Gray

if ($allGood) {
    Write-Host "All required files are present!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Next Steps:" -ForegroundColor Cyan
    Write-Host "   1. Push your code to GitHub" -ForegroundColor White
    Write-Host "      git add ." -ForegroundColor Gray
    Write-Host "      git commit -m 'Add Render deployment'" -ForegroundColor Gray
    Write-Host "      git push origin main" -ForegroundColor Gray
    Write-Host ""
    Write-Host "   2. Deploy on Render" -ForegroundColor White
    Write-Host "      -> https://dashboard.render.com/" -ForegroundColor Gray
    Write-Host ""
    Write-Host "   3. Access your deployed app" -ForegroundColor White
    Write-Host "      -> https://your-app-name.onrender.com/html.html" -ForegroundColor Gray
    Write-Host ""
    Write-Host "Full guide: DEPLOYMENT.md" -ForegroundColor Cyan
    Write-Host "Quick start: RENDER_QUICKSTART.md" -ForegroundColor Cyan
} else {
    Write-Host "Some required files are missing!" -ForegroundColor Red
    Write-Host "Please ensure all required files are present before deploying." -ForegroundColor Yellow
}

Write-Host ""
