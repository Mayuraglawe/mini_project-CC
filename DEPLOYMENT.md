# 🚀 Deployment Guide - Render

## Prerequisites

Before deploying to Render, make sure you have:

1. ✅ A **Render account** (free tier available) - [Sign up here](https://render.com/)
2. ✅ Your project pushed to a **Git repository** (GitHub, GitLab, or Bitbucket)
3. ✅ Java 17 installed locally (for testing before deployment)

---

## 📦 Files Added for Deployment

The following files have been created for Render deployment:

1. **Dockerfile** - Multi-stage Docker build configuration
2. **render.yaml** - Render service configuration
3. **.dockerignore** - Excludes unnecessary files from Docker build
4. **application.properties** (updated) - Now uses PORT environment variable

---

## 🛠️ Deployment Steps

### Step 1: Push Your Code to Git Repository

If you haven't already, initialize a Git repository and push to GitHub:

```bash
# Initialize git (if not already done)
git init

# Add all files
git add .

# Commit changes
git commit -m "Add Render deployment configuration"

# Add your remote repository (replace with your repo URL)
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git

# Push to GitHub
git push -u origin main
```

---

### Step 2: Create a New Web Service on Render

1. **Log in to Render Dashboard**
   - Go to [https://dashboard.render.com/](https://dashboard.render.com/)

2. **Create New Web Service**
   - Click **"New +"** button (top right)
   - Select **"Web Service"**

3. **Connect Your Repository**
   - Choose **"Build and deploy from a Git repository"**
   - Click **"Connect account"** if you haven't connected GitHub/GitLab
   - Select your repository: `mini_project-CC`
   - Click **"Connect"**

---

### Step 3: Configure the Web Service

Render should auto-detect the configuration from `render.yaml`, but verify these settings:

#### Basic Settings:
- **Name**: `tac-assembly-converter` (or your preferred name)
- **Region**: Choose the closest region to you
- **Branch**: `main` (or your default branch)
- **Runtime**: **Docker**

#### Build Settings:
- **Dockerfile Path**: `./Dockerfile`
- **Docker Context**: `.` (root directory)

#### Service Settings:
- **Plan**: **Free** (or choose a paid plan)
- **Auto-Deploy**: ✅ **Yes** (deploys automatically on git push)

#### Health Check:
- **Health Check Path**: `/api/compile/health`

#### Environment Variables:
Add these environment variables (should be auto-configured from `render.yaml`):

| Key | Value |
|-----|-------|
| `JAVA_OPTS` | `-Xmx512m -Xms256m` |
| `SERVER_PORT` | `8080` |

---

### Step 4: Deploy

1. **Review Configuration**
   - Double-check all settings
   
2. **Create Web Service**
   - Click **"Create Web Service"** button
   
3. **Wait for Build**
   - Render will:
     - Clone your repository
     - Build the Docker image (takes 3-5 minutes)
     - Deploy the container
   - Monitor the build logs in real-time

4. **Check Deployment Status**
   - Wait for status to change to **"Live"** (green indicator)

---

### Step 5: Access Your Application

Once deployed, you'll get a URL like:
```
https://tac-assembly-converter.onrender.com
```

**Test the endpoints:**

1. **Health Check**:
   ```
   https://your-app-name.onrender.com/api/compile/health
   ```

2. **Main Application**:
   ```
   https://your-app-name.onrender.com/html.html
   ```

---

## 🧪 Testing Your Deployed Application

### Test the API with curl:

```bash
# Health Check
curl https://your-app-name.onrender.com/api/compile/health

# Validate TAC
curl -X POST https://your-app-name.onrender.com/api/compile/validate \
  -H "Content-Type: application/json" \
  -d '{"tacCode": "t1 = a + 5\nt2 = b * 3"}'

# Compile TAC to Assembly
curl -X POST https://your-app-name.onrender.com/api/compile/tac \
  -H "Content-Type: application/json" \
  -d '{"tacCode": "t1 = a + 5\nt2 = b * 3\nreturn t1"}'

# Version 2 - Complete Pipeline
curl -X POST https://your-app-name.onrender.com/api/compile/v2/complete \
  -H "Content-Type: application/json" \
  -d '{"tacCode": "t1 = a + 5\nt2 = b * 2\nresult = t1 + t2"}'
```

### Test with the Web Interface:
1. Open: `https://your-app-name.onrender.com/html.html`
2. Try both Version 1 and Version 2 features
3. Enter sample TAC code and test all modules

---

## ⚙️ Configuration Details

### Dockerfile Explanation

The Dockerfile uses a **multi-stage build**:

**Stage 1 - Build** (Maven image):
- Uses Maven 3.9.6 with Java 17
- Copies `pom.xml` and downloads dependencies
- Copies source code
- Builds the JAR file

**Stage 2 - Runtime** (JRE image):
- Uses lighter JRE-only image (smaller size)
- Copies the built JAR file
- Exposes port 8080
- Runs the Spring Boot application

**Benefits**:
- ✅ Smaller final image size (~200MB vs ~600MB)
- ✅ Faster deployments
- ✅ More secure (no build tools in production)

### render.yaml Explanation

```yaml
services:
  - type: web                    # Web service type
    runtime: docker              # Use Docker runtime
    plan: free                   # Free tier
    autoDeploy: true            # Auto-deploy on git push
    healthCheckPath: /api/compile/health  # Health endpoint
```

---

## 🔧 Troubleshooting

### Build Fails

**Issue**: Docker build fails
- Check build logs in Render dashboard
- Verify `pom.xml` is valid
- Ensure Java version matches (17)

**Solution**:
```bash
# Test Docker build locally
docker build -t tac-converter .
docker run -p 8080:8080 tac-converter
```

### Application Won't Start

**Issue**: Container starts but crashes
- Check application logs in Render dashboard
- Look for Java exceptions or memory errors

**Solution**:
- Increase memory limits in `JAVA_OPTS`
- Upgrade to a paid plan for more resources

### Health Check Failing

**Issue**: Render shows "Unhealthy"
- Verify health check endpoint is correct
- Check if application is actually starting

**Solution**:
```bash
# Test health endpoint locally
curl http://localhost:8080/api/compile/health
```

### Slow Performance (Free Tier)

**Issue**: Application is slow or spins down
- Free tier spins down after 15 minutes of inactivity
- First request after spin-down takes ~30 seconds

**Solution**:
- Upgrade to a paid plan ($7/month) for always-on service
- Or accept the cold start time for free tier

---

## 💰 Pricing

### Free Tier Limitations:
- ✅ 750 hours/month (shared across all free services)
- ⚠️ Spins down after 15 minutes of inactivity
- ⚠️ Limited to 512MB RAM
- ✅ Automatic SSL certificates
- ✅ Custom domain support

### Paid Plans:
- **Starter**: $7/month - Always on, more RAM
- **Standard**: $25/month - Even more resources
- **Pro**: $85/month - Dedicated resources

---

## 🔄 Continuous Deployment

Once set up, any changes you push to your Git repository will automatically trigger a new deployment:

```bash
# Make changes to your code
git add .
git commit -m "Update feature"
git push

# Render automatically:
# 1. Detects the push
# 2. Builds new Docker image
# 3. Deploys the new version
# 4. Routes traffic to new container
```

---

## 📊 Monitoring

### View Logs
1. Go to your service in Render dashboard
2. Click **"Logs"** tab
3. See real-time application logs

### View Metrics
1. Click **"Metrics"** tab
2. Monitor:
   - CPU usage
   - Memory usage
   - Request count
   - Response times

### Set Up Alerts
1. Go to **"Settings"** → **"Notifications"**
2. Configure alerts for:
   - Deploy failures
   - Service crashes
   - High memory usage

---

## 🔐 Security Best Practices

1. **Environment Variables**: 
   - Don't commit sensitive data
   - Use Render's environment variable manager

2. **HTTPS**:
   - Render provides automatic SSL
   - All traffic is encrypted

3. **CORS** (if needed):
   - Configure CORS in Spring Boot for frontend access

---

## 🎯 Quick Deployment Checklist

- [ ] Create Render account
- [ ] Push code to Git repository
- [ ] Connect repository to Render
- [ ] Verify `render.yaml` configuration
- [ ] Create web service
- [ ] Wait for build to complete
- [ ] Test health endpoint
- [ ] Test web interface
- [ ] Test API endpoints
- [ ] Set up custom domain (optional)
- [ ] Configure monitoring alerts

---

## 📞 Support

- **Render Documentation**: [https://render.com/docs](https://render.com/docs)
- **Render Community**: [https://community.render.com/](https://community.render.com/)
- **Render Status**: [https://status.render.com/](https://status.render.com/)

---

## 🎉 You're Ready to Deploy!

Your application is now configured for Render deployment. Follow the steps above, and you'll have your TAC to Assembly Converter live on the internet in minutes!

**Next Steps**:
1. Push your code to GitHub
2. Connect to Render
3. Deploy and share your URL! 🚀
