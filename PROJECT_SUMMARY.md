# Junior Developer Assessment - Complete Setup Guide

## ✅ What's Included

This is a **complete, ready-to-run** junior developer assessment for a payment platform position. Everything needed for candidates to start working is included.

### 📦 Complete Package Includes:

1. **Database Challenge (Part 1)** ✅
   - Full PostgreSQL schema
   - 3,000+ sample transactions
   - Realistic payment platform data
   - Problematic query to optimize

2. **Backend Boilerplate (Part 3)** ✅
   - Micronaut 4.2.0 + Java 17 project
   - Complete entity layer
   - Repository interfaces
   - Working health endpoint
   - OpenAPI/Swagger documentation
   - Docker configuration

3. **Frontend Boilerplate (Part 4)** ✅
   - React 18 + TypeScript project
   - Vite build system
   - Complete type definitions
   - API service layer
   - 6 reusable UI components
   - Utility functions

4. **Debugging Challenge (Part 5)** ✅
   - Buggy backend service (concurrency issues)
   - Buggy frontend component (memory leaks)
   - Buggy SQL query (logic errors)
   - Complete solutions for evaluators

5. **Development Infrastructure** ✅
   - Docker Compose configuration
   - Startup scripts (Windows + macOS/Linux)
   - GitHub Codespaces support
   - Automated database seeding

6. **Documentation** ✅
   - Main README with assessment overview
   - Detailed SETUP.md guide
   - EVALUATION_RUBRIC.md (100 points breakdown)
   - GETTING_STARTED.md quick start guide
   - UML diagrams (4 different views)
   - Per-challenge README files

## 🚀 Quick Start for Evaluators

### Option 1: One-Command Start (Recommended)

**Windows:**
```powershell
cd docs\assignment-junior-developer
.\start-dev.ps1
```

**macOS/Linux:**
```bash
cd docs/assignment-junior-developer
chmod +x start-dev.sh
./start-dev.sh
```

This will:
1. Start PostgreSQL with sample data
2. Optionally start backend API
3. Optionally start frontend

### Option 2: Docker Compose (Full Stack)

```bash
cd docs/assignment-junior-developer
docker-compose up -d
```

Access:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- API Docs: http://localhost:8080/swagger-ui
- PostgreSQL: localhost:5432

### Option 3: Manual (For Development)

```bash
# Terminal 1: Database
docker-compose up -d postgres

# Terminal 2: Backend
cd part3-backend-challenge
./mvnw mn:run

# Terminal 3: Frontend
cd part4-frontend-challenge
npm install
npm run dev
```

## 📊 Assessment Structure

### **Part 1: Database & Query Optimization** (20 points, 30-40 min)
**What candidates get:**
- Complete database schema (transaction_master, transaction_details, members)
- 3,000 sample transactions spanning 3 days
- Slow query with correlated subquery

**What candidates do:**
- Analyze problematic query
- Identify performance issues
- Optimize query (indexes, JOINs, CTEs)
- Explain improvements

**Evaluation criteria:**
- Query performance improvement
- Index strategy
- EXPLAIN ANALYZE understanding
- Code documentation

---

### **Part 3: Backend API Development** (30 points, 60-90 min)
**What candidates get:**
- Complete Micronaut project structure
- Entity classes with proper annotations
- Repository interfaces
- Working health endpoint example
- Error handling framework

**What candidates do:**
- Implement `TransactionController`
- Create `TransactionService` business logic
- Add endpoint for transaction search
- Implement filtering and pagination
- Write unit tests

**Evaluation criteria:**
- REST API design
- Service layer architecture
- Error handling
- Test coverage
- Code quality

---

### **Part 4: Frontend Development** (30 points, 60-90 min)
**What candidates get:**
- Complete React + TypeScript setup
- Type definitions for all data structures
- API service boilerplate
- 6 reusable UI components (Button, Card, Table, etc.)
- Utility functions (formatters, validators)
- **📋 Point-based system**: Choose features totaling 100 points

**What candidates do:**
- **🎯 Target**: Complete 100 points worth of features (flexible selection)
- Choose from 8 available features across 2 pages:
  - **Merchants Management**: 4 features (100 pts available)
  - **Reports & Analytics**: 4 features (100 pts available)
- Document task selection using provided template
- Implement chosen features with quality focus

**Evaluation criteria:**
- Point achievement (internal 100-point system converts to 25-point rubric score)
- Code quality (component architecture, TypeScript usage)
- Task selection strategy and prioritization
- UI/UX quality

**📖 See**: `ASSIGNMENT_SCORING.md` for complete point breakdown

---

### **Part 5: Debugging Challenge** (20 points, 30-40 min)
**What candidates get:**
- 3 buggy code samples (backend, frontend, SQL)
- Instructions to identify and fix bugs

**Bug 1 - Backend:** Payment processing service with concurrency issues
**Bug 2 - Frontend:** React component with memory leaks and performance issues
**Bug 3 - SQL:** Settlement report with GROUP BY logic error

**What candidates do:**
- Identify all bugs
- Explain impact and root cause
- Provide working fixes
- Suggest improvements

**Evaluation criteria:**
- Bug identification completeness
- Technical explanation depth
- Fix correctness
- Best practices application

---

## 🎯 Assessment Goals

This assessment evaluates:

1. **Problem-Solving Skills**
   - SQL optimization strategies
   - Debugging methodology
   - Performance analysis

2. **Multi-Stack Adaptability**
   - Java/Micronaut backend
   - React/TypeScript frontend
   - PostgreSQL database
   - Docker containers

3. **Code Quality**
   - Clean code principles
   - Proper error handling
   - Test coverage
   - Documentation

4. **System Thinking**
   - API design decisions
   - Component architecture
   - Data modeling
   - Performance considerations

## 📁 Project Structure

```
assignment-junior-developer/
│
├── README.md                       # Main overview
├── SETUP.md                        # Setup instructions
├── EVALUATION_RUBRIC.md            # Scoring guide (100 pts)
├── GETTING_STARTED.md              # Quick start guide
├── INDEX.md                        # Navigation guide
│
├── docker-compose.yml              # Full stack orchestration
├── start-dev.ps1                   # Windows startup script
├── start-dev.sh                    # macOS/Linux startup script
│
├── .devcontainer/                  # GitHub Codespaces config
│   ├── devcontainer.json
│   ├── docker-compose.yml
│   ├── post-create.sh
│   └── post-start.sh
│
├── docs/
│   └── uml/                        # Architecture diagrams
│       ├── system-architecture.puml
│       ├── backend-layered-architecture.puml
│       ├── frontend-component-architecture.puml
│       └── database-schema.puml
│
├── part1-database-challenge/       # SQL Optimization
│   ├── README.md
│   ├── schema.sql                  # ✅ Database schema
│   ├── sample-data.sql             # ✅ 3,000 transactions
│   └── original-query.sql          # ✅ Slow query to optimize
│
├── part3-backend-challenge/        # Backend API
│   ├── README.md
│   ├── pom.xml                     # ✅ Maven config
│   ├── Dockerfile                  # ✅ Docker build
│   ├── src/
│   │   └── main/
│   │       ├── java/com/payment/
│   │       │   ├── Application.java              # ✅ Main entry
│   │       │   ├── entity/                       # ✅ Complete entities
│   │       │   │   ├── TransactionMaster.java
│   │       │   │   ├── TransactionDetail.java
│   │       │   │   └── Member.java
│   │       │   ├── repository/                   # ✅ Data access
│   │       │   │   ├── TransactionRepository.java
│   │       │   │   ├── TransactionDetailRepository.java
│   │       │   │   └── MemberRepository.java
│   │       │   ├── controller/                   # ⚠️ Candidate implements
│   │       │   │   ├── HealthController.java     # ✅ Example
│   │       │   │   └── TransactionController.java # ⚠️ TODO
│   │       │   └── exception/                    # ✅ Error handling
│   │       │       └── GlobalExceptionHandler.java
│   │       └── resources/
│   │           ├── application.yml               # ✅ Configuration
│   │           └── logback.xml                   # ✅ Logging
│   └── test/                                     # ⚠️ Candidate writes tests
│
├── part4-frontend-challenge/       # Frontend UI
│   ├── README.md
│   ├── package.json                # ✅ Dependencies
│   ├── Dockerfile                  # ✅ Production build
│   ├── nginx.conf                  # ✅ Server config
│   ├── tsconfig.json               # ✅ TypeScript strict mode
│   ├── vite.config.ts              # ✅ Build config
│   ├── .env.example                # ✅ Environment template
│   ├── src/
│   │   ├── main.tsx                # ✅ Entry point
│   │   ├── App.tsx                 # ⚠️ Candidate implements features
│   │   ├── types/                  # ✅ Complete type definitions
│   │   │   └── transaction.ts
│   │   ├── services/               # ✅ API services
│   │   │   ├── api.ts              # ✅ Axios client
│   │   │   └── transactionService.ts # ⚠️ Candidate extends
│   │   ├── utils/                  # ✅ Helper functions
│   │   │   └── formatters.ts       # ✅ Currency, date formatters
│   │   └── components/
│   │       └── common/             # ✅ Reusable UI components
│   │           ├── Header.tsx
│   │           ├── LoadingSpinner.tsx
│   │           ├── Button.tsx
│   │           ├── Card.tsx
│   │           ├── Input.tsx
│   │           └── Table.tsx
│   └── public/                     # ✅ Static assets
│
└── part5-debugging-challenge/      # Bug Hunting
    ├── README.md
    ├── buggy-backend/
    │   ├── PaymentProcessingService.java  # ✅ Concurrency bugs
    │   └── SOLUTION.md                    # 🔒 For evaluators
    ├── buggy-frontend/
    │   ├── TransactionList.tsx            # ✅ Memory leak bugs
    │   └── SOLUTION.md                    # 🔒 For evaluators
    └── buggy-sql/
        ├── settlement-report.sql          # ✅ Logic error
        └── SOLUTION.md                    # 🔒 For evaluators
```

**Legend:**
- ✅ = Complete and ready to use
- ⚠️ = Candidate needs to implement/extend
- 🔒 = For evaluator use only (solutions)

## 🧪 Verification Checklist

Before giving to candidates, verify:

- [ ] **Database starts successfully**
  ```bash
  docker exec payment-platform-db psql -U postgres -d payment_platform -c "SELECT COUNT(*) FROM operators.transaction_master;"
  ```
  Should return ~3000

- [ ] **Backend health check works**
  ```bash
  curl http://localhost:8080/health
  ```
  Should return HTTP 200 with UP status

- [ ] **Frontend loads**
  ```
  Open http://localhost:3000 in browser
  ```
  Should see "Transaction Dashboard" header

- [ ] **API documentation accessible**
  ```
  Open http://localhost:8080/swagger-ui
  ```
  Should see Swagger UI with health endpoint

- [ ] **Sample data is realistic**
  ```sql
  SELECT * FROM operators.transaction_master LIMIT 10;
  ```
  Should show varied transaction dates, amounts, statuses

## 📝 For Candidates

### What's Already Implemented

**You DON'T need to:**
- Set up the database (already done)
- Create entities/models (already provided)
- Configure Docker (already configured)
- Write basic UI components (provided)
- Set up API client (already configured)

**You DO need to:**
- Optimize the slow database query
- Implement the transaction API endpoints
- Build the transaction dashboard UI
- Fix the buggy code samples

### Expected Time Investment

- **Part 1 (Database)**: 30-40 minutes
- **Part 3 (Backend)**: 60-90 minutes
- **Part 4 (Frontend)**: 60-90 minutes
- **Part 5 (Debugging)**: 30-40 minutes

**Total: 3-4 hours**

### Success Criteria

You'll succeed if you:
1. ✅ Optimize query to run 10x+ faster
2. ✅ Implement working REST API with tests
3. ✅ Build functional transaction dashboard
4. ✅ Identify and fix all major bugs

## 🎓 For Evaluators

### Scoring Breakdown

- **Part 1**: 20 points (Query optimization, indexing strategy)
- **Part 3**: 30 points (API implementation, architecture, tests)
- **Part 4**: 30 points (UI components, state management, UX)
- **Part 5**: 20 points (Bug identification, fixes, explanations)

**Total: 100 points**

See `EVALUATION_RUBRIC.md` for detailed scoring criteria.

### Evaluation Tips

1. **Check Git Commits**: Look for meaningful commit messages and logical progression
2. **Run Tests**: Execute backend and frontend test suites
3. **Review Code Quality**: Check for clean code, proper error handling, documentation
4. **Test Manually**: Actually use the application, try edge cases
5. **Read Explanations**: Candidate's written explanations are as important as code

### Red Flags

- ❌ No error handling
- ❌ No tests written
- ❌ Hard-coded values
- ❌ Copy-paste code without understanding
- ❌ No comments or documentation
- ❌ Ignoring provided boilerplate patterns

### Green Flags

- ✅ Comprehensive error handling
- ✅ Good test coverage
- ✅ Clear, self-documenting code
- ✅ Follows established patterns
- ✅ Thoughtful commit messages
- ✅ Performance considerations
- ✅ Accessibility features

## 🔧 Troubleshooting

### Common Issues

**"Port 8080 already in use"**
```bash
# Find and kill process
netstat -ano | findstr :8080  # Windows
lsof -ti:8080 | xargs kill -9  # macOS/Linux
```

**"Database connection failed"**
```bash
# Check if PostgreSQL is running
docker ps
# Restart if needed
docker-compose restart postgres
```

**"Frontend won't start"**
```bash
# Clear cache and reinstall
cd part4-frontend-challenge
rm -rf node_modules package-lock.json
npm install
```

**"Maven build fails"**
```bash
# Verify Java version
java -version  # Should be 17+
# Clean build
cd part3-backend-challenge
./mvnw clean install
```

## 📞 Support

If you encounter issues:

1. Check `SETUP.md` for detailed setup instructions
2. Review `GETTING_STARTED.md` for quick start guide
3. Verify Docker Desktop is running
4. Ensure ports 3000, 5432, 8080 are available
5. Check that Java 17+ and Node.js 18+ are installed

## 🎉 Ready to Go!

Everything is set up and ready for candidates. The boilerplate provides:

- ✅ **Working infrastructure** (database, containers)
- ✅ **Complete project structure** (backend + frontend)
- ✅ **Reusable components** (UI, API, entities)
- ✅ **Clear examples** (health endpoint, formatters)
- ✅ **Comprehensive docs** (README files, comments)

Candidates can focus on:
- 💡 Problem-solving
- 💻 Writing quality code
- 🐛 Debugging skills
- 🏗️ Architecture decisions

**Time to assess some junior developers!** 🚀

---

**Created with ❤️ for effective technical assessment**
