import React, { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { api } from '../api/client';
import SearchBar from '../components/SearchBar';
import ProductGrid from '../components/ProductGrid';
import BrandFilter from '../components/BrandFilter';
import PriceFilter from '../components/PriceFilter';
import SortDropdown from '../components/SortDropdown';
import Pagination from '../components/Pagination';
import CategoryList from '../components/CategoryList';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';
import { Filter, X } from 'lucide-react';

export default function SearchResultsPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const query = searchParams.get('q') || '';
  const brandId = searchParams.get('brand') ? Number(searchParams.get('brand')) : null;
  const categoryId = searchParams.get('category') ? Number(searchParams.get('category')) : null;
  const minPrice = searchParams.get('minPrice') ? Number(searchParams.get('minPrice')) : null;
  const maxPrice = searchParams.get('maxPrice') ? Number(searchParams.get('maxPrice')) : null;
  const sort = searchParams.get('sort') || 'relevance';
  const page = searchParams.get('page') ? Number(searchParams.get('page')) : 0;

  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [mobileFilterOpen, setMobileFilterOpen] = useState(false);

  useEffect(() => {
    async function executeSearch() {
      setLoading(true);
      setError(null);
      try {
        const res = await api.searchProducts({
          q: query,
          brand: brandId,
          category: categoryId,
          minPrice,
          maxPrice,
          sort,
          page,
          size: 12,
        });
        setData(res);
      } catch (err) {
        setError(err.message || 'Error occurred while searching products');
      } finally {
        setLoading(false);
      }
    }
    executeSearch();
  }, [query, brandId, categoryId, minPrice, maxPrice, sort, page]);

  const updateParam = (key, value) => {
    const next = new URLSearchParams(searchParams);
    if (value !== null && value !== undefined && value !== '') {
      next.set(key, value);
    } else {
      next.delete(key);
    }
    next.set('page', '0'); // reset to first page on filter change
    setSearchParams(next);
  };

  const handlePriceApply = (min, max) => {
    const next = new URLSearchParams(searchParams);
    if (min) next.set('minPrice', min); else next.delete('minPrice');
    if (max) next.set('maxPrice', max); else next.delete('maxPrice');
    next.set('page', '0');
    setSearchParams(next);
  };

  const clearAllFilters = () => {
    setSearchParams(query ? { q: query } : {});
  };

  const hasActiveFilters = brandId || categoryId || minPrice || maxPrice;

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-6">
      {/* Top Search Bar */}
      <div className="max-w-3xl mx-auto">
        <SearchBar initialQuery={query} />
      </div>

      {/* Categories chips */}
      {data?.availableCategories && (
        <div className="pt-2">
          <CategoryList
            categories={data.availableCategories}
            selectedId={categoryId}
            onSelect={(id) => updateParam('category', id)}
          />
        </div>
      )}

      {/* Main layout: Sidebar filters + Product grid */}
      <div className="flex flex-col md:flex-row gap-8 items-start pt-4">
        {/* Mobile Filter Toggle */}
        <div className="md:hidden w-full flex items-center justify-between pb-2 border-b border-slate-200">
          <button
            onClick={() => setMobileFilterOpen(true)}
            className="inline-flex items-center gap-1.5 px-3 py-2 bg-white border border-slate-200 rounded-xl text-xs font-semibold text-slate-700 shadow-xs"
          >
            <Filter className="w-4 h-4 text-sky-600" />
            Filters {hasActiveFilters && <span className="w-2 h-2 rounded-full bg-sky-600"></span>}
          </button>
          <SortDropdown value={sort} onChange={(val) => updateParam('sort', val)} />
        </div>

        {/* Sidebar Filters Desktop */}
        <aside className="hidden md:block w-64 bg-white border border-slate-200 rounded-2xl p-5 shadow-xs shrink-0 space-y-6 self-start">
          <div className="flex items-center justify-between pb-3 border-b border-slate-100">
            <h3 className="text-xs font-bold uppercase tracking-wider text-slate-900 flex items-center gap-1.5">
              <Filter className="w-3.5 h-3.5 text-sky-600" />
              Filter Products
            </h3>
            {hasActiveFilters && (
              <button
                onClick={clearAllFilters}
                className="text-[11px] font-semibold text-sky-600 hover:text-sky-800 transition"
              >
                Reset
              </button>
            )}
          </div>

          {/* Brand Filter */}
          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wide mb-2">Brand</label>
            <BrandFilter
              brands={data?.availableBrands || []}
              selectedBrandId={brandId}
              onSelect={(id) => updateParam('brand', id)}
            />
          </div>

          {/* Price Range Filter */}
          <div className="pt-4 border-t border-slate-100">
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wide mb-2">Price Range (₹)</label>
            <PriceFilter
              minPrice={minPrice}
              maxPrice={maxPrice}
              onApply={handlePriceApply}
            />
          </div>
        </aside>

        {/* Results Area */}
        <main className="flex-1 w-full space-y-4">
          <div className="hidden md:flex items-center justify-between pb-2 border-b border-slate-200">
            <p className="text-xs text-slate-500">
              {data?.results?.totalElements !== undefined ? (
                <>Found <span className="font-bold text-slate-900">{data.results.totalElements}</span> comparable products</>
              ) : 'Searching...'}
            </p>
            <SortDropdown value={sort} onChange={(val) => updateParam('sort', val)} />
          </div>

          <ErrorMessage message={error} />

          {loading ? (
            <LoadingSpinner message="Searching verified offers..." />
          ) : (
            <>
              <ProductGrid
                products={data?.results?.content || []}
                emptyTitle={query ? `No products matching "${query}"` : 'No products found'}
                emptyDescription="Try clearing filters, searching for a brand name, or verifying the model number."
              />

              <Pagination
                currentPage={data?.results?.pageNumber || 0}
                totalPages={data?.results?.totalPages || 0}
                onPageChange={(p) => updateParam('page', p)}
              />
            </>
          )}
        </main>
      </div>

      {/* Mobile filter slide-over */}
      {mobileFilterOpen && (
        <div className="fixed inset-0 z-50 bg-slate-900/40 backdrop-blur-sm flex justify-end md:hidden">
          <div className="w-80 max-w-full bg-white h-full p-6 space-y-6 overflow-y-auto">
            <div className="flex items-center justify-between pb-3 border-b border-slate-100">
              <h3 className="text-sm font-bold text-slate-900">Filters</h3>
              <button onClick={() => setMobileFilterOpen(false)} className="p-1 text-slate-400">
                <X className="w-5 h-5" />
              </button>
            </div>
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wide mb-2">Brand</label>
              <BrandFilter
                brands={data?.availableBrands || []}
                selectedBrandId={brandId}
                onSelect={(id) => { updateParam('brand', id); setMobileFilterOpen(false); }}
              />
            </div>
            <div className="pt-4 border-t border-slate-100">
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wide mb-2">Price Range (₹)</label>
              <PriceFilter
                minPrice={minPrice}
                maxPrice={maxPrice}
                onApply={(min, max) => { handlePriceApply(min, max); setMobileFilterOpen(false); }}
              />
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

