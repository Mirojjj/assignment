# Assignment Scoring System

## 🎯 Overview

This assignment uses a **flexible point-based system** where you need to complete **100 points** worth of tasks.

### Key Points
- **Target Score**: 100 points total
- **Flexible Completion**: Choose any combination of tasks to reach 100 points
- **Two Feature Areas**: Merchants (100pts available) and Reports (100pts available)
- **You don't need to complete everything** - just reach 100 points across all tasks

## 📋 Available Tasks

### Merchants Management (100 Points Available)

#### 1. Merchant List View (30 points total)
- Display merchant information in table format (10 pts)
- Search and filter by name, ID, or status (5 pts)
- Sort by various criteria (5 pts)
- Pagination for large datasets (5 pts)
- Loading states and error handling (5 pts)

#### 2. Add New Merchant (25 points total)
- Form with merchant details (name, email, phone) (8 pts)
- Business information and registration (5 pts)
- Submit to POST /api/v1/merchants (5 pts)
- Input validation and error handling (4 pts)
- Success notifications and form reset (3 pts)

#### 3. Edit Merchant Details (20 points total)
- Pre-populate form with existing data (5 pts)
- Update contact details and address (5 pts)
- Manage merchant status (active/inactive) (5 pts)
- Submit to PUT /api/v1/merchants/:id (3 pts)
- Confirmation dialogs (2 pts)

#### 4. Merchant Details View (25 points total)
- Display complete merchant profile (5 pts)
- Show transaction statistics (8 pts)
- List recent transactions (7 pts)
- View merchant activity timeline (3 pts)
- Export transaction history (2 pts)

### Reports & Analytics (100 Points Available)

#### 1. Transaction Analytics (35 points total)
- Total transaction volume by day/week/month (10 pts)
- Success vs. failure rate analysis (8 pts)
- Average transaction amount trends (7 pts)
- Peak transaction times heatmap (5 pts)
- Card type distribution (5 pts)

#### 2. Revenue Reports (30 points total)
- Revenue by time period (daily/weekly/monthly) (8 pts)
- Revenue breakdown by merchant (7 pts)
- Revenue forecasting based on trends (8 pts)
- Year-over-year growth analysis (4 pts)
- Top performing merchants ranking (3 pts)

#### 3. Export & Download (20 points total)
- CSV export for Excel analysis (5 pts)
- PDF report generation (7 pts)
- Scheduled email delivery (4 pts)
- Custom date range selection (2 pts)
- Export history tracking (2 pts)

#### 4. Interactive Charts (15 points total)
- Line charts for trends over time (4 pts)
- Bar charts for comparisons (4 pts)
- Pie charts for distribution (3 pts)
- Real-time data updates (2 pts)
- Drill-down capabilities (2 pts)

## 📝 Submission Requirements

### 1. Task Selection Document

Create a file named `TASK_SELECTION.md` in the root directory with:

```markdown
# Task Selection

## Developer Information
- Name: [Your Name]
- Date: [Submission Date]

## Selected Tasks (Total: 100 points)

### Merchants Management
- [ ] Merchant List View - Display table (10 pts)
- [ ] Merchant List View - Search/filter (5 pts)
- [ ] Add New Merchant - Form fields (8 pts)
...

### Reports & Analytics
- [ ] Transaction Analytics - Volume charts (10 pts)
- [ ] Revenue Reports - Time period breakdown (8 pts)
...

## Total Points: 100

## Implementation Plan
Brief description of your approach and timeline.
```

### 2. Implementation

- Complete the selected tasks following technical specifications on each page
- Write clean, well-documented code
- Include error handling and loading states
- Test your implementation thoroughly

### 3. Submission

Submit:
1. Your `TASK_SELECTION.md` file
2. All implementation code
3. Brief documentation of any assumptions or design decisions

## 📊 Review Process

### Scoring Criteria

Each task will be evaluated on:

1. **Functionality** (60%)
   - Does it work as specified?
   - Handles edge cases?
   - Error handling implemented?

2. **Code Quality** (25%)
   - Clean, readable code
   - Proper TypeScript types
   - Follows established patterns
   - Well-organized components

3. **User Experience** (15%)
   - Intuitive interface
   - Responsive design
   - Loading states
   - Error messages

### Point Assignment

- **Full Points**: Task fully functional, good code quality, excellent UX
- **Partial Points**: Task works but has issues (bugs, poor UX, code quality issues)
- **No Points**: Task not completed or doesn't work

### Final Score Calculation

```
Final Score = Sum of (Task Points × Quality Multiplier)

Where Quality Multiplier:
- Excellent: 1.0 (100%)
- Good: 0.8 (80%)
- Acceptable: 0.6 (60%)
- Poor: 0.3 (30%)
- Not Working: 0.0 (0%)
```

## 💡 Strategy Tips

### Recommended Approach

1. **Start with List Views** (30-35 pts)
   - Usually easiest to implement
   - Builds foundation for other features
   - High point value

2. **Add CRUD Operations** (25-30 pts)
   - Practical, commonly-used skills
   - Good demonstration of form handling

3. **Finish with Analytics or Details** (35-45 pts)
   - More complex but impressive
   - Shows advanced skills
   - Can be time-consuming

### Example Strategies

**Strategy A: Breadth** (Cover both areas)
- Merchant List View (30 pts)
- Add New Merchant (25 pts)
- Transaction Analytics (35 pts)
- CSV Export (10 pts from Export feature)
= **100 points**

**Strategy B: Depth** (Master one area)
- All Merchant features (100 pts)
= **100 points**

**Strategy C: Balanced** (Mix of features)
- Merchant List (30 pts) + Details View (25 pts)
- Transaction Analytics (35 pts)
- Export & Charts (10 pts from both)
= **100 points**

## ❓ FAQ

**Q: Can I exceed 100 points?**
A: Yes! Extra points demonstrate initiative, but 100 is the target.

**Q: What if I don't complete all selected tasks?**
A: You'll receive points only for completed tasks. Choose carefully!

**Q: Can I change my task selection?**
A: Document any changes in your submission notes. Focus on completing what you start.

**Q: Which features should I prioritize?**
A: Start with List Views (high points, foundation for others). Then add features that showcase your strengths.

**Q: How long should this take?**
A: Estimate 4-6 hours for 100 points, depending on your experience level.

## 🚀 Getting Started

1. Review all available tasks on the Merchants and Reports pages
2. Choose tasks totaling 100 points
3. Create your `TASK_SELECTION.md` file
4. Begin implementation
5. Test thoroughly
6. Submit your work

---

**Remember**: Quality over quantity. It's better to complete fewer tasks excellently than many tasks poorly.
