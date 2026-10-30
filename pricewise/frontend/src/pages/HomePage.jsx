import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Sparkles, TrendingUp, Zap, ArrowRight } from 'lucide-react';
import { api } from '../api/client';
import SearchBar from '../components/SearchBar';
import CategoryList from '../components/CategoryList';
import ProductGrid from '../components/ProductGrid';
import LoadingSpinner from '../components/LoadingSpinner';

export default function HomePage() {
  const navigate = useNavigate();
  const [categories, setCategories] = useState([]);
  const [trendingProducts, setTrendingProducts] = useState([]);
  const [dealsProducts, setDealsProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchData() {
      try {
        const [catData, prodData] = await Promise.all([
          api.getCategories(),
          api.searchProducts({ size: 8, sort: 'relevance' }),
        ]);
        setCategories(catData || []);
        const items = prodData?.results?.content || [];
        setTrendingProducts(items.slice(0, 4));
        setDealsProducts(items.slice(2, 6));
      } catch (err) {
        console.error('Failed to load homepage data:', err);
      } finally {
        setLoading(false);
      }
    }
    fetchData();
  }, []);

  return (
    <div className="space-y-16 pb-16">
      {/* Hero Section */}
      <section className="relative overflow-hidden bg-gradient-to-b from-sky-50/70 via-white to-slate-50 pt-12 pb-20 border-b border-slate-200/60">
        <div className="max-w-5xl mx-auto px-4 text-center">
          <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full bg-sky-100/80 text-sky-800 text-xs font-semibold mb-6 border border-sky-200">
            <Sparkles className="w-4 h-4 text-sky-600" />
            Verified Prices Across Amazon India, Flipkart & More
          </div>

          <h1 className="text-3xl sm:text-5xl font-extrabold text-slate-900 tracking-tight leading-tight mb-4">
            Never Overpay Online Again.{' '}
            <span className="bg-gradient-to-r from-sky-600 to-teal-500 bg-clip-text text-transparent">
              Compare Real Prices.
            </span>
          </h1>

          <p className="text-sm sm:text-base text-slate-600 max-w-2xl mx-auto mb-8">
            PriceWise cross-checks verified offers across multiple trusted e-commerce stores, verifies model variants, reveals delivery charges, and finds the true lowest price.
          </p>

          {/* Prominent Search Bar */}
          <div className="max-w-2xl mx-auto mb-6">
            <SearchBar placeholder="Search Samsung Galaxy S25, iPhone 17, HP Pavilion, Sony headphones..." />
          </div>

          <div className="flex flex-wrap items-center justify-center gap-3 text-xs text-slate-500">
            <span className="font-semibold text-slate-700">Popular Searches:</span>
            <Link to="/search?q=Samsung+Galaxy+S25" className="px-2.5 py-1 bg-white border border-slate-200 rounded-lg hover:border-sky-300 transition">Samsung Galaxy S25</Link>
            <Link to="/search?q=iPhone+17" className="px-2.5 py-1 bg-white border border-slate-200 rounded-lg hover:border-sky-300 transition">iPhone 17</Link>
            <Link to="/search?q=HP+Pavilion" className="px-2.5 py-1 bg-white border border-slate-200 rounded-lg hover:border-sky-300 transition">HP Pavilion</Link>
            <Link to="/search?q=Sony+WH-1000XM5" className="px-2.5 py-1 bg-white border border-slate-200 rounded-lg hover:border-sky-300 transition">Sony WH-1000XM5</Link>
          </div>
        </div>
      </section>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-16">
        {/* Popular Categories */}
        <section>
          <div className="flex items-center justify-between mb-4">
            <div>
              <h2 className="text-xl font-bold text-slate-900">Popular Categories</h2>
              <p className="text-xs text-slate-500">Browse by electronics departments</p>
            </div>
            <Link to="/search" className="text-xs font-semibold text-sky-600 hover:text-sky-700 flex items-center gap-1">
              View All <ArrowRight className="w-3.5 h-3.5" />
            </Link>
          </div>
          <CategoryList
            categories={categories}
            onSelect={(catId) => navigate(catId ? `/search?category=${catId}` : '/search')}
          />
        </section>

        {/* Trending Products */}
        <section>
          <div className="flex items-center justify-between mb-6">
            <div>
              <h2 className="text-xl font-bold text-slate-900 flex items-center gap-2">
                <TrendingUp className="w-5 h-5 text-sky-600" />
                Trending Products
              </h2>
              <p className="text-xs text-slate-500">Based on recent searches and verified price observations</p>
            </div>
            <Link to="/search" className="text-xs font-semibold text-sky-600 hover:text-sky-700 flex items-center gap-1">
              Explore All <ArrowRight className="w-3.5 h-3.5" />
            </Link>
          </div>

          {loading ? (
            <LoadingSpinner message="Loading trending products..." />
          ) : (
            <ProductGrid products={trendingProducts} />
          )}
        </section>

        {/* High Price Difference Deals */}
        <section>
          <div className="flex items-center justify-between mb-6">
            <div>
              <h2 className="text-xl font-bold text-slate-900 flex items-center gap-2">
                <Zap className="w-5 h-5 text-amber-500" />
                Products with Significant Price Differences
              </h2>
              <p className="text-xs text-slate-500">Big savings between competing stores</p>
            </div>
          </div>

          {loading ? (
            <LoadingSpinner message="Finding biggest price gaps..." />
          ) : (
            <ProductGrid products={dealsProducts} />
          )}
        </section>

        {/* How Price Comparison Works */}
        <section className="bg-white rounded-3xl border border-slate-200 p-8 md:p-12 shadow-sm">
          <div className="text-center max-w-2xl mx-auto mb-10">
            <h2 className="text-2xl font-bold text-slate-900 mb-2">How PriceWise Works</h2>
            <p className="text-xs text-slate-500">
              Our 5-stage comparison engine ensures you compare the exact identical model and pay the lowest price.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="space-y-3">
              <div className="w-10 h-10 rounded-xl bg-sky-50 text-sky-600 flex items-center justify-center font-bold text-sm">
                1
              </div>
              <h3 className="text-sm font-bold text-slate-900">Strict Variant Matching</h3>
              <p className="text-xs text-slate-500 leading-relaxed">
                We never match a 128 GB phone against a 256 GB model. Our engine uses GTIN, model numbers, and normalized specs to ensure 100% fair comparisons.
              </p>
            </div>

            <div className="space-y-3">
              <div className="w-10 h-10 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center font-bold text-sm">
                2
              </div>
              <h3 className="text-sm font-bold text-slate-900">Delivered Cost Transparency</h3>
              <p className="text-xs text-slate-500 leading-relaxed">
                A lower price isn't cheaper if shipping costs more. We compute total delivered cost (product + delivery) or clearly flag unknown charges.
              </p>
            </div>

            <div className="space-y-3">
              <div className="w-10 h-10 rounded-xl bg-teal-50 text-teal-600 flex items-center justify-center font-bold text-sm">
                3
              </div>
              <h3 className="text-sm font-bold text-slate-900">Data Freshness & Price Alerts</h3>
              <p className="text-xs text-slate-500 leading-relaxed">
                Every offer records its verification timestamp. Set target price alerts to automatically know when a product drops to your budget.
              </p>
            </div>
          </div>
        </section>
      </div>
    </div>
  );
}

