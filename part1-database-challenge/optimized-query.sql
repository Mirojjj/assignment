-- ============================================================================
-- OPTIMIZED QUERY
-- ============================================================================
-- Strategy:
-- 1. Filter transaction_master first to get the smallest working set.
-- 2. Pre-aggregate transaction_details in a CTE for ONLY the relevant transactions.
--    This avoids the N+1 subquery problem and the redundant join explosion.
-- 3. Join the filtered master records with the pre-aggregated details.
-- ============================================================================

WITH relevant_transactions AS (
    -- Step 1: Filter master table first
    -- This reduces the set from 5M rows to just the ~100k we need.
    SELECT 
        txn_id,
        gp_acquirer_id,
        gp_issuer_id,
        txn_date,
        local_txn_date_time
    FROM operators.transaction_master
    WHERE txn_date > DATE '2025-11-16' 
      AND txn_date < DATE '2025-11-18'
),
transaction_details_agg AS (
    -- Step 2: Aggregate details for only the relevant transactions
    -- We join to the filtered CTE, so we only scan details for the 100k transactions.
    -- We do the JSON aggregation here ONCE per transaction, not in a loop.
    SELECT 
        td.master_txn_id,
        json_agg(
            json_build_object(
                'txn_detail_id', td.txn_detail_id,
                'master_txn_id', td.master_txn_id,
                'detail_type', td.detail_type,
                'amount', td.amount,
                'currency', td.currency,
                'description', td.description,
                'local_txn_date_time', td.local_txn_date_time,
                'converted_date', td.local_txn_date_time AT TIME ZONE 'UTC'
            ) ORDER BY td.local_txn_date_time DESC
        ) AS details
    FROM operators.transaction_details td
    JOIN relevant_transactions rt ON td.master_txn_id = rt.txn_id
    GROUP BY td.master_txn_id
)
SELECT
    rt.txn_id,
    rt.gp_acquirer_id,
    rt.gp_issuer_id,
    rt.txn_date,
    rt.local_txn_date_time,
    rt.txn_id AS "tm.txnId",
    rt.local_txn_date_time AT TIME ZONE 'UTC' AS "tm.localTxnDateTime",
    COALESCE(tda.details, '[]'::json) AS details,
    ins.member_name AS member,
    iss.member_name AS issuer
FROM relevant_transactions rt
LEFT JOIN transaction_details_agg tda ON rt.txn_id = tda.master_txn_id
LEFT JOIN operators.members ins ON rt.gp_acquirer_id = ins.member_id
LEFT JOIN operators.members iss ON rt.gp_issuer_id = iss.member_id
ORDER BY rt.local_txn_date_time DESC;

