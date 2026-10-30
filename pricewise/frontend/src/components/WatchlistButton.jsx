import React, { useState, useEffect } from 'react';
import { Bookmark } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { api } from '../api/client';
import { useNavigate } from 'react-router-dom';

export default function WatchlistButton({ productId, initialSaved = false, onToggle, className = '' }) {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [saved, setSaved] = useState(initialSaved);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setSaved(initialSaved);
  }, [initialSaved]);

  const handleClick = async (e) => {
    e.preventDefault();
    e.stopPropagation();

    if (!user) {
      navigate('/login');
      return;
    }

    setLoading(true);
    try {
      if (saved) {
        await api.removeFromWatchlist(productId);
        setSaved(false);
        if (onToggle) onToggle(false);
      } else {
        await api.addToWatchlist(productId);
        setSaved(true);
        if (onToggle) onToggle(true);
      }
    } catch (err) {
      console.error('Failed to toggle watchlist:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <button
      onClick={handleClick}
      disabled={loading}
      aria-label="Add to watchlist"
      className={`p-2 rounded-xl transition backdrop-blur flex items-center justify-center ${
        saved
          ? 'bg-amber-50 text-amber-600 border border-amber-200'
          : 'bg-white/80 hover:bg-white text-slate-400 hover:text-slate-600 border border-slate-200'
      } ${className}`}
      title={saved ? 'Remove from Watchlist' : 'Add to Watchlist'}
    >
      <Bookmark className={`w-4 h-4 ${saved ? 'fill-current' : ''}`} />
    </button>
  );
}

