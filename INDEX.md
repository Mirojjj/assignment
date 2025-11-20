# Assignment Index - Quick Navigation

## 📋 Main Documents

| Document | Description | Path |
|----------|-------------|------|
| **Main README** | Complete assignment overview and instructions | [README.md](./README.md) |
| **Setup Guide** | Development environment setup (local & Codespaces) | [SETUP.md](./SETUP.md) |
| **Evaluation Rubric** | Scoring criteria and grading guidelines | [EVALUATION_RUBRIC.md](./EVALUATION_RUBRIC.md) |
| **Assignment Scoring** | Part 4 point-based system (100-point flexible completion) | [ASSIGNMENT_SCORING.md](./ASSIGNMENT_SCORING.md) |
| **Task Selection Template** | Template for candidates to document their Part 4 choices | [TASK_SELECTION_TEMPLATE.md](./TASK_SELECTION_TEMPLATE.md) |

## 🎨 UML Diagrams

| Diagram | Description | Path |
|---------|-------------|------|
| **System Architecture** | High-level payment platform architecture | [system-architecture.puml](./system-architecture.puml) |
| **Backend Architecture** | Micronaut layered architecture pattern | [backend-layered-architecture.puml](./backend-layered-architecture.puml) |
| **Frontend Architecture** | React component architecture | [frontend-component-architecture.puml](./frontend-component-architecture.puml) |
| **Database Schema** | PostgreSQL database schema with relationships | [database-schema.puml](./database-schema.puml) |

### Viewing PlantUML Diagrams

**Option 1: VS Code**
```bash
# Install PlantUML extension
code --install-extension jebbs.plantuml

# Then open any .puml file and press Alt+D (preview)
```

**Option 2: Online Viewer**
- Visit: https://www.plantuml.com/plantuml/uml/
- Copy/paste diagram code

**Option 3: Generate PNG**
```bash
# Install PlantUML
npm install -g node-plantuml

# Generate images
puml generate system-architecture.puml -o ./images/
```

## 📂 Assignment Parts Structure

```
assignment-junior-developer/
├── README.md                              # Main overview
├── SETUP.md                               # Setup instructions
├── EVALUATION_RUBRIC.md                   # Scoring rubric
│
├── system-architecture.puml               # UML diagrams
├── backend-layered-architecture.puml
├── frontend-component-architecture.puml
├── database-schema.puml
│
├── .devcontainer/                         # Codespaces config
│   ├── devcontainer.json
│   ├── docker-compose.yml
│   ├── post-create.sh
│   └── post-start.sh
│
├── part1-database-challenge/              # Database optimization
│   ├── README.md
│   ├── original-query.sql
│   └── sample-data.sql
│
├── part2-system-design/                   # System design scenario
│   ├── README.md
│   └── requirements.md
│
├── part3-backend-challenge/               # Backend API (Micronaut)
│   ├── README.md
│   ├── pom.xml
│   ├── src/
│   └── Dockerfile
│
├── part4-frontend-challenge/              # Frontend (React)
│   ├── README.md
│   ├── package.json
│   ├── src/
│   └── Dockerfile
│
└── part5-debugging-challenge/             # Debugging tasks
    ├── README.md
    ├── buggy-java-service.java
    ├── buggy-react-component.tsx
    └── buggy-query.sql
```

## 🚀 Quick Start

### For Candidates

1. **Read First**:
   - [Main README](./README.md) - Complete overview
   - [SETUP.md](./SETUP.md) - Environment setup

2. **Setup Environment**:
   - **Codespaces**: Click "Code" → "Create codespace" (recommended)
   - **Local**: Follow [SETUP.md](./SETUP.md) instructions

3. **Review Architecture**:
   - Open UML diagrams (`.puml` files)
   - Understand system structure

4. **Start Assessment**:
   - Begin with Part 1 (or your strongest area)
   - Follow instructions in each part's README

5. **Submit**:
   - Push code to your fork
   - Create submission document

### For Evaluators

1. **Review Documents**:
   - [EVALUATION_RUBRIC.md](./EVALUATION_RUBRIC.md) - Scoring guidelines
   - [Main README](./README.md) - Assignment requirements

2. **Clone Candidate Fork**:
   ```bash
   git clone <candidate-fork-url>
   cd junior-developer-assessment
   ```

3. **Run Their Code**:
   ```bash
   # Using Codespaces (easiest)
   # Or local Docker:
   docker-compose up -d
   ```

4. **Evaluate Each Part**:
   - Use rubric consistently
   - Document observations
   - Provide constructive feedback

## 📚 Technology Stack

### Backend
- **Framework**: Micronaut 4.x
- **Language**: Java 17+
- **Database**: PostgreSQL 14+
- **Build Tool**: Maven or Gradle
- **Testing**: JUnit 5, Mockito
- **API Docs**: OpenAPI/Swagger

### Frontend
- **Framework**: React 18+
- **Language**: TypeScript
- **Build Tool**: Vite
- **State**: Context API / Zustand
- **Styling**: CSS Modules / Tailwind
- **Testing**: Vitest, React Testing Library

### Infrastructure
- **Containerization**: Docker
- **Development**: GitHub Codespaces
- **Database**: PostgreSQL
- **Caching**: Redis (optional)

## 🎯 Learning Objectives

By completing this assessment, candidates demonstrate:

1. **Multi-Stack Proficiency**
   - Backend: Java/Micronaut
   - Frontend: React/TypeScript
   - Database: PostgreSQL/SQL

2. **Problem-Solving Skills**
   - Query optimization
   - System design
   - Debugging

3. **Software Engineering Practices**
   - Clean code
   - Testing
   - API design
   - Error handling

4. **Domain Knowledge**
   - Payment systems
   - Transaction processing
   - Data modeling

## 📖 Additional Resources

### Official Documentation
- [Micronaut Documentation](https://docs.micronaut.io/latest/guide/)
- [React Documentation](https://react.dev/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)

### Recommended Reading
- **Backend**: "Clean Architecture" by Robert C. Martin
- **Frontend**: "React Design Patterns" by John Larson
- **Database**: "High Performance PostgreSQL" by Gregory Smith
- **System Design**: "Designing Data-Intensive Applications" by Martin Kleppmann

### Tools & Extensions
- **VS Code Extensions**:
  - Java Extension Pack
  - Micronaut Tools
  - ESLint
  - Prettier
  - PostgreSQL
  - PlantUML

- **API Testing**:
  - Thunder Client (VS Code)
  - Postman
  - curl

## ⚡ Tips for Success

### For Candidates
1. **Time Management**: Don't spend too long on one part
2. **Read Carefully**: Understand requirements before coding
3. **Start Simple**: Get something working, then improve
4. **Document Thinking**: Comment your reasoning
5. **Test Your Code**: Run and verify before submitting
6. **Ask Questions**: Clarify ambiguous requirements

### For Evaluators
1. **Be Consistent**: Apply rubric uniformly
2. **Focus on Fundamentals**: Junior-level expectations
3. **Value Learning**: Look for growth potential
4. **Provide Feedback**: Specific, actionable guidance
5. **Consider Context**: Time constraints, experience level

## 🤝 Support & Contact

**For Technical Issues**:
- Check [SETUP.md](./SETUP.md) troubleshooting section
- Review error messages carefully
- Search documentation

**For Assessment Questions**:
- [Contact information]

---

**Ready to Start?** Begin with the [Main README](./README.md)! 🚀
