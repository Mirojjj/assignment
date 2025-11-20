# Bug 2: Frontend - Transaction List Component - SOLUTION

## Issues Found

### 1. **Memory Leak - Missing Interval Cleanup (Critical)**
```typescript
useEffect(() => {
    fetchData();
    const interval = setInterval(fetchData, refreshInterval);
    
    // Missing cleanup!
}, [merchantId]);
```

**Problem:** Interval continues running after component unmounts.

**Impact:**
- Memory leak - interval keeps executing
- API calls continue even when component is gone
- Multiple intervals stack up if component remounts
- Browser tab becomes slow over time
- Unnecessary server load

**How to Reproduce:**
1. Navigate to page with TransactionList
2. Navigate away
3. Check Network tab - API calls still happening
4. Repeat navigation 10 times - 10 intervals running!

### 2. **Missing Dependency - `refreshInterval` (Major)**
```typescript
useEffect(() => {
    const interval = setInterval(fetchData, refreshInterval);
}, [merchantId]); // refreshInterval missing!
```

**Problem:** Effect doesn't re-run when `refreshInterval` prop changes.

**Impact:**
- If parent changes refresh interval, old interval keeps running
- New interval never created with updated timing
- Stale closures capture old `refreshInterval` value

### 3. **Performance Issue - NumberFormat Recreation (Major)**
```typescript
const formatAmount = (amount: number) => {
    return new Intl.NumberFormat('en-US', {  // Created on EVERY render!
        style: 'currency',
        currency: 'USD'
    }).format(amount);
};
```

**Problem:** `Intl.NumberFormat` is expensive to create, created on every render.

**Impact:**
- If 100 transactions displayed, 100 formatters created per render
- Component re-renders on every keystroke in search input
- Typing "merchant" = 8 keystrokes × 100 rows = 800 formatter creations
- Noticeable lag in UI

**Benchmark:**
```javascript
// Creating formatter: ~0.1ms each
// For 100 items: ~10ms
// At 60fps, budget is 16ms per frame
// Searching causes dropped frames!
```

### 4. **No Error Handling (Major)**
```typescript
const fetchData = async () => {
    const data = await transactionService.getTransactions({...});
    setTransactions(data.content);
};
```

**Problem:** If API fails, no error shown to user.

**Impact:**
- Network errors = blank screen
- User doesn't know what's wrong
- No retry mechanism
- Poor user experience

### 5. **No Loading State (Minor)**

**Problem:** No indication that data is being fetched.

**Impact:**
- User sees empty table while loading
- Can't distinguish between "loading" and "no data"
- Poor UX

### 6. **Unnecessary State - `filteredTransactions` (Minor)**

**Problem:** Filtered transactions stored in state instead of being derived.

**Impact:**
- Extra state management
- Two effects instead of one computed value
- Harder to debug
- More re-renders

## Root Cause Analysis

The developer:
1. Didn't understand useEffect cleanup functions
2. Missed dependency array warnings (or ignored them!)
3. Didn't profile performance
4. Didn't implement proper error boundaries
5. Focused on happy path, ignored edge cases

## Fixed Code

```typescript
import React, { useState, useEffect, useMemo, useCallback } from 'react';
import { transactionService } from '../services/transactionService';
import { Transaction } from '../types/transaction';
import { LoadingSpinner } from '../components/common/LoadingSpinner';

interface TransactionListProps {
  merchantId: string;
  refreshInterval?: number;
}

export const TransactionList: React.FC<TransactionListProps> = ({ 
  merchantId, 
  refreshInterval = 5000 
}) => {
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  
  // FIX 3: Create formatter once and reuse (useMemo)
  const currencyFormatter = useMemo(
    () => new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }),
    [] // Empty deps - create once
  );
  
  // Format amount function using memoized formatter
  const formatAmount = useCallback(
    (amount: number) => currencyFormatter.format(amount),
    [currencyFormatter]
  );
  
  // FIX 4: Add error handling with useCallback
  const fetchData = useCallback(async () => {
    try {
      setError(null);
      const data = await transactionService.getTransactions({
        merchantId,
        page: 1,
        size: 100
      });
      setTransactions(data.content);
    } catch (err) {
      console.error('Failed to fetch transactions:', err);
      setError(err instanceof Error ? err.message : 'Failed to fetch transactions');
    } finally {
      setLoading(false);
    }
  }, [merchantId]);
  
  // FIX 1 & 2: Proper cleanup and complete dependencies
  useEffect(() => {
    setLoading(true);
    fetchData();
    
    // Set up interval
    const interval = setInterval(fetchData, refreshInterval);
    
    // FIX 1: Clean up interval on unmount or when deps change
    return () => {
      clearInterval(interval);
    };
  }, [merchantId, refreshInterval, fetchData]); // FIX 2: Include all dependencies
  
  // FIX 6: Derive filtered transactions instead of storing in state
  const filteredTransactions = useMemo(() => {
    if (!searchTerm) return transactions;
    
    const lowerSearch = searchTerm.toLowerCase();
    return transactions.filter(txn => 
      txn.merchantName.toLowerCase().includes(lowerSearch) ||
      txn.transactionId.toLowerCase().includes(lowerSearch)
    );
  }, [searchTerm, transactions]);
  
  // FIX 5: Handle loading state
  if (loading && transactions.length === 0) {
    return <LoadingSpinner />;
  }
  
  // FIX 4: Handle error state
  if (error) {
    return (
      <div className="error-container">
        <p className="error-message">Error: {error}</p>
        <button onClick={() => fetchData()}>Retry</button>
      </div>
    );
  }
  
  return (
    <div className="transaction-list">
      <div className="search-bar">
        <input 
          type="text" 
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          placeholder="Search transactions..."
          className="search-input"
        />
        {loading && <span className="loading-indicator">Refreshing...</span>}
      </div>
      
      {filteredTransactions.length === 0 ? (
        <p className="no-results">No transactions found</p>
      ) : (
        <table className="transaction-table">
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
                <td>
                  <span className={`status status-${txn.status.toLowerCase()}`}>
                    {txn.status}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};
```

## Key Improvements

### 1. Memory Leak Fixed
```typescript
// Before:
const interval = setInterval(fetchData, refreshInterval);
// No cleanup!

// After:
useEffect(() => {
    const interval = setInterval(fetchData, refreshInterval);
    return () => clearInterval(interval); // ✅ Cleanup
}, [merchantId, refreshInterval, fetchData]);
```

### 2. Dependencies Fixed
```typescript
// Before:
}, [merchantId]); // ❌ Missing refreshInterval

// After:
}, [merchantId, refreshInterval, fetchData]); // ✅ Complete
```

### 3. Performance Optimized
```typescript
// Before: Creating formatter on every render
const formatAmount = (amount: number) => {
    return new Intl.NumberFormat(...).format(amount); // ❌ Slow
};

// After: Create once, reuse forever
const currencyFormatter = useMemo(
    () => new Intl.NumberFormat('en-US', {...}),
    []
);
```

**Performance Improvement:**
- Before: ~10ms per render with 100 items
- After: ~0.1ms per render
- **100x faster!**

### 4. Error Handling Added
```typescript
try {
    const data = await transactionService.getTransactions({...});
    setTransactions(data.content);
} catch (err) {
    setError(err.message); // ✅ Show error to user
}
```

### 5. Loading State Added
```typescript
if (loading && transactions.length === 0) {
    return <LoadingSpinner />;
}
```

### 6. Derived State Instead of Stored State
```typescript
// Before: Stored in state with useEffect
const [filteredTransactions, setFilteredTransactions] = useState([]);
useEffect(() => {
    const filtered = transactions.filter(...);
    setFilteredTransactions(filtered);
}, [searchTerm, transactions]);

// After: Computed with useMemo
const filteredTransactions = useMemo(() => {
    return transactions.filter(...);
}, [searchTerm, transactions]);
```

## Alternative Solutions

### Option 1: Custom Hook (Better Separation of Concerns)
```typescript
// useTransactions.ts
export const useTransactions = (
  merchantId: string, 
  refreshInterval: number = 5000
) => {
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  
  const fetchData = useCallback(async () => {
    // ... fetch logic
  }, [merchantId]);
  
  useEffect(() => {
    fetchData();
    const interval = setInterval(fetchData, refreshInterval);
    return () => clearInterval(interval);
  }, [merchantId, refreshInterval, fetchData]);
  
  return { transactions, loading, error, refetch: fetchData };
};

// Component
export const TransactionList: React.FC<Props> = ({ merchantId }) => {
  const { transactions, loading, error, refetch } = useTransactions(merchantId);
  // ... rest of component
};
```

### Option 2: React Query (Production Approach)
```typescript
import { useQuery } from '@tanstack/react-query';

export const TransactionList: React.FC<Props> = ({ merchantId, refreshInterval }) => {
  const { data, isLoading, error, refetch } = useQuery({
    queryKey: ['transactions', merchantId],
    queryFn: () => transactionService.getTransactions({ merchantId, page: 1, size: 100 }),
    refetchInterval: refreshInterval,
    // Built-in cleanup, caching, and error handling!
  });
  
  // ... rest of component
};
```

## Testing the Fix

### Test 1: Memory Leak Verification
```typescript
import { render, unmount } from '@testing-library/react';

test('cleans up interval on unmount', () => {
  jest.useFakeTimers();
  const fetchSpy = jest.spyOn(transactionService, 'getTransactions');
  
  const { unmount } = render(
    <TransactionList merchantId="M1" refreshInterval={1000} />
  );
  
  // After 5 seconds, should have fetched 6 times (initial + 5 intervals)
  jest.advanceTimersByTime(5000);
  expect(fetchSpy).toHaveBeenCalledTimes(6);
  
  // Unmount component
  unmount();
  
  // Advance another 5 seconds
  jest.advanceTimersByTime(5000);
  
  // Should still be 6 (no new calls after unmount)
  expect(fetchSpy).toHaveBeenCalledTimes(6);
  
  jest.useRealTimers();
});
```

### Test 2: Performance Verification
```typescript
test('formatter is created only once', () => {
  const spy = jest.spyOn(Intl, 'NumberFormat');
  
  const { rerender } = render(
    <TransactionList merchantId="M1" />
  );
  
  // Initial render
  expect(spy).toHaveBeenCalledTimes(1);
  
  // Re-render 10 times
  for (let i = 0; i < 10; i++) {
    rerender(<TransactionList merchantId="M1" />);
  }
  
  // Still only called once!
  expect(spy).toHaveBeenCalledTimes(1);
});
```

### Test 3: Dependency Array Fix
```typescript
test('recreates interval when refreshInterval changes', () => {
  jest.useFakeTimers();
  const fetchSpy = jest.spyOn(transactionService, 'getTransactions');
  
  const { rerender } = render(
    <TransactionList merchantId="M1" refreshInterval={1000} />
  );
  
  jest.advanceTimersByTime(3000);
  const callsAt1000ms = fetchSpy.mock.calls.length;
  
  // Change refresh interval
  rerender(<TransactionList merchantId="M1" refreshInterval={500} />);
  
  jest.advanceTimersByTime(3000);
  const callsAt500ms = fetchSpy.mock.calls.length - callsAt1000ms;
  
  // Should have more calls at 500ms interval
  expect(callsAt500ms).toBeGreaterThan(callsAt1000ms);
  
  jest.useRealTimers();
});
```

## Score Breakdown

**Full Credit (7/7):**
- Found all 4 major bugs (memory leak, deps, performance, error handling)
- Explained impact with technical depth
- Fixed with proper hooks (useMemo, useCallback)
- Added tests or verification steps

**Partial Credit (4-6):**
- Found 2-3 bugs
- Basic fix but not optimized
- Missing test verification

**Minimal Credit (1-3):**
- Found only cleanup issue
- Fix doesn't address all problems
- No performance consideration

## Learning Resources

- [React useEffect Cleanup](https://react.dev/reference/react/useEffect#connecting-to-an-external-system)
- [useMemo Performance Optimization](https://react.dev/reference/react/useMemo)
- [Intl.NumberFormat Performance](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl/NumberFormat)
- [React Query for Data Fetching](https://tanstack.com/query/latest)
