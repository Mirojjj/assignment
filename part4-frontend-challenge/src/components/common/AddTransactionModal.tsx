import React, { useState } from 'react';
import { FaTimes } from 'react-icons/fa';
import './AddTransactionModal.css';

interface AddTransactionModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSubmit: (data: any) => void;
}

export const AddTransactionModal = ({ isOpen, onClose, onSubmit }: AddTransactionModalProps) => {
    const [formData, setFormData] = useState({
        merchantId: '',
        gpAcquirerId: 1,
        gpIssuerId: 2,
        amount: '',
        currency: 'USD',
        cardType: 'VISA',
        cardLast4: '',
        authCode: '',
        responseCode: '00'
    });

    if (!isOpen) return null;

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        // Convert amount to number before submitting
        onSubmit({
            ...formData,
            amount: Number(formData.amount)
        });
        onClose();
    };

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-container" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h2>Add Transaction</h2>
                    <button className="close-button" onClick={onClose}>
                        <FaTimes size={20} />
                    </button>
                </div>

                <form onSubmit={handleSubmit}>
                    <div className="modal-body">
                        <div className="form-group">
                            <label htmlFor="merchantId">Merchant ID</label>
                            <input
                                type="text"
                                id="merchantId"
                                value={formData.merchantId}
                                onChange={e => setFormData({ ...formData, merchantId: e.target.value })}
                                placeholder="Enter Merchant ID"
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="amount">Amount</label>
                            <input
                                type="number"
                                id="amount"
                                value={formData.amount}
                                onChange={e => setFormData({ ...formData, amount: e.target.value })}
                                placeholder="0.00"
                                required
                                min="0"
                                step="0.01"
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="currency">Currency</label>
                            <select
                                id="currency"
                                value={formData.currency}
                                onChange={e => setFormData({ ...formData, currency: e.target.value })}
                            >
                                <option value="USD">USD</option>
                                <option value="EUR">EUR</option>
                                <option value="GBP">GBP</option>
                            </select>
                        </div>

                        <div className="form-group">
                            <label htmlFor="cardType">Card Type</label>
                            <select
                                id="cardType"
                                value={formData.cardType}
                                onChange={e => setFormData({ ...formData, cardType: e.target.value })}
                            >
                                <option value="VISA">VISA</option>
                                <option value="MASTERCARD">MASTERCARD</option>
                                <option value="AMEX">AMEX</option>
                            </select>
                        </div>

                        <div className="form-group">
                            <label htmlFor="cardLast4">Card Last 4</label>
                            <input
                                type="text"
                                id="cardLast4"
                                value={formData.cardLast4}
                                onChange={e => setFormData({ ...formData, cardLast4: e.target.value })}
                                placeholder="1234"
                                maxLength={4}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="authCode">Auth Code</label>
                            <input
                                type="text"
                                id="authCode"
                                value={formData.authCode}
                                onChange={e => setFormData({ ...formData, authCode: e.target.value })}
                                placeholder="A1B2C3"
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="responseCode">Response Code</label>
                            <input
                                type="text"
                                id="responseCode"
                                value={formData.responseCode}
                                onChange={e => setFormData({ ...formData, responseCode: e.target.value })}
                                placeholder="00"
                                required
                            />
                        </div>

                        {/* Hidden fields for IDs if they are fixed or need to be editable later */}
                        <input type="hidden" value={formData.gpAcquirerId} />
                        <input type="hidden" value={formData.gpIssuerId} />
                    </div>

                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" onClick={onClose}>
                            Cancel
                        </button>
                        <button type="submit" className="btn btn-primary">
                            Add Transaction
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};
