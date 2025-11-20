# Part 1: Database & Query Optimization Challenge

**Time Estimate**: 45 minutes  
**Points**: 15/100

## Objective
Analyze and optimize a slow-running SQL query from our payment platform. This query retrieves merchant transactions with details, but it's causing performance issues in production.

## Background
The payment platform processes millions of transactions daily. The reporting system needs to fetch transaction data with all associated details for merchant dashboards. Currently, this query takes 15-20 seconds for date ranges with 100K+ transactions, causing timeouts and poor user experience.

## Database Schema

```sql
-- operators.transaction_master
-- Primary table storing transaction headers
-- ~5M rows, growing by 200K/day

-- operators.transaction_details  
-- Detail records for each transaction (fees, adjustments, etc.)
-- ~25M rows, average 5 details per transaction

-- operators.members
-- Acquirer and issuer member information
-- ~500 rows (relatively static)
```

See `schema.sql` for complete structure and sample data in `sample-data.sql`.

## The Problem Query

Review `original-query.sql` - this is the current production query that's causing performance issues.

**Known Issues**:
- Takes 15-20 seconds for date ranges with 100K transactions
- Database CPU spikes to 90%+ when this query runs
- Causes connection pool exhaustion during peak hours
- Reports timing out for users

## Your Tasks

### Task 1: Performance Analysis (6 points)

Create a file `analysis.md` with the following:

1. **Identify Performance Bottlenecks** (3 points)
   - What are the main performance issues?
   - Explain WHY each issue causes slowness
   - What is the time complexity (Big O notation)?

2. **Explain Query Execution** (3 points)
   - How does PostgreSQL execute this query?
   - What operations are most expensive?
   - Estimate performance impact for 100K vs 1M transactions

### Task 2: Optimized Query (6 points)

Create a file `optimized-query.sql` with:

1. **Rewritten Query** (4 points)
   - Eliminate performance bottlenecks
   - Maintain identical output structure
   - Add comments explaining key optimizations

2. **Alternative Approaches** (2 points)
   - Provide at least one alternative optimization approach
   - Explain trade-offs between approaches

### Task 3: Index Strategy (3 points)

Create a file `indexes.sql` with:

1. **Index Recommendations** (2 points)
   - Suggest appropriate indexes
   - Explain reasoning for each index
   - Consider both read and write performance

2. **Performance Estimation** (1 point)
   - Estimate performance improvement (e.g., "50x faster")
   - Justify your estimation

## Deliverables

```
part1-database-challenge/
├── analysis.md           # Your performance analysis
├── optimized-query.sql   # Your optimized version
└── indexes.sql           # Index recommendations
```

## Evaluation Criteria

- **Correctness**: Does the optimized query produce the same results?
- **Performance**: How much improvement does your solution provide?
- **Understanding**: Do you understand WHY the original is slow?
- **Communication**: Can you explain technical concepts clearly?

## Testing Your Solution

```sql
-- 1. Load sample data
\i sample-data.sql

-- 2. Test original query
\timing
\i original-query.sql

-- 3. Test your optimized query
\timing
\i optimized-query.sql

-- 4. Compare results
-- Both should return identical data
```

## Hints

- Look for correlated subqueries (subqueries that reference outer query)
- Consider when data is being aggregated
- Think about how many times each table is scanned
- EXPLAIN ANALYZE is your friend!

## Resources

- PostgreSQL EXPLAIN: https://www.postgresql.org/docs/current/sql-explain.html
- Query Optimization: https://www.postgresql.org/docs/current/performance-tips.html
- Your existing analysis file: `../../query-optimization-analysis.sql`

## Bonus Challenges (Optional)

- Run EXPLAIN ANALYZE on both queries and compare execution plans
- Calculate actual performance improvement ratio
- Suggest application-level caching strategies
- Consider partitioning strategies for very large tables

---

**Good luck!** Focus on understanding the problem first, then solving it systematically.
