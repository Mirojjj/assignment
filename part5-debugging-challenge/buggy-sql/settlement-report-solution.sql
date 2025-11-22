/*
 * FIXED SQL - Settlement Report
 * Correctly groups by date to show daily settlement amounts per merchant.
 */

-- Correct version: Calculate daily settlement amounts per merchant
SELECT 
    m.member_id,
    m.member_name,
    DATE(tm.txn_date) as settlement_date,
    SUM(tm.local_amount) as total_amount,
    COUNT(tm.txn_id) as transaction_count,
    AVG(tm.local_amount) as avg_transaction,
FROM operators.members m
INNER JOIN operators.transaction_master tm ON m.member_id = tm.acq_id
WHERE tm.status = 'COMPLETED'
  AND tm.txn_date >= '2025-11-16'
  AND tm.txn_date < '2025-11-19'
GROUP BY 
    m.member_id, 
    m.member_name, 
    DATE(tm.txn_date)  -- Added settlement_date to GROUP BY
ORDER BY 
    settlement_date ASC,     -- Then by date ascending
    total_amount DESC;       -- Then by amount descending


/**
Why SQL Allows This (And It's Wrong)
MySQL Behavior:

Has ONLY_FULL_GROUP_BY mode (default in modern versions)
With it OFF: Returns arbitrary values for non-grouped columns
With it ON: Throws error (correct!)

PostgreSQL Behavior:

Stricter: Always errors if non-aggregated column not in GROUP BY
More SQL-standard compliant

SQL Server Behavior:

Always errors: Doesn't allow non-aggregated columns outside GROUP BY

Why Databases Allow It:

Legacy compatibility
Performance optimization shortcuts
Developers sometimes intentionally use arbitrary selection
*/


-- Test queries
-- For member 101, sum all amounts manually
SELECT 
    SUM(tm.local_amount) as total_should_match
FROM operators.transaction_master tm
WHERE tm.acq_id = 101
  AND tm.status = 'COMPLETED'
  AND tm.txn_date >= '2025-11-16'
  AND tm.txn_date < '2025-11-19';

-- matches the result to above correct query

-- Verify aggregation
-- Check if daily totals add up to member total
SELECT 
    m.member_id,
    SUM(daily_total) as member_total
FROM (
    SELECT 
        m.member_id,
        SUM(tm.local_amount) as daily_total
    FROM operators.members m
    INNER JOIN operators.transaction_master tm ON m.member_id = tm.acq_id
    WHERE tm.status = 'COMPLETED'
      AND tm.txn_date >= '2025-11-16'
      AND tm.txn_date < '2025-11-19'
    GROUP BY m.member_id, m.member_name, DATE(tm.txn_date)
) grouped
GROUP BY m.member_id;