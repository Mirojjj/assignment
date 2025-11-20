# Junior Developer Assessment - Payment Platform
**Duration:** 3-4 hours | **Level:** Junior | **Focus:** Problem-solving, Reasoning, Multi-stack Adaptability

## Overview
This assessment evaluates your ability to work on a modern payment platform that handles merchant management and high-volume data processing. You'll demonstrate problem-solving skills, technical reasoning, and adaptability across multiple technology stacks.

## Business Context
You're joining a payment platform team that processes merchant transactions at scale. The system handles:
- **Merchant Management**: Onboarding, profiles, settlements
- **Transaction Processing**: Real-time payment processing (10K+ transactions/sec)
- **Data Analytics**: Transaction reconciliation, reporting, fraud detection
- **Multi-tenant Architecture**: Multiple payment operators with shared infrastructure

## Assessment Structure

### Part 1: Database & Query Optimization (45 minutes)
**Objective**: Evaluate SQL proficiency, performance analysis, and data modeling skills

📁 See: `part1-database-challenge/`

**Tasks**:
1. Analyze the provided slow-running query
2. Identify performance bottlenecks and explain reasoning
3. Provide an optimized version with detailed explanation
4. Suggest appropriate indexes
5. Estimate performance improvement

**What We're Looking For**:
- Understanding of query execution plans
- Knowledge of SQL optimization techniques (CTEs, JOINs, indexes)
- Ability to reason about scalability
- Clear technical communication

---

### Part 2: System Design & Architecture (45 minutes)
**Objective**: Assess architectural thinking, scalability reasoning, and technology choices

📁 See: `part2-system-design/`

**Scenario**: Design a merchant settlement processing system that:
- Processes daily settlements for 10,000 merchants
- Handles transaction volumes of 5M transactions/day
- Ensures data consistency and idempotency
- Provides real-time status tracking

**Deliverables**:
1. Architecture diagram (can use draw.io, PlantUML, or hand-drawn)
2. Written explanation (500-800 words) covering:
   - Component breakdown and responsibilities
   - Technology choices with justifications
   - Data flow and processing strategy
   - Scalability and failure handling approach
   - Trade-offs and limitations

**What We're Looking For**:
- Logical problem decomposition
- Understanding of distributed systems concepts
- Awareness of trade-offs (CAP theorem, consistency vs availability)
- Practical technology choices

---

### Part 3: Backend Development - REST API (60 minutes)
**Objective**: Test coding ability, API design, and framework knowledge

📁 See: `part3-backend-challenge/`

**Task**: Build a REST API using **Java + Micronaut** (or Spring Boot if preferred)

**Requirements**:
Create an API endpoint: `GET /api/v1/merchants/{merchantId}/transactions`

**Features**:
- Pagination support (page, size)
- Date range filtering (startDate, endDate)
- Timezone conversion (user's timezone → UTC)
- Transaction aggregation (total amount, count by status)
- Error handling with proper HTTP status codes

**Acceptance Criteria**:
- Clean code with proper layering (controller → service → repository)
- Input validation
- Unit tests for service layer
- API documentation (OpenAPI/Swagger)
- Docker-ready configuration

**Sample Response**:
```json
{
  "merchantId": "MCH-12345",
  "dateRange": {
    "start": "2025-11-01T00:00:00Z",
    "end": "2025-11-18T23:59:59Z"
  },
  "summary": {
    "totalTransactions": 1523,
    "totalAmount": 245670.50,
    "currency": "USD",
    "byStatus": {
      "completed": 1450,
      "pending": 50,
      "failed": 23
    }
  },
  "transactions": [
    {
      "txnId": "TXN-98765",
      "amount": 150.00,
      "status": "completed",
      "timestamp": "2025-11-18T14:32:15Z",
      "cardType": "VISA",
      "last4": "4242"
    }
  ],
  "pagination": {
    "page": 1,
    "size": 20,
    "totalPages": 77,
    "totalElements": 1523
  }
}
```

**What We're Looking For**:
- Clean code structure and SOLID principles
- Proper use of framework features (dependency injection, configuration)
- Error handling and validation
- Testing mindset
- API design best practices

---

### Part 4: Frontend Development - React Dashboard (60-90 minutes)
**Objective**: Evaluate React skills, state management, and UI/UX thinking

📁 See: `part4-frontend-challenge/` | 📋 See: `ASSIGNMENT_SCORING.md`

**🎯 Point-Based System**: This part uses a **flexible 100-point system**. You choose which features to implement!

**Task**: Build merchant management and analytics features using **React + TypeScript**

#### Assignment Structure
- **Transactions Page**: Already implemented (reference example)
- **Merchants Page**: 100 points available across 4 features
- **Reports Page**: 100 points available across 4 features

#### How It Works
1. Review available tasks on both pages (each has point values)
2. Select tasks totaling **100 points**
3. Complete your selected tasks
4. You don't need to complete everything - just reach 100 points!

#### Available Features

**Merchants Management** (100 points available):
- Merchant List View (30 pts) - Table, search, filter, pagination
- Add New Merchant (25 pts) - Form, validation, API integration
- Edit Merchant Details (20 pts) - Update form, status management
- Merchant Details View (25 pts) - Profile, statistics, history

**Reports & Analytics** (100 points available):
- Transaction Analytics (35 pts) - Charts, trends, metrics
- Revenue Reports (30 pts) - Period analysis, forecasting
- Export & Download (20 pts) - CSV, PDF, email delivery
- Interactive Charts (15 pts) - Visualizations, real-time updates

#### Submission Requirements
1. Create `TASK_SELECTION.md` listing your chosen tasks (must total 100 pts)
2. Implement selected features
3. Test thoroughly
4. Submit with brief documentation

**Technical Requirements**:
- React 18+ with TypeScript
- State management (Context API or custom hooks)
- API integration with error handling
- Responsive design (mobile-friendly)
- Loading states and error boundaries
- Accessible UI (ARIA labels, keyboard navigation)

**What We're Looking For**:
- Component composition and reusability
- State management strategy
- TypeScript proficiency
- Error handling patterns
- UI/UX awareness
- Task prioritization and completion
- Code quality over quantity

---

### Part 5: Debugging & Code Review (30 minutes)
**Objective**: Test debugging skills and code quality awareness

📁 See: `part5-debugging-challenge/`

**Task**: Review and fix provided buggy code samples

**Samples Provided**:
1. **Java Service** with concurrency issues
2. **React Component** with performance problems
3. **SQL Query** with incorrect results

**For Each Sample**:
1. Identify all bugs/issues
2. Explain why each is problematic
3. Provide corrected code
4. Suggest improvements beyond bug fixes

**What We're Looking For**:
- Bug detection accuracy
- Understanding of underlying issues
- Code quality awareness
- Ability to explain technical concepts

---

## Setup Instructions

### Prerequisites
- **Git**: Version control
- **Docker Desktop**: For containerized development
- **Java 17+**: For backend development
- **Node.js 18+**: For frontend development
- **PostgreSQL 14+**: Database (or use Docker)
- **VS Code** or **IntelliJ IDEA**: Recommended IDEs

### Getting Started

#### Option 1: Local Development
```bash
# Clone the repository
git clone <your-fork-url>
cd junior-developer-assessment

# Backend setup
cd part3-backend-challenge
./mvnw clean install
./mvnw mn:run

# Frontend setup
cd ../part4-frontend-challenge
npm install
npm run dev
```

#### Option 2: GitHub Codespaces (Recommended)
1. Fork this repository
2. Click "Code" → "Codespaces" → "Create codespace on main"
3. Everything will be pre-configured!
4. Services will auto-start:
   - Backend API: `http://localhost:8080`
   - Frontend: `http://localhost:3000`
   - PostgreSQL: `localhost:5432`

📁 See: `SETUP.md` for detailed instructions

---

## Submission Guidelines

### What to Submit
1. **Code**: Push to your forked repository
2. **Documentation**: README for each part explaining your approach
3. **Time Log**: Roughly how long each part took
4. **Reflection** (optional): What you learned, what was challenging

### Repository Structure
```
your-fork/
├── part1-database-challenge/
│   ├── original-query.sql
│   ├── optimized-query.sql
│   └── analysis.md
├── part2-system-design/
│   ├── architecture-diagram.png
│   └── design-document.md
├── part3-backend-challenge/
│   ├── src/
│   ├── pom.xml
│   └── README.md
├── part4-frontend-challenge/
│   ├── src/
│   ├── package.json
│   └── README.md
└── part5-debugging-challenge/
    └── solutions/
```

### Evaluation Criteria
📁 See: `EVALUATION_RUBRIC.md`

**Scoring Breakdown**:
- Part 1 (Database): 15%
- Part 2 (System Design): 20%
- Part 3 (Backend): 25%
- Part 4 (Frontend): 25%
- Part 5 (Debugging): 15%

**Key Evaluation Factors**:
- **Problem-Solving** (30%): Approach, reasoning, edge cases
- **Code Quality** (25%): Clean code, patterns, maintainability
- **Technical Depth** (25%): Framework knowledge, best practices
- **Adaptability** (20%): Multi-stack proficiency, learning ability

---

## Time Management Tips

**Suggested Schedule**:
- Part 1: 45 min (9:00 - 9:45)
- Part 2: 45 min (9:45 - 10:30)
- *Break*: 15 min (10:30 - 10:45)
- Part 3: 60 min (10:45 - 11:45)
- *Break*: 15 min (11:45 - 12:00)
- Part 4: 60 min (12:00 - 13:00)
- Part 5: 30 min (13:00 - 13:30)

**Total**: ~4 hours active work

**Tips**:
- Start with parts you're most confident in
- Don't get stuck - move on and come back if needed
- Focus on working solutions over perfect solutions
- Comment your reasoning in code
- Use TODO comments for things you'd add with more time

---

## Resources & Documentation

**Allowed Resources**:
✅ Official documentation (Micronaut, React, PostgreSQL)
✅ Stack Overflow for syntax reference
✅ Your own notes and previous projects
✅ AI assistants for syntax help (must understand and explain)

**Not Allowed**:
❌ Copying solutions from others
❌ Using complete code templates without understanding
❌ Outsourcing the work

---

## Questions & Support

**During Assessment**:
- You may ask clarifying questions about requirements
- Technical setup issues should be resolved before starting
- Questions about "the right answer" won't be answered - use your judgment!

**Contact**: [Your email/Slack channel]

---

## What Happens Next

**After Submission**:
1. **Code Review** (1-2 days): We'll review your submission
2. **Technical Discussion** (30-45 min): Live code walkthrough
   - Explain your design decisions
   - Discuss alternative approaches
   - Answer follow-up questions
3. **Feedback**: Regardless of outcome, you'll receive constructive feedback

---

## Good Luck! 🚀

Remember: This assessment is as much about your thought process and reasoning as it is about the final code. We want to see how you approach problems, make decisions, and communicate your thinking.

**Be yourself, show your skills, and have fun building!**
