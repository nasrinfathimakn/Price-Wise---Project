import React from 'react';

export default function BrandFilter({ brands = [], selectedBrandId, onSelect }) {
  return (
    <div className="space-y-1 max-h-48 overflow-y-auto pr-1">
      <button
        onClick={() => onSelect(null)}
        className={`w-full text-left px-2.5 py-1.5 rounded-lg text-xs transition ${
          !selectedBrandId
            ? 'bg-sky-50 text-sky-700 font-semibold'
            : 'text-slate-600 hover:bg-slate-50'
        }`}
      >
        All Brands
      </button>
      {brands.map((b) => (
        <button
          key={b.id}
          onClick={() => onSelect(b.id)}
          className={`w-full text-left px-2.5 py-1.5 rounded-lg text-xs transition ${
            selectedBrandId === b.id
              ? 'bg-sky-50 text-sky-700 font-semibold'
              : 'text-slate-600 hover:bg-slate-50'
          }`}
        >
          {b.name}
        </button>
      ))}
    </div>
  );
}

