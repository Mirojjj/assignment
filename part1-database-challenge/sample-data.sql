-- ============================================================================
-- SAMPLE DATA FOR TESTING
-- ============================================================================
-- This script creates realistic test data for the payment platform.
-- Run this after creating the schema.
-- ============================================================================

-- Clear existing data
TRUNCATE operators.transaction_details CASCADE;
TRUNCATE operators.transaction_master CASCADE;
TRUNCATE operators.members CASCADE;

-- Reset sequences
ALTER SEQUENCE operators.members_member_id_seq RESTART WITH 1;
ALTER SEQUENCE operators.transaction_master_txn_id_seq RESTART WITH 1;
ALTER SEQUENCE operators.transaction_details_txn_detail_id_seq RESTART WITH 1;

-- ============================================================================
-- Insert Members (Acquirers and Issuers)
-- ============================================================================

INSERT INTO operators.members (member_name, member_type, member_code, country, status) VALUES
('Global Payment Services', 'acquirer', 'GPS-001', 'USA', 'active'),
('European Bank Network', 'acquirer', 'EBN-002', 'GBR', 'active'),
('Asia Pacific Payments', 'acquirer', 'APP-003', 'SGP', 'active'),
('MasterCard International', 'issuer', 'MCI-101', 'USA', 'active'),
('Visa Worldwide', 'issuer', 'VWW-102', 'USA', 'active'),
('American Express', 'both', 'AMX-103', 'USA', 'active'),
('Discover Financial', 'both', 'DFS-104', 'USA', 'active'),
('UnionPay International', 'issuer', 'UPI-105', 'CHN', 'active'),
('JCB International', 'issuer', 'JCB-106', 'JPN', 'active'),
('Regional Processor', 'acquirer', 'RPR-004', 'CAN', 'active');

-- ============================================================================
-- Insert Transaction Master Records
-- ============================================================================
-- Creates transactions for a 3-day period (Nov 16-18, 2025)
-- Realistic distribution: ~1000 transactions per day

DO $$
DECLARE
    v_merchant_id VARCHAR(50);
    v_acquirer_id BIGINT;
    v_issuer_id BIGINT;
    v_txn_date DATE;
    v_txn_time TIMESTAMP WITH TIME ZONE;
    v_amount DECIMAL(15,2);
    v_status VARCHAR(20);
    v_card_types VARCHAR[] := ARRAY['VISA', 'MASTERCARD', 'AMEX', 'DISCOVER'];
    v_statuses VARCHAR[] := ARRAY['completed', 'completed', 'completed', 'completed', 'completed', 'completed', 'completed', 'completed', 'pending', 'failed'];
    i INTEGER;
    j INTEGER;
BEGIN
    -- Generate transactions for 3 days
    FOR i IN 0..2 LOOP
        v_txn_date := DATE '2025-11-16' + i;
        
        -- Generate ~1000 transactions per day
        FOR j IN 1..1000 LOOP
            -- Random merchant (50 merchants)
            v_merchant_id := 'MCH-' || LPAD((j % 50 + 1)::TEXT, 5, '0');
            
            -- Random acquirer and issuer
            v_acquirer_id := (j % 3) + 1; -- Members 1-3 are acquirers
            v_issuer_id := (j % 5) + 4;   -- Members 4-8 are issuers
            
            -- Random timestamp during business hours
            v_txn_time := v_txn_date + ((8 + (j % 12))::TEXT || ' hours')::INTERVAL 
                         + ((j % 60)::TEXT || ' minutes')::INTERVAL
                         + ((j % 60)::TEXT || ' seconds')::INTERVAL;
            
            -- Random amount between $10 and $5000
            v_amount := (RANDOM() * 4990 + 10)::DECIMAL(15,2);
            
            -- Status (90% completed, 8% pending, 2% failed)
            v_status := v_statuses[(j % 10) + 1];
            
            INSERT INTO operators.transaction_master (
                merchant_id,
                gp_acquirer_id,
                gp_issuer_id,
                txn_date,
                local_txn_date_time,
                amount,
                currency,
                status,
                card_type,
                card_last4,
                auth_code,
                response_code
            ) VALUES (
                v_merchant_id,
                v_acquirer_id,
                v_issuer_id,
                v_txn_date,
                v_txn_time,
                v_amount,
                'USD',
                v_status,
                v_card_types[(j % 4) + 1],
                LPAD((1000 + j % 9000)::TEXT, 4, '0'),
                CASE WHEN v_status = 'completed' THEN 'AUTH' || LPAD(j::TEXT, 6, '0') ELSE NULL END,
                CASE WHEN v_status = 'completed' THEN '00' WHEN v_status = 'pending' THEN '01' ELSE '05' END
            );
        END LOOP;
    END LOOP;
END $$;

-- ============================================================================
-- Insert Transaction Details
-- ============================================================================
-- Creates 3-7 detail records per transaction (fees, taxes, etc.)

DO $$
DECLARE
    v_txn_record RECORD;
    v_detail_count INTEGER;
    v_detail_types VARCHAR[] := ARRAY['fee', 'tax', 'adjustment'];
    i INTEGER;
BEGIN
    FOR v_txn_record IN 
        SELECT txn_id, amount, local_txn_date_time, currency 
        FROM operators.transaction_master 
    LOOP
        -- Random number of details (3-7 per transaction)
        v_detail_count := 3 + (RANDOM() * 4)::INTEGER;
        
        FOR i IN 1..v_detail_count LOOP
            INSERT INTO operators.transaction_details (
                master_txn_id,
                detail_type,
                amount,
                currency,
                description,
                local_txn_date_time
            ) VALUES (
                v_txn_record.txn_id,
                v_detail_types[(i % 3) + 1],
                (v_txn_record.amount * (0.01 + RANDOM() * 0.05))::DECIMAL(15,2), -- 1-6% of transaction
                v_txn_record.currency,
                'Auto-generated ' || v_detail_types[(i % 3) + 1] || ' for transaction',
                v_txn_record.local_txn_date_time + ((i * 100)::TEXT || ' milliseconds')::INTERVAL
            );
        END LOOP;
    END LOOP;
END $$;

-- ============================================================================
-- VERIFY DATA
-- ============================================================================

SELECT 'Members' AS table_name, COUNT(*) AS row_count FROM operators.members
UNION ALL
SELECT 'Transaction Master', COUNT(*) FROM operators.transaction_master
UNION ALL
SELECT 'Transaction Details', COUNT(*) FROM operators.transaction_details;

-- Show sample transactions
SELECT 
    tm.txn_id,
    tm.merchant_id,
    tm.amount,
    tm.status,
    tm.txn_date,
    COUNT(td.txn_detail_id) AS detail_count
FROM operators.transaction_master tm
LEFT JOIN operators.transaction_details td ON tm.txn_id = td.master_txn_id
GROUP BY tm.txn_id
ORDER BY tm.txn_id
LIMIT 10;

-- ============================================================================
-- VACUUM ANALYZE
-- ============================================================================
-- Update table statistics for accurate query planning

VACUUM ANALYZE operators.members;
VACUUM ANALYZE operators.transaction_master;
VACUUM ANALYZE operators.transaction_details;

-- ============================================================================
-- DATA GENERATION COMPLETE
-- ============================================================================

SELECT '✅ Sample data created successfully!' AS status;
SELECT '📊 Total transactions: ' || COUNT(*) FROM operators.transaction_master;
SELECT '📋 Total details: ' || COUNT(*) FROM operators.transaction_details;
SELECT '🏦 Total members: ' || COUNT(*) FROM operators.members;
