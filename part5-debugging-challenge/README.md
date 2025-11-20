# Part 5: Debugging Challenge

**Time Allocation:** 30-40 minutes  
**Points:** 20 points  
**Difficulty:** Intermediate

## 🎯 Objective

Identify and fix bugs in provided code samples from different parts of the stack. This tests your debugging skills, attention to detail, and understanding of common pitfalls.

## 📋 Overview

You'll be given three buggy code samples:
1. **Backend (Java)**: Concurrency and transaction handling issues
2. **Frontend (React)**: Performance and state management bugs
3. **Database (SQL)**: Query logic errors

For each bug:
- ✅ Identify what's wrong
- ✅ Explain the issue and its impact
- ✅ Provide a working fix
- ✅ Explain why your fix works

## 🐛 Bug 1: Backend - Payment Processing Service

**File:** `buggy-backend/PaymentProcessingService.java`

### The Bug

The following service processes multiple payments concurrently but has critical bugs:

```java
@Singleton
public class PaymentProcessingService {
    
    private final PaymentRepository paymentRepository;
    private final AuditService auditService;
    private BigDecimal totalProcessed = BigDecimal.ZERO;
    
    @Inject
    public PaymentProcessingService(PaymentRepository paymentRepository, 
                                   AuditService auditService) {
        this.paymentRepository = paymentRepository;
        this.auditService = auditService;
    }
    
    public void processPaymentBatch(List<Payment> payments) {
        payments.parallelStream().forEach(payment -> {
            try {
                // Validate payment
                if (payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("Invalid amount");
                }
                
                // Update total
                totalProcessed = totalProcessed.add(payment.getAmount());
                
                // Process payment
                payment.setStatus("COMPLETED");
                paymentRepository.update(payment);
                
                // Audit log
                auditService.log("Payment " + payment.getId() + " processed");
                
            } catch (Exception e) {
                payment.setStatus("FAILED");
                // Note: Not saving failed status to DB
            }
        });
    }
    
    public BigDecimal getTotalProcessed() {
        return totalProcessed;
    }
}
```

### Questions

1. **What are the bugs in this code?** (List all issues)
2. **What are the potential impacts?** (e.g., data corruption, race conditions)
3. **How would you fix this code?** (Provide corrected version)

---

## 🐛 Bug 2: Frontend - Transaction List Component

**File:** `buggy-frontend/TransactionList.tsx`

### The Bug

This React component fetches and displays transactions but has performance issues:

```typescript
import React, { useState, useEffect } from 'react';
import { transactionService } from '../services/transactionService';
import { Transaction } from '../types/transaction';

interface TransactionListProps {
  merchantId: string;
  refreshInterval?: number;
}

export const TransactionList: React.FC<TransactionListProps> = ({ 
  merchantId, 
  refreshInterval = 5000 
}) => {
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [filteredTransactions, setFilteredTransactions] = useState<Transaction[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  
  // Fetch transactions
  useEffect(() => {
    const fetchData = async () => {
      const data = await transactionService.getTransactions({
        merchantId,
        page: 1,
        size: 100
      });
      setTransactions(data.content);
    };
    
    fetchData();
    const interval = setInterval(fetchData, refreshInterval);
    
    // Missing cleanup
  }, [merchantId]);
  
  // Filter transactions
  useEffect(() => {
    const filtered = transactions.filter(txn => 
      txn.merchantName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      txn.transactionId.includes(searchTerm)
    );
    setFilteredTransactions(filtered);
  }, [searchTerm, transactions]);
  
  // Format currency
  const formatAmount = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  };
  
  return (
    <div>
      <input 
        type="text" 
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        placeholder="Search transactions..."
      />
      
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Merchant</th>
            <th>Amount</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {filteredTransactions.map((txn) => (
            <tr key={txn.transactionId}>
              <td>{txn.transactionId}</td>
              <td>{txn.merchantName}</td>
              <td>{formatAmount(txn.totalAmount)}</td>
              <td>{txn.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
```

### Questions

1. **What are the bugs/issues in this component?** (List all problems)
2. **What are the performance implications?** (Memory leaks, unnecessary re-renders, etc.)
3. **How would you fix this code?** (Provide corrected version)

---

## 🐛 Bug 3: Database - Settlement Report Query

**File:** `buggy-sql/settlement-report.sql`

### The Bug

This query calculates daily settlement amounts for merchants but returns incorrect results:

```sql
-- Calculate daily settlement amounts per merchant
SELECT 
    m.member_id,
    m.member_name,
    DATE(tm.txn_date) as settlement_date,
    SUM(tm.local_amount) as total_amount,
    COUNT(tm.txn_id) as transaction_count,
    AVG(tm.local_amount) as avg_transaction
FROM operators.members m
INNER JOIN operators.transaction_master tm ON m.member_id = tm.acq_id
WHERE tm.status = 'COMPLETED'
  AND tm.txn_date >= '2025-11-16'
  AND tm.txn_date < '2025-11-19'
GROUP BY m.member_id, m.member_name
ORDER BY settlement_date, total_amount DESC;
```

### Sample Output Issue

When executed, this query shows:
- Wrong settlement dates (not grouped by date)
- Missing some merchants who have transactions
- Incorrect average calculations

### Questions

1. **What is wrong with this query?** (Identify the logical error)
2. **Why does it produce incorrect results?** (Explain the issue)
3. **How would you fix it?** (Provide corrected query)

---

## 📊 Evaluation Criteria

### Bug 1: Backend (7 points)
- **Identification (2 points)**: Found all concurrency issues
  - Non-thread-safe `totalProcessed` variable
  - Parallel stream without synchronization
  - Failed payments not persisted
  - Missing transaction management
- **Explanation (2 points)**: Clear explanation of race conditions and data integrity issues
- **Fix (3 points)**: Correct implementation using proper synchronization or atomic operations

### Bug 2: Frontend (7 points)
- **Identification (2 points)**: Found all issues
  - Memory leak (missing interval cleanup)
  - Missing `refreshInterval` in dependency array
  - Creating `NumberFormat` on every render
  - No error handling
- **Explanation (2 points)**: Explained performance impact and memory leak
- **Fix (3 points)**: Proper cleanup, useMemo for formatter, error boundaries

### Bug 3: Database (6 points)
- **Identification (2 points)**: Found the GROUP BY issue
  - `settlement_date` not in GROUP BY clause
  - Result is effectively random date per merchant
- **Explanation (2 points)**: Explained why SQL allows this but gives wrong results
- **Fix (2 points)**: Corrected GROUP BY to include date

## 📝 Submission Requirements

For each bug, provide:

1. **Bug Report** (Markdown file)
   ```markdown
   ## Bug [Number]: [Title]
   
   ### Issues Found
   1. [Issue description]
   2. [Issue description]
   
   ### Impact
   [Explain the impact of these bugs]
   
   ### Root Cause
   [Explain why these bugs occur]
   ```

2. **Fixed Code** (Source file)
   - Provide the complete corrected code
   - Add comments explaining key fixes

3. **Test Verification** (Optional but recommended)
   - How would you test that your fix works?
   - What edge cases should be considered?

## 💡 Hints

### Bug 1 Hints
<details>
<summary>Click to reveal</summary>

- What happens when multiple threads modify `totalProcessed` simultaneously?
- Are failed payment statuses saved to the database?
- Should database operations be in a transaction?
</details>

### Bug 2 Hints
<details>
<summary>Click to reveal</summary>

- What happens to the interval when the component unmounts?
- Is the interval recreated when `refreshInterval` changes?
- When is `NumberFormat` being instantiated?
</details>

### Bug 3 Hints
<details>
<summary>Click to reveal</summary>

- Look at the SELECT clause vs GROUP BY clause
- What happens when you group by fewer columns than you select?
- Try running the query and check if dates match expectations
</details>

## 🎯 Success Metrics

**Excellent (18-20 points):**
- Found all bugs in all three samples
- Provided detailed explanations with technical depth
- Fixes are correct, production-ready, and well-commented

**Good (14-17 points):**
- Found most major bugs
- Explanations are clear and accurate
- Fixes work but may lack some edge case handling

**Needs Improvement (10-13 points):**
- Found some bugs but missed critical ones
- Explanations are superficial
- Fixes are incomplete or introduce new issues

**Insufficient (<10 points):**
- Found few bugs
- Explanations are vague or incorrect
- Fixes don't resolve the issues

## 📁 Files Provided

```
part5-debugging-challenge/
├── README.md (this file)
├── buggy-backend/
│   ├── PaymentProcessingService.java
│   └── SOLUTION.md
├── buggy-frontend/
│   ├── TransactionList.tsx
│   └── SOLUTION.md
└── buggy-sql/
    ├── settlement-report.sql
    └── SOLUTION.md
```

**Note:** SOLUTION.md files contain the answers and are for evaluator use only. Do not share with candidates.

---

## 🚀 Getting Started

1. Review each buggy code sample carefully
2. Try to run/test the code to see the issues firsthand
3. Document your findings
4. Implement fixes
5. Verify your fixes work correctly

**Time Management:**
- Bug 1 (Backend): ~15 minutes
- Bug 2 (Frontend): ~15 minutes  
- Bug 3 (Database): ~10 minutes

Good luck! 🐛🔍
