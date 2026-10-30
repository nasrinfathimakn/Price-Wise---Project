import React from 'react';
import { Search } from 'lucide-react';

export default function SearchSuggestions({ suggestions = [], query = '', onSelect }) {
  if (suggestions.length === 0) return null;

  return (
    <div className="absolute left-0 right-0 top-full mt-1.5 bg-white border border-slate-200 rounded-xl shadow-xl z-50 overflow-hidden divide-y divide-slate-100 animate-in fade-in slide-in-from-top-1 duration-150">
      <div className="px-3 py-1.5 bg-slate-50 text-[11px] font-semibold text-slate-500 uppercase tracking-wider">
        Search Suggestions
      </div>
      <ul className="max-h-60 overflow-y-auto">
        {suggestions.map((suggestion, index) => (
          <li key={index}>
            <button
              type="button"
              onClick={() => onSelect(suggestion)}
              className="w-full text-left px-4 py-2.5 text-xs text-slate-700 hover:bg-sky-50 hover:text-sky-700 flex items-center gap-2.5 transition"
            >
              <Search className="w-3.5 h-3.5 text-slate-400 shrink-0" />
              <span className="truncate">{suggestion}</span>
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

