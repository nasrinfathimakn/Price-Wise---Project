import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { api } from '../api/client';
import ProductDetails from '../components/ProductDetails';
import PriceComparisonTable from '../components/PriceComparisonTable';
import PriceHistoryChart from '../components/PriceHistoryChart';
import ProductGrid from '../components/ProductGrid';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';
import { ChevronRight, ExternalLink, ArrowRight } from 'lucide-react';

export default function ProductDetailsPage() {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [comparison, setComparison] = useState(null);
  const [history, setHistory] = useState(null);
  const [related, setRelated] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function loadData() {
      setLoading(true);
      setError(null);
      try {
        const [prodData, compData, histData, relData] = await Promise.all([
          api.getProduct(id),
          api.getProductComparison(id),
          api.getPriceHistory(id),
          api.getRelatedProducts(id),
        ]);
        setProduct(prodData);
        setComparison(compData);
        setHistory(histData);
        setRelated(relData || []);
      } catch (err) {
        setError(err.message || 'Failed to load product details');
      } finally {
        setLoading(false);
      }
    }
    loadData();
  }, [id]);

  if (loading) {
    return <LoadingSpinner message="Loading canonical product specifications and offers..." />;
  }

  if (error || !product) {
    return (
      <div className="max-w-4xl mx-auto px-4 py-12">
        <ErrorMessage message={error || 'Product not found'} />
        <Link to="/search" className="text-xs font-semibold text-sky-600 hover:underline">
          ← Back to Search
        </Link>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-12">
      {/* Breadcrumb Navigation */}
      <nav className="flex items-center space-x-2 text-xs text-slate-500">
        <Link to="/" className="hover:text-sky-600 transition">Home</Link>
        <ChevronRight className="w-3.5 h-3.5" />
        <Link to={`/search?category=${product.categoryId}`} className="hover:text-sky-600 transition">
          {product.categoryName}
        </Link>
        <ChevronRight className="w-3.5 h-3.5" />
        <span className="text-slate-800 font-medium truncate max-w-xs">{product.name}</span>
      </nav>

      {/* Main Canonical Product Info */}
      <ProductDetails product={product} />

      {/* Retailer Offers Comparison Table */}
      <section className="space-y-4">
        <div className="flex items-center justify-between">
          <div>
            <h2 className="text-xl font-bold text-slate-900">Retailer Price Comparison</h2>
            <p className="text-xs text-slate-500">Verified live prices, delivery fees, and stock statuses across stores</p>
          </div>
          <Link
            to={`/compare/${product.id}`}
            className="text-xs font-semibold text-sky-600 hover:text-sky-700 flex items-center gap-1"
          >
            Full Comparison View <ArrowRight className="w-3.5 h-3.5" />
          </Link>
        </div>

        <PriceComparisonTable offers={comparison?.availableOffers || []} />
      </section>

      {/* Price History Chart */}
      <section className="space-y-4">
        <PriceHistoryChart priceHistory={history} />
      </section>

      {/* Similar Alternatives & Related Products */}
      {related.length > 0 && (
        <section className="space-y-6 pt-4 border-t border-slate-200">
          <div>
            <h2 className="text-xl font-bold text-slate-900">Similar Alternatives</h2>
            <p className="text-xs text-slate-500">Other popular products in {product.categoryName}</p>
          </div>
          <ProductGrid products={related} />
        </section>
      )}
    </div>
  );
}

