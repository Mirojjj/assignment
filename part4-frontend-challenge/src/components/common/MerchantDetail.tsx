import React, { useEffect, useState } from "react";
import { getMerchantById } from "@/services/merchantService";
import { getTransactions } from "@/services/transactionService";
import { MerchantDetailResponse } from "@/types/merchant";
import { TransactionResponse } from "@/types/transaction";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  CartesianGrid,
} from "recharts";
import { FaCalendarAlt } from "react-icons/fa";
import "./MerchantDetail.css";

const MerchantDetail = () => {
  const [merchantId, setMerchantId] = useState<string>("3");
  const [transactionMerchantId, setTransactionMerchantId] =
    useState<string>("MCH-00004");

  const [merchant, setMerchant] = useState<MerchantDetailResponse | null>(null);
  const [transactions, setTransactions] = useState<TransactionResponse | null>(
    null
  );

  const [loading, setLoading] = useState(false);
  const [loadingTransactions, setLoadingTransactions] = useState(false);

  const [error, setError] = useState("");
  const [transactionError, setTransactionError] = useState("");

  // Transaction filters
  const [page, setPage] = useState<number>(0);
  const [size, setSize] = useState<number>(20);
  const [startDate, setStartDate] = useState<string>("2025-11-16");
  const [endDate, setEndDate] = useState<string>("2025-11-18");

  // Fetch merchant details
  const fetchMerchant = async (id: string) => {
    if (!id) return;
    setLoading(true);
    setError("");
    try {
      const res = await getMerchantById(id);
      if (res.response_code === "200" && res.data) {
        setMerchant(res.data);
      } else {
        setMerchant(null);
        setError(res.response_message || "Merchant not found");
      }
    } catch (err) {
      setMerchant(null);
      setError("Failed to fetch merchant details");
    } finally {
      setLoading(false);
    }
  };

  // Fetch transactions with query params
  const fetchTransactions = async (id: string) => {
    if (!id) return;
    setLoadingTransactions(true);
    setTransactionError("");
    try {
      const res = await getTransactions(id, {
        page,
        size,
        startDate,
        endDate,
      });
      if (res.response_code === "200" && res.data) {
        setTransactions(res.data);
      } else {
        setTransactions(null);
        setTransactionError(res.response_message || "Transactions not found");
      }
    } catch (err) {
      setTransactions(null);
      setTransactionError("Failed to fetch transactions");
    } finally {
      setLoadingTransactions(false);
    }
  };

  useEffect(() => {
    fetchMerchant(merchantId);
  }, [merchantId]);

  useEffect(() => {
    fetchTransactions(transactionMerchantId);
  }, [transactionMerchantId, page, size, startDate, endDate]);

  // Prepare data for chart
  const chartData =
    transactions?.summary?.byStatus
      ? Object.entries(transactions.summary.byStatus).map(([status, value]) => ({
        status,
        count: value,
      }))
      : [];

  return (
    <div className="merchant-detail-container">
      {/* Merchant Section */}
      <section className="detail-section merchant-section">
        <div className="section-header">
          <h2>Merchant Profile</h2>
          <div className="minimal-input-group">
            <label htmlFor="merchantId">Merchant ID</label>
            <input
              id="merchantId"
              type="text"
              value={merchantId}
              onChange={(e) => setMerchantId(e.target.value)}
              placeholder="Enter ID"
            />
          </div>
        </div>

        {loading && <div className="loading-state">Loading merchant...</div>}
        {error && <div className="error-state">{error}</div>}

        {merchant && (
          <div className="merchant-content">
            <div className="merchant-identity">
              <img
                src={merchant.merchantLogo}
                alt={merchant.merchantName}
                className="merchant-logo"
              />
              <div className="identity-text">
                <h3>{merchant.merchantName}</h3>
                <span className={`status-badge ${merchant.merchantStatus.toLowerCase()}`}>
                  {merchant.merchantStatus}
                </span>
                <span className="merchant-type">{merchant.merchantType}</span>
                <a
                  href={merchant.merchantWebsite}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="website-link"
                >
                  Visit Website
                </a>
              </div>
            </div>

            <div className="merchant-info-grid">
              <div className="info-item">
                <span className="label">Contact</span>
                <span className="value">{merchant.contactInfo || "-"}</span>
              </div>
              <div className="info-item">
                <span className="label">Category</span>
                <span className="value">{merchant.merchantCategory || "-"}</span>
              </div>
              <div className="info-item">
                <span className="label">Location</span>
                <span className="value">{merchant.merchantLocation || "-"}</span>
              </div>
              <div className="info-item">
                <span className="label">Orders</span>
                <span className="value">{merchant.numOrders || "-"}</span>
              </div>
              <div className="info-item">
                <span className="label">Rating</span>
                <span className="value">{merchant.merchantRating || "-"}</span>
              </div>
              <div className="info-item">
                <span className="label">Payment</span>
                <span className="value">{merchant.paymentMethod || "-"}</span>
              </div>
            </div>

            {merchant.merchantTags && merchant.merchantTags.length > 0 && (
              <div className="tags-container">
                {merchant.merchantTags.map((tag, i) => (
                  <span key={i} className="tag-pill">
                    {tag}
                  </span>
                ))}
              </div>
            )}
          </div>
        )}
      </section>

      {/* Transactions Section */}
      <section className="detail-section transactions-section">
        <div className="section-header">
          <h2>Transaction Insights</h2>
          <div className="controls-bar">
            <div className="minimal-input-group">
              <label htmlFor="transMerchantId">ID</label>
              <input
                id="transMerchantId"
                type="text"
                value={transactionMerchantId}
                onChange={(e) => setTransactionMerchantId(e.target.value)}
                placeholder="Merchant ID"
              />
            </div>
            <div className="date-range-group">
              <div className="calendar-input-container">
                <label htmlFor="startDate">From</label>
                <div className="input-wrapper">
                  <FaCalendarAlt className="calendar-icon" />
                  <input
                    id="startDate"
                    type="date"
                    value={startDate}
                    onChange={(e) => setStartDate(e.target.value)}
                  />
                </div>
              </div>
              <div className="calendar-input-container">
                <label htmlFor="endDate">To</label>
                <div className="input-wrapper">
                  <FaCalendarAlt className="calendar-icon" />
                  <input
                    id="endDate"
                    type="date"
                    value={endDate}
                    onChange={(e) => setEndDate(e.target.value)}
                  />
                </div>
              </div>
            </div>
          </div>
        </div>

        {loadingTransactions && <div className="loading-state">Loading transactions...</div>}
        {transactionError && <div className="error-state">{transactionError}</div>}

        {transactions && (
          <div className="transactions-content">
            <div className="summary-cards">
              <div className="summary-card">
                <span className="summary-label">Total Transactions</span>
                <span className="summary-value">{transactions.summary.totalTransactions}</span>
              </div>
              <div className="summary-card">
                <span className="summary-label">Total Volume</span>
                <span className="summary-value">
                  {transactions.summary.totalAmount.toLocaleString()} <span className="currency">{transactions.summary.currency}</span>
                </span>
              </div>
            </div>

            <div className="chart-container">
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={chartData} margin={{ top: 20, right: 30, left: 0, bottom: 5 }}>
                  <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f0f0f0" />
                  <XAxis
                    dataKey="status"
                    axisLine={false}
                    tickLine={false}
                    tick={{ fill: '#888', fontSize: 12 }}
                    dy={10}
                  />
                  <YAxis
                    axisLine={false}
                    tickLine={false}
                    tick={{ fill: '#888', fontSize: 12 }}
                  />
                  <Tooltip
                    cursor={{ fill: '#f9fafb' }}
                    contentStyle={{ borderRadius: '8px', border: 'none', boxShadow: '0 4px 12px rgba(0,0,0,0.1)' }}
                  />
                  <Bar dataKey="count" fill="#4f46e5" radius={[4, 4, 0, 0]} barSize={40} />
                </BarChart>
              </ResponsiveContainer>
            </div>

            <div className="recent-transactions">
              <h3>Recent Transactions</h3>
              <div className="transaction-list-header">
                <span>ID</span>
                <span>Date</span>
                <span>Amount</span>
                <span>Status</span>
              </div>
              <div className="transaction-list">
                {transactions.transactions.map((txn) => (
                  <div key={txn.txnId} className="transaction-item">
                    <span className="txn-id">#{txn.txnId}</span>
                    <span className="txn-date">{new Date(txn.timestamp).toLocaleDateString()}</span>
                    <span className="txn-amount">
                      {txn.amount.toFixed(2)} {txn.currency}
                    </span>
                    <span className={`txn-status ${txn.status}`}>
                      {txn.status}
                    </span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}
      </section>
    </div>
  );
};

export default MerchantDetail;
