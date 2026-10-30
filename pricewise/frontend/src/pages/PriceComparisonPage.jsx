import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { api } from '../api/client';
import RetailerOfferCard from '../components/RetailerOfferCard';
import PriceComparisonTable from '../components/PriceComparisonTable';
import PriceBadge from '../components/PriceBadge';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';
import { ChevronRight, ArrowLeft, Clock } from 'lucide-react';

export default function PriceComparisonPage() {
  const { id } = useParams();
  const [comparison, setComparison] = useState(null);
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [viewMode, setViewMode] = useState('cards'); // 'cards' | 'table'

  useEffect(() => {
    async function loadComparison() {
      setLoading(true);
      setError(null);
      try {
        const [compData, prodData] = await Promise.all([
          api.getProductComparison(id),
          api.getProduct(id),
        ]);
        setComparison(compData);
        setProduct(prodData);
      } catch (err) {
        setError(err.message || 'Failed to load price comparison');
      } finally {
        setLoading(false);
      }
    }
    loadComparison();
  }, [id]);

  if (loading) {
    return <LoadingSpinner message="Calculating real-time lowest price across stores..." />;
  }

  if (error || !comparison) {
    return (
      <div className="max-w-4xl mx-auto px-4 py-12">
        <ErrorMessage message={error || 'Comparison data unavailable'} />
        <Link to="/search" className="text-xs font-semibold text-sky-600 hover:underline">
          ← Back to Search
        </Link>
      </div>
    );
  }

  const formatINR = (val) =>
    val != null
      ? new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(val)
      : '₹0';

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8">
      {/* Breadcrumbs */}
      <nav className="flex items-center space-x-2 text-xs text-slate-500">
        <Link to="/" className="hover:text-sky-600 transition">Home</Link>
        <ChevronRight className="w-3.5 h-3.5" />
        <Link to={`/products/${id}`} className="hover:text-sky-600 transition">
          {product?.name || 'Product'}
        </Link>
        <ChevronRight className="w-3.5 h-3.5" />
        <span className="text-slate-800 font-medium">Compare Offers</span>
      </nav>

      {/* Header Banner */}
      <div className="bg-white rounded-3xl border border-slate-200 p-6 md:p-8 shadow-sm">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 pb-6 border-b border-slate-100">
          <div>
            <span className="text-xs font-semibold text-sky-600 uppercase tracking-wide block mb-1">
              Multi-Retailer Price Comparison
            </span>
            <h1 className="text-2xl md:text-3xl font-extrabold text-slate-900 mb-2">
              {comparison.productName}
            </h1>
            {comparison.productVariantInformation && (
              <p className="text-xs text-slate-500 font-mono">
                {comparison.productVariantInformation}
              </p>
            )}
          </div>

          {/* Lowest verified callout */}
          <div className="p-4 rounded-2xl bg-emerald-50 border border-emerald-200 shrink-0 text-left md:text-right">
            <span className="text-[11px] font-semibold text-emerald-800 uppercase block">
              Lowest Verified Price
            </span>
            <span className="text-3xl font-extrabold text-emerald-600 block">
              {comparison.lowestVerifiedComparablePrice != null
                ? formatINR(comparison.lowestVerifiedComparablePrice)
                : 'No Offers'}
            </span>
            <span className="text-xs text-slate-600">
              {comparison.lowestOfferRetailerName
                ? <>Found at <strong className="text-slate-900">{comparison.lowestOfferRetailerName}</strong></>
                : 'No retailer offers available'}
            </span>
          </div>
        </div>

        {/* Freshness & Delivery Transparency Notice */}
        <div className="pt-4 flex flex-col sm:flex-row sm:items-center justify-between gap-3 text-xs text-slate-600">
          <div className="flex items-center gap-2">
            <PriceBadge
              type={comparison.dataFreshnessStatus === 'FRESH' ? 'fresh' : 'stale'}
              label={comparison.dataFreshnessStatus === 'FRESH' ? 'Verified Fresh' : 'Update Notice'}
            />
            <span>{comparison.freshnessExplanation}</span>
          </div>

          <div className="flex items-center gap-2 text-slate-400">
            <Clock className="w-3.5 h-3.5" />
            <span>
              Last checked: {comparison.lastSuccessfulUpdateTimestamp ? new Date(comparison.lastSuccessfulUpdateTimestamp).toLocaleTimeString() : 'Recent'}
            </span>
          </div>
        </div>
      </div>

      {/* Offers Section */}
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-lg font-bold text-slate-900">
            All Retailer Offers ({comparison.availableOffers?.length || 0})
          </h2>
          <div className="flex items-center gap-1 bg-white border border-slate-200 p-1 rounded-xl text-xs font-semibold">
            <button
              onClick={() => setViewMode('cards')}
              className={`px-3 py-1 rounded-lg transition ${
                viewMode === 'cards' ? 'bg-sky-600 text-white shadow-sm' : 'text-slate-600 hover:text-slate-900'
              }`}
            >
              Cards
            </button>
            <button
              onClick={() => setViewMode('table')}
              className={`px-3 py-1 rounded-lg transition ${
                viewMode === 'table' ? 'bg-sky-600 text-white shadow-sm' : 'text-slate-600 hover:text-slate-900'
              }`}
            >
              Table
            </button>
          </div>
        </div>

        {viewMode === 'cards' ? (
          comparison.availableOffers && comparison.availableOffers.length > 0 ? (
            <div className="space-y-4">
              {comparison.availableOffers.map((offer) => (
                <RetailerOfferCard key={offer.id} offer={offer} />
              ))}
            </div>
          ) : (
            <div className="p-8 text-center text-sm text-slate-500 bg-white rounded-2xl border border-slate-200">
              No active retailer offers available for comparison.
            </div>
          )
        ) : (
          <PriceComparisonTable offers={comparison.availableOffers || []} />
        )}
      </div>

      {/* Back to product */}
      <div className="pt-4">
        <Link
          to={`/products/${id}`}
          className="inline-flex items-center gap-1.5 text-xs font-semibold text-sky-600 hover:text-sky-700 transition"
        >
          <ArrowLeft className="w-3.5 h-3.5" />
          Back to Product Overview & Specifications
        </Link>
      </div>
    </div>
  );
}

