import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { api } from '../api/client';
import { useAuth } from '../context/AuthContext';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';
import EmptyState from '../components/EmptyState';
import { Bookmark, Trash2, ArrowRight, ExternalLink } from 'lucide-react';

export default function UserWatchlistPage() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!user) {
      navigate('/login');
      return;
    }

    async function loadWatchlist() {
      setLoading(true);
      setError(null);
      try {
        const data = await api.getWatchlist();
        setItems(data || []);
      } catch (err) {
        setError(err.message || 'Failed to load watchlist');
      } finally {
        setLoading(false);
      }
    }
    loadWatchlist();
  }, [user, navigate]);

  const handleRemove = async (productId) => {
    try {
      await api.removeFromWatchlist(productId);
      setItems(items.filter((i) => i.productId !== productId));
    } catch (err) {
      console.error('Failed to remove item:', err);
    }
  };

  const formatINR = (val) =>
    val != null
      ? new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(val)
      : 'Check Offers';

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      <div>
        <h1 className="text-2xl font-bold text-slate-900 flex items-center gap-2">
          <Bookmark className="w-6 h-6 text-sky-600" />
          My Product Watchlist
        </h1>
        <p className="text-xs text-slate-500 mt-1">
          Monitor your saved products and track real-time price updates
        </p>
      </div>

      <ErrorMessage message={error} />

      {loading ? (
        <LoadingSpinner message="Loading your saved products..." />
      ) : items.length === 0 ? (
        <EmptyState
          title="Your watchlist is empty"
          description="Browse products and click the bookmark icon to save products for price tracking."
          actionText="Browse Trending Products"
          onAction={() => navigate('/search')}
        />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {items.map((item) => (
            <div
              key={item.id}
              className="bg-white rounded-2xl border border-slate-200 p-5 shadow-sm hover:shadow-md transition flex items-center gap-4 justify-between"
            >
              <div className="flex items-center gap-3.5 flex-1 min-w-0">
                <img
                  src={item.productImageUrl || 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=200&q=80'}
                  alt={item.productName}
                  className="w-16 h-16 rounded-xl object-contain bg-slate-50 p-2 shrink-0 border border-slate-100"
                />
                <div className="min-w-0 flex-1">
                  <span className="text-[10px] font-semibold text-slate-400 uppercase tracking-wide">
                    {item.brandName || 'Brand'}
                  </span>
                  <Link to={`/products/${item.productId}`}>
                    <h3 className="text-xs font-bold text-slate-900 truncate hover:text-sky-600 transition">
                      {item.productName}
                    </h3>
                  </Link>
                  <div className="mt-1 flex items-baseline gap-2">
                    <span className="text-sm font-bold text-emerald-600">
                      {formatINR(item.lowestPrice)}
                    </span>
                    <span className="text-[10px] text-slate-400">
                      ({item.offerCount || 0} stores)
                    </span>
                  </div>
                </div>
              </div>

              <div className="flex items-center gap-2 shrink-0">
                <Link
                  to={`/compare/${item.productId}`}
                  className="p-2 text-sky-600 hover:bg-sky-50 rounded-xl transition"
                  title="Compare Prices"
                >
                  <ArrowRight className="w-4 h-4" />
                </Link>
                <button
                  onClick={() => handleRemove(item.productId)}
                  className="p-2 text-slate-400 hover:text-rose-600 hover:bg-rose-50 rounded-xl transition"
                  title="Remove from Watchlist"
                >
                  <Trash2 className="w-4 h-4" />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

