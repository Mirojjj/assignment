import React, { useState, useEffect } from 'react';
import { transactionService } from '../services/transactionService';
import { Transaction } from '../types/transaction';

/**
 * BUGGY CODE - DO NOT USE IN PRODUCTION
 * This component has multiple bugs related to memory leaks, 
 * performance issues, and missing dependencies.
 * Candidates should identify and fix these issues.
 */

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
  
  // BUG 1: Missing cleanup for interval (memory leak)
  // BUG 2: Missing refreshInterval in dependency array
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
    
    // Missing cleanup:
    // return () => clearInterval(interval);
  }, [merchantId]); // BUG: refreshInterval missing from deps
  
  // Filter transactions
  useEffect(() => {
    const filtered = transactions.filter(txn => 
      txn.merchantName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      txn.transactionId.includes(searchTerm)
    );
    setFilteredTransactions(filtered);
  }, [searchTerm, transactions]);
  
  // BUG 3: Creating new NumberFormat instance on every render (performance)
  const formatAmount = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  };
  
  // BUG 4: No error handling
  // BUG 5: No loading state
  
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
          {/* BUG 6: Map function with formatAmount call on every render */}
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
