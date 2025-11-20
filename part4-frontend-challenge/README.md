# Part 4: Frontend Development - Flexible Point-Based System

**Time Estimate**: 60-90 minutes  
**Points**: 25/100 (rubric score)  
**Internal Scoring**: 100-point flexible system

## 🎯 Objective

Choose and implement features totaling **100 points** from two available pages (Merchants Management and Reports & Analytics). You have complete flexibility in what you implement - pick any combination that reaches 100 points!

## 📋 Point-Based System Overview

This challenge uses a flexible scoring approach:
- **Target**: Complete 100 points worth of features
- **Flexibility**: Choose from 8 features across 2 pages
- **Strategy**: Pick features that align with your strengths
- **Documentation**: Use `TASK_SELECTION_TEMPLATE.md` to document your choices
- **Quality Matters**: Your score = Points × Quality Multiplier

📖 **See `ASSIGNMENT_SCORING.md` for complete details**, including:
- All available features and point values
- Sub-task breakdowns
- Submission requirements
- Review process and quality criteria
- Strategy tips and examples

## What's Already Provided

This boilerplate includes:
- ✅ React 18 + TypeScript setup (Vite)
- ✅ Project structure and folder organization
- ✅ Basic components (Button, Card, Table, etc.)
- ✅ API service boilerplate
- ✅ TypeScript types/interfaces
- ✅ Styling setup (CSS Modules)
- ✅ ESLint and Prettier configuration

## Your Task

### Step 1: Choose Your Features (100 Points Total)

Visit the assignment pages to see all available features:
- **Merchants Page**: `http://localhost:3000/merchants` - 4 features (100 pts available)
- **Reports Page**: `http://localhost:3000/reports` - 4 features (100 pts available)

Each page displays:
- Feature descriptions
- Point values (main + sub-tasks)
- Implementation requirements

### Step 2: Document Your Selection

1. Copy `TASK_SELECTION_TEMPLATE.md` to `TASK_SELECTION.md` in your project root
2. Check the features you plan to implement
3. List all sub-tasks you'll complete
4. Calculate your point total (must equal 100)
5. Add your implementation plan

### Step 3: Implement Your Chosen Features

Build the features you selected with focus on:
- **Functionality (60%)**: Features work as described
- **Code Quality (25%)**: Clean, maintainable, well-structured code
- **UX (15%)**: Intuitive interface, good error/loading states

### Available Features Quick Reference

**Merchants Management (100 points available):**
1. Merchant List View (30 pts) - Display, search, filter, sort, pagination
2. Add New Merchant (25 pts) - Form with validation and API integration
3. Edit Merchant Details (20 pts) - Update merchant information
4. Merchant Details View (25 pts) - Profile, stats, transactions, timeline

**Reports & Analytics (100 points available):**
1. Transaction Analytics (35 pts) - Volume charts, success rates, trends
2. Revenue Reports (30 pts) - Period analysis, forecasting, breakdowns
3. Export & Download (20 pts) - CSV/PDF export with email delivery
4. Interactive Charts (15 pts) - Various chart types with drill-down

📋 **See assignment pages for complete details on each feature's requirements**

## Technical Requirements

### Required Features
- ✅ React 18+ with Hooks
- ✅ TypeScript (strict mode)
- ✅ Responsive design (mobile-friendly)
- ✅ Loading indicators
- ✅ Error handling and display
- ✅ Accessible UI (ARIA labels)
- ✅ Clean component structure

### API Integration

Depending on your chosen features, you may need to implement:

**Merchant APIs** (for Merchants Management features):
```
GET    /api/v1/merchants                    # List merchants
POST   /api/v1/merchants                    # Create merchant
GET    /api/v1/merchants/{id}               # Get merchant details
PUT    /api/v1/merchants/{id}               # Update merchant
GET    /api/v1/merchants/{id}/transactions  # Get merchant transactions
GET    /api/v1/merchants/{id}/stats         # Get merchant statistics
```

**Transaction APIs** (for Reports & Analytics features):
```
GET    /api/v1/transactions                 # List transactions with filters
GET    /api/v1/transactions/analytics       # Get analytics data
GET    /api/v1/transactions/revenue         # Get revenue reports
POST   /api/v1/transactions/export          # Export transactions (CSV/PDF)
```

**Note**: These are example endpoints. You may mock API responses or implement the actual backend API endpoints as needed for your chosen features.

## Project Structure

```
part4-frontend-challenge/
├── public/
│   └── vite.svg
├── src/
│   ├── components/
│   │   ├── common/                       ✅ Basic components provided
│   │   │   ├── Button.tsx
│   │   │   ├── Card.tsx
│   │   │   ├── Input.tsx
│   │   │   ├── Table.tsx
│   │   │   └── LoadingSpinner.tsx
│   │   ├── merchants/                    ⚠️  TODO: Implement based on selection
│   │   ├── reports/                      ⚠️  TODO: Implement based on selection
│   │   └── layout/
│   │       ├── Header.tsx                ✅ Provided
│   │       └── Layout.tsx                ✅ Provided
│   ├── pages/
│   │   ├── Transactions.tsx              ✅ Example page (reference)
│   │   ├── Merchants.tsx                 ✅ Assignment page (shows tasks)
│   │   └── Reports.tsx                   ✅ Assignment page (shows tasks)
│   ├── services/
│   │   ├── api.ts                        ✅ API client provided
│   │   └── [your services]               ⚠️  TODO: Implement as needed
│   ├── hooks/
│   │   └── [your hooks]                  ⚠️  TODO: Implement as needed
│   ├── types/
│   │   └── transaction.ts                ✅ Types provided
│   ├── utils/
│   │   ├── formatters.ts                 ✅ Utility functions provided
│   │   └── constants.ts                  ✅ Constants provided
│   ├── App.tsx                           ✅ Provided
│   ├── App.css                           ✅ Provided
│   ├── main.tsx                          ✅ Provided
│   └── vite-env.d.ts                     ✅ Provided
├── package.json                          ✅ Provided
├── tsconfig.json                         ✅ Provided
├── vite.config.ts                        ✅ Provided
├── .env.example                          ✅ Provided
└── README.md                             ✅ This file
```

## Getting Started

### 1. Install Dependencies

```bash
npm install
```

### 2. Configure Environment

```bash
# Copy example env file
cp .env.example .env.local

# Edit .env.local with your API URL
VITE_API_BASE_URL=http://localhost:8080/api/v1
```

### 3. Start Development Server

```bash
npm run dev
# App will be available at http://localhost:3000
```

### 4. Development Workflow

```bash
# Type checking
npm run type-check

# Linting
npm run lint

# Build for production
npm run build

# Preview production build
npm run preview
```

## Implementation Guide

### Step 1: Review Assignment Pages

1. Start the development server: `npm run dev`
2. Visit `http://localhost:3000/merchants` and `http://localhost:3000/reports`
3. Review all available features and their point values
4. Note the sub-tasks and acceptance criteria for each

### Step 2: Select Your Features

1. Copy `TASK_SELECTION_TEMPLATE.md` to `TASK_SELECTION.md`
2. Choose features that total 100 points
3. Consider your strengths and time management
4. Document your implementation plan

**Example Selection Strategies:**

**Breadth Approach** (implement partially across multiple features):
- Merchant List (30) + Add Merchant (25) + Edit Merchant (20) + Merchant Details (25) = 100

**Depth Approach** (implement fully from one or two features):
- Transaction Analytics (35) + Revenue Reports (30) + Export (20) + Charts (15) = 100

**Balanced Approach** (mix of complete and partial):
- Merchant List (30) + Transaction Analytics (35) + Merchant Details (25) + Charts (10) = 100

### Step 3: Set Up Your Component Structure

Based on your selections, create appropriate folders:

```bash
# For Merchants features
mkdir src/components/merchants
mkdir src/services/merchants

# For Reports features
mkdir src/components/reports
mkdir src/services/reports

# For shared functionality
mkdir src/hooks
```

### Step 4: Implement Incrementally

1. Start with the highest-point features
2. Focus on core functionality first
3. Add polish and error handling
4. Test as you go
5. Track your progress in TASK_SELECTION.md

### Example Component Pattern

```typescript
// src/components/merchants/MerchantList.tsx
import { useState, useEffect } from 'react';
import { Table } from '../common/Table';
import { LoadingSpinner } from '../common/LoadingSpinner';

export const MerchantList = () => {
  const [merchants, setMerchants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    // Fetch merchants from API or use mock data
    fetchMerchants()
      .then(setMerchants)
      .catch(err => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <LoadingSpinner />;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="merchant-list">
      <h2>Merchants</h2>
      <Table data={merchants} columns={columns} />
    </div>
  );
};
```

## Evaluation Criteria

### Point Achievement (15/25 rubric points)
Your internal 100-point score converts to rubric score based on:
- **90-110 points** (high quality) → 13-15 rubric points
- **75-89 points** (good quality) → 10-12 rubric points  
- **60-74 points** (acceptable quality) → 7-9 rubric points
- **Below 60 points** → 0-6 rubric points

Quality multipliers are based on:
- **Functionality (60%)**: Features work correctly and meet requirements
- **Code Quality (25%)**: Clean architecture, TypeScript usage, maintainability
- **UX (15%)**: Intuitive interface, responsive, good error/loading states

### Component Architecture & Code Quality (5/25 rubric points)
- Clean component hierarchy and composition
- Proper TypeScript usage (no 'any', strong typing)
- Reusable components following single responsibility
- Clean separation of concerns

### Task Selection & Strategy (3/25 rubric points)
- Strategic task selection demonstrating prioritization
- Clear documentation in TASK_SELECTION.md
- Realistic time estimation
- Thoughtful implementation plan

### UI/UX Quality (2/25 rubric points)
- Clean, intuitive interface
- Responsive design
- Consistent styling
- Good loading and error states

📋 **See `ASSIGNMENT_SCORING.md` for detailed quality criteria and examples**

## Tips for Success

### Strategy
1. **Choose Wisely**: Pick features that align with your strengths
2. **Time Box**: Allocate time per feature based on points
3. **Start with High Value**: Tackle high-point features first
4. **Quality over Quantity**: Better to do 80 points well than 100 points poorly

### Implementation
5. **Use Provided Code**: Leverage the boilerplate components (Button, Card, Table, etc.)
6. **TypeScript First**: Define types before implementing features
7. **Test Incrementally**: Build and test one feature at a time
8. **Mock When Needed**: Use mock data if backend APIs aren't ready

### Best Practices
9. **Clean Code**: Keep components small and focused
10. **Error Handling**: Always handle loading, error, and empty states
11. **Responsive Design**: Test on different screen sizes
12. **Document As You Go**: Update TASK_SELECTION.md with progress

## Common Pitfalls to Avoid

❌ **Scope Creep**: Don't try to implement everything  
❌ **Poor Planning**: Skipping the selection/documentation step  
❌ **No Error Handling**: Ignoring loading/error states  
❌ **Type Errors**: Using 'any' instead of proper types  
❌ **Bad UX**: Forgetting mobile responsiveness or accessibility  
❌ **Time Management**: Spending too long on one feature  

## Example Time Allocation (90 minutes)

- **10 min**: Review pages, select features, document choices
- **60 min**: Implementation (divide by number of features)
- **15 min**: Testing, polish, error handling
- **5 min**: Final documentation update

## Need Help?

- **Assignment Pages**: Visit `/merchants` and `/reports` for feature details
- **Scoring Guide**: See `ASSIGNMENT_SCORING.md` for complete breakdown
- **Template**: Use `TASK_SELECTION_TEMPLATE.md` for documentation
- **Example Code**: Check `src/pages/Transactions.tsx` for reference implementation
- Implement transaction detail modal
- Add export to CSV functionality
- Implement dark mode
- Add animations/transitions
- Implement virtualized scrolling for large lists

## Testing Your Work

Test with different scenarios:
- ✅ Large dataset (1000+ transactions)
- ✅ Empty results
- ✅ API errors (disconnect backend)
- ✅ Different screen sizes
- ✅ Keyboard navigation
- ✅ Fast filter changes

## Resources

- [React Documentation](https://react.dev/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [Vite Guide](https://vitejs.dev/guide/)
- [React Hooks](https://react.dev/reference/react)

---

**Good luck!** Remember: User experience matters. Make it work, then make it beautiful.
