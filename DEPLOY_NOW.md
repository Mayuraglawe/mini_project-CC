# 🚀 Deployment Steps for Render - Complete Guide

## ✅ What We've Done

1. ✅ Created a new branch called `deployment`
2. ✅ Added all deployment files to the new branch
3. ✅ Committed the changes locally
4. ⏳ Need to push to GitHub (see steps below)

---

## 📋 Step-by-Step Deployment Process

### Step 1: Push to GitHub ⏳

You need to push the `deployment` branch to GitHub. You got a permission error, so let's fix that:

#### Option A: Using GitHub Desktop (Easiest)
1. Open **GitHub Desktop**
2. It should show the `deployment` branch
3. Click **"Publish branch"** or **"Push origin"**
4. Done! ✅

#### Option B: Using Git Command Line
```powershell
# Set your GitHub credentials (one-time setup)
git config --global user.name "Your GitHub Username"
git config --global user.email "your-email@example.com"

# Push the deployment branch
git push -u origin deployment
```

If you get authentication errors, you may need to:
- Use a **Personal Access Token** instead of password
- Generate one at: https://github.com/settings/tokens
- Or use **GitHub Desktop** (easier!)

---

### Step 2: Deploy on Render 🌐

Once your `deployment` branch is pushed to GitHub:

#### 1. Go to Render Dashboard
- Visit: **https://dashboard.render.com/**
- Sign up or log in (it's free!)
- You can sign in with your GitHub account

#### 2. Create New Web Service
- Click **"New +"** button (top right)
- Select **"Web Service"**

#### 3. Connect Your Repository
- Click **"Connect account"** if needed (authorize GitHub)
- Search for: **mini_project-CC**
- Click **"Connect"**

#### 4. Configure the Service

**Basic Settings:**
- **Name**: `tac-assembly-converter` (or any name you prefer)
- **Region**: Choose closest to you (e.g., Oregon, Frankfurt, Singapore)
- **Branch**: **deployment** ⭐ (select the deployment branch, NOT main!)
- **Root Directory**: Leave blank
- **Runtime**: **Docker**

**Build Settings:**
- **Dockerfile Path**: `./Dockerfile` (should auto-detect)
- **Docker Build Context Directory**: `.` (root)

**Plan:**
- **Instance Type**: **Free** (or choose paid if you want always-on)

**Advanced Settings (Auto-configured from render.yaml):**
- **Health Check Path**: `/api/compile/health`
- **Auto-Deploy**: Yes (deploys automatically on git push)

**Environment Variables:**
These should auto-configure from `render.yaml`, but verify:
| Key | Value |
|-----|-------|
| `JAVA_OPTS` | `-Xmx512m -Xms256m` |
| `SERVER_PORT` | `8080` |

#### 5. Create Web Service
- Review all settings
- Click **"Create Web Service"**
- Wait 3-5 minutes for the build

#### 6. Monitor Build
Watch the logs in real-time:
- You'll see Docker building your image
- Maven downloading dependencies
- Spring Boot packaging
- Container starting

#### 7. Check Deployment Status
- Wait for status to show **"Live"** (green)
- You'll get a URL like: `https://tac-assembly-converter.onrender.com`

---

### Step 3: Test Your Deployed App 🧪

Once deployment is complete:

#### Main Website:
```
https://your-app-name.onrender.com/
```

#### API Endpoints:
```
https://your-app-name.onrender.com/api/compile/health
https://your-app-name.onrender.com/api/compile/tac
https://your-app-name.onrender.com/api/compile/v2/complete
```

#### Test with curl:
```powershell
# Health check
curl https://your-app-name.onrender.com/api/compile/health

# Compile TAC
curl -X POST https://your-app-name.onrender.com/api/compile/tac -H "Content-Type: application/json" -d '{\"tacCode\": \"t1 = a + 5\\nt2 = b * 3\\nreturn t1\"}'
```

---

## 🎯 Current Status

### Local Branch Status:
```
Branch: deployment
Status: ✅ Committed locally
Files: 23 files changed (deployment configuration added)
Next: Push to GitHub
```

### What's in the Deployment Branch:
- ✅ Dockerfile (multi-stage build)
- ✅ render.yaml (Render configuration)
- ✅ .gitignore (clean commits)
- ✅ .dockerignore (efficient builds)
- ✅ Static files properly structured
- ✅ Maven wrapper configured
- ✅ All documentation files

### Main Branch Status:
```
Branch: main
Status: ✅ Unchanged (clean, original code)
```

---

## 🔄 Future Updates

After initial deployment, to update your app:

```powershell
# Make your changes

# Commit to deployment branch
git checkout deployment
git add .
git commit -m "Update feature"
git push origin deployment

# Render will automatically rebuild and deploy! 🎉
```

---

## 📱 Quick Commands Reference

```powershell
# Check current branch
git branch

# Switch to deployment branch
git checkout deployment

# Switch back to main
git checkout main

# Push deployment branch
git push origin deployment

# View commit history
git log --oneline
```

---

## 🆘 Troubleshooting

### Can't Push to GitHub?
**Problem**: Permission denied or 403 error

**Solutions**:
1. **Use GitHub Desktop** (easiest way)
2. **Create Personal Access Token**:
   - Go to: https://github.com/settings/tokens
   - Click "Generate new token (classic)"
   - Select scopes: `repo` (full access)
   - Copy the token
   - Use it as your password when pushing

3. **Configure Git credentials**:
   ```powershell
   git config --global credential.helper wincred
   ```

### Build Fails on Render?
- Check build logs in Render dashboard
- Verify Dockerfile is correct
- Ensure `deployment` branch is selected

### Application Won't Start?
- Check application logs in Render
- Verify port 8080 is exposed
- Check health endpoint configuration

---

## 💰 Free Tier Info

**Render Free Tier:**
- ✅ 750 hours/month
- ⚠️ Spins down after 15 min inactivity
- ⚠️ Cold start: ~30 seconds
- ✅ Automatic HTTPS
- ✅ Custom domains supported

**Upgrade to $7/month for:**
- Always-on service
- No cold starts
- More resources

---

## 🎊 Next Steps

1. **Push the deployment branch to GitHub** (using GitHub Desktop or git push)
2. **Go to Render Dashboard**: https://dashboard.render.com/
3. **Create Web Service** and connect your repository
4. **Select `deployment` branch** (important!)
5. **Deploy and test!**

---

## 📖 Additional Resources

- **Render Docs**: https://render.com/docs
- **Deployment Guide**: See `DEPLOYMENT.md` for detailed info
- **Quick Start**: See `RENDER_QUICKSTART.md`
- **Local Testing**: See `LOCAL_TESTING_SUCCESS.md`

---

## ✅ Checklist

- [x] Create deployment branch
- [x] Add all deployment files
- [x] Commit changes
- [ ] Push to GitHub (YOU ARE HERE)
- [ ] Create Render account
- [ ] Connect repository to Render
- [ ] Select deployment branch
- [ ] Configure settings
- [ ] Deploy
- [ ] Test deployed app

---

**You're almost there! Just push to GitHub and deploy on Render! 🚀**

## Quick Push Command:
```powershell
# If you have GitHub Desktop - use that!
# OR use this:
git push -u origin deployment
```

Good luck! 🎉
