import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Lock, Mail, ArrowRight } from 'lucide-react';
import ErrorMessage from './ErrorMessage';

export default function LoginForm({ onSuccess, redirectUrl = '/' }) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      await login(email, password);
      if (onSuccess) onSuccess();
      navigate(redirectUrl);
    } catch (err) {
      setError(err.message || 'Failed to sign in. Please verify your email and password.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <ErrorMessage message={error} />

      <div>
        <label className="block text-xs font-semibold text-slate-700 mb-1">Email Address</label>
        <div className="relative">
          <Mail className="w-4 h-4 text-slate-400 absolute left-3 top-3 pointer-events-none" />
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="you@example.com"
            required
            className="w-full pl-9 pr-3 py-2.5 bg-white border border-slate-300 rounded-xl text-xs text-slate-900 focus:outline-none focus:ring-2 focus:ring-sky-500"
          />
        </div>
      </div>

      <div>
        <label className="block text-xs font-semibold text-slate-700 mb-1">Password</label>
        <div className="relative">
          <Lock className="w-4 h-4 text-slate-400 absolute left-3 top-3 pointer-events-none" />
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="••••••••"
            required
            className="w-full pl-9 pr-3 py-2.5 bg-white border border-slate-300 rounded-xl text-xs text-slate-900 focus:outline-none focus:ring-2 focus:ring-sky-500"
          />
        </div>
      </div>

      <button
        type="submit"
        disabled={loading}
        className="w-full py-2.5 px-4 bg-sky-600 hover:bg-sky-700 text-white text-xs font-bold rounded-xl shadow-sm transition flex items-center justify-center gap-1.5 disabled:opacity-50"
      >
        {loading ? 'Authenticating...' : 'Sign In'}
        <ArrowRight className="w-4 h-4" />
      </button>

      <div className="p-3 bg-slate-50 rounded-xl text-[11px] text-slate-500 space-y-1">
        <p className="font-semibold text-slate-700">Demo Accounts:</p>
        <p>• Admin: <span className="font-mono text-slate-800">admin@pricewise.local</span> / <span className="font-mono text-slate-800">Password@123</span></p>
        <p>• User: <span className="font-mono text-slate-800">john.doe@example.com</span> / <span className="font-mono text-slate-800">Password@123</span></p>
      </div>
    </form>
  );
}

