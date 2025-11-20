# Payment Platform - Junior Developer Assessment

Complete ready-to-run development environment for the junior developer assessment.

## 🚀 Quick Start

### Prerequisites
- **Docker Desktop** installed and running
- **Java 17+** (for backend development)
- **Node.js 18+** (for frontend development)
- **Git** (for version control)

### Option 1: Start Everything (Recommended for Testing)

**Windows (PowerShell):**
```powershell
cd docs\assignment-junior-developer
.\start-dev.ps1
```

**macOS/Linux (Bash):**
```bash
cd docs/assignment-junior-developer
chmod +x start-dev.sh
./start-dev.sh
```

This will:
1. ✅ Start PostgreSQL with sample data
2. ✅ Optionally start the backend API
3. ✅ Optionally start the frontend

### Option 2: Manual Setup (Recommended for Development)

#### 1. Start Database Only
```bash
docker-compose up -d postgres

# Wait for it to be ready
docker exec payment-platform-db pg_isready -U postgres
```

#### 2. Start Backend (in new terminal)
```bash
cd part3-backend-challenge
./mvnw mn:run
# API will be available at http://localhost:8080
```

#### 3. Start Frontend (in new terminal)
```bash
cd part4-frontend-challenge
npm install
cp .env.example .env.local
npm run dev
# UI will be available at http://localhost:3000
```

### Option 3: Docker Compose (All Services)

**Start everything with Docker:**
```bash
docker-compose up -d
```

**Stop everything:**
```bash
docker-compose down
```

## 📁 Project Structure

```
assignment-junior-developer/
├── part1-database-challenge/      # SQL optimization challenge
│   ├── README.md
│   ├── schema.sql                 # Database schema
│   ├── sample-data.sql            # Sample data (3000 transactions)
│   └── original-query.sql         # Query to optimize
│
├── part3-backend-challenge/       # Backend API (Micronaut + Java)
│   ├── src/                       # Source code
│   ├── pom.xml                    # Maven configuration
│   └── README.md                  # Backend instructions
│
├── part4-frontend-challenge/      # Frontend (React + TypeScript)
│   ├── src/                       # Source code
│   ├── package.json               # Dependencies
│   └── README.md                  # Frontend instructions
│
├── docker-compose.yml             # Docker services configuration
├── start-dev.ps1                  # Windows startup script
└── start-dev.sh                   # macOS/Linux startup script
```

## 🔌 Service URLs

Once started, access:

- **Frontend Dashboard**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **API Documentation (Swagger)**: http://localhost:8080/swagger-ui
- **Health Check**: http://localhost:8080/health
- **PostgreSQL**: localhost:5432 (user: `postgres`, password: `postgres`, database: `payment_platform`)

## 🗄️ Database Access

**Connect to PostgreSQL:**

```bash
# Using Docker
docker exec -it payment-platform-db psql -U postgres -d payment_platform

# Using psql locally
psql -U postgres -h localhost -d payment_platform
```

**Sample Queries:**
```sql
-- View merchants
SELECT * FROM operators.members LIMIT 10;

-- View transactions
SELECT * FROM operators.transaction_master 
WHERE txn_date >= '2025-11-16' 
LIMIT 10;

-- Count transactions by status
SELECT status, COUNT(*) 
FROM operators.transaction_master 
GROUP BY status;
```

## 🧪 Testing the Setup

1. **Check database is populated:**
   ```bash
   docker exec payment-platform-db psql -U postgres -d payment_platform -c "SELECT COUNT(*) FROM operators.transaction_master;"
   ```
   Should return ~3000 transactions

2. **Test backend API:**
   ```bash
   curl http://localhost:8080/health
   ```
   Should return: `{"status":"UP",...}`

3. **Test frontend:**
   Open http://localhost:3000 in browser
   Should see "Transaction Dashboard"

## 🏗️ For Candidates

### What's Already Implemented

**Database (Part 1):**
- ✅ Complete schema with 3 tables
- ✅ Sample data (3000 transactions, 10 members)
- ✅ Problematic query to optimize

**Backend (Part 3):**
- ✅ Micronaut project structure
- ✅ Database entities and repositories
- ✅ Health check endpoint
- ✅ Error handling framework
- ✅ OpenAPI/Swagger setup
- ⚠️ **TODO**: Implement TransactionController and Service

**Frontend (Part 4):**
- ✅ React + TypeScript + Vite setup
- ✅ Basic UI components (Button, Card, Table, etc.)
- ✅ API service boilerplate
- ✅ Type definitions
- ✅ Utility functions
- ⚠️ **TODO**: Implement features to reach 100 points
- 📋 **Scoring**: See `ASSIGNMENT_SCORING.md` for point-based system

### Your Tasks

1. **Part 1**: Analyze and optimize the slow query
2. **Part 3**: Build the transaction API endpoint
3. **Part 4**: Choose and implement features totaling 100 points (see `ASSIGNMENT_SCORING.md`)
   - 🎯 **Flexible System**: Pick any combination of tasks from Merchants or Reports pages
   - 📝 **Document Selection**: Use `TASK_SELECTION_TEMPLATE.md` to outline your choices
   - 💡 **Strategic**: Complete 100 points worth of features - you don't need to do everything!
4. **Part 5**: Debug provided buggy code

See individual README files in each part directory for detailed instructions.

## 🛠️ Development Commands

### Backend
```bash
cd part3-backend-challenge

# Build
./mvnw clean install

# Run
./mvnw mn:run

# Run tests
./mvnw test

# Build Docker image
docker build -t payment-api .
```

### Frontend
```bash
cd part4-frontend-challenge

# Install dependencies
npm install

# Start dev server
npm run dev

# Build for production
npm run build

# Run type checking
npm run type-check

# Run linting
npm run lint
```

### Database
```bash
# Start database
docker-compose up -d postgres

# View logs
docker-compose logs -f postgres

# Stop database
docker-compose down

# Reset database (delete all data)
docker-compose down -v
docker-compose up -d postgres
```

## 🐛 Troubleshooting

### Port Already in Use

**Backend (8080):**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# macOS/Linux
lsof -ti:8080 | xargs kill -9
```

**Frontend (3000):**
```bash
# Windows
netstat -ano | findstr :3000
taskkill /PID <PID> /F

# macOS/Linux
lsof -ti:3000 | xargs kill -9
```

### Database Connection Issues

```bash
# Check if PostgreSQL container is running
docker ps

# Check logs
docker logs payment-platform-db

# Restart container
docker-compose restart postgres
```

### Backend Won't Start

```bash
# Check Java version
java -version  # Should be 17+

# Clean Maven cache
cd part3-backend-challenge
./mvnw clean

# Check application logs
./mvnw mn:run
```

### Frontend Won't Start

```bash
# Clear npm cache
npm cache clean --force

# Delete node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

## 📊 Sample Data

The database comes pre-loaded with:
- **10 members** (acquirers and issuers)
- **~3000 transactions** over 3 days (Nov 16-18, 2025)
- **~15,000 transaction details** (avg 5 per transaction)

Date range for queries: **2025-11-16** to **2025-11-18**

Merchant IDs: **MCH-00001** through **MCH-00050**

## 🔒 Security Note

This is a development environment. Default credentials are:
- Database: `postgres` / `postgres`
- No authentication on API endpoints

**Do NOT use these settings in production!**

## 📞 Support

Having issues? Check:
1. Docker Desktop is running
2. Ports 3000, 5432, 8080 are available
3. Java 17+ and Node.js 18+ are installed
4. All dependencies are installed

## 🎯 Success Criteria

You'll know everything is working when:
- ✅ Database has 3000+ transactions
- ✅ Backend health check returns HTTP 200
- ✅ Frontend loads without errors
- ✅ You can see the "TODO" placeholders in the UI

---

**Ready to start?** Begin with Part 1 (Database Challenge) and work your way through!

Good luck! 🚀
