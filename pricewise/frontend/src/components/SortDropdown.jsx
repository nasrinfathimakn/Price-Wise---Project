import React from 'react';
import { ArrowUpDown } from 'lucide-react';

export default function SortDropdown({ value = 'relevance', onChange }) {
  return (
    <div className="relative inline-flex items-center">
      <div className="absolute left-3 pointer-events-none text-slate-400">
        <ArrowUpDown className="w-3.5 h-3.5" />
      </div>
      <select
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="pl-8 pr-8 py-2 bg-white border border-slate-200 rounded-xl text-xs font-medium text-slate-700 shadow-xs focus:outline-none focus:ring-1 focus:ring-sky-500 cursor-pointer appearance-none"
      >
        <option value="relevance">Sort: Relevance</option>
        <option value="price_asc">Price: Low to High</option>
        <option value="price_desc">Price: High to Low</option>
        <option value="name_asc">Name: A to Z</option>
      </select>
    </div>
  );
}

