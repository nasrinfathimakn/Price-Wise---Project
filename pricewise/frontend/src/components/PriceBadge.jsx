import React from 'react';
import { CheckCircle2, Clock, AlertTriangle, Database } from 'lucide-react';

export default function PriceBadge({ type = 'fresh', label }) {
  switch (type) {
    case 'fresh':
      return (
        <span className="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-medium bg-emerald-50 text-emerald-700 border border-emerald-200">
          <CheckCircle2 className="w-3 h-3" />
          {label || 'Verified Fresh'}
        </span>
      );
    case 'stale':
      return (
        <span className="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-medium bg-amber-50 text-amber-700 border border-amber-200">
          <Clock className="w-3 h-3" />
          {label || 'Stale Price'}
        </span>
      );
    case 'unavailable':
      return (
        <span className="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-medium bg-slate-100 text-slate-600 border border-slate-200">
          <AlertTriangle className="w-3 h-3" />
          {label || 'Out of Stock'}
        </span>
      );
    case 'mock':
    default:
      return (
        <span className="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-medium bg-sky-50 text-sky-700 border border-sky-200">
          <Database className="w-3 h-3" />
          {label || 'Demo Price'}
        </span>
      );
  }
}

