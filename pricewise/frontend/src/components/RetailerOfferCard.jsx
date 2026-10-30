import React from 'react';
import { ExternalLink, Truck, Check, AlertCircle, Clock, ShieldCheck } from 'lucide-react';
import PriceBadge from './PriceBadge';

export default function RetailerOfferCard({ offer }) {
  const formatINR = (val) =>
    val != null
      ? new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(val)
      : null;

  return (
    <div
      className={`p-5 rounded-2xl border transition duration-200 flex flex-col md:flex-row md:items-center justify-between gap-4 ${
        offer.isLowestPrice
          ? 'bg-gradient-to-r from-emerald-50/70 via-white to-white border-emerald-300 shadow-sm'
          : 'bg-white border-slate-200 hover:border-slate-300'
      }`}
    >
      {/* Left: Retailer & Listing info */}
      <div className="flex items-start gap-4 flex-1">
        <div className="w-14 h-14 rounded-xl border border-slate-100 bg-white shadow-xs p-2 shrink-0 flex items-center justify-center">
          {offer.retailerLogoUrl ? (
            <img src={offer.retailerLogoUrl} alt={offer.retailerName} className="max-h-full max-w-full object-contain" />
          ) : (
            <span className="text-xs font-bold text-slate-700">{offer.retailerCode}</span>
          )}
        </div>

        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-2 mb-1 flex-wrap">
            <span className="font-semibold text-slate-900 text-sm">{offer.retailerName}</span>
            {offer.isLowestPrice && (
              <span className="inline-flex items-center gap-1 text-[11px] font-bold text-emerald-700 bg-emerald-100 px-2.5 py-0.5 rounded-full">
                <ShieldCheck className="w-3 h-3" />
                Lowest Verified Price
              </span>
            )}
            {offer.isStale && <PriceBadge type="stale" label="Price Update Pending" />}
          </div>

          <h4 className="text-xs text-slate-600 line-clamp-1 mb-2" title={offer.productTitle}>
            {offer.productTitle}
          </h4>

          {/* Delivery & Stock Details */}
          <div className="flex items-center gap-4 text-xs text-slate-500 flex-wrap">
            <div className="flex items-center gap-1">
              <Truck className="w-3.5 h-3.5 text-slate-400" />
              <span>
                {offer.isDeliveryUnknown ? (
                  <span className="text-amber-600 font-medium">Delivery: Unknown</span>
                ) : offer.deliveryCharge === 0 ? (
                  <span className="text-emerald-600 font-medium">Free Delivery</span>
                ) : (
                  <span>Delivery: {formatINR(offer.deliveryCharge)}</span>
                )}
              </span>
            </div>

            {offer.availabilityStatus === 'OUT_OF_STOCK' ? (
              <div className="flex items-center gap-1 text-rose-600 font-medium">
                <AlertCircle className="w-3.5 h-3.5" />
                <span>Out of Stock</span>
              </div>
            ) : (
              <div className="flex items-center gap-1 text-emerald-600">
                <Check className="w-3.5 h-3.5" />
                <span>In Stock</span>
              </div>
            )}

            <div className="flex items-center gap-1 text-slate-400">
              <Clock className="w-3.5 h-3.5" />
              <span>Updated: {offer.lastSuccessfulFetchAt ? new Date(offer.lastSuccessfulFetchAt).toLocaleDateString() : 'Recent'}</span>
            </div>
          </div>
        </div>
      </div>

      {/* Right: Pricing breakdown & Action */}
      <div className="flex items-end md:items-center justify-between md:justify-end gap-6 pt-3 md:pt-0 border-t md:border-t-0 border-slate-100">
        <div className="text-left md:text-right">
          <div className="flex items-baseline gap-2">
            <span className="text-2xl font-bold text-slate-900">
              {formatINR(offer.currentPrice)}
            </span>
            {offer.originalPrice && offer.originalPrice > offer.currentPrice && (
              <span className="text-xs text-slate-400 line-through">
                {formatINR(offer.originalPrice)}
              </span>
            )}
          </div>

          {offer.discountPercentage && offer.discountPercentage > 0 && (
            <div className="text-[11px] font-semibold text-emerald-600">
              {Math.round(offer.discountPercentage)}% Off
            </div>
          )}

          {/* Total delivered price when available */}
          {!offer.isDeliveryUnknown && offer.totalDeliveredPrice && (
            <div className="text-[11px] text-slate-500 mt-0.5">
              Total with delivery: <span className="font-semibold text-slate-700">{formatINR(offer.totalDeliveredPrice)}</span>
            </div>
          )}
        </div>

        <a
          href={offer.productUrl || '#'}
          target="_blank"
          rel="noopener noreferrer"
          className={`inline-flex items-center gap-1.5 px-4 py-2.5 rounded-xl text-xs font-semibold shadow-sm transition whitespace-nowrap ${
            offer.isLowestPrice
              ? 'bg-emerald-600 hover:bg-emerald-700 text-white'
              : 'bg-sky-600 hover:bg-sky-700 text-white'
          }`}
        >
          View Deal
          <ExternalLink className="w-3.5 h-3.5" />
        </a>
      </div>
    </div>
  );
}

