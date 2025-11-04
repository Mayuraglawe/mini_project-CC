# 🎯 Local Testing Guide - SUCCESS!

## ✅ Your Application is Running!

Your TAC to Assembly Converter is now running locally on:
- **URL**: http://localhost:8080/html.html
- **Port**: 8080
- **Status**: ✅ RUNNING

---

## 🚀 How to Run Locally (What We Did)

### Step 1: Build the Project ✅
```powershell
cd "f:\TAC version 2\mini_project-CC"
.\mvnw.cmd clean package -DskipTests
```

**Result**: Creates `target\LowLevel_Language-0.0.1-SNAPSHOT.jar`

### Step 2: Run the Application ✅
```powershell
java -jar target\LowLevel_Language-0.0.1-SNAPSHOT.jar
```

**Result**: Application starts on http://localhost:8080

---

## 🌐 Access Your Application

### Web Interface:
Open in your browser:
```
http://localhost:8080/html.html
```

### API Endpoints:

**Health Check:**
```
http://localhost:8080/api/compile/health
```

**Version 1 - Compile TAC:**
```
POST http://localhost:8080/api/compile/tac
```

**Version 1 - Validate TAC:**
```
POST http://localhost:8080/api/compile/validate
```

**Version 2 - Complete Pipeline:**
```
POST http://localhost:8080/api/compile/v2/complete
```

**Version 2 - Assembly Only:**
```
POST http://localhost:8080/api/compile/v2/assembly
```

**Version 2 - Resource Analysis:**
```
POST http://localhost:8080/api/compile/v2/resources
```

**Version 2 - Optimization:**
```
POST http://localhost:8080/api/compile/v2/optimize
```

---

## 🧪 Test the Application

### Option 1: Use the Web Interface ✅ EASIEST
1. Open: http://localhost:8080/html.html
2. Try **Version 1** tab:
   - Enter TAC code
   - Click "Validate TAC"
   - Click "Compile to Assembly"
3. Try **Version 2** tab:
   - Enter TAC code
   - Click "Complete Pipeline" or individual modules

### Option 2: Test with curl
```powershell
# Health check
curl http://localhost:8080/api/compile/health

# Validate TAC
curl -X POST http://localhost:8080/api/compile/validate -H "Content-Type: application/json" -d "{\"tacCode\": \"t1 = a + 5\nt2 = b * 3\"}"

# Compile TAC
curl -X POST http://localhost:8080/api/compile/tac -H "Content-Type: application/json" -d "{\"tacCode\": \"t1 = a + 5\nt2 = b * 3\nreturn t1\"}"

# V2 Complete Pipeline
curl -X POST http://localhost:8080/api/compile/v2/complete -H "Content-Type: application/json" -d "{\"tacCode\": \"t1 = a + 5\nt2 = b * 2\nresult = t1 + t2\"}"
```

### Option 3: Test with PowerShell (Windows)
```powershell
# Health check
Invoke-RestMethod -Uri "http://localhost:8080/api/compile/health" -Method GET

# Compile TAC
$body = @{
    tacCode = "t1 = a + 5`nt2 = b * 3`nreturn t1"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/compile/tac" -Method POST -Body $body -ContentType "application/json"
```

---

## 🛑 Stop the Application

To stop the running application:
1. Press `Ctrl + C` in the terminal where it's running
2. Or close the terminal window

---

## 🔄 Restart After Code Changes

If you make code changes:

```powershell
# 1. Stop the current application (Ctrl + C)

# 2. Rebuild
.\mvnw.cmd clean package -DskipTests

# 3. Restart
java -jar target\LowLevel_Language-0.0.1-SNAPSHOT.jar
```

---

## 📝 Sample TAC Code to Test

### Simple Arithmetic:
```
t1 = a + 5
t2 = b * 3
t3 = t1 - t2
result = t3
```

### With Conditionals:
```
t1 = a + 5
t2 = b * 3
if t1 > t2 goto L1
return t1
L1:
return t2
```

### Optimization Test (Version 2):
```
t1 = a + 5
t2 = b * 2
t3 = t1 - 0
t4 = t3 * 1
result = t4
```

**Expected optimizations**:
- `b * 2` → `shift left by 1`
- `t1 - 0` → removed
- `t3 * 1` → removed

---

## 🎯 What's Working

✅ **Build**: Maven wrapper builds successfully  
✅ **Run**: Application starts on port 8080  
✅ **Web UI**: Accessible at http://localhost:8080/html.html  
✅ **Version 1**: TAC validation and compilation  
✅ **Version 2**: All three modules (Assembly, Resources, Optimization)  
✅ **API**: All REST endpoints functional  

---

## 🐛 Troubleshooting

### Port Already in Use
If you see "Port 8080 already in use":
```powershell
# Find what's using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID with actual process ID)
taskkill /F /PID <PID>
```

### Application Won't Start
- Check if Java is installed: `java -version`
- Make sure you're in the project directory
- Rebuild: `.\mvnw.cmd clean package -DskipTests`

### Build Fails
- Ensure Java 17 or higher is installed
- Check internet connection (Maven downloads dependencies)
- Delete `target` folder and rebuild

---

## 💡 Quick Commands Reference

```powershell
# Navigate to project
cd "f:\TAC version 2\mini_project-CC"

# Build
.\mvnw.cmd clean package -DskipTests

# Run
java -jar target\LowLevel_Language-0.0.1-SNAPSHOT.jar

# Build and run in one command
.\mvnw.cmd clean package -DskipTests ; java -jar target\LowLevel_Language-0.0.1-SNAPSHOT.jar
```

---

## 🎉 Next Steps

Now that it's running locally:

1. ✅ **Test all features** in the web interface
2. ✅ **Try different TAC code** samples
3. ✅ **See the optimizations** in action
4. 🚀 **Deploy to Render** when ready (see DEPLOYMENT.md)

---

## 📖 Related Documentation

- **Deployment Guide**: [DEPLOYMENT.md](./DEPLOYMENT.md)
- **Quick Deploy**: [RENDER_QUICKSTART.md](./RENDER_QUICKSTART.md)
- **Project Info**: [README.md](./README.md)
- **Version 2 Features**: [VERSION2_SUMMARY.md](./VERSION2_SUMMARY.md)

---

**Your application is live and ready to use! 🎊**

Open http://localhost:8080/html.html and start converting TAC to Assembly!
