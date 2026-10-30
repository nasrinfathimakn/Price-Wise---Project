import React from 'react';
import { ExternalLink, Check, AlertTriangle } from 'lucide-react';

export default function PriceComparisonTable({ offers = [] }) {
  const formatINR = (val) =>
    val != null
      ? new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(val)
      : 'Unknown';

  if (!offers || offers.length === 0) {
    return (
      <div className="p-8 text-center text-sm text-slate-500 bg-white rounded-2xl border border-slate-200">
        No active retailer offers available for comparison.
      </div>
    );
  }

  return (
    <div className="overflow-x-auto bg-white rounded-2xl border border-slate-200 shadow-sm">
      <table className="w-full text-left border-collapse text-xs">
        <thead>
          <tr className="bg-slate-50 border-b border-slate-200 text-slate-600 font-semibold uppercase text-[11px] tracking-wider">
            <th className="py-3 px-4">Retailer</th>
            <th className="py-3 px-4">Offer Title</th>
            <th className="py-3 px-4 text-right">Product Price</th>
            <th className="py-3 px-4 text-right">Delivery</th>
            <th className="py-3 px-4 text-right">Total Delivered</th>
            <th className="py-3 px-4 text-center">Availability</th>
            <th className="py-3 px-4 text-right">Action</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-slate-100">
          {offers.map((offer) => (
            <tr
              key={offer.id}
              className={`hover:bg-slate-50/80 transition ${
                offer.isLowestPrice ? 'bg-emerald-50/40 font-medium' : ''
              }`}
            >
              <td className="py-3.5 px-4">
                <div className="flex items-center gap-2">
                  <span className="font-semibold text-slate-900">{offer.retailerName}</span>
                  {offer.isLowestPrice && (
                    <span className="inline-flex items-center gap-0.5 text-[10px] font-bold text-emerald-700 bg-emerald-100 px-1.5 py-0.5 rounded">
                      Lowest
                    </span>
                  )}
                </div>
              </td>
              <td className="py-3.5 px-4 max-w-xs truncate text-slate-600" title={offer.productTitle}>
                {offer.productTitle}
              </td>
              <td className="py-3.5 px-4 text-right">
                <span className={`text-sm font-bold ${offer.isLowestPrice ? 'text-emerald-700' : 'text-slate-900'}`}>
                  {formatINR(offer.currentPrice)}
                </span>
                {offer.discountPercentage > 0 && (
                  <span className="block text-[10px] text-emerald-600">
                    {Math.round(offer.discountPercentage)}% off
                  </span>
                )}
              </td>
              <td className="py-3.5 px-4 text-right">
                {offer.isDeliveryUnknown ? (
                  <span className="text-amber-600 italic">Unknown</span>
                ) : offer.deliveryCharge === 0 ? (
                  <span className="text-emerald-600 font-medium">₹0 (Free)</span>
                ) : (
                  <span>{formatINR(offer.deliveryCharge)}</span>
                )}
              </td>
              <td className="py-3.5 px-4 text-right font-semibold">
                {offer.isDeliveryUnknown ? (
                  <span className="text-slate-400 italic font-normal">Unknown</span>
                ) : (
                  <span className={offer.isLowestPrice ? 'text-emerald-700' : 'text-slate-900'}>
                    {formatINR(offer.totalDeliveredPrice)}
                  </span>
                )}
              </td>
              <td className="py-3.5 px-4 text-center">
                {offer.availabilityStatus === 'OUT_OF_STOCK' ? (
                  <span className="inline-flex items-center gap-1 text-[11px] font-medium text-rose-700 bg-rose-50 px-2 py-0.5 rounded-full border border-rose-100">
                    <AlertTriangle className="w-3 h-3" />
                    Out of Stock
                  </span>
                ) : (
                  <span className="inline-flex items-center gap-1 text-[11px] font-medium text-emerald-700 bg-emerald-50 px-2 py-0.5 rounded-full border border-emerald-100">
                    <Check className="w-3 h-3" />
                    In Stock
                  </span>
                )}
              </td>
              <td className="py-3.5 px-4 text-right">
                {offer.productUrl ? (
                  <a
                    href={offer.productUrl}
                    target="_blank"
                    rel="noopener noreferrer"
                    className={`inline-flex items-center gap-1 px-3 py-1.5 rounded-lg text-xs font-semibold shadow-sm transition ${
                      offer.isLowestPrice
                        ? 'bg-emerald-600 hover:bg-emerald-700 text-white'
                        : 'bg-sky-600 hover:bg-sky-700 text-white'
                    }`}
                  >
                    View Deal
                    <ExternalLink className="w-3 h-3" />
                  </a>
                ) : (
                  <span className="text-slate-400 text-xs italic">Unavailable</span>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

