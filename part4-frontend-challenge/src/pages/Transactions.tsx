import { useState } from 'react';
import { FilterState, DEFAULT_FILTERS } from '../types/transaction';
import { useTransactions } from '../hooks/useTransactions';
import { createTransaction } from '../services/transactionService';
import { TransactionFilters } from '../components/common/TransactionFilters';
import { TransactionSummary } from '../components/common/TransactionSummary';
import { TransactionList } from '../components/common/TransactionList';
import { AddTransactionModal } from '../components/modal/AddTransactionModal';

export const Transactions = () => {
  const defaultMerchantId = import.meta.env.VITE_DEFAULT_MERCHANT_ID || 'MCH-00009';
  const [merchantIdInput, setMerchantIdInput] = useState(defaultMerchantId);
  const [merchantId, setMerchantId] = useState(defaultMerchantId);
  const [filters, setFilters] = useState<FilterState>(DEFAULT_FILTERS);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const { data, loading, error, refetch } = useTransactions(merchantId, filters);

  const handleMerchantSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setMerchantId(merchantIdInput);
    setFilters(DEFAULT_FILTERS);
  };

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

  const totalPages = data ? Math.ceil(Math.min(data.totalTransactions, 60) / filters.size) : 0;

  return (
    <main className="container mx-auto p-6">
      <div className="flex flex-col md:flex-row md:items-center md:justify-between mb-6">
        <h1 className="text-3xl font-bold text-gray-800 mb-4 md:mb-0">Transaction Dashboard</h1>
      </div>

      {/* Merchant ID input */}
      <form
        onSubmit={handleMerchantSubmit}
        className="flex flex-col sm:flex-row items-start sm:items-center gap-3 mb-6"
      >
        <label htmlFor="merchantId" className="font-medium text-gray-700">
          Merchant ID:
        </label>
        <input
          type="text"
          id="merchantId"
          value={merchantIdInput}
          onChange={(e) => setMerchantIdInput(e.target.value)}
          className="p-2 rounded-lg border border-gray-300 w-full sm:w-64 focus:ring-2 focus:ring-blue-400 focus:outline-none"
        />
        {/* <button
          type="submit"
          className="px-4 py-2 rounded-lg bg-blue-600 text-white font-medium hover:bg-blue-700 transition"
        >
          Load
        </button> */}
      </form>

      <p className="text-gray-600 mb-6">
        Currently Viewing: <span className="font-semibold">{merchantId}</span>
      </p>

      {/* Filters */}
      <div className="mb-6">
        <TransactionFilters
          filters={filters}
          onFilterChange={(newFilters) =>
            setFilters((prev) => ({ ...prev, ...newFilters, page: 0 }))
          }
        />
      </div>

      {/* Error */}
      {error && (
        <div className="p-4 mb-6 rounded-lg bg-red-50 border border-red-200 text-red-700 shadow-sm">
          <strong>Error:</strong> {error.message}
        </div>
      )}

      {/* Loading */}
      {loading && !data && (
        <div className="p-6 text-center text-gray-500 text-lg">Loading transactions...</div>
      )}

      {/* Data */}
      {data && (
        <>
          {/* No transactions */}
          {data.transactions?.length === 0 && !loading && (
            <div className="p-4 bg-yellow-50 text-yellow-700 rounded-lg mb-6 shadow-sm">
              No transactions found for the selected filters.
            </div>
          )}

          {data.transactions?.length > 0 && (
            <>
              <div className="summary-section mb-6">
                <TransactionSummary
                  transactions={data.transactions}
                  totalTransactions={data.totalTransactions}
                />
              </div>

              <div className="transactions-section mb-6 bg-white shadow rounded-lg overflow-hidden">
                <TransactionList transactions={data.transactions} loading={loading} />
              </div>

              {/* Pagination */}
              <div className="pagination-section flex justify-center items-center gap-4">
                <button
                  onClick={handlePrevPage}
                  disabled={filters.page === 0}
                  className={`px-4 py-2 rounded-lg font-medium transition ${filters.page === 0
                    ? 'bg-gray-200 cursor-not-allowed'
                    : 'bg-gray-100 hover:bg-gray-200'
                    }`}
                >
                  Previous
                </button>

                <span className="font-medium text-gray-700">
                  Page {filters.page + 1} of {totalPages}
                </span>

                <button
                  onClick={handleNextPage}
                  disabled={filters.page + 1 >= totalPages}
                  className={`px-4 py-2 rounded-lg font-medium transition ${filters.page + 1 >= totalPages
                    ? 'bg-gray-200 cursor-not-allowed'
                    : 'bg-gray-100 hover:bg-gray-200'
                    }`}
                >
                  Next
                </button>
              </div>
            </>
          )}
        </>
      )}

      {/* Add Transaction Modal */}
      <AddTransactionModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        merchantId={merchantId}
        onSubmit={async (payload) => {
          console.log('Create transaction :: ', payload);
          await createTransaction(payload);
          await refetch();
          setIsModalOpen(false);
        }}
      />
    </main>
  );
};
