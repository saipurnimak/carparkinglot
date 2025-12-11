# 🚗 Parking Garage Application - Complete Beginner's Guide

## 📚 Table of Contents
1. [What is This Application?](#what-is-this-application)
2. [How the Internet Works (Simplified)](#how-the-internet-works)
3. [Understanding Backend vs Frontend](#understanding-backend-vs-frontend)
4. [Project Structure Explained](#project-structure-explained)
5. [Step-by-Step: What We Built](#step-by-step-what-we-built)
6. [How to Use the Application](#how-to-use-the-application)
7. [Testing with Swagger](#testing-with-swagger)
8. [Common Concepts Explained](#common-concepts-explained)

---

## 🎯 What is This Application?

Imagine a real parking garage where:
- You drive in and need to find a parking spot
- The system tracks which spots are empty or occupied
- You can register your car
- You can park your car in a specific spot OR let the system choose the best one
- You can see where your car is parked
- You can "leave" and free up the spot

This application does exactly that, but digitally!

---

## 🌐 How the Internet Works (Simplified)

Think of it like ordering food at a restaurant:

1. **You (Browser)** = Customer who places an order
2. **Waiter (Internet)** = Takes your order to the kitchen
3. **Kitchen (Backend Server)** = Prepares your food
4. **Menu (Frontend)** = What you see and interact with

When you click "Register" on a website:
- Your browser sends a **REQUEST** (like ordering food)
- The server receives it, processes it (like cooking)
- The server sends back a **RESPONSE** (like serving the food)

---

## 🔄 Understanding Backend vs Frontend

### Frontend (What Users See)
- **Technology Used**: React (JavaScript)
- **Location**: `frontend/` folder
- **Port**: 3001 (runs on http://localhost:3001)
- **What it does**:
  - Shows pretty buttons, forms, and pages
  - Handles user clicks and inputs
  - Sends requests to backend
  - Displays results to users

**Think of it as**: The restaurant's dining area and menu

### Backend (The Brain)
- **Technology Used**: Java with Spring Boot
- **Location**: `backend/` folder  
- **Port**: 8080 (runs on http://localhost:8080)
- **What it does**:
  - Stores data in a database
  - Checks if passwords are correct
  - Enforces rules (e.g., "can't park in occupied spot")
  - Sends data back to frontend

**Think of it as**: The restaurant's kitchen and storage

### How They Talk to Each Other

```
User clicks "Register" button
        ↓
Frontend: "Hey backend, register this user!"
        ↓
Backend: "Let me check... password is strong... email is unique... OK, saved!"
        ↓
Backend: "Here's a token to remember you're logged in"
        ↓
Frontend: "Great! Let me show the dashboard"
        ↓
User sees: "Welcome to your dashboard!"
```

---

## 📁 Project Structure Explained

```
parkinglot-Java/
│
├── backend/                          ← Java/Spring Boot (Server)
│   ├── src/main/java/com/parkinglot/
│   │   ├── model/                    ← Data structures (User, Car, ParkingSpot, etc.)
│   │   ├── repository/               ← Database access (save, find, delete data)
│   │   ├── service/                  ← Business logic (parking rules, validations)
│   │   ├── controller/               ← API endpoints (receives requests from frontend)
│   │   ├── security/                 ← Login/JWT token handling
│   │   ├── dto/                      ← Data Transfer Objects (request/response formats)
│   │   └── config/                   ← Configuration (security, CORS, Swagger)
│   │
│   ├── src/main/resources/
│   │   └── application.properties    ← Settings (database, port, etc.)
│   │
│   └── pom.xml                       ← Lists all Java libraries needed
│
└── frontend/                         ← React (User Interface)
    ├── src/
    │   ├── pages/                    ← Different screens (Login, Dashboard, etc.)
    │   ├── components/               ← Reusable UI pieces (Navigation bar)
    │   ├── api.js                    ← Functions to call backend APIs
    │   ├── AuthContext.jsx           ← Manages user login state
    │   ├── App.jsx                   ← Main app component with routing
    │   └── main.jsx                  ← Entry point
    │
    ├── package.json                  ← Lists all JavaScript libraries needed
    └── vite.config.js                ← Frontend build configuration
```

---

## 🛠️ Step-by-Step: What We Built

### **Phase 1: Upgraded Java** ✅
**What**: Changed Java version from 17 to 21 (LTS - Long Term Support)  
**Why**: Java 21 is newer, faster, and has better security  
**How**: Used OpenRewrite tool to automatically update code

---

### **Phase 2: Built the Database Models** ✅

Think of models as **templates** for storing data.

#### **1. User Model** (`User.java`)
```java
User {
    id: 1
    firstName: "John"
    lastName: "Doe"
    email: "john@example.com"
    password: "encrypted_password_here"
}
```
**What it stores**: User account information  
**Why**: Need to know who owns which cars

#### **2. Car Model** (`Car.java`)
```java
Car {
    id: 1
    licensePlate: "ABC123"
    make: "Toyota"
    model: "Camry"
    color: "Blue"
    user: [Reference to User who owns this car]
}
```
**What it stores**: Car details  
**Why**: Track which cars are in the garage

#### **3. ParkingSpot Model** (`ParkingSpot.java`)
```java
ParkingSpot {
    id: 1
    floor: 1
    spotNumber: 5
    occupied: false
}
```
**What it stores**: Parking spot information  
**Why**: Know which spots are available  
**Note**: We have 75 total spots (Floor 1: 20, Floor 2: 25, Floor 3: 30)

#### **4. ParkingSession Model** (`ParkingSession.java`)
```java
ParkingSession {
    id: 1
    user: [Reference to User]
    car: [Reference to Car]
    parkingSpot: [Reference to ParkingSpot]
    startTime: "2025-12-10 10:30:00"
    endTime: null
    active: true
}
```
**What it stores**: Active parking records  
**Why**: Track which car is parked where and for how long

---

### **Phase 3: Built the Repository Layer** ✅

**What are Repositories?**  
They're like a librarian who helps you find, save, or remove books (data) from the library (database).

**Examples**:
- `UserRepository.findByEmail("john@example.com")` → Find user with this email
- `CarRepository.findByIdAndUser(1, user)` → Find car #1 that belongs to this user
- `ParkingSpotRepository.findByOccupied(false)` → Find all empty spots

---

### **Phase 4: Built the Service Layer** ✅

**What are Services?**  
They contain **business logic** - the rules and operations of your application.

#### **AuthService** - Handles Login/Registration
```java
register(request) {
    1. Check if email already exists
    2. Encrypt the password
    3. Save user to database
    4. Generate JWT token (login key)
    5. Return token to user
}

login(request) {
    1. Find user by email
    2. Check if password matches
    3. Generate JWT token
    4. Return token
}
```

#### **CarService** - Manages Cars
```java
addCar(email, carData) {
    1. Find user by email
    2. Create new car linked to user
    3. Save car to database
    4. Return car details
}

getCars(email) {
    1. Find user by email
    2. Get all cars belonging to this user
    3. Return list of cars
}
```

#### **ParkingService** - Handles Parking Logic
```java
parkCar(email, request) {
    1. Find user by email
    2. Verify car belongs to user
    3. Check if car is already parked (can't park twice!)
    
    IF user didn't specify a spot:
        4a. Find first available spot (prefer lower floors)
        4b. Assign that spot
    ELSE:
        4a. Check if requested spot exists
        4b. Check if spot is occupied
        4c. If available, assign it
    
    5. Create parking session record
    6. Mark spot as occupied
    7. Return parking details
}

leaveParkingSpot(email, sessionId) {
    1. Find the parking session
    2. Verify it belongs to this user
    3. Mark session as inactive
    4. Mark spot as available again
    5. Record end time
}
```

---

### **Phase 5: Built the Controller Layer** ✅

**What are Controllers?**  
They're like receptionists who receive requests and direct them to the right service.

#### **AuthController** - Login/Register Endpoints

**Endpoint**: `POST /api/auth/register`  
**Request Body**:
```json
{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "password": "password123"
}
```
**Response**:
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
        "id": 1,
        "firstName": "John",
        "lastName": "Doe",
        "email": "john@example.com"
    }
}
```

**Endpoint**: `POST /api/auth/login`  
**Request Body**:
```json
{
    "email": "john@example.com",
    "password": "password123"
}
```

#### **CarController** - Car Management Endpoints

**Endpoint**: `GET /api/cars`  
**What it does**: Get all cars for logged-in user  
**Response**:
```json
[
    {
        "id": 1,
        "licensePlate": "ABC123",
        "make": "Toyota",
        "model": "Camry",
        "color": "Blue"
    }
]
```

**Endpoint**: `POST /api/cars`  
**Request Body**:
```json
{
    "licensePlate": "XYZ789",
    "make": "Honda",
    "model": "Accord",
    "color": "Red"
}
```

**Endpoint**: `DELETE /api/cars/{carId}`  
**What it does**: Remove a car

#### **ParkingController** - Parking Endpoints

**Endpoint**: `GET /api/spots/available`  
**Query Params**: `floor` (optional)  
**What it does**: Get all available parking spots  
**Response**:
```json
[
    {
        "id": 1,
        "floor": 1,
        "spotNumber": 5,
        "occupied": false
    },
    {
        "id": 2,
        "floor": 1,
        "spotNumber": 6,
        "occupied": false
    }
]
```

**Endpoint**: `POST /api/parking/park`  
**Request Body (Auto-park)**:
```json
{
    "carId": 1
}
```
**Request Body (Manual spot selection)**:
```json
{
    "carId": 1,
    "floor": 2,
    "spotNumber": 15
}
```

**Endpoint**: `GET /api/parking/active`  
**What it does**: Get all active parking sessions for user

**Endpoint**: `POST /api/parking/{sessionId}/leave`  
**What it does**: End a parking session and free up the spot

---

### **Phase 6: Built Security System** ✅

#### **How Login Works (JWT Authentication)**

1. **User Registers/Logs In**:
   - Backend generates a **JWT Token** (like a digital key)
   - Token format: `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

2. **Frontend Stores Token**:
   - Saved in browser's `localStorage`
   - Automatically attached to all future requests

3. **Protected Endpoints**:
   - Every request to `/api/cars`, `/api/parking`, etc. needs this token
   - Backend checks: "Is this token valid? Who does it belong to?"
   - If valid: Process request
   - If invalid: Return 401 Unauthorized error

#### **Password Security**
- Passwords are **encrypted** using BCrypt
- Original password: `password123`
- Stored in database: `$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy`
- Even database admins can't see real passwords!

#### **CORS Configuration**
**Problem**: Browsers block requests from one website to another (security)  
**Our Setup**:
- Frontend: http://localhost:3001
- Backend: http://localhost:8080
- We configured backend to **allow** requests from port 3001

---

### **Phase 7: Built the Frontend (React)** ✅

#### **Page Components**

**1. Login Page** (`Login.jsx`)
- Email and password form
- Calls `POST /api/auth/login`
- Saves token to localStorage
- Redirects to Dashboard

**2. Register Page** (`Register.jsx`)
- Form with: firstName, lastName, email, password
- Calls `POST /api/auth/register`
- Auto-logs in user after registration

**3. Dashboard** (`Dashboard.jsx`)
- Welcome page with navigation links
- Shows quick stats

**4. My Cars Page** (`MyCars.jsx`)
- Displays all user's cars
- "Add New Car" button opens a form
- Each car has a "Delete" button
- Calls `GET /api/cars` to load cars
- Calls `POST /api/cars` to add cars
- Calls `DELETE /api/cars/{id}` to remove cars

**5. Park Car Page** (`ParkCar.jsx`)
- **Step 1**: Select a car from dropdown
- **Step 2 (Option A)**: Click "Auto-Park" button → system picks best spot
- **Step 2 (Option B)**: Browse available spots and click one to park there
- Filter spots by floor
- Calls `POST /api/parking/park`

**6. Active Sessions Page** (`ActiveSessions.jsx`)
- Shows where your cars are currently parked
- Each session shows:
  - Which car (Make, Model, License Plate)
  - Where (Floor, Spot Number)
  - When (Start Time)
  - "Leave Spot" button
- Calls `GET /api/parking/active`
- Calls `POST /api/parking/{sessionId}/leave`

#### **How Routing Works** (`App.jsx`)

```javascript
Route: /login        → Shows Login Page
Route: /register     → Shows Register Page
Route: /dashboard    → Shows Dashboard (protected)
Route: /cars         → Shows My Cars (protected)
Route: /park         → Shows Park Car (protected)
Route: /active       → Shows Active Sessions (protected)
```

**Protected** means: Must be logged in to access

#### **API Communication** (`api.js`)

This file contains functions that make HTTP requests:

```javascript
// Example: Register a user
authAPI.register({
    firstName: "John",
    lastName: "Doe",
    email: "john@example.com",
    password: "password123"
})

// Behind the scenes:
axios.post('/api/auth/register', data)
```

All requests automatically include the JWT token in headers:
```javascript
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

### **Phase 8: Added Swagger UI** ✅

**What is Swagger?**  
It's an interactive documentation tool where you can:
- See all available API endpoints
- Read what each endpoint does
- Test endpoints directly in the browser
- See request/response examples

**Access it at**: http://localhost:8080/swagger-ui/index.html

**How to use**:
1. Open Swagger UI
2. Click "Authorize" button
3. Paste your JWT token (get it from login response)
4. Now you can test any endpoint by clicking "Try it out"

---

## 🚀 How to Use the Application

### **Starting the Servers**

**Backend** (Terminal 1):
```bash
cd /Users/purnimasrikanth/Desktop/parkinglot-Java/backend
mvn spring-boot:run
```
Wait for: "Started ParkingGarageApplication in X seconds"

**Frontend** (Terminal 2):
```bash
cd /Users/purnimasrikanth/Desktop/parkinglot-Java/frontend
npm run dev
```
Wait for: "VITE ready in X ms"

### **Using the Application**

#### **Step 1: Register an Account**
1. Open http://localhost:3001
2. Click "Register"
3. Fill in:
   - First Name: John
   - Last Name: Doe
   - Email: john@example.com
   - Password: password123
4. Click "Register"
5. You're automatically logged in!

#### **Step 2: Add Your Cars**
1. Click "My Cars" in navigation
2. Click "Add New Car"
3. Fill in:
   - License Plate: ABC123
   - Make: Toyota
   - Model: Camry
   - Color: Blue
4. Click "Add Car"
5. Repeat to add more cars

#### **Step 3: Park a Car**

**Option A - Auto-Park (Easy)**:
1. Click "Park Car"
2. Select a car from dropdown
3. Click "Auto-Park" button
4. System automatically assigns the best available spot!
5. Redirects to "Active Sessions"

**Option B - Choose Your Spot**:
1. Click "Park Car"
2. Select a car from dropdown
3. Optionally filter by floor
4. Browse available spots (green boxes)
5. Click "Park Here" on your chosen spot
6. Redirects to "Active Sessions"

#### **Step 4: View Active Parking**
1. Click "Active Sessions"
2. See all your parked cars:
   - Car details
   - Floor and spot number
   - Start time
3. Click "Leave Spot" when you want to exit

#### **Step 5: Leave Parking**
1. In "Active Sessions"
2. Find your car
3. Click "Leave Spot"
4. Spot becomes available again

---

## 🔧 Testing with Swagger

### **1. Open Swagger UI**
http://localhost:8080/swagger-ui/index.html

### **2. Register a User**
1. Expand "auth-controller"
2. Click on `POST /api/auth/register`
3. Click "Try it out"
4. Edit the request body:
```json
{
  "firstName": "Test",
  "lastName": "User",
  "email": "test@example.com",
  "password": "password123"
}
```
5. Click "Execute"
6. Copy the `token` from response (you'll need it!)

### **3. Authorize Future Requests**
1. Click the "Authorize" button at the top
2. In the "Value" field, paste: `Bearer YOUR_TOKEN_HERE`
   - Example: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`
3. Click "Authorize"
4. Close the dialog

### **4. Test Adding a Car**
1. Expand "car-controller"
2. Click on `POST /api/cars`
3. Click "Try it out"
4. Edit request body:
```json
{
  "licensePlate": "TEST123",
  "make": "Honda",
  "model": "Civic",
  "color": "Red"
}
```
5. Click "Execute"
6. Check response - your car is saved!

### **5. Test Parking**
1. Expand "parking-controller"
2. Click on `POST /api/parking/park`
3. Click "Try it out"
4. Auto-park example:
```json
{
  "carId": 1
}
```
5. Click "Execute"
6. Response shows which spot was assigned!

---

## 📖 Common Concepts Explained

### **What is an API?**
**API** = Application Programming Interface  
**Simple Definition**: A menu of functions that the backend offers

Like a restaurant menu:
- Menu Item: "Cheeseburger" → API Endpoint: `POST /api/cars`
- You order: "One cheeseburger please" → You send: `{"make": "Toyota"}`
- Kitchen makes it → Backend processes it
- Waiter brings food → Backend returns response

### **What is REST?**
**REST** = Representational State Transfer  
**Simple Definition**: A standard way to design APIs

**REST Rules**:
- Use HTTP methods clearly:
  - `GET` = Retrieve data (like reading a book)
  - `POST` = Create new data (like writing a new chapter)
  - `PUT/PATCH` = Update data (like editing a chapter)
  - `DELETE` = Remove data (like tearing out a page)
- Use clear URLs: `/api/cars` not `/api/getAllCarsForUser123`
- Return JSON format

### **What is JSON?**
**JSON** = JavaScript Object Notation  
**Simple Definition**: A way to structure data that both humans and computers can read

Example:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "age": 30,
  "cars": [
    {"make": "Toyota", "model": "Camry"},
    {"make": "Honda", "model": "Civic"}
  ]
}
```

### **What is a JWT Token?**
**JWT** = JSON Web Token  
**Simple Definition**: A secure way to prove who you are without logging in every time

**How it works**:
1. You log in → Backend creates a token
2. Token contains: your user ID, email, expiration date
3. Token is **encrypted** (like a sealed envelope)
4. You send this token with every request
5. Backend opens the envelope and says "Ah, this is John!"

**Benefits**:
- No need to send password every time
- Stateless (backend doesn't need to remember you)
- Secure (can't be tampered with)

### **What is CORS?**
**CORS** = Cross-Origin Resource Sharing  
**Simple Definition**: Permission for websites on different domains to talk to each other

**Why it exists**: Security! Prevents evil.com from stealing data from yourbank.com

**Our setup**:
- Frontend: localhost:3001 (different "origin")
- Backend: localhost:8080 (different "origin")
- We told backend: "Allow requests from 3001"

### **What is Maven?**
**Maven** = Build tool for Java projects  
**Simple Definition**: Like a shopping assistant that:
- Downloads all the libraries you need (Spring, JWT, etc.)
- Compiles your Java code
- Packages everything into a runnable application
- Runs tests

**File**: `pom.xml` - lists all dependencies (libraries)

### **What is Vite?**
**Vite** = Build tool for JavaScript/React projects  
**Simple Definition**: Like Maven but for frontend
- Bundles all your JavaScript files
- Provides hot reload (instant updates when you edit code)
- Optimizes for production

**File**: `package.json` - lists all dependencies

### **What is React?**
**React** = JavaScript library for building user interfaces  
**Simple Definition**: Instead of writing HTML directly, you create reusable components

**Example**:
```jsx
function Button({ text, onClick }) {
    return <button onClick={onClick}>{text}</button>
}

// Use it:
<Button text="Click Me" onClick={handleClick} />
<Button text="Submit" onClick={handleSubmit} />
```

**Benefits**:
- Reusable components
- Automatically updates UI when data changes
- Easy to organize code

### **What is Spring Boot?**
**Spring Boot** = Java framework for building web applications  
**Simple Definition**: A toolkit that handles the boring stuff so you focus on your app

**It automatically provides**:
- Web server (Tomcat)
- Database connection
- Security
- API routing
- Configuration

**Without Spring Boot**: 100 lines of setup code  
**With Spring Boot**: 3 lines + annotations

---

## 🎓 Learning Path

### **If you want to learn Backend**:
1. ✅ Understand HTTP (GET, POST, PUT, DELETE)
2. ✅ Learn Java basics (classes, objects, methods)
3. ✅ Study Spring Boot annotations (@RestController, @Service, etc.)
4. ✅ Learn JPA (database operations)
5. ✅ Understand security (JWT, authentication)

### **If you want to learn Frontend**:
1. ✅ HTML/CSS basics
2. ✅ JavaScript fundamentals
3. ✅ React concepts (components, props, state, hooks)
4. ✅ HTTP requests (axios, fetch)
5. ✅ Routing (React Router)

### **If you want to learn Full Stack**:
Do both! 🎉 You'll understand how everything connects.

---

## 🐛 Common Errors and Solutions

### **Error: "Port 8080 already in use"**
**Meaning**: Backend is already running  
**Solution**: 
```bash
# Kill existing process
pkill -f "spring-boot:run"
# Then restart
```

### **Error: "Cannot GET /api/cars"**
**Meaning**: Backend is not running  
**Solution**: Start backend server

### **Error: "401 Unauthorized"**
**Meaning**: You're not logged in or token expired  
**Solution**: Log in again to get a fresh token

### **Error: "SPOT_ALREADY_OCCUPIED"**
**Meaning**: Someone else parked there first  
**Solution**: Choose a different spot or use auto-park

### **Error: "CAR_ALREADY_PARKED"**
**Meaning**: Your car is already parked somewhere  
**Solution**: Leave the current spot first, then park again

---

## 🎉 Congratulations!

You now understand:
- ✅ How backend and frontend work together
- ✅ What each file and folder does
- ✅ How to use the application
- ✅ How to test with Swagger
- ✅ Common programming concepts
- ✅ How data flows from user → frontend → backend → database

**You're not a "lame user" anymore - you're a developer in training!** 🚀

---

## 📚 Additional Resources

- **Spring Boot Docs**: https://spring.io/projects/spring-boot
- **React Docs**: https://react.dev
- **JWT Explained**: https://jwt.io
- **REST API Tutorial**: https://restfulapi.net
- **Maven Guide**: https://maven.apache.org/guides

---

**Need Help?** Check the README.md file or ask questions!
