# Parking Garage Management System

A full-stack parking garage management application with React frontend and Java Spring Boot backend.

## Features

### Backend (Java Spring Boot)
- ✅ User authentication with JWT
- ✅ Car management (CRUD operations)
- ✅ Parking spot management
- ✅ Parking session tracking
- ✅ RESTful API endpoints
- ✅ H2 in-memory database
- ✅ Spring Security with BCrypt password encoding

### Frontend (React)
- ✅ User registration and login
- ✅ Dashboard with statistics
- ✅ Manage multiple cars
- ✅ View available parking spots by floor
- ✅ Park cars in specific spots
- ✅ Track active parking sessions
- ✅ Leave parking spots
- ✅ Responsive design

## Technology Stack

### Backend
- Java 21
- Spring Boot 3.2.0
- Spring Security
- Spring Data JPA
- JWT (io.jsonwebtoken)
- H2 Database
- Lombok
- Maven

### Frontend
- React 18
- React Router DOM
- Axios
- Vite

## Getting Started

### Prerequisites
- Java 21 (LTS)
- Node.js 18+ and npm
- Maven 3.9+

### Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Build and run the application:
```bash
mvn spring-boot:run
```

The backend will start on http://localhost:8080

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm run dev
```

The frontend will start on http://localhost:3000

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `GET /api/auth/me` - Get current user (requires auth)

### Cars
- `GET /api/cars` - Get user's cars (requires auth)
- `POST /api/cars` - Add new car (requires auth)
- `DELETE /api/cars/{carId}` - Delete car (requires auth)

### Parking
- `GET /api/spots/available` - Get available parking spots
- `GET /api/spots/available?floor=2` - Get available spots on specific floor
- `POST /api/parking` - Park a car (requires auth)
- `GET /api/parking/active` - Get active parking sessions (requires auth)
- `POST /api/parking/{sessionId}/leave` - Leave parking spot (requires auth)

## Database

The application uses H2 in-memory database. You can access the H2 console at:
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:parkingdb`
- Username: `sa`
- Password: (leave empty)

The database is automatically initialized with:
- 75 parking spots across 3 floors
  - Floor 1: 20 spots
  - Floor 2: 25 spots
  - Floor 3: 30 spots

## Usage Flow

1. **Register/Login**: Create an account or login
2. **Add Cars**: Register your vehicles with license plates
3. **Park Car**: Select a car and choose an available parking spot
4. **View Sessions**: Monitor your active parking sessions
5. **Leave Spot**: Free up parking spots when leaving

## Validation Rules

### User Registration
- Password must be at least 8 characters
- Email must be valid and unique

### Car Registration
- License plate must be exactly 7 alphanumeric characters
- Make and model are required

### Parking
- Car must belong to the authenticated user
- Parking spot must be available
- Cannot park in already occupied spots

## Project Structure

```
parkinglot-Java/
├── backend/
│   ├── src/main/java/com/parkinglot/
│   │   ├── config/          # Security, CORS, Data initialization
│   │   ├── controller/      # REST controllers
│   │   ├── dto/            # Data transfer objects
│   │   ├── model/          # JPA entities
│   │   ├── repository/     # JPA repositories
│   │   ├── security/       # JWT and security components
│   │   └── service/        # Business logic
│   └── pom.xml
└── frontend/
    ├── src/
    │   ├── components/     # React components
    │   ├── pages/         # Page components
    │   ├── api.js         # API client
    │   ├── AuthContext.jsx # Authentication context
    │   └── App.jsx        # Main app component
    └── package.json
```

## License

This project is for educational purposes.
