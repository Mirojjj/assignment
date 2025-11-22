import { useState } from 'react';
import { FilterState, DEFAULT_FILTERS } from '../types/transaction';
import { useTransactions } from '../hooks/useTransactions';
import { TransactionFilters } from '../components/common/TransactionFilters';
import { TransactionSummary } from '../components/common/TransactionSummary';
import { TransactionList } from '../components/common/TransactionList';

export const Transactions = () => {
  // default merchant ID
  const defaultMerchantId = import.meta.env.VITE_DEFAULT_MERCHANT_ID || 'MCH-00009';

  // state to hold the merchant ID input
  const [merchantIdInput, setMerchantIdInput] = useState(defaultMerchantId);
  const [merchantId, setMerchantId] = useState(defaultMerchantId);

  const [filters, setFilters] = useState<FilterState>(DEFAULT_FILTERS);

  const { data, loading, error, refetch } = useTransactions(merchantId, filters);

  // handle merchant submit
  const handleMerchantSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setMerchantId(merchantIdInput);
    setFilters(DEFAULT_FILTERS);
  };

  // pagination handlers
  const handleNextPage = () => {
    if (data && (filters.page + 1) * filters.size < data.totalTransactions) {
      setFilters(prev => ({ ...prev, page: prev.page + 1 }));
    }
  };

  const handlePrevPage = () => {
    if (filters.page > 0) {
      setFilters(prev => ({ ...prev, page: prev.page - 1 }));
    }
  };

  // calculate total pages
  const totalPages = data ? Math.ceil(Math.min(data.totalTransactions, 60) / filters.size) : 0;

  return (
    <main className="container">
      <h1>Transaction Dashboard</h1>

      {/* Merchant ID input */}
      <form onSubmit={handleMerchantSubmit} className="merchant-form" style={{ marginBottom: '1rem' }}>
        <label htmlFor="merchantId" style={{ marginRight: '0.5rem' }}>Merchant ID:</label>
        <input
          type="text"
          id="merchantId"
          value={merchantIdInput}
          onChange={(e) => setMerchantIdInput(e.target.value)}
          style={{ padding: '0.5rem', borderRadius: '4px', border: '1px solid #ccc', marginRight: '0.5rem' }}
        />
        <button type="submit" style={{ padding: '0.5rem 1rem', borderRadius: '4px', background: '#3b82f6', color: '#fff' }}>
          Load
        </button>
      </form>

      <p className="subtitle">Currently Viewing: {merchantId}</p>

      {/* Filters */}
      <div className="filters-section">
        <TransactionFilters
          filters={filters}
          onFilterChange={(newFilters) => setFilters(prev => ({ ...prev, ...newFilters, page: 0 }))}
        />
      </div>

      {/* Error */}
      {error && (
        <div className="error-message" style={{ padding: '1rem', background: '#fee2e2', borderRadius: '8px', color: '#991b1b', margin: '1rem 0' }}>
          Error loading transactions: {error.message}
        </div>
      )}

      {/* Loading */}
      {loading && !data && (
        <div className="loading-message" style={{ padding: '2rem', textAlign: 'center', color: '#64748b' }}>
          Loading transactions...
        </div>
      )}

      {/* Data */}
      {data && (
        <>
          {/* No transactions */}
          {data.transactions?.length === 0 && !loading && (
            <div className="no-data-message" style={{ padding: '1rem', background: '#e0f2fe', borderRadius: '8px', color: '#0369a1', marginTop: '1rem' }}>
              No transactions found for the selected filters.
            </div>
          )}

          {/* Transactions exist */}
          {data.transactions?.length > 0 && (
            <>
              <div className="summary-section">
                <TransactionSummary
                  transactions={data.transactions}
                  totalTransactions={data.totalTransactions}
                />
              </div>

              <div className="transactions-section">
                <TransactionList
                  transactions={data.transactions}
                  loading={loading}
                />
              </div>

              {/* Pagination */}
              <div className="pagination-section" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '1rem', marginTop: '1rem' }}>
                <button
                  onClick={handlePrevPage}
                  disabled={filters.page === 0}
                  style={{ padding: '0.5rem 1rem', borderRadius: '4px', background: '#e5e7eb', cursor: filters.page === 0 ? 'not-allowed' : 'pointer' }}
                >
                  Previous
                </button>

                <span>
                  Page {filters.page + 1} of {totalPages}
                </span>

                <button
                  onClick={handleNextPage}
                  disabled={filters.page + 1 >= totalPages}
                  style={{ padding: '0.5rem 1rem', borderRadius: '4px', background: '#e5e7eb', cursor: filters.page + 1 >= totalPages ? 'not-allowed' : 'pointer' }}
                >
                  Next
                </button>
              </div>
            </>
          )}
        </>
      )}
    </main>
  );
};
