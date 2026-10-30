import React from 'react';
import { Link } from 'react-router-dom';
import LoginForm from '../components/LoginForm';
import { Tag } from 'lucide-react';

export default function LoginPage() {
  return (
    <div className="max-w-md mx-auto px-4 py-16 space-y-6">
      <div className="text-center space-y-2">
        <div className="w-12 h-12 rounded-2xl bg-sky-600 text-white mx-auto flex items-center justify-center shadow-md">
          <Tag className="w-6 h-6" />
        </div>
        <h1 className="text-2xl font-bold text-slate-900">Sign in to PriceWise</h1>
        <p className="text-xs text-slate-500">
          Access your personal watchlist and real-time price alerts
        </p>
      </div>

      <div className="bg-white rounded-3xl border border-slate-200 p-6 md:p-8 shadow-sm">
        <LoginForm />
      </div>

      <p className="text-center text-xs text-slate-500">
        Don't have an account yet?{' '}
        <Link to="/register" className="font-semibold text-sky-600 hover:underline">
          Create an account
        </Link>
      </p>
    </div>
  );
}

