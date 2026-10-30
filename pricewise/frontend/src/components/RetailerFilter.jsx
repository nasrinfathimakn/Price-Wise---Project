import React from 'react';

export default function RetailerFilter({ retailers = [], selectedRetailerId, onSelect }) {
  return (
    <div className="space-y-1">
      <button
        onClick={() => onSelect(null)}
        className={`w-full text-left px-2.5 py-1.5 rounded-lg text-xs transition ${!selectedRetailerId
          ? 'bg-sky-50 text-sky-700 font-semibold'
          : 'text-slate-600 hover:bg-slate-50'
          }`}
      >
        All Retailers
      </button>
      {retailers.map((r) => (
        <button
          key={r.id}
          onClick={() => onSelect(r.id)}
          className={`w-full text-left px-2.5 py-1.5 rounded-lg text-xs transition ${selectedRetailerId === r.id
            ? 'bg-sky-50 text-sky-700 font-semibold'
            : 'text-slate-600 hover:bg-slate-50'
            }`}
        >
          {r.name}
        </button>
      ))}
    </div>
  );
}

