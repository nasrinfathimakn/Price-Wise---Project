import React from 'react';
import { Link } from 'react-router-dom';
import { ArrowRight } from 'lucide-react';
import WatchlistButton from './WatchlistButton';

export default function ProductCard({ product }) {
  const formattedPrice = product.lowestPrice
    ? new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(product.lowestPrice)
    : 'Check Price';

  return (
    <div className="group bg-white rounded-2xl border border-slate-200 hover:border-sky-300 shadow-sm hover:shadow-md transition duration-200 flex flex-col overflow-hidden relative">
      {/* Top badges & Watchlist */}
      <div className="absolute top-3 left-3 right-3 flex items-center justify-between z-10 pointer-events-none">
        <span className="text-[11px] font-semibold tracking-wide uppercase px-2.5 py-0.5 rounded-full bg-slate-900/70 backdrop-blur text-white pointer-events-auto">
          {product.brandName || 'Brand'}
        </span>
        <div className="pointer-events-auto">
          <WatchlistButton productId={product.id} />
        </div>
      </div>

      {/* Product Image */}
      <Link to={`/products/${product.id}`} className="block relative pt-[75%] bg-slate-100 overflow-hidden">
        <img
          src={product.imageUrl || 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&q=80'}
          alt={product.name}
          className="absolute inset-0 w-full h-full object-contain p-6 group-hover:scale-105 transition duration-300"
          loading="lazy"
        />
      </Link>

      {/* Content */}
      <div className="p-4 flex-1 flex flex-col justify-between">
        <div>
          <div className="flex items-center gap-2 text-[11px] text-slate-500 mb-1">
            <span>{product.categoryName}</span>
            {product.modelNumber && (
              <>
                <span>•</span>
                <span className="font-mono">{product.modelNumber}</span>
              </>
            )}
          </div>
          <Link to={`/products/${product.id}`}>
            <h3 className="font-semibold text-sm text-slate-900 line-clamp-2 hover:text-sky-600 transition mb-2">
              {product.name}
            </h3>
          </Link>
        </div>

        <div className="pt-3 border-t border-slate-100">
          <div className="flex items-baseline justify-between mb-3">
            <div>
              <span className="text-[10px] text-slate-400 font-medium block">Lowest Verified Price</span>
              <span className="text-lg font-bold text-emerald-600">
                {formattedPrice}
              </span>
            </div>
            <span className="text-[11px] font-medium text-slate-500 bg-slate-100 px-2 py-0.5 rounded-full">
              {product.offerCount || 0} {product.offerCount === 1 ? 'offer' : 'offers'}
            </span>
          </div>

          <div className="grid grid-cols-2 gap-2">
            <Link
              to={`/products/${product.id}`}
              className="w-full text-center py-2 px-3 bg-slate-100 hover:bg-slate-200 text-slate-700 text-xs font-semibold rounded-xl transition"
            >
              Details
            </Link>
            <Link
              to={`/compare/${product.id}`}
              className="w-full inline-flex items-center justify-center gap-1 py-2 px-3 bg-sky-600 hover:bg-sky-700 text-white text-xs font-semibold rounded-xl shadow-sm transition"
            >
              Compare
              <ArrowRight className="w-3.5 h-3.5" />
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}

