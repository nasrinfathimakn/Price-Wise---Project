import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Tag, Bookmark, Bell, ShieldCheck, LogOut, LogIn, UserPlus } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
  const { user, logout, isAdmin } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <header className="sticky top-0 z-40 bg-white/95 backdrop-blur border-b border-slate-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo & Brand */}
          <Link to="/" className="flex items-center gap-2.5 group">
            <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-sky-600 to-teal-500 flex items-center justify-center text-white shadow-md shadow-sky-500/20 group-hover:scale-105 transition transform">
              <Tag className="w-5 h-5" />
            </div>
            <div>
              <span className="text-xl font-bold bg-gradient-to-r from-sky-700 to-teal-600 bg-clip-text text-transparent">
                PriceWise
              </span>
              <span className="hidden sm:inline-block text-[10px] font-semibold text-sky-600 bg-sky-50 px-1.5 py-0.5 rounded ml-1.5 border border-sky-100">
                INDIA
              </span>
            </div>
          </Link>

          {/* Center Navigation Links */}
          <nav className="hidden md:flex items-center space-x-6 text-sm font-medium text-slate-600">
            <Link to="/" className="hover:text-sky-600 transition">Home</Link>
            <Link to="/search" className="hover:text-sky-600 transition">Compare Offers</Link>
            <Link to="/search?category=2" className="hover:text-sky-600 transition flex items-center gap-1">
              Smartphones
            </Link>
            <Link to="/search?category=3" className="hover:text-sky-600 transition">Laptops</Link>
            <Link to="/search?category=4" className="hover:text-sky-600 transition">Audio</Link>
          </nav>

          {/* Right Navigation & User Actions */}
          <div className="flex items-center space-x-3">
            {user ? (
              <>
                <Link
                  to="/watchlist"
                  className="p-2 text-slate-600 hover:text-sky-600 hover:bg-slate-50 rounded-lg transition relative"
                  title="Watchlist"
                >
                  <Bookmark className="w-5 h-5" />
                </Link>
                <Link
                  to="/alerts"
                  className="p-2 text-slate-600 hover:text-sky-600 hover:bg-slate-50 rounded-lg transition relative"
                  title="Price Alerts"
                >
                  <Bell className="w-5 h-5" />
                </Link>

                {isAdmin && (
                  <Link
                    to="/admin"
                    className="hidden sm:inline-flex items-center gap-1.5 px-3 py-1.5 text-xs font-semibold text-teal-700 bg-teal-50 border border-teal-200 rounded-lg hover:bg-teal-100 transition"
                  >
                    <ShieldCheck className="w-4 h-4" />
                    Admin
                  </Link>
                )}

                <div className="flex items-center pl-2 border-l border-slate-200 gap-2">
                  <span className="text-xs font-medium text-slate-600 hidden sm:inline-block max-w-[120px] truncate">
                    {user.name}
                  </span>
                  <button
                    onClick={handleLogout}
                    className="p-1.5 text-slate-500 hover:text-rose-600 hover:bg-rose-50 rounded-lg transition"
                    title="Log out"
                  >
                    <LogOut className="w-4 h-4" />
                  </button>
                </div>
              </>
            ) : (
              <div className="flex items-center space-x-2">
                <Link
                  to="/login"
                  className="inline-flex items-center gap-1 px-3 py-1.5 text-sm font-medium text-slate-700 hover:text-sky-600 hover:bg-slate-50 rounded-lg transition"
                >
                  <LogIn className="w-4 h-4" />
                  Log in
                </Link>
                <Link
                  to="/register"
                  className="inline-flex items-center gap-1 px-3.5 py-1.5 text-sm font-semibold text-white bg-sky-600 hover:bg-sky-700 rounded-lg shadow-sm transition"
                >
                  <UserPlus className="w-4 h-4" />
                  Register
                </Link>
              </div>
            )}
          </div>
        </div>
      </div>
    </header>
  );
}

