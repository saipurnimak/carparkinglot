# 🎨 Quick Start Guide

## 📁 Project Structure
```
parkinglot-Java/
├── 📄 README.md                          # Complete documentation
├── 📄 IMPLEMENTATION_SUMMARY.md          # What was built
├── 🚀 start-backend.sh                   # Backend startup script
├── 🚀 start-frontend.sh                  # Frontend startup script
│
├── backend/                              # Java Spring Boot (Java 21)
│   ├── pom.xml                          # Maven dependencies
│   └── src/main/java/com/parkinglot/
│       ├── 📦 config/                   # Security & initialization
│       ├── 🎮 controller/               # REST endpoints
│       ├── 📊 dto/                      # Data transfer objects
│       ├── 🗄️  model/                    # JPA entities
│       ├── 💾 repository/               # Database access
│       ├── 🔐 security/                 # JWT & auth
│       └── ⚙️  service/                  # Business logic
│
└── frontend/                             # React 18
    ├── package.json                     # npm dependencies
    ├── vite.config.js                   # Vite configuration
    └── src/
        ├── 🧩 components/               # Reusable components
        ├── 📄 pages/                    # Page components
        ├── 🔌 api.js                    # API client
        ├── 🔐 AuthContext.jsx           # Auth state
        └── 🎨 index.css                 # Global styles
```

## ⚡ Quick Start (3 Steps)

### Step 1: Start Backend
```bash
cd backend
mvn spring-boot:run

# Or use the script:
./start-backend.sh
```
✅ Backend running at: **http://localhost:8080**

### Step 2: Start Frontend (in new terminal)
```bash
cd frontend
npm install  # First time only
npm run dev

# Or use the script:
./start-frontend.sh
```
✅ Frontend running at: **http://localhost:3000**

### Step 3: Use the Application
1. Open **http://localhost:3000**
2. Click "Register" to create an account
3. Add your first car
4. Select a parking spot and park!

## 🎯 Key Features to Try

### 1️⃣ **User Management**
- Register with email and password (8+ chars)
- Login to get JWT token
- View your profile

### 2️⃣ **Car Management**
- Add multiple cars
- License plate: 7 characters (e.g., "ABC1234")
- Delete cars you no longer need

### 3️⃣ **Parking Operations**
- View 75 available spots across 3 floors
- Filter by floor (1, 2, or 3)
- Park your car in any available spot
- Track active parking sessions
- Leave parking spot when done

## 🎨 UI Flow

```
┌─────────────┐
│   Login/    │
│  Register   │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│  Dashboard  │◄─── Statistics & Quick Actions
└──────┬──────┘
       │
       ├──────► 🚗 My Cars        (Add/Delete Cars)
       │
       ├──────► 🅿️  Park Car      (Select Spot & Park)
       │
       └──────► 📊 Active Sessions (View/End Parking)
```

## 🌐 API Endpoints

### Authentication
```
POST   /api/auth/register    # Create account
POST   /api/auth/login       # Get JWT token
GET    /api/auth/me          # Current user info
```

### Cars
```
GET    /api/cars             # List your cars
POST   /api/cars             # Add new car
DELETE /api/cars/{id}        # Delete car
```

### Parking
```
GET    /api/spots/available?floor=2   # Available spots
POST   /api/parking                    # Park a car
GET    /api/parking/active             # Your sessions
POST   /api/parking/{id}/leave         # Leave spot
```

## 🔧 Troubleshooting

### Backend won't start?
- ✅ Check Java 21 is installed: `java -version`
- ✅ Check port 8080 is free: `lsof -i :8080`

### Frontend won't start?
- ✅ Check Node.js is installed: `node -v`
- ✅ Run `npm install` in frontend directory
- ✅ Check port 3000 is free: `lsof -i :3000`

### Can't login?
- ✅ Make sure backend is running
- ✅ Check browser console for errors
- ✅ Verify email format is valid

## 🗄️ Database Access

**H2 Console:** http://localhost:8080/h2-console

Settings:
- JDBC URL: `jdbc:h2:mem:parkingdb`
- Username: `sa`
- Password: (leave empty)

Tables: `users`, `cars`, `parking_spots`, `parking_sessions`

## 📱 Sample Test Scenario

1. **Register**: Create account with email `test@example.com`
2. **Add Car**: Toyota Camry, plate "ABC1234"
3. **Park**: Select Floor 2, Spot 15
4. **Check Session**: View active parking session
5. **Leave**: End parking session
6. **Verify**: Check spot is available again

## 🎉 Success Indicators

✅ Backend shows: "Initialized parking garage with 75 spots"  
✅ Frontend loads without errors  
✅ Can register and login  
✅ Dashboard shows statistics  
✅ Can add and manage cars  
✅ Can park and leave spots  

## 📞 Need Help?

Check these files:
- `README.md` - Complete documentation
- `IMPLEMENTATION_SUMMARY.md` - What was built
- Backend logs - Terminal running backend
- Frontend console - Browser developer tools

---

**Built with:** Java 21, Spring Boot 3.2, React 18, JWT, H2 Database
