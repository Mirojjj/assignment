# Junior Developer Assessment - Quick Reference

## 🚀 For Evaluators: Getting Started in 30 Seconds

```powershell
# 1. Navigate to assignment directory
cd c:\Users\CITYTECH\projects\GroupManagement\docs\assignment-junior-developer

# 2. Verify setup
.\verify-setup.ps1

# 3. Start all services
.\start-dev.ps1
```

**Access Points:**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- API Docs: http://localhost:8080/swagger-ui
- Database: localhost:5432 (user: postgres, pass: postgres)

## 📊 Assessment Overview

| Part | Title | Duration | Points | What Candidates Do |
|------|-------|----------|--------|-------------------|
| 1 | Database Query Optimization | 30-40 min | 20 | Optimize slow SQL query, add indexes |
| 3 | Backend API Development | 60-90 min | 30 | Implement REST endpoints with tests |
| 4 | Frontend UI Development | 60-90 min | 30 | Choose features totaling 100 pts (flexible system) 📋 |
| 5 | Debugging Challenge | 30-40 min | 20 | Fix bugs in backend, frontend, SQL |
| **Total** | | **3-4 hours** | **100** | |

**📋 Note**: Part 4 uses an internal 100-point system. See `ASSIGNMENT_SCORING.md` for details.

## 📁 Key Files

### Documentation
- `README.md` - Main overview
- `GETTING_STARTED.md` - Quick start guide
- `SETUP.md` - Detailed setup instructions
- `EVALUATION_RUBRIC.md` - Scoring criteria
- `PROJECT_SUMMARY.md` - Complete project documentation

### Scripts
- `verify-setup.ps1` - Verify all files are ready
- `start-dev.ps1` - Start all services (Windows)
- `start-dev.sh` - Start all services (macOS/Linux)
- `docker-compose.yml` - Full stack orchestration

### Challenge Directories
```
part1-database-challenge/     # ✅ Complete: Schema + data + slow query
part3-backend-challenge/      # ⚠️  Boilerplate: Candidates implement API
part4-frontend-challenge/     # ⚠️  Boilerplate: Candidates build UI
part5-debugging-challenge/    # ✅ Complete: Buggy code + solutions
```

## 🎯 What's Implemented vs. What Candidates Build

### ✅ Already Implemented (Working)

**Database:**
- Complete schema with 3 tables
- 3,000 sample transactions
- Realistic payment data
- Slow query example

**Backend:**
- Micronaut project structure
- Entity classes (TransactionMaster, TransactionDetail, Member)
- Repository interfaces
- Health check endpoint
- Error handling framework
- Docker configuration

**Frontend:**
- React + TypeScript + Vite setup
- Type definitions
- API client (Axios)
- 6 UI components (Button, Card, Table, Input, LoadingSpinner, Header)
- Utility functions (formatters)
- Basic App.tsx structure

### ⚠️ What Candidates Implement

**Part 1 - Database:**
- Analyze problematic query
- Create optimized version
- Add appropriate indexes
- Document improvements

**Part 3 - Backend:**
- `TransactionController` with search endpoint
- `TransactionService` business logic
- Filtering and pagination
- Unit tests
- Integration tests

**Part 4 - Frontend:**
- 🎯 **Target**: Complete 100 points from available features
- **Choose from**:
  - Merchants Management (4 features, 100 pts available)
  - Reports & Analytics (4 features, 100 pts available)
- Document selection in `TASK_SELECTION.md`
- Quality matters: Points × Quality Multiplier
- 📋 See `ASSIGNMENT_SCORING.md` for complete breakdown

**Part 5 - Debugging:**
- Identify bugs in all 3 samples
- Explain root causes
- Provide fixes
- Test solutions

## 📝 Evaluation Quick Tips

### Excellent Candidate (80-100 points)
- ✅ Query runs 10x+ faster with proper indexes
- ✅ Clean REST API with comprehensive tests
- ✅ Frontend: 90+ internal points with high quality (Part 4)
- ✅ Strategic task selection demonstrating prioritization
- ✅ Found all bugs with thorough explanations
- ✅ Well-documented code
- ✅ Follows established patterns

### Good Candidate (60-79 points)
- ✅ Query improved but not optimal
- ✅ Working API with basic tests
- ✅ Frontend: 70-89 internal points with good quality (Part 4)
- ✅ Found most bugs
- ✅ Code works but needs refinement

### Needs Improvement (<60 points)
- ❌ Query still slow or incorrect
- ❌ API incomplete or buggy
- ❌ UI broken or poor UX
- ❌ Missed critical bugs
- ❌ Poor code quality

## 🔧 Common Issues & Solutions

### "Docker not running"
```powershell
# Start Docker Desktop manually
# OR check Task Manager for Docker processes
```

### "Port already in use"
```powershell
# Find process using port
netstat -ano | findstr :8080
# Kill process
taskkill /PID <PID> /F
```

### "Backend won't compile"
```bash
cd part3-backend-challenge
./mvnw clean install
```

### "Frontend won't start"
```bash
cd part4-frontend-challenge
rm -rf node_modules
npm install
```

### "Database has no data"
```bash
docker-compose down -v
docker-compose up -d postgres
# Wait 30 seconds for initialization
```

## 📞 Quick Checks

### Verify Database
```bash
docker exec payment-platform-db psql -U postgres -d payment_platform -c "SELECT COUNT(*) FROM operators.transaction_master;"
# Should return ~3000
```

### Verify Backend
```bash
curl http://localhost:8080/health
# Should return: {"status":"UP",...}
```

### Verify Frontend
Open http://localhost:3000 in browser
Should see "Transaction Dashboard"

## 🎓 Assessment Tips

1. **Give candidates the full codebase** - Everything in `assignment-junior-developer/`
2. **Set up GitHub repository** - Candidates should commit often
3. **Provide time limits** - Enforce 3-4 hour maximum
4. **Review commits** - Check thought process and progression
5. **Ask follow-up questions** - Understand their reasoning
6. **Test manually** - Actually run their code
7. **Check documentation** - README updates, code comments

## 📊 Time Allocation Guidance

Advise candidates:
- **Part 1**: 30-40 minutes
- **Part 3**: 60-90 minutes (30% of time)
- **Part 4**: 60-90 minutes (30% of time)
- **Part 5**: 30-40 minutes

If running over time, they should:
1. Focus on one part completely rather than all parts partially
2. Document what they would do with more time
3. Submit what they have

## ✅ Pre-Distribution Checklist

- [ ] Run `.\verify-setup.ps1` - all tests pass
- [ ] Test database startup: `docker-compose up -d postgres`
- [ ] Verify sample data loaded (3000 rows)
- [ ] Test backend health endpoint
- [ ] Test frontend loads
- [ ] Review all README files
- [ ] Check solutions are in place (Part 5)
- [ ] Prepare evaluation rubric
- [ ] Set up Git repository
- [ ] Send candidates GETTING_STARTED.md

## 📦 Distribution Package

Send candidates:
1. Zip file of entire `assignment-junior-developer/` directory
2. GETTING_STARTED.md as email body
3. Time limit: 3-4 hours
4. Submission: Git repository URL or zip file
5. Include: Code + README with explanations

## 🎉 Ready to Use!

Everything is set up and tested. The assessment is:
- ✅ Complete and working
- ✅ Realistic and relevant
- ✅ Well-documented
- ✅ Fair and measurable
- ✅ Ready for candidates

**Good luck with your assessments!**

---

*For detailed information, see:*
- Main overview: `README.md`
- Setup guide: `SETUP.md`
- Scoring: `EVALUATION_RUBRIC.md`
- Complete docs: `PROJECT_SUMMARY.md`
