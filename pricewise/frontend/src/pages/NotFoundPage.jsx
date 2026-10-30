import React from 'react';
import { Link } from 'react-router-dom';
import { SearchX, ArrowLeft } from 'lucide-react';

export default function NotFoundPage() {
  return (
    <div className="max-w-md mx-auto px-4 py-24 text-center space-y-4">
      <div className="w-16 h-16 rounded-full bg-slate-100 flex items-center justify-center mx-auto text-slate-400">
        <SearchX className="w-8 h-8" />
      </div>
      <h1 className="text-3xl font-bold text-slate-900">404 - Page Not Found</h1>
      <p className="text-xs text-slate-500">
        The page you are looking for does not exist or may have been relocated.
      </p>
      <div className="pt-2">
        <Link
          to="/"
          className="inline-flex items-center gap-1.5 px-4 py-2 bg-sky-600 hover:bg-sky-700 text-white text-xs font-semibold rounded-xl shadow-xs transition"
        >
          <ArrowLeft className="w-4 h-4" />
          Back to PriceWise Home
        </Link>
      </div>
    </div>
  );
}

