import React from 'react';
import { Link } from 'react-router-dom';
import { Smartphone, Laptop, Headphones, Watch, Layers } from 'lucide-react';

const categoryIcons = {
  Smartphones: Smartphone,
  Laptops: Laptop,
  'Audio & Headphones': Headphones,
  Wearables: Watch,
};

export default function CategoryList({ categories = [], selectedId, onSelect }) {
  return (
    <div className="flex items-center gap-2.5 overflow-x-auto pb-2 scrollbar-none">
      <button
        onClick={() => onSelect && onSelect(null)}
        className={`inline-flex items-center gap-1.5 px-3.5 py-2 rounded-xl text-xs font-semibold whitespace-nowrap transition border ${
          !selectedId
            ? 'bg-sky-600 text-white border-sky-600 shadow-sm'
            : 'bg-white text-slate-700 border-slate-200 hover:bg-slate-50'
        }`}
      >
        <Layers className="w-4 h-4" />
        All Categories
      </button>

      {categories.map((cat) => {
        const Icon = categoryIcons[cat.name] || Layers;
        const isSelected = selectedId === cat.id;

        return (
          <button
            key={cat.id}
            onClick={() => onSelect && onSelect(cat.id)}
            className={`inline-flex items-center gap-1.5 px-3.5 py-2 rounded-xl text-xs font-semibold whitespace-nowrap transition border ${
              isSelected
                ? 'bg-sky-600 text-white border-sky-600 shadow-sm'
                : 'bg-white text-slate-700 border-slate-200 hover:bg-slate-50'
            }`}
          >
            <Icon className="w-4 h-4" />
            {cat.name}
          </button>
        );
      })}
    </div>
  );
}

