import { useState, useEffect } from 'react';
import { getTransactions } from '../services/transactionService';
import { FilterState } from '../types/transaction';

interface Transaction {
  txnId: number;
  merchantId: string;
  amount: number;
  currency: string;
  status: string;
  cardType: string;
  cardLast4: string;
  authCode: string;
  txnDate: string;
  createdAt: string;
}

interface UseTransactionsResult {
  data: {
    transactions: Transaction[];
    totalTransactions: number;
    page: number;
    size: number;
  } | null;
  loading: boolean;
  error: Error | null;
  refetch: () => void;
}

export const useTransactions = (
  merchantId: string,
  filters: FilterState
): UseTransactionsResult => {
  const [data, setData] = useState<UseTransactionsResult['data']>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchTransactions = async () => {
    try {
      setLoading(true);
      setError(null);

      const response = await getTransactions(merchantId, filters);

      // NEW STRUCTURE: extract response.data
      const payload = response.data;

      setData({
        transactions: payload.transactions.map((t: any) => ({
          txnId: t.txnId,
          merchantId: t.merchantId,
          amount: t.amount,
          currency: t.currency,
          status: t.status,
          cardType: t.cardType,
          cardLast4: t.cardLast4,
          authCode: t.authCode,
          txnDate: t.txnDate,
          createdAt: t.createdAt,
        })),

        totalTransactions: payload.totalTransactions,
        page: payload.page,
        size: payload.size,
      });
    } catch (err) {
      setError(err as Error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTransactions();
  }, [
    merchantId,
    filters.page,
    filters.size,
    filters.status,
    filters.startDate,
    filters.endDate,
  ]);

  return {
    data,
    loading,
    error,
    refetch: fetchTransactions,
  };
};
