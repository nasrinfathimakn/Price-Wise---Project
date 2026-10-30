import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { api } from '../api/client';
import { useAuth } from '../context/AuthContext';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';
import EmptyState from '../components/EmptyState';
import { Bell, CheckCircle2, Trash2, Power, ArrowRight } from 'lucide-react';

export default function PriceAlertsPage() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!user) {
      navigate('/login');
      return;
    }

    async function loadAlerts() {
      setLoading(true);
      setError(null);
      try {
        const data = await api.getPriceAlerts();
        setAlerts(data || []);
      } catch (err) {
        setError(err.message || 'Failed to load price alerts');
      } finally {
        setLoading(false);
      }
    }
    loadAlerts();
  }, [user, navigate]);

  const handleToggle = async (alertId, currentActive) => {
    try {
      const updated = await api.togglePriceAlert(alertId, !currentActive);
      setAlerts((prev) => prev.map((a) => (a.id === alertId ? { ...a, ...updated } : a)));
    } catch (err) {
      console.error('Failed to toggle alert:', err);
      setError(err.message || 'Failed to toggle price alert');
    }
  };

  const handleDelete = async (alertId) => {
    try {
      await api.deletePriceAlert(alertId);
      setAlerts((prev) => prev.filter((a) => a.id !== alertId));
    } catch (err) {
      console.error('Failed to delete alert:', err);
      setError(err.message || 'Failed to delete price alert');
    }
  };

  const formatINR = (val) =>
    val != null
      ? new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(val)
      : '₹0';

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      <div>
        <h1 className="text-2xl font-bold text-slate-900 flex items-center gap-2">
          <Bell className="w-6 h-6 text-sky-600" />
          Target Price Alerts
        </h1>
        <p className="text-xs text-slate-500 mt-1">
          Automated notifications evaluated against newly verified retailer prices
        </p>
      </div>

      <ErrorMessage message={error} />

      {loading ? (
        <LoadingSpinner message="Checking active alert evaluations..." />
      ) : alerts.length === 0 ? (
        <EmptyState
          title="No price alerts configured"
          description="Open any product and set a target price alert to get notified when prices drop."
          actionText="Find Products to Track"
          onAction={() => navigate('/search')}
        />
      ) : (
        <div className="space-y-4">
          {alerts.map((alert) => {
            const isReached = Boolean(alert.isTargetReached ?? alert.targetReached);
            return (
              <div
                key={alert.id}
                className={`p-5 rounded-2xl border transition flex flex-col sm:flex-row sm:items-center justify-between gap-4 ${
                  isReached
                    ? 'bg-emerald-50/50 border-emerald-200'
                    : alert.active
                    ? 'bg-white border-slate-200'
                    : 'bg-slate-50 border-slate-200 opacity-60'
                }`}
              >
                <div className="flex items-center gap-4 flex-1 min-w-0">
                  <img
                    src={alert.productImageUrl || 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=160&q=80'}
                    alt={alert.productName}
                    className="w-14 h-14 rounded-xl object-contain bg-white p-2 shrink-0 border border-slate-100"
                  />

                  <div className="min-w-0 flex-1">
                    <div className="flex items-center gap-2 mb-1 flex-wrap">
                      <Link to={`/products/${alert.productId}`}>
                        <h3 className="text-sm font-bold text-slate-900 hover:text-sky-600 transition truncate max-w-sm">
                          {alert.productName}
                        </h3>
                      </Link>
                      {isReached && (
                        <span className="inline-flex items-center gap-1 text-[11px] font-bold text-emerald-700 bg-emerald-100 px-2 py-0.5 rounded-full">
                          <CheckCircle2 className="w-3 h-3" />
                          Target Reached!
                        </span>
                      )}
                    </div>

                    <div className="flex items-center gap-4 text-xs">
                      <div>
                        <span className="text-slate-400">Target Price: </span>
                        <span className="font-bold text-slate-800">{formatINR(alert.targetPrice)}</span>
                      </div>
                      <div>
                        <span className="text-slate-400">Current Lowest: </span>
                        <span className={`font-bold ${isReached ? 'text-emerald-600' : 'text-slate-800'}`}>
                          {alert.currentPrice != null ? formatINR(alert.currentPrice) : 'No offers yet'}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>

              {/* Controls */}
              <div className="flex items-center gap-3 self-end sm:self-center">
                <Link
                  to={`/compare/${alert.productId}`}
                  className="px-3 py-1.5 bg-sky-50 hover:bg-sky-100 text-sky-700 text-xs font-semibold rounded-xl transition flex items-center gap-1"
                >
                  View Deals
                  <ArrowRight className="w-3.5 h-3.5" />
                </Link>

                <button
                  onClick={() => handleToggle(alert.id, alert.active)}
                  className={`p-2 rounded-xl transition ${
                    alert.active
                      ? 'text-emerald-600 hover:bg-emerald-50'
                      : 'text-slate-400 hover:bg-slate-200'
                  }`}
                  title={alert.active ? 'Pause Alert' : 'Resume Alert'}
                >
                  <Power className="w-4 h-4" />
                </button>

                <button
                  onClick={() => handleDelete(alert.id)}
                  className="p-2 text-slate-400 hover:text-rose-600 hover:bg-rose-50 rounded-xl transition"
                  title="Delete Alert"
                >
                  <Trash2 className="w-4 h-4" />
                </button>
              </div>
            </div>
          );
        })}
        </div>
      )}
    </div>
  );
}

