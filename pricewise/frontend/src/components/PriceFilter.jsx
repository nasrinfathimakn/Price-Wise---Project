import React, { useState } from 'react';

export default function PriceFilter({ minPrice, maxPrice, onApply }) {
  const [min, setMin] = useState(minPrice || '');
  const [max, setMax] = useState(maxPrice || '');

  const handleSubmit = (e) => {
    e.preventDefault();
    onApply(min ? Number(min) : null, max ? Number(max) : null);
  };

  const handleClear = () => {
    setMin('');
    setMax('');
    onApply(null, null);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-3">
      <div className="flex items-center gap-2">
        <input
          type="number"
          placeholder="Min ₹"
          value={min}
          onChange={(e) => setMin(e.target.value)}
          className="w-full px-2.5 py-1.5 text-xs bg-white border border-slate-200 rounded-lg focus:outline-none focus:ring-1 focus:ring-sky-500"
        />
        <span className="text-slate-400 text-xs">to</span>
        <input
          type="number"
          placeholder="Max ₹"
          value={max}
          onChange={(e) => setMax(e.target.value)}
          className="w-full px-2.5 py-1.5 text-xs bg-white border border-slate-200 rounded-lg focus:outline-none focus:ring-1 focus:ring-sky-500"
        />
      </div>
      <div className="flex items-center gap-2">
        <button
          type="submit"
          className="w-full py-1.5 bg-sky-600 hover:bg-sky-700 text-white text-xs font-medium rounded-lg shadow-xs transition"
        >
          Apply
        </button>
        {(min || max) && (
          <button
            type="button"
            onClick={handleClear}
            className="px-2 py-1.5 text-xs text-slate-500 hover:text-slate-800 transition"
          >
            Clear
          </button>
        )}
      </div>
    </form>
  );
}

