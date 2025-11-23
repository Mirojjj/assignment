import { useState } from 'react';
import { FaPlus, FaArrowUp, FaArrowDown, FaClock } from 'react-icons/fa';
import { AddTransactionModal } from './AddTransactionModal';
import { Toast, ToastType } from './Toast';
import { createTransaction } from '@/services/transactionService';
import './TransactionSummary.css';

interface Transaction {
  amount: number;
  currency: string;
  status: string;
}

interface TransactionSummaryProps {
  transactions: Transaction[];
  totalTransactions: number;
}

export const TransactionSummary = ({ transactions, totalTransactions }: TransactionSummaryProps) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [notification, setNotification] = useState<{ message: string; type: ToastType } | null>(null);

  const totalAmount = transactions.reduce((sum, txn) => sum + txn.amount, 0);
  const currency = transactions[0]?.currency || 'USD';

  const statusCounts = transactions.reduce((acc, txn) => {
    const status = txn.status.toLowerCase();
    acc[status] = (acc[status] || 0) + 1;
    return acc;
  }, {} as Record<string, number>);

  const formatAmount = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: currency,
    }).format(amount);
  };

  const handleAddTransaction = async (data: any) => {
    try {
      const response = await createTransaction(data);
      if (response.response_code === "200") {
        setNotification({
          message: response.data.message || "Successfully inserted New Transaction",
          type: 'success'
        });
        setIsModalOpen(false);
        // Ideally, we would trigger a refresh of the transaction list here
      } else {
        setNotification({
          message: "Failed to add transaction: " + (response.response_message || "Unknown error"),
          type: 'error'
        });
      }
    } catch (error) {
      console.error("Error adding transaction:", error);
      setNotification({
        message: "An error occurred while adding the transaction.",
        type: 'error'
      });
    }
  };

  return (
    <div className="transaction-summary-container">
      {notification && (
        <Toast
          message={notification.message}
          type={notification.type}
          onClose={() => setNotification(null)}
        />
      )}

      <div className="summary-header">
        <h2>Overview</h2>
        <button className="add-transaction-btn" onClick={() => setIsModalOpen(true)}>
          <FaPlus /> Add Transaction
        </button>
      </div>

      <div className="summary-grid">
        <div className="summary-card total-card">
          <div className="card-icon total-icon">
            <span className="currency-symbol">$</span>
          </div>
          <div className="card-content">
            <div className="summary-label">Total Volume</div>
            <div className="summary-value">{formatAmount(totalAmount)}</div>
            <div className="summary-subtext">{totalTransactions} transactions</div>
          </div>
        </div>

        <div className="summary-card status-card completed">
          <div className="card-icon completed-icon">
            <FaArrowUp />
          </div>
          <div className="card-content">
            <div className="summary-label">Completed</div>
            <div className="summary-value">{statusCounts['completed'] || 0}</div>
          </div>
        </div>

        <div className="summary-card status-card pending">
          <div className="card-icon pending-icon">
            <FaClock />
          </div>
          <div className="card-content">
            <div className="summary-label">Pending</div>
            <div className="summary-value">{statusCounts['pending'] || 0}</div>
          </div>
        </div>

        <div className="summary-card status-card failed">
          <div className="card-icon failed-icon">
            <FaArrowDown />
          </div>
          <div className="card-content">
            <div className="summary-label">Failed</div>
            <div className="summary-value">{statusCounts['failed'] || 0}</div>
          </div>
        </div>
      </div>

      <AddTransactionModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSubmit={handleAddTransaction}
      />
    </div>
  );
};
