# Setup Guide - Payment Platform Development Environment

## Table of Contents
- [Prerequisites](#prerequisites)
- [Option 1: GitHub Codespaces (Recommended)](#option-1-github-codespaces-recommended)
- [Option 2: Local Development](#option-2-local-development)
- [Backend Setup (Micronaut)](#backend-setup-micronaut)
- [Frontend Setup (React)](#frontend-setup-react)
- [Database Setup](#database-setup)
- [Git Workflow](#git-workflow)
- [Docker Setup](#docker-setup)
- [Troubleshooting](#troubleshooting)

---

## Prerequisites

### Required Software
- **Git**: v2.30+
- **Docker Desktop**: v20.10+ (with Docker Compose)
- **Java**: JDK 17 or 21 (Recommended: Amazon Corretto or Eclipse Temurin)
- **Node.js**: v18+ (LTS recommended)
- **PostgreSQL**: v14+ (or use Docker)
- **IDE**: VS Code or IntelliJ IDEA

### Recommended VS Code Extensions
```bash
# Install via VS Code Extensions Marketplace
- Extension Pack for Java (Microsoft)
- Micronaut Tools (Oracle)
- React Developer Tools
- ESLint
- Prettier
- PostgreSQL (Chris Kolkman)
- Docker (Microsoft)
- GitLens
- Thunder Client (for API testing)
```

### Recommended IntelliJ IDEA Plugins
- Micronaut
- Database Tools and SQL
- Docker
- JavaScript and TypeScript

---

## Option 1: GitHub Codespaces (Recommended)

**Fastest way to get started - everything pre-configured!**

### Step 1: Fork the Repository
```bash
# Go to GitHub and click "Fork" on the repository
# Or use GitHub CLI:
gh repo fork <original-repo-url> --clone
```

### Step 2: Create Codespace
1. Navigate to your forked repository on GitHub
2. Click the **"Code"** button
3. Select the **"Codespaces"** tab
4. Click **"Create codespace on main"**

### Step 3: Wait for Environment Setup
The codespace will automatically:
- ✅ Install Java 17 and Maven
- ✅ Install Node.js 18 and npm
- ✅ Start PostgreSQL in a container
- ✅ Configure environment variables
- ✅ Install all dependencies
- ✅ Start both backend and frontend in dev mode

### Step 4: Access Services
Once setup completes (2-3 minutes), you'll have:
- **Backend API**: `http://localhost:8080`
- **API Docs**: `http://localhost:8080/swagger-ui`
- **Frontend**: `http://localhost:3000`
- **PostgreSQL**: `localhost:5432` (inside codespace)

### Codespace Benefits
- ☁️ Cloud-based (no local setup needed)
- 🚀 Fast provisioning (60 hours free/month)
- 🔧 Pre-configured development environment
- 🔄 Consistent across all developers
- 💾 Auto-save and persistence

---

## Option 2: Local Development

### Step 1: Install Java

#### Windows (PowerShell)
```powershell
# Using Chocolatey
choco install microsoft-openjdk17

# Or download manually from:
# https://learn.microsoft.com/en-us/java/openjdk/download
```

#### macOS
```bash
# Using Homebrew
brew install openjdk@17

# Add to PATH
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

**Verify Installation**:
```bash
java -version
# Output: openjdk version "17.0.x"
```

### Step 2: Install Node.js

#### Windows
```powershell
# Using Chocolatey
choco install nodejs-lts

# Or download from: https://nodejs.org/
```

#### macOS
```bash
# Using Homebrew
brew install node@18
```

#### Linux
```bash
# Using NodeSource
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs
```

**Verify Installation**:
```bash
node --version  # v18.x.x
npm --version   # 9.x.x
```

### Step 3: Install Docker Desktop

Download from: https://www.docker.com/products/docker-desktop/

**Post-installation**:
```bash
docker --version
docker-compose --version
```

### Step 4: Clone Repository

```bash
# Fork the repo first on GitHub, then:
git clone https://github.com/YOUR_USERNAME/junior-developer-assessment.git
cd junior-developer-assessment
```

---

## Backend Setup (Micronaut)

### Directory Structure
```
part3-backend-challenge/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/payment/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── entity/
│   │   │       ├── dto/
│   │   │       └── Application.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/migration/  # Flyway migrations
│   └── test/
├── pom.xml  (or build.gradle)
└── README.md
```

### Step 1: Navigate to Backend Directory
```bash
cd part3-backend-challenge
```

### Step 2: Configure Database Connection

Create/edit `src/main/resources/application.yml`:
```yaml
micronaut:
  application:
    name: payment-api
  server:
    port: 8080
    cors:
      enabled: true

datasources:
  default:
    url: jdbc:postgresql://localhost:5432/payment_platform
    driverClassName: org.postgresql.Driver
    username: postgres
    password: postgres
    dialect: POSTGRES
    schema-generate: NONE  # Use Flyway for migrations
    
flyway:
  datasources:
    default:
      enabled: true
      locations: classpath:db/migration

jpa:
  default:
    properties:
      hibernate:
        hbm2ddl:
          auto: none
        show_sql: false
        format_sql: true

jackson:
  serialization:
    writeDatesAsTimestamps: false
  deserialization:
    adjustDatesToContextTimeZone: false
```

### Step 3: Install Dependencies & Build

#### Using Maven
```bash
# Install dependencies
./mvnw clean install

# Skip tests for faster build
./mvnw clean install -DskipTests
```

#### Using Gradle
```bash
./gradlew clean build
```

### Step 4: Run Database Migrations
```bash
# Migrations run automatically on startup
# Or manually:
./mvnw flyway:migrate
```

### Step 5: Start Backend Server

```bash
# Maven
./mvnw mn:run

# Or for hot reload during development
./mvnw compile mn:run

# Gradle
./gradlew run
```

**Expected Output**:
```
 __  __ _                                  _   
|  \/  (_) ___ _ __ ___  _ __   __ _ _   _| |_ 
| |\/| | |/ __| '__/ _ \| '_ \ / _` | | | | __|
| |  | | | (__| | | (_) | | | | (_| | |_| | |_ 
|_|  |_|_|\___|_|  \___/|_| |_|\__,_|\__,_|\__|

   Micronaut (v4.x.x)

Startup completed in 1234ms. Server Running: http://localhost:8080
```

### Step 6: Verify Backend is Running

```bash
# Test health endpoint
curl http://localhost:8080/health

# Access Swagger UI
# Open in browser: http://localhost:8080/swagger-ui
```

### Key Micronaut Concepts

#### 1. Dependency Injection
```java
@Controller("/api/v1/merchants")
public class MerchantController {
    
    private final MerchantService merchantService;
    
    // Constructor injection (recommended)
    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }
}
```

#### 2. Request Handling
```java
@Get("/{id}")
public HttpResponse<MerchantDTO> getMerchant(@PathVariable String id) {
    return merchantService.findById(id)
        .map(HttpResponse::ok)
        .orElse(HttpResponse.notFound());
}

@Post
public HttpResponse<MerchantDTO> createMerchant(@Valid @Body CreateMerchantRequest request) {
    MerchantDTO created = merchantService.create(request);
    return HttpResponse.created(created);
}
```

#### 3. Validation
```java
public class CreateMerchantRequest {
    @NotBlank
    @Size(min = 3, max = 255)
    private String name;
    
    @Email
    private String email;
}
```

#### 4. Database Access (Micronaut Data)
```java
@Repository
public interface MerchantRepository extends JpaRepository<MerchantEntity, Long> {
    
    Optional<MerchantEntity> findByMerchantId(String merchantId);
    
    @Query("SELECT m FROM MerchantEntity m WHERE m.status = :status")
    List<MerchantEntity> findByStatus(String status);
}
```

#### 5. Service Layer
```java
@Singleton
public class MerchantServiceImpl implements MerchantService {
    
    private final MerchantRepository repository;
    
    public MerchantServiceImpl(MerchantRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public MerchantDTO create(CreateMerchantRequest request) {
        // Business logic here
    }
}
```

---

## Frontend Setup (React)

### Directory Structure
```
part4-frontend-challenge/
├── src/
│   ├── components/
│   │   ├── common/        # Reusable components
│   │   ├── transactions/  # Feature components
│   │   └── layout/
│   ├── hooks/             # Custom hooks
│   ├── services/          # API services
│   ├── contexts/          # React contexts
│   ├── types/             # TypeScript types
│   ├── utils/             # Helper functions
│   ├── App.tsx
│   └── main.tsx
├── public/
├── package.json
├── tsconfig.json
├── vite.config.ts
└── README.md
```

### Step 1: Navigate to Frontend Directory
```bash
cd part4-frontend-challenge
```

### Step 2: Install Dependencies

```bash
npm install

# Or use Yarn
yarn install

# Or use pnpm (faster)
pnpm install
```

### Step 3: Configure Environment Variables

Create `.env.local`:
```env
VITE_API_BASE_URL=http://localhost:8080/api/v1
VITE_API_TIMEOUT=30000
VITE_ENABLE_MOCK_API=false
```

### Step 4: Start Development Server

```bash
npm run dev

# Or with specific port
npm run dev -- --port 3000
```

**Expected Output**:
```
VITE v4.x.x  ready in 1234 ms

  ➜  Local:   http://localhost:3000/
  ➜  Network: http://192.168.1.x:3000/
```

### Step 5: Build for Production

```bash
npm run build

# Preview production build
npm run preview
```

### Key React + TypeScript Patterns

#### 1. Component Structure
```typescript
// TransactionList.tsx
import React from 'react';
import { Transaction } from '@/types';

interface TransactionListProps {
  transactions: Transaction[];
  onSelect: (transaction: Transaction) => void;
  loading?: boolean;
}

export const TransactionList: React.FC<TransactionListProps> = ({
  transactions,
  onSelect,
  loading = false
}) => {
  if (loading) return <LoadingSpinner />;
  
  return (
    <div className="transaction-list">
      {transactions.map(txn => (
        <TransactionRow 
          key={txn.id} 
          transaction={txn} 
          onClick={() => onSelect(txn)}
        />
      ))}
    </div>
  );
};
```

#### 2. Custom Hooks
```typescript
// hooks/useTransactions.ts
import { useState, useEffect } from 'react';
import { transactionService } from '@/services';
import { Transaction } from '@/types';

export const useTransactions = (merchantId: string) => {
  const [data, setData] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const result = await transactionService.getByMerchant(merchantId);
        setData(result);
      } catch (err) {
        setError(err as Error);
      } finally {
        setLoading(false);
      }
    };
    
    fetchData();
  }, [merchantId]);

  return { data, loading, error, refetch: () => {} };
};
```

#### 3. API Service
```typescript
// services/transactionService.ts
import axios from 'axios';

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Request interceptor
apiClient.interceptors.request.use(config => {
  const token = localStorage.getItem('authToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Response interceptor
apiClient.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      // Handle unauthorized
    }
    return Promise.reject(error);
  }
);

export const transactionService = {
  getByMerchant: async (merchantId: string, params?: any) => {
    const response = await apiClient.get(`/merchants/${merchantId}/transactions`, { params });
    return response.data;
  }
};
```

#### 4. Context for State Management
```typescript
// contexts/TransactionContext.tsx
import React, { createContext, useContext, useReducer } from 'react';

type TransactionState = {
  transactions: Transaction[];
  filters: FilterState;
};

type TransactionAction =
  | { type: 'SET_TRANSACTIONS'; payload: Transaction[] }
  | { type: 'UPDATE_FILTERS'; payload: Partial<FilterState> };

const TransactionContext = createContext<{
  state: TransactionState;
  dispatch: React.Dispatch<TransactionAction>;
} | undefined>(undefined);

export const TransactionProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [state, dispatch] = useReducer(transactionReducer, initialState);
  
  return (
    <TransactionContext.Provider value={{ state, dispatch }}>
      {children}
    </TransactionContext.Provider>
  );
};

export const useTransactionContext = () => {
  const context = useContext(TransactionContext);
  if (!context) throw new Error('useTransactionContext must be used within TransactionProvider');
  return context;
};
```

---

## Database Setup

### Option 1: Using Docker (Recommended)

```bash
# Start PostgreSQL container
docker run --name payment-db \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=payment_platform \
  -p 5432:5432 \
  -d postgres:14

# Verify container is running
docker ps

# Access PostgreSQL CLI
docker exec -it payment-db psql -U postgres -d payment_platform
```

### Option 2: Local PostgreSQL Installation

#### Windows
```powershell
# Using Chocolatey
choco install postgresql14

# Or download from: https://www.postgresql.org/download/windows/
```

#### macOS
```bash
brew install postgresql@14
brew services start postgresql@14
```

#### Linux
```bash
sudo apt update
sudo apt install postgresql-14
sudo systemctl start postgresql
```

### Create Database and Schema

```sql
-- Connect to PostgreSQL
psql -U postgres

-- Create database
CREATE DATABASE payment_platform;

-- Connect to new database
\c payment_platform

-- Create schema
CREATE SCHEMA IF NOT EXISTS operators;

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE payment_platform TO postgres;
GRANT ALL ON SCHEMA operators TO postgres;
```

### Load Sample Data

```bash
# Run seed script (if provided)
psql -U postgres -d payment_platform -f scripts/seed-data.sql
```

---

## Git Workflow

### Initial Setup

```bash
# 1. Fork repository on GitHub

# 2. Clone your fork
git clone https://github.com/YOUR_USERNAME/junior-developer-assessment.git
cd junior-developer-assessment

# 3. Add upstream remote
git remote add upstream https://github.com/ORIGINAL_OWNER/junior-developer-assessment.git

# 4. Verify remotes
git remote -v
```

### Branch Strategy

```bash
# Create feature branch for each part
git checkout -b part1-database-optimization
git checkout -b part2-system-design
git checkout -b part3-backend-api
git checkout -b part4-frontend-dashboard
git checkout -b part5-debugging

# Or create a single development branch
git checkout -b submission/your-name
```

### Commit Guidelines

```bash
# Atomic commits with clear messages
git add .
git commit -m "feat: implement merchant transaction API endpoint"
git commit -m "fix: resolve timezone conversion issue"
git commit -m "docs: add API documentation"

# Use conventional commits
# feat: new feature
# fix: bug fix
# docs: documentation
# test: add tests
# refactor: code refactoring
# chore: maintenance
```

### Pushing Changes

```bash
# Push to your fork
git push origin part3-backend-api

# Create Pull Request on GitHub
# (Optional if submitting for assessment)
```

### Keeping Fork Updated

```bash
# Fetch upstream changes
git fetch upstream

# Merge upstream main into your branch
git checkout main
git merge upstream/main
git push origin main
```

---

## Docker Setup

### Full Stack with Docker Compose

Create `docker-compose.yml` in project root:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:14-alpine
    container_name: payment-db
    environment:
      POSTGRES_DB: payment_platform
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres-data:/var/lib/postgresql/data
      - ./scripts/init.sql:/docker-entrypoint-initdb.d/init.sql
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5

  backend:
    build:
      context: ./part3-backend-challenge
      dockerfile: Dockerfile
    container_name: payment-api
    ports:
      - "8080:8080"
    environment:
      DATASOURCES_DEFAULT_URL: jdbc:postgresql://postgres:5432/payment_platform
      DATASOURCES_DEFAULT_USERNAME: postgres
      DATASOURCES_DEFAULT_PASSWORD: postgres
    depends_on:
      postgres:
        condition: service_healthy
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/health"]
      interval: 30s
      timeout: 10s
      retries: 3

  frontend:
    build:
      context: ./part4-frontend-challenge
      dockerfile: Dockerfile
    container_name: payment-ui
    ports:
      - "3000:80"
    depends_on:
      - backend
    environment:
      VITE_API_BASE_URL: http://localhost:8080/api/v1

volumes:
  postgres-data:

networks:
  default:
    name: payment-network
```

### Backend Dockerfile

`part3-backend-challenge/Dockerfile`:

```dockerfile
# Multi-stage build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Frontend Dockerfile

`part4-frontend-challenge/Dockerfile`:

```dockerfile
# Build stage
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# Production stage
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

### Run Full Stack

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Rebuild and start
docker-compose up -d --build
```

---

## Troubleshooting

### Java Issues

**Problem**: Java version mismatch
```bash
# Check Java version
java -version

# Set JAVA_HOME (Windows PowerShell)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"

# Set JAVA_HOME (macOS/Linux)
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

### Database Connection Issues

**Problem**: Connection refused
```bash
# Check if PostgreSQL is running
docker ps  # (if using Docker)

# Test connection
psql -U postgres -h localhost -p 5432 -d payment_platform

# Check logs
docker logs payment-db
```

### Port Already in Use

**Problem**: Port 8080 or 3000 already in use
```bash
# Find process using port (Windows)
netstat -ano | findstr :8080

# Kill process (Windows)
taskkill /PID <PID> /F

# Find and kill process (macOS/Linux)
lsof -ti:8080 | xargs kill -9
```

### NPM/Node Issues

**Problem**: npm install fails
```bash
# Clear npm cache
npm cache clean --force

# Delete node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

### Docker Issues

**Problem**: Docker containers won't start
```bash
# Check Docker daemon
docker info

# Restart Docker Desktop

# Remove all containers and start fresh
docker-compose down -v
docker-compose up -d --build
```

---

## Useful Commands Reference

### Backend (Micronaut)
```bash
./mvnw clean install              # Build project
./mvnw mn:run                     # Run application
./mvnw test                       # Run tests
./mvnw test -Dtest=ClassName      # Run specific test
./mvnw flyway:migrate             # Run migrations
./mvnw dependency:tree            # View dependencies
```

### Frontend (React)
```bash
npm run dev                       # Start dev server
npm run build                     # Build for production
npm run preview                   # Preview production build
npm run lint                      # Run ESLint
npm run type-check                # TypeScript type checking
npm test                          # Run tests
```

### Docker
```bash
docker-compose up -d              # Start services
docker-compose down               # Stop services
docker-compose logs -f service    # View logs
docker-compose restart service    # Restart service
docker-compose exec service sh    # Shell into container
```

### Git
```bash
git status                        # Check status
git log --oneline --graph         # View commit history
git diff                          # View changes
git stash                         # Stash changes
git stash pop                     # Apply stashed changes
```

---

## Next Steps

1. ✅ Verify all services are running
2. ✅ Access Swagger UI: `http://localhost:8080/swagger-ui`
3. ✅ Access Frontend: `http://localhost:3000`
4. ✅ Test database connection
5. ✅ Start working on assessment parts!

**Need Help?**
- Check the `README.md` in each part directory
- Review UML diagrams in the docs folder
- Reference official documentation:
  - [Micronaut Docs](https://docs.micronaut.io)
  - [React Docs](https://react.dev)
  - [PostgreSQL Docs](https://www.postgresql.org/docs/)

**Good luck!** 🚀
