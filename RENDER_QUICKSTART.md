# 🚀 Quick Start - Deploy to Render

## Files Created ✅

1. **Dockerfile** - Multi-stage Docker build
2. **render.yaml** - Render configuration
3. **.dockerignore** - Docker build exclusions
4. **.gitignore** - Git exclusions
5. **application.properties** (updated) - Port configuration
6. **DEPLOYMENT.md** - Complete deployment guide

---

## Deployment Steps (5 Minutes)

### 1️⃣ Push to GitHub

```powershell
# Initialize git
git init

# Add files
git add .

# Commit
git commit -m "Add Render deployment configuration"

# Add remote (replace with your repo URL)
git remote add origin https://github.com/YOUR_USERNAME/mini_project-CC.git

# Push
git push -u origin main
```

---

### 2️⃣ Deploy on Render

1. **Go to**: [https://dashboard.render.com/](https://dashboard.render.com/)
2. **Click**: "New +" → "Web Service"
3. **Connect**: Your GitHub repository
4. **Select**: `mini_project-CC`
5. **Verify settings**:
   - Runtime: **Docker**
   - Dockerfile Path: `./Dockerfile`
   - Plan: **Free**
6. **Click**: "Create Web Service"
7. **Wait**: 3-5 minutes for build

---

### 3️⃣ Access Your App

Your app will be available at:
```
https://your-app-name.onrender.com/html.html
```

**Test endpoints**:
- Health: `https://your-app-name.onrender.com/api/compile/health`
- API: `https://your-app-name.onrender.com/api/compile/tac`

---

## ⚡ Important Notes

### Free Tier Limitations:
- ⚠️ **Spins down** after 15 minutes of inactivity
- ⚠️ **Cold start** takes ~30 seconds on first request
- ✅ **750 hours/month** free
- ✅ **Automatic HTTPS**

### Memory Configuration:
- Default: 512MB RAM
- Configured with: `-Xmx512m -Xms256m`

---

## 🧪 Test Locally First (Optional)

```powershell
# Build Docker image
docker build -t tac-converter .

# Run container
docker run -p 8080:8080 tac-converter

# Test
curl http://localhost:8080/api/compile/health
```

---

## 📖 Full Guide

For detailed instructions, troubleshooting, and configuration options, see:
**[DEPLOYMENT.md](./DEPLOYMENT.md)**

---

## 🎯 That's It!

Your TAC to Assembly Converter is now ready to deploy! 🎉
