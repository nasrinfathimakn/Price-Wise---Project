import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Share2, Bell, Check, ArrowRight } from 'lucide-react';
import WatchlistButton from './WatchlistButton';
import PriceAlertForm from './PriceAlertForm';

export default function ProductDetails({ product }) {
  const [showAlertModal, setShowAlertModal] = useState(false);
  const [copied, setCopied] = useState(false);

  const formatINR = (val) =>
    val != null
      ? new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(val)
      : '₹0';

  const handleShare = () => {
    navigator.clipboard.writeText(window.location.href);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="bg-white rounded-3xl border border-slate-200 p-6 md:p-8 shadow-sm">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8 items-start">
        {/* Product Image Gallery Preview */}
        <div className="bg-slate-50 rounded-2xl p-8 flex items-center justify-center relative overflow-hidden aspect-square border border-slate-100">
          <img
            src={product.imageUrl || 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&q=80'}
            alt={product.name}
            className="max-h-full max-w-full object-contain hover:scale-105 transition duration-300"
          />
          <div className="absolute top-4 right-4 flex items-center gap-2">
            <WatchlistButton productId={product.id} className="shadow-xs" />
            <button
              onClick={handleShare}
              className="p-2 rounded-xl bg-white/80 hover:bg-white text-slate-500 hover:text-slate-800 border border-slate-200 shadow-xs transition"
              title="Share link"
            >
              {copied ? <Check className="w-4 h-4 text-emerald-600" /> : <Share2 className="w-4 h-4" />}
            </button>
          </div>
        </div>

        {/* Product Meta & Highlights */}
        <div className="flex flex-col justify-between space-y-6">
          <div>
            <div className="flex items-center gap-2 mb-2">
              <span className="text-xs font-semibold px-2.5 py-1 rounded-lg bg-sky-50 text-sky-700 border border-sky-100">
                {product.brandName}
              </span>
              <span className="text-xs font-medium text-slate-500">
                {product.categoryName}
              </span>
              {product.modelNumber && (
                <span className="text-xs font-mono text-slate-400 bg-slate-100 px-2 py-0.5 rounded">
                  Model: {product.modelNumber}
                </span>
              )}
            </div>

            <h1 className="text-2xl md:text-3xl font-bold text-slate-900 leading-tight mb-3">
              {product.name}
            </h1>

            <p className="text-xs text-slate-600 leading-relaxed mb-6">
              {product.description}
            </p>

            {/* Price Box */}
            <div className="p-4 rounded-2xl bg-gradient-to-r from-emerald-50/70 to-sky-50/40 border border-emerald-100 mb-6">
              <span className="text-[11px] font-semibold text-emerald-800 uppercase tracking-wide block">
                Lowest Verified Price Across Retailers
              </span>
              <div className="flex items-baseline gap-3 mt-1">
                <span className="text-3xl font-extrabold text-emerald-600">
                  {formatINR(product.lowestPrice)}
                </span>
                <span className="text-xs text-slate-500">
                  Across {product.offerCount || 0} supported stores
                </span>
              </div>
            </div>

            {/* Distinct Variants */}
            {product.availableVariants && product.availableVariants.length > 0 && (
              <div className="mb-6">
                <h4 className="text-xs font-semibold text-slate-700 uppercase tracking-wider mb-2">
                  Available Variant Configurations
                </h4>
                <div className="flex flex-wrap gap-2">
                  <div className="px-3 py-1.5 rounded-xl text-xs font-bold bg-sky-600 text-white shadow-xs">
                    This Variant ({product.modelNumber || 'Current'})
                  </div>
                  {product.availableVariants.map((v) => (
                    <Link
                      key={v.id}
                      to={`/products/${v.id}`}
                      className="px-3 py-1.5 rounded-xl text-xs font-medium bg-slate-100 hover:bg-slate-200 text-slate-700 transition"
                    >
                      {v.name} {v.lowestPrice ? `(${formatINR(v.lowestPrice)})` : ''}
                    </Link>
                  ))}
                </div>
              </div>
            )}
          </div>

          {/* Action Row */}
          <div className="flex flex-col sm:flex-row items-center gap-3 pt-4 border-t border-slate-100">
            <Link
              to={`/compare/${product.id}`}
              className="w-full sm:flex-1 py-3 px-4 bg-sky-600 hover:bg-sky-700 text-white text-xs font-bold rounded-xl shadow-sm transition flex items-center justify-center gap-2"
            >
              Compare Live Offers
              <ArrowRight className="w-4 h-4" />
            </Link>
            <button
              onClick={() => setShowAlertModal(true)}
              className="w-full sm:w-auto py-3 px-4 bg-slate-100 hover:bg-slate-200 text-slate-700 text-xs font-semibold rounded-xl transition flex items-center justify-center gap-2"
            >
              <Bell className="w-4 h-4 text-slate-500" />
              Set Price Alert
            </button>
          </div>
        </div>
      </div>

      {/* Technical Specifications Section */}
      {product.specifications && product.specifications.length > 0 && (
        <div className="mt-10 pt-8 border-t border-slate-100">
          <h3 className="text-base font-bold text-slate-900 mb-4">Technical Specifications</h3>
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-3">
            {product.specifications.map((spec, i) => (
              <div key={i} className="p-3 bg-slate-50 rounded-xl border border-slate-100">
                <span className="text-[11px] font-semibold text-slate-400 block uppercase">
                  {spec.attributeName}
                </span>
                <span className="text-xs font-medium text-slate-800">
                  {spec.attributeValue}
                </span>
              </div>
            ))}
          </div>
        </div>
      )}

      {showAlertModal && (
        <PriceAlertForm
          productId={product.id}
          productName={product.name}
          currentPrice={product.lowestPrice}
          onClose={() => setShowAlertModal(false)}
        />
      )}
    </div>
  );
}

