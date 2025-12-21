# Course Learning Platform

A full-stack application with React frontend and Spring Boot backend.

## Prerequisites

- Docker and Docker Compose
- Node.js 18+ (for local development)
- Java 21+ (for local development)
- Maven 3.9+ (for local development)

## Running with Docker

1. Clone the repository
2. Navigate to the project root
3. Run the following commands:

```bash
# Build and start the services
docker compose up --build

# Or run in detached mode
docker compose up -d --build
```

The application will be available at:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080

## Local Development

### Backend
```bash
cd backend
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

## Docker Commands

```bash
# Stop services
docker compose down

# View logs
docker compose logs

# Rebuild specific service
docker compose build backend
docker compose build frontend

# Clean up
docker compose down -v --rmi all
