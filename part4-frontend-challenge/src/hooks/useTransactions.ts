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

      // Check server response code
      if (response.response_code !== '200') {
        setError(new Error(response.response_message || 'Unknown server error'));
        setData(null);
        return;
      }

      const payload = response?.data;

      if (!payload) {
        setError(new Error('No data returned from API'));
        setData(null);
        return;
      }

      // Safe mapping of transactions
      const transactions: Transaction[] = Array.isArray(payload.transactions)
        ? payload.transactions.map((t: any) => ({
          txnId: t.txnId ?? 0,
          merchantId: t.merchantId ?? '',
          amount: t.amount ?? 0,
          currency: t.currency ?? '',
          status: t.status ?? '',
          cardType: t.cardType ?? '',
          cardLast4: t.cardLast4 ?? '',
          authCode: t.authCode ?? '',
          txnDate: t.timestamp ?? '',
          createdAt: t.createdAt ?? '',
        }))
        : [];

      const totalTransactions = payload.summary?.totalTransactions ?? 0;
      const page = payload.pagination?.page ?? 0;
      const size = payload.pagination?.size ?? 0;

      setData({
        transactions,
        totalTransactions,
        page,
        size,
      });
    } catch (err) {
      setError(err as Error);
      setData(null);
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
