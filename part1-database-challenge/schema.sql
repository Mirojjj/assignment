-- ============================================================================
-- DATABASE SCHEMA FOR PAYMENT PLATFORM
-- ============================================================================

-- Create schema
CREATE SCHEMA IF NOT EXISTS operators;

-- Enable extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- ============================================================================
-- Table: members
-- Stores acquirer and issuer member information
-- ============================================================================

DROP TABLE IF EXISTS operators.members CASCADE;

CREATE TABLE operators.members (
    member_id BIGSERIAL PRIMARY KEY,
    member_name VARCHAR(255) NOT NULL,
    member_type VARCHAR(20) NOT NULL CHECK (member_type IN ('acquirer', 'issuer', 'both')),
    member_code VARCHAR(20) UNIQUE NOT NULL,
    country VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'active',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- ============================================================================
-- Table: transaction_master
-- Primary transaction records
-- ============================================================================

DROP TABLE IF EXISTS operators.transaction_master CASCADE;

CREATE TABLE operators.transaction_master (
    txn_id BIGSERIAL PRIMARY KEY,
    merchant_id VARCHAR(50) NOT NULL,
    gp_acquirer_id BIGINT REFERENCES operators.members(member_id),
    gp_issuer_id BIGINT REFERENCES operators.members(member_id),
    txn_date DATE NOT NULL,
    local_txn_date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    status VARCHAR(20) NOT NULL CHECK (status IN ('pending', 'completed', 'failed', 'reversed')),
    card_type VARCHAR(20),
    card_last4 VARCHAR(4),
    auth_code VARCHAR(20),
    response_code VARCHAR(10),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- ============================================================================
-- Table: transaction_details
-- Detail records for each transaction (fees, taxes, adjustments)
-- ============================================================================

DROP TABLE IF EXISTS operators.transaction_details CASCADE;

CREATE TABLE operators.transaction_details (
    txn_detail_id BIGSERIAL PRIMARY KEY,
    master_txn_id BIGINT NOT NULL REFERENCES operators.transaction_master(txn_id),
    detail_type VARCHAR(50) NOT NULL CHECK (detail_type IN ('fee', 'tax', 'adjustment', 'refund', 'chargeback')),
    amount DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    description TEXT,
    local_txn_date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- ============================================================================
-- INDEXES (Current - Suboptimal)
-- ============================================================================

-- Basic indexes only - NOT optimized for the problematic query
CREATE INDEX idx_transaction_master_merchant_id ON operators.transaction_master(merchant_id);
CREATE INDEX idx_transaction_master_txn_date ON operators.transaction_master(txn_date);
CREATE INDEX idx_transaction_details_master_txn_id ON operators.transaction_details(master_txn_id);
CREATE INDEX idx_members_member_id ON operators.members(member_id);

-- ============================================================================
-- COMMENTS
-- ============================================================================

COMMENT ON TABLE operators.transaction_master IS 'Main transaction records - header level';
COMMENT ON TABLE operators.transaction_details IS 'Transaction detail records - multiple per transaction';
COMMENT ON TABLE operators.members IS 'Acquirer and issuer member directory';

COMMENT ON COLUMN operators.transaction_master.txn_date IS 'Transaction date (used for partitioning in production)';
COMMENT ON COLUMN operators.transaction_master.local_txn_date_time IS 'Transaction timestamp in local timezone';
COMMENT ON COLUMN operators.transaction_details.master_txn_id IS 'Foreign key to transaction_master';

-- ============================================================================
-- TABLE STATISTICS
-- ============================================================================

-- Production table sizes (for reference):
-- transaction_master: ~5,000,000 rows (growing 200K/day)
-- transaction_details: ~25,000,000 rows (avg 5 per transaction)
-- members: ~500 rows (relatively static)

-- ============================================================================
-- NOTES FOR CANDIDATES
-- ============================================================================

/*
This schema represents a simplified version of the production database.

Key characteristics:
1. transaction_master stores header-level transaction info
2. transaction_details stores multiple detail records per transaction
3. Members table is small and relatively static
4. Typical queries filter by merchant_id and txn_date range
5. Detail records need to be aggregated and returned as JSON

Performance considerations:
- High read volume on transaction tables
- Date range queries are common (last 7 days, last 30 days)
- JSON aggregation of details is expensive
- Timezone conversions happen frequently

Your task: Optimize queries against this schema!
*/
