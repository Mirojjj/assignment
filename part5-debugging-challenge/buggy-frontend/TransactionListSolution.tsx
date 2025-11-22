import React, { useState, useEffect, useMemo, useCallback } from 'react';
import { transactionService } from '../services/transactionService';
import { Transaction } from '../types/transaction';
import { LoadingSpinner } from '../components/common/LoadingSpinner';

interface TransactionListProps {
    merchantId: string;
    refreshInterval?: number;
}

export const TransactionListSolution: React.FC<TransactionListProps> = ({
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
