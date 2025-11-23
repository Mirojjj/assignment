import { useState, useEffect } from "react";

interface AddTransactionModalProps {
    isOpen: boolean;
    onClose: () => void;
    merchantId: string;
    onSubmit: (payload: any) => Promise<void>;
}

export const AddTransactionModal = ({
    isOpen,
    onClose,
    merchantId,
    onSubmit,
}: AddTransactionModalProps) => {
    const [isAnimating, setIsAnimating] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const [form, setForm] = useState({
        merchantId: merchantId,
        gpAcquirerId: "",
        gpIssuerId: "",
        amount: "",
        currency: "",
        cardType: "",
        cardLast4: "",
        authCode: "",
        responseCode: "",
        status: "completed",
    });

    useEffect(() => {
        if (isOpen) {
            setIsAnimating(true);
            setForm(prev => ({ ...prev, merchantId }));
        } else {
            const timer = setTimeout(() => setIsAnimating(false), 300);
            return () => clearTimeout(timer);
        }
    }, [isOpen, merchantId]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setForm({ ...form, [e.target.name]: e.target.value });
        if (error) setError(null);
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setError(null);

        try {
            await onSubmit({
                ...form,
                gpAcquirerId: Number(form.gpAcquirerId),
                gpIssuerId: Number(form.gpIssuerId),
                amount: Number(form.amount),
            });
            // Form reset is handled by parent or on next open
        } catch (err) {
            setError("Failed to create transaction. Please try again.");
        } finally {
            setIsLoading(false);
        }
    };

    if (!isOpen && !isAnimating) return null;

    return (
        <div className={`fixed inset-0 z-50 flex items-center justify-center transition-opacity duration-300 ${isOpen ? 'opacity-100' : 'opacity-0'}`}>
            {/* Backdrop */}
            <div
                className="absolute inset-0 bg-black/60 backdrop-blur-sm"
                onClick={onClose}
            />

            {/* Modal Content */}
            <div className={`relative bg-white w-full max-w-2xl rounded-2xl shadow-2xl transform transition-all duration-300 ${isOpen ? 'scale-100 translate-y-0' : 'scale-95 translate-y-4'} flex flex-col max-h-[90vh]`}>

                {/* Header */}
                <div className="px-8 py-6 border-b border-gray-100 flex justify-between items-center bg-gray-50/50 rounded-t-2xl">
                    <div>
                        <h2 className="text-2xl font-bold text-gray-800">New Transaction</h2>
                        <p className="text-sm text-gray-500 mt-1">Enter transaction details manually</p>
                    </div>
                    <button
                        onClick={onClose}
                        className="text-gray-400 hover:text-gray-600 transition-colors p-2 hover:bg-gray-100 rounded-full"
                    >
                        <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                        </svg>
                    </button>
                </div>

                {/* Body */}
                <div className="p-8 overflow-y-auto custom-scrollbar">
                    {error && (
                        <div className="mb-6 p-4 bg-red-50 border border-red-200 text-red-700 rounded-xl flex items-center gap-3">
                            <svg className="w-5 h-5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                            {error}
                        </div>
                    )}

                    <form id="add-transaction-form" onSubmit={handleSubmit} className="space-y-6">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            {/* Merchant ID */}
                            <div className="col-span-full">
                                <label className="block text-sm font-semibold text-gray-700 mb-2">Merchant ID</label>
                                <input
                                    type="text"
                                    name="merchantId"
                                    value={form.merchantId}
                                    disabled
                                    className="w-full px-4 py-3 rounded-xl border border-gray-200 bg-gray-50 text-gray-500 font-medium focus:outline-none"
                                />
                            </div>

                            {/* Amount & Currency */}
                            <div>
                                <label className="block text-sm font-semibold text-gray-700 mb-2">Amount</label>
                                <div className="relative">
                                    <span className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 font-medium">$</span>
                                    <input
                                        type="number"
                                        name="amount"
                                        value={form.amount}
                                        onChange={handleChange}
                                        required
                                        min="0"
                                        step="0.01"
                                        className="w-full pl-8 pr-4 py-3 rounded-xl border border-gray-200 focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 transition-all outline-none"
                                        placeholder="0.00"
                                    />
                                </div>
                            </div>

                            <div>
                                <label className="block text-sm font-semibold text-gray-700 mb-2">Currency</label>
                                <input
                                    type="text"
                                    name="currency"
                                    value={form.currency}
                                    onChange={handleChange}
                                    required
                                    className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 transition-all outline-none uppercase"
                                    placeholder="USD"
                                />
                            </div>

                            {/* Card Details */}
                            <div>
                                <label className="block text-sm font-semibold text-gray-700 mb-2">Card Type</label>
                                <input
                                    type="text"
                                    name="cardType"
                                    value={form.cardType}
                                    onChange={handleChange}
                                    required
                                    className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 transition-all outline-none"
                                    placeholder="Visa"
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-semibold text-gray-700 mb-2">Last 4 Digits</label>
                                <input
                                    type="text"
                                    name="cardLast4"
                                    value={form.cardLast4}
                                    onChange={handleChange}
                                    required
                                    maxLength={4}
                                    pattern="\d{4}"
                                    className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 transition-all outline-none"
                                    placeholder="1234"
                                />
                            </div>

                            {/* IDs */}
                            <div>
                                <label className="block text-sm font-semibold text-gray-700 mb-2">Acquirer ID</label>
                                <input
                                    type="number"
                                    name="gpAcquirerId"
                                    value={form.gpAcquirerId}
                                    onChange={handleChange}
                                    required
                                    className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 transition-all outline-none"
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-semibold text-gray-700 mb-2">Issuer ID</label>
                                <input
                                    type="number"
                                    name="gpIssuerId"
                                    value={form.gpIssuerId}
                                    onChange={handleChange}
                                    required
                                    className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 transition-all outline-none"
                                />
                            </div>

                            {/* Codes */}
                            <div>
                                <label className="block text-sm font-semibold text-gray-700 mb-2">Auth Code</label>
                                <input
                                    type="text"
                                    name="authCode"
                                    value={form.authCode}
                                    onChange={handleChange}
                                    required
                                    className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 transition-all outline-none"
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-semibold text-gray-700 mb-2">Response Code</label>
                                <input
                                    type="text"
                                    name="responseCode"
                                    value={form.responseCode}
                                    onChange={handleChange}
                                    required
                                    className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 transition-all outline-none"
                                />
                            </div>

                            {/* Status */}
                            <div className="col-span-full">
                                <label className="block text-sm font-semibold text-gray-700 mb-2">Status</label>
                                <select
                                    name="status"
                                    value={form.status}
                                    onChange={handleChange}
                                    className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 transition-all outline-none bg-white"
                                >
                                    <option value="completed">Completed</option>
                                    <option value="pending">Pending</option>
                                    <option value="failed">Failed</option>
                                    <option value="reversed">Reversed</option>
                                </select>
                            </div>
                        </div>
                    </form>
                </div>

                {/* Footer */}
                <div className="px-8 py-6 border-t border-gray-100 bg-gray-50/50 rounded-b-2xl flex justify-end gap-4">
                    <button
                        type="button"
                        onClick={onClose}
                        className="px-6 py-2.5 rounded-xl font-semibold text-gray-600 hover:bg-gray-200/50 hover:text-gray-800 transition-all"
                        disabled={isLoading}
                    >
                        Cancel
                    </button>
                    <button
                        type="submit"
                        form="add-transaction-form"
                        disabled={isLoading}
                        className="px-6 py-2.5 rounded-xl font-semibold text-white bg-blue-600 hover:bg-blue-700 active:bg-blue-800 transition-all shadow-lg shadow-blue-600/20 disabled:opacity-70 disabled:cursor-not-allowed flex items-center gap-2"
                    >
                        {isLoading ? (
                            <>
                                <svg className="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                </svg>
                                Processing...
                            </>
                        ) : (
                            'Create Transaction'
                        )}
                    </button>
                </div>
            </div>
        </div>
    );
};
