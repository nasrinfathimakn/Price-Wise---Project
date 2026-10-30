import React, { useState } from 'react';
import { Bell, Check, X } from 'lucide-react';
import { api } from '../api/client';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

export default function PriceAlertForm({ productId, productName, currentPrice, onClose, onSuccess }) {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [targetPrice, setTargetPrice] = useState(
    currentPrice ? Math.floor(currentPrice * 0.95) : ''
  );
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!user) {
      navigate('/login');
      return;
    }

    if (!targetPrice || Number(targetPrice) <= 0) {
      setError('Please specify a valid target price');
      return;
    }

    setLoading(true);
    setError(null);
    try {
      await api.createPriceAlert({
        productId,
        targetPrice: Number(targetPrice),
        currency: 'INR',
      });
      if (onSuccess) onSuccess();
      if (onClose) onClose();
    } catch (err) {
      setError(err.message || 'Failed to create price alert');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 bg-slate-900/40 backdrop-blur-sm flex items-center justify-center p-4">
      <div className="bg-white rounded-2xl border border-slate-200 shadow-xl max-w-md w-full p-6 animate-in fade-in zoom-in-95 duration-150">
        <div className="flex items-center justify-between pb-3 border-b border-slate-100 mb-4">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-lg bg-sky-50 text-sky-600 flex items-center justify-center">
              <Bell className="w-4 h-4" />
            </div>
            <h3 className="text-sm font-bold text-slate-900">Set Price Drop Alert</h3>
          </div>
          {onClose && (
            <button onClick={onClose} className="text-slate-400 hover:text-slate-600 p-1">
              <X className="w-4 h-4" />
            </button>
          )}
        </div>

        <p className="text-xs text-slate-500 mb-4">
          Receive notification when verified prices for <span className="font-semibold text-slate-700">{productName}</span> drop to or below your target price.
        </p>

        {error && (
          <div className="text-xs text-rose-600 bg-rose-50 border border-rose-200 p-2.5 rounded-xl mb-3">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-xs font-semibold text-slate-700 mb-1">
              Target Price (₹)
            </label>
            <div className="relative">
              <span className="absolute left-3 top-2.5 text-xs text-slate-400">₹</span>
              <input
                type="number"
                step="any"
                value={targetPrice}
                onChange={(e) => setTargetPrice(e.target.value)}
                placeholder="e.g. 70000"
                className="w-full pl-7 pr-3 py-2 bg-white border border-slate-300 rounded-xl text-xs text-slate-900 focus:outline-none focus:ring-2 focus:ring-sky-500"
                required
              />
            </div>
            {currentPrice && (
              <span className="text-[11px] text-slate-400 mt-1 block">
                Current lowest: ₹{Number(currentPrice).toLocaleString('en-IN')}
              </span>
            )}
          </div>

          <div className="flex items-center justify-end gap-2 pt-2">
            {onClose && (
              <button
                type="button"
                onClick={onClose}
                className="px-4 py-2 text-xs font-medium text-slate-600 hover:bg-slate-50 rounded-xl transition"
              >
                Cancel
              </button>
            )}
            <button
              type="submit"
              disabled={loading}
              className="px-4 py-2 text-xs font-semibold text-white bg-sky-600 hover:bg-sky-700 rounded-xl shadow-sm transition disabled:opacity-50"
            >
              {loading ? 'Setting...' : 'Create Alert'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

