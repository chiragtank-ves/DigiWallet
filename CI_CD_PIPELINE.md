# 🚀 DigiWallet CI/CD Pipeline Documentation

## ✅ CI/CD Pipeline Created Successfully!

### 📋 Overview

A complete GitHub Actions CI/CD pipeline has been configured for the DigiWallet project that:
- ✅ Runs all unit tests
- ✅ Builds the application
- ✅ Deploys to self-hosted runner
- ✅ Performs health checks
- ✅ Runs on the same machine

---

## 📁 Pipeline File Location

```
.github/workflows/ssh-test.yml
```

---

## 🎯 Pipeline Triggers

The pipeline runs automatically on:

```yaml
on:
  push:
    branches:
      - main
      - final_fsd
  pull_request:
    branches:
      - main
      - final_fsd
```

**Triggers:**
- ✅ Push to `main` branch
- ✅ Push to `final_fsd` branch
- ✅ Pull request to `main` branch
- ✅ Pull request to `final_fsd` branch

---

## 🔄 Pipeline Stages

### **Stage 1: Checkout & Setup**
```
1. Checkout Code (actions/checkout@v4)
2. Set up JDK 17 (actions/setup-java@v4)
3. Display Environment Info
```

### **Stage 2: Build**
```
4. Build Project (mvnw.cmd clean compile -DskipTests)
```

### **Stage 3: Test** ⭐ IMPORTANT
```
5. Run Unit Tests (mvnw.cmd test)
6. Generate Test Report (always runs)
```
- ⚠️ **Pipeline FAILS if tests fail** (continue-on-error: false)
- Tests must pass before deployment

### **Stage 4: Package**
```
7. Package Application (mvnw.cmd package -DskipTests)
8. Verify JAR Created
```

### **Stage 5: Deploy**
```
9. Stop Running Application (kills port 8080 process)
10. Deploy Application (starts new instance)
11. Health Check (waits and checks port 8080)
```

### **Stage 6: Summary**
```
12. Deployment Summary (success)
13. Deployment Failed (failure)
```

---

## 📊 Detailed Pipeline Steps

### 1️⃣ **Checkout Code**
```yaml
- name: Checkout Code
  uses: actions/checkout@v4
```
- Clones the repository
- Checks out the branch that triggered the workflow

### 2️⃣ **Set up JDK 17**
```yaml
- name: Set up JDK 17
  uses: actions/setup-java@v4
  with:
    java-version: '17'
    distribution: 'temurin'
    cache: 'maven'
```
- Installs Java 17
- Caches Maven dependencies for faster builds

### 3️⃣ **Display Environment Info**
```bash
echo "Working Directory: %CD%"
java -version
./mvnw.cmd -version
```
- Shows Java version
- Shows Maven version
- Displays working directory

### 4️⃣ **Build Project**
```bash
./mvnw.cmd clean compile -DskipTests
```
- Cleans previous build
- Compiles Java source code
- Skips tests (tests run in next stage)

### 5️⃣ **Run Unit Tests** ⭐
```bash
./mvnw.cmd test
continue-on-error: false
```
- **Runs all JUnit tests**
- **Tests MUST pass to continue**
- If any test fails, pipeline stops here
- No deployment happens if tests fail

### 6️⃣ **Generate Test Report**
```bash
if exist target\surefire-reports\*.txt (
  type target\surefire-reports\*.txt
)
```
- Displays test results
- Shows which tests passed/failed
- Runs even if tests fail (if: always())

### 7️⃣ **Package Application**
```bash
./mvnw.cmd package -DskipTests
```
- Creates JAR file
- Skips tests (already ran)
- Output: `target/DigiWallet-0.0.1-SNAPSHOT.jar`

### 8️⃣ **Verify JAR Created**
```bash
if exist target\DigiWallet-0.0.1-SNAPSHOT.jar (
  echo "✓ JAR file created successfully"
) else (
  exit 1
)
```
- Checks if JAR exists
- Fails pipeline if JAR not found

### 9️⃣ **Stop Running Application**
```bash
FOR /F "tokens=5" %%P IN ('netstat -ano ^| findstr :8080') DO (
  taskkill /F /PID %%P
)
```
- Finds process using port 8080
- Kills the process
- Continues even if no process found

### 🔟 **Deploy Application**
```bash
start "DigiWallet-Backend" cmd /c "java -jar target\DigiWallet-0.0.1-SNAPSHOT.jar > logs\app.log 2>&1"
timeout /t 10 /nobreak
```
- Starts application in background
- Redirects output to `logs/app.log`
- Waits 10 seconds for startup

### 1️⃣1️⃣ **Health Check**
```bash
timeout /t 15 /nobreak
curl -f http://localhost:8080/actuator/health
netstat -ano | findstr :8080
```
- Waits 15 seconds
- Checks health endpoint
- Verifies port 8080 is listening

### 1️⃣2️⃣ **Deployment Summary**
```bash
echo "✓ DEPLOYMENT SUCCESSFUL"
echo "Backend API: http://localhost:8080"
echo "Swagger UI: http://localhost:8080/swagger-ui.html"
```
- Shows success message
- Displays access URLs

---

## 🎯 Pipeline Behavior

### ✅ **Success Flow**
```
Checkout → Setup → Build → Test ✅ → Package → Deploy → Health Check ✅
└─> Application running on port 8080
```

### ❌ **Failure Flow**
```
Checkout → Setup → Build → Test ❌ STOP
└─> No deployment happens
└─> Previous version keeps running (if any)
```

---

## 📝 Test Execution Details

### What Tests Run?
```
All tests in src/test/java/
├── controller tests (MockMvc)
├── service tests (Mockito)
├── repository tests (@DataJpaTest)
└── integration tests
```

### Test Reports Location
```
target/surefire-reports/
├── *.txt (text reports)
└── TEST-*.xml (XML reports)
```

### Test Failure Behavior
- ❌ Pipeline **STOPS** immediately
- ❌ No deployment happens
- ❌ GitHub Actions shows ❌ red X
- ✅ Previous deployment remains running

### Test Success Behavior
- ✅ Pipeline **CONTINUES**
- ✅ Application gets deployed
- ✅ GitHub Actions shows ✅ green check
- ✅ New version replaces old version

---

## 🖥️ Self-Hosted Runner Requirements

### Required on Runner Machine:
- ✅ Java 17 JDK
- ✅ Maven (via mvnw.cmd)
- ✅ Git
- ✅ Port 8080 available
- ✅ MySQL database running

### Runner Setup:
```bash
# Your self-hosted runner should have:
1. GitHub Actions runner installed
2. Runner registered to your repository
3. Runner service running
```

---

## 🔍 Monitoring Deployment

### Check Pipeline Status:
**GitHub Actions Tab:**
```
https://github.com/chiragtank-ves/DigiWallet/actions
```

### View Logs:
```
Workflow run → Click on job → Expand steps
```

### Check Application Logs:
```bash
# On self-hosted runner machine
cd C:\Users\Administrator\Downloads\digitalApp\DigiWallet
type logs\app.log
```

### Check Application Status:
```bash
# On self-hosted runner machine
netstat -ano | findstr :8080
curl http://localhost:8080/actuator/health
```

---

## 🎯 Testing the Pipeline

### Trigger Pipeline Manually:
```bash
# Make a small change
echo "test" > test.txt
git add test.txt
git commit -m "test pipeline"
git push origin final_fsd
```

### Watch Pipeline Execute:
1. Go to GitHub Actions tab
2. Click on running workflow
3. Watch each step execute
4. See test results
5. Verify deployment

---

## 📊 Expected Output

### When Tests Pass:
```
✓ Checkout Code
✓ Set up JDK 17
✓ Display Environment Info
✓ Build Project (Skip Tests)
✓ Run Unit Tests ← ALL TESTS PASS
✓ Generate Test Report
✓ Package Application
✓ Verify JAR Created
✓ Stop Running Application
✓ Deploy Application
✓ Health Check
✓ Deployment Summary

RESULT: ✅ Deployment Successful
```

### When Tests Fail:
```
✓ Checkout Code
✓ Set up JDK 17
✓ Display Environment Info
✓ Build Project (Skip Tests)
❌ Run Unit Tests ← TEST FAILURE
✓ Generate Test Report (shows failures)

RESULT: ❌ Pipeline Failed (No Deployment)
```

---

## 🔧 Configuration

### Modify Deployment Port:
Edit in workflow file:
```yaml
findstr :8080  # Change to your port
```

### Modify Test Timeout:
```yaml
timeout /t 15  # Change seconds
```

### Modify Health Check:
```yaml
curl -f http://localhost:8080/your-endpoint
```

---

## 🚀 Access Deployed Application

After successful deployment:

| Service | URL |
|---------|-----|
| **Backend API** | http://localhost:8080 |
| **Swagger UI** | http://localhost:8080/swagger-ui.html |
| **Health Check** | http://localhost:8080/actuator/health |
| **Frontend** | http://localhost:3000 (manual start) |

---

## 📚 Logs

### Application Logs:
```
logs/app.log
```

### GitHub Actions Logs:
```
GitHub → Actions → Select workflow run → View logs
```

---

## ✅ Verification Checklist

- [x] Pipeline file created
- [x] Tests run before deployment
- [x] Deployment only on test success
- [x] Old instance stopped before new deployment
- [x] Health check after deployment
- [x] Logs directory created
- [x] .gitignore updated
- [x] Changes committed to final_fsd branch
- [x] Changes pushed to remote

---

## 🎯 Summary

### What Was Configured:

✅ **Complete CI/CD Pipeline**
- Runs on self-hosted runner
- Tests → Build → Deploy on same machine

✅ **Test-First Deployment**
- All tests must pass
- No deployment if tests fail

✅ **Automated Deployment**
- Stops old version
- Starts new version
- Health check verification

✅ **Comprehensive Logging**
- Test reports
- Application logs
- Deployment status

---

## 🎊 Pipeline is Ready!

Your CI/CD pipeline is now configured and will:

1. ✅ Run automatically on push to `final_fsd` branch
2. ✅ Execute all unit tests
3. ✅ Only deploy if tests pass
4. ✅ Deploy to the same self-hosted machine
5. ✅ Verify deployment with health check

---

**Next Push to `final_fsd` will trigger the pipeline!** 🚀

---

*Pipeline Location:* `.github/workflows/ssh-test.yml`
*Branch:* `final_fsd`
*Status:* ✅ Active
*Last Updated:* February 3, 2026
