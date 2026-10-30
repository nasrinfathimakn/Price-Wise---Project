import React from 'react';
import { Link } from 'react-router-dom';
import RegisterForm from '../components/RegisterForm';
import { Tag } from 'lucide-react';

export default function RegistrationPage() {
  return (
    <div className="max-w-md mx-auto px-4 py-16 space-y-6">
      <div className="text-center space-y-2">
        <div className="w-12 h-12 rounded-2xl bg-teal-600 text-white mx-auto flex items-center justify-center shadow-md">
          <Tag className="w-6 h-6" />
        </div>
        <h1 className="text-2xl font-bold text-slate-900">Create PriceWise Account</h1>
        <p className="text-xs text-slate-500">
          Track verified prices and receive alerts when products drop
        </p>
      </div>

      <div className="bg-white rounded-3xl border border-slate-200 p-6 md:p-8 shadow-sm">
        <RegisterForm />
      </div>

      <p className="text-center text-xs text-slate-500">
        Already have an account?{' '}
        <Link to="/login" className="font-semibold text-sky-600 hover:underline">
          Sign in
        </Link>
      </p>
    </div>
  );
}

