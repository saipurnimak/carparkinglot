# 🚀 Getting Started - Setup Guide

This guide will help you set up and run the Parking Garage application on your local machine after cloning from GitHub.

## 📋 Prerequisites

Before you begin, make sure you have these installed on your computer:

### Required Software:
1. **Java 21** (JDK 21)
   - Download: https://adoptium.net/ or https://www.oracle.com/java/technologies/downloads/
   - Verify installation: `java -version` (should show version 21.x.x)

2. **Maven 3.6+**
   - Download: https://maven.apache.org/download.cgi
   - Verify installation: `mvn -version`

3. **Node.js 18+** (includes npm)
   - Download: https://nodejs.org/
   - Verify installation: `node -v` and `npm -v`

4. **Git**
   - Download: https://git-scm.com/downloads
   - Verify installation: `git --version`

---

## 📥 Step 1: Clone the Repository

Open your terminal/command prompt and run:

```bash
git clone https://github.com/saipurnimak/carparkinglot.git
cd carparkinglot
```

---

## 🔧 Step 2: Backend Setup

### 2.1 Navigate to Backend Directory
```bash
cd backend
```

### 2.2 Install Dependencies & Build
Maven will automatically download all required dependencies:
```bash
mvn clean install
```

**Expected Output:**
- Downloads Spring Boot, Security, JWT, H2 Database, and other dependencies
- Compiles all Java files
- Should end with `BUILD SUCCESS`

**If you see errors:**
- Ensure Java 21 is installed: `java -version`
- Check Maven settings: `mvn -version`
- Try: `mvn clean install -U` (forces update of dependencies)

### 2.3 Start Backend Server
```bash
mvn spring-boot:run
```

**Expected Output:**
```
Started ParkingGarageApplication in X seconds
Tomcat started on port 8080
Initialized parking garage with 75 spots across 3 floors
```

**Backend is ready when you see:**
- Server running on: http://localhost:8080
- Swagger UI available at: http://localhost:8080/swagger-ui/index.html

**Keep this terminal window open!** The backend must stay running.

---

## 🎨 Step 3: Frontend Setup

### 3.1 Open a NEW Terminal Window
Leave the backend running and open a new terminal.

### 3.2 Navigate to Frontend Directory
```bash
cd carparkinglot/frontend
```

### 3.3 Install Dependencies
npm will download all React, Vite, and other JavaScript libraries:
```bash
npm install
```

**Expected Output:**
- Downloads ~90 packages
- Creates `node_modules/` folder (~200MB)
- Should complete without errors

**If you see errors:**
- Ensure Node.js is installed: `node -v` (should be 18+)
- Clear cache: `npm cache clean --force`
- Try again: `rm -rf node_modules package-lock.json && npm install`

### 3.4 Start Frontend Server
```bash
npm run dev
```

**Expected Output:**
```
VITE v5.4.21  ready in XXX ms
➜  Local:   http://localhost:3001/
```

**Frontend is ready when you see:**
- Server running on: http://localhost:3001

**Keep this terminal window open too!** The frontend must stay running.

---

## 🌐 Step 4: Access the Application

Open your web browser and go to:
- **Main App**: http://localhost:3001
- **API Docs**: http://localhost:8080/swagger-ui/index.html

---

## ✅ Step 5: Verify Everything Works

### Test 1: Register a New User
1. Go to http://localhost:3001
2. Click "Register"
3. Fill in the form:
   - First Name: John
   - Last Name: Doe
   - Email: john@test.com
   - Password: password123
4. Click "Register"
5. ✅ You should be automatically logged in and redirected to Dashboard

### Test 2: Add a Car
1. Click "My Cars" in navigation
2. Click "Add New Car"
3. Fill in:
   - License Plate: ABC123
   - Make: Toyota
   - Model: Camry
   - Color: Blue
4. Click "Add Car"
5. ✅ Car should appear in your list

### Test 3: Park a Car
1. Click "Park Car"
2. Select your car from dropdown
3. Click "Auto-Park" button
4. ✅ System assigns best available spot
5. ✅ Redirects to "Active Sessions"

### Test 4: View Active Parking
1. Click "Active Sessions"
2. ✅ See your parked car with spot details
3. Click "Leave Spot"
4. ✅ Spot becomes available again

---

## 🛠️ Configuration Changes (Optional)

### Change Frontend Port
If port 3001 is already in use on your machine:

**Edit:** `frontend/vite.config.js`
```javascript
server: {
  port: 3001,  // Change this to any available port (e.g., 3000, 3002, 5173)
  // ...
}
```

**Important:** If you change the port, you must also update the backend CORS configuration!

### Update Backend CORS for Different Port
**Edit:** `backend/src/main/java/com/parkinglot/config/SecurityConfig.java`

Find this line (~line 73):
```java
configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:3001", "http://localhost:3002", "http://localhost:5173"));
```

Add your new port to the list:
```java
configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:3001", "http://localhost:YOUR_PORT", "http://localhost:5173"));
```

**After editing:** Restart the backend server (Ctrl+C, then `mvn spring-boot:run`)

---

## 🐛 Troubleshooting

### Problem: Port 8080 is already in use
**Error:** `Web server failed to start. Port 8080 was already in use.`

**Solutions:**
1. Find and kill the process using port 8080:
   ```bash
   # macOS/Linux:
   lsof -ti:8080 | xargs kill -9
   
   # Windows (PowerShell):
   Get-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess | Stop-Process
   ```

2. Or change backend port in `backend/src/main/resources/application.properties`:
   ```properties
   server.port=8081
   ```
   Then update frontend proxy in `vite.config.js` to point to new port.

---

### Problem: Port 3001 is already in use
**Error:** `Port 3001 is in use, trying another one...`

**Solutions:**
1. Kill the process:
   ```bash
   # macOS/Linux:
   lsof -ti:3001 | xargs kill -9
   
   # Windows (PowerShell):
   Get-Process -Id (Get-NetTCPConnection -LocalPort 3001).OwningProcess | Stop-Process
   ```

2. Or let Vite auto-assign another port (it will show the new port in terminal)

---

### Problem: Registration shows 403 Forbidden
**Error in browser console:** `Failed to load resource: the server responded with a status of 403`

**Cause:** CORS configuration doesn't allow your frontend port

**Solution:**
1. Check which port your frontend is running on (shown in terminal)
2. Add that port to CORS configuration (see "Update Backend CORS" above)
3. Restart backend server

---

### Problem: Cannot connect to backend
**Error:** `Network Error` or `ERR_CONNECTION_REFUSED`

**Checklist:**
- ✅ Is backend running? Check terminal for "Started ParkingGarageApplication"
- ✅ Is it on port 8080? Visit http://localhost:8080/swagger-ui/index.html
- ✅ Check firewall settings (allow Java/Maven)

---

### Problem: npm install fails
**Error:** `npm ERR! code ERESOLVE` or similar

**Solutions:**
1. Use legacy peer deps:
   ```bash
   npm install --legacy-peer-deps
   ```

2. Clear npm cache:
   ```bash
   npm cache clean --force
   rm -rf node_modules package-lock.json
   npm install
   ```

3. Update npm:
   ```bash
   npm install -g npm@latest
   ```

---

### Problem: Maven build fails
**Error:** `BUILD FAILURE`

**Solutions:**
1. Clean and rebuild:
   ```bash
   mvn clean install -U
   ```

2. Check Java version (must be 21):
   ```bash
   java -version
   ```

3. Clear Maven cache:
   ```bash
   rm -rf ~/.m2/repository
   mvn clean install
   ```

---

## 📂 Project Structure Overview

```
carparkinglot/
├── backend/                 # Spring Boot (Java 21)
│   ├── src/
│   │   ├── main/java/       # Java source code
│   │   └── main/resources/  # Configuration files
│   ├── pom.xml              # Maven dependencies
│   └── target/              # Compiled files (auto-generated, not in Git)
│
├── frontend/                # React + Vite
│   ├── src/                 # React source code
│   ├── package.json         # npm dependencies
│   ├── vite.config.js       # Build configuration
│   └── node_modules/        # Downloaded libraries (auto-generated, not in Git)
│
├── README.md                # Project overview
├── BEGINNER_GUIDE.md        # Learning guide
└── SETUP.md                 # This file
```

---

## 🚀 Quick Start Commands (Summary)

**Terminal 1 - Backend:**
```bash
git clone https://github.com/saipurnimak/carparkinglot.git
cd carparkinglot/backend
mvn clean install
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd carparkinglot/frontend
npm install
npm run dev
```

**Then visit:** http://localhost:3001

---

## 📚 Additional Resources

- **Backend API Documentation**: http://localhost:8080/swagger-ui/index.html (when running)
- **H2 Database Console**: http://localhost:8080/h2-console (when running)
  - JDBC URL: `jdbc:h2:mem:parkingdb`
  - Username: `sa`
  - Password: (leave empty)

- **Spring Boot Docs**: https://spring.io/projects/spring-boot
- **React Docs**: https://react.dev
- **Vite Docs**: https://vitejs.dev

---

## 🎯 Next Steps

After successfully running the application:

1. ✅ Read `BEGINNER_GUIDE.md` to understand how the code works
2. ✅ Explore the API using Swagger UI
3. ✅ Try all features (register, add cars, park, etc.)
4. ✅ Check the code structure in your IDE
5. ✅ Make your own modifications and experiment!

---

## 🆘 Need Help?

If you encounter issues not covered here:

1. Check existing GitHub Issues: https://github.com/saipurnimak/carparkinglot/issues
2. Create a new issue with:
   - Your operating system (Windows/Mac/Linux)
   - Java version (`java -version`)
   - Node version (`node -v`)
   - Full error message
   - Steps to reproduce

---

## ⚡ Pro Tips

1. **Use an IDE**: IntelliJ IDEA (Java) or VS Code (Frontend) will make development easier
2. **Keep terminals visible**: You'll see real-time logs for debugging
3. **Browser DevTools**: Press F12 to see network requests and console errors
4. **Test with Swagger first**: If frontend has issues, test backend directly via Swagger UI
5. **Hot reload works**: Code changes auto-refresh (frontend instantly, backend needs rebuild)

---

## 🎉 Success!

If you see the login page at http://localhost:3001 and can register/login, everything is working! 🚀

Happy coding! 👨‍💻👩‍💻
