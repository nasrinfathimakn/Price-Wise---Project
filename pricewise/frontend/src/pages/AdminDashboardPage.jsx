import React, { useEffect, useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { api } from '../api/client';
import { useAuth } from '../context/AuthContext';
import AdminSidebar from '../components/AdminSidebar';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';
import {
  ShoppingBag,
  RefreshCw,
  CheckCircle,
  Plus,
  Clock,
  Shield,
  ExternalLink,
} from 'lucide-react';

export default function AdminDashboardPage() {
  const { user, isAdmin } = useAuth();
  const navigate = useNavigate();
  const [currentTab, setCurrentTab] = useState('overview');
  const [dashboard, setDashboard] = useState(null);
  const [retailers, setRetailers] = useState([]);
  const [updateLogs, setUpdateLogs] = useState([]);
  const [pendingMatches, setPendingMatches] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [syncingId, setSyncingId] = useState(null);
  const [actionMessage, setActionMessage] = useState(null);
  const [error, setError] = useState(null);

  // New Product Modal State
  const [showAddProductModal, setShowAddProductModal] = useState(false);
  const [newProduct, setNewProduct] = useState({
    name: '',
    brandId: 1,
    categoryId: 2,
    modelNumber: '',
    description: '',
    imageUrl: '',
    productIdentifier: '',
  });

  useEffect(() => {
    if (!isAdmin) {
      navigate('/login');
      return;
    }

    async function loadAdminData() {
      setLoading(true);
      setError(null);
      try {
        const [dashData, retData, logData, matchData, prodData] = await Promise.all([
          api.getAdminDashboard(),
          api.getAdminRetailers(),
          api.getAdminUpdateLogs(),
          api.getAdminPendingMatches(),
          api.searchProducts({ size: 50 }),
        ]);
        setDashboard(dashData);
        setRetailers(retData || []);
        setUpdateLogs(logData || []);
        setPendingMatches(matchData || []);
        setProducts(prodData?.results?.content || []);
      } catch (err) {
        setError(err.message || 'Failed to load administrator data');
      } finally {
        setLoading(false);
      }
    }
    loadAdminData();
  }, [isAdmin, navigate]);

  const handleTriggerSync = async (retailerId) => {
    setSyncingId(retailerId);
    setActionMessage(null);
    try {
      const res = await api.triggerRetailerSync(retailerId);
      setActionMessage(`Sync triggered for retailer #${retailerId}. Status: ${res.status}. Processed: ${res.offersProcessed ?? 0} offers.`);
      const updatedLogs = await api.getAdminUpdateLogs();
      setUpdateLogs(updatedLogs || []);
    } catch (err) {
      setError(err.message || 'Failed to trigger sync');
    } finally {
      setSyncingId(null);
    }
  };

  const handleResolveMatch = async (matchId, approve) => {
    try {
      await api.resolveProductMatch(matchId, approve);
      setPendingMatches((prev) => prev.filter((m) => m.offerId !== matchId));
      setActionMessage(approve ? 'Match approved and linked!' : 'Match rejected and flagged.');
    } catch (err) {
      setError(err.message || 'Failed to resolve match');
    }
  };

  const handleCreateProduct = async (e) => {
    e.preventDefault();
    try {
      await api.createProduct(newProduct);
      setShowAddProductModal(false);
      setActionMessage(`Canonical product "${newProduct.name}" created successfully!`);
      const [dashData, prodData] = await Promise.all([
        api.getAdminDashboard(),
        api.searchProducts({ size: 50 }),
      ]);
      setDashboard(dashData);
      setProducts(prodData?.results?.content || []);
      setNewProduct({
        name: '',
        brandId: 1,
        categoryId: 2,
        modelNumber: '',
        description: '',
        imageUrl: '',
        productIdentifier: '',
      });
    } catch (err) {
      setError(err.message || 'Failed to create product');
    }
  };

  if (loading) {
    return <LoadingSpinner message="Loading administrator cockpit..." />;
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-4 border-b border-slate-200">
        <div>
          <h1 className="text-2xl font-bold text-slate-900 flex items-center gap-2">
            <Shield className="w-6 h-6 text-teal-600" />
            PriceWise Operations Console
          </h1>
          <p className="text-xs text-slate-500 mt-1">
            Retailer integration monitoring, catalog synchronization & match disambiguation
          </p>
        </div>
        <button
          onClick={() => setShowAddProductModal(true)}
          className="inline-flex items-center gap-1.5 px-4 py-2 bg-sky-600 hover:bg-sky-700 text-white text-xs font-semibold rounded-xl shadow-xs transition"
        >
          <Plus className="w-4 h-4" />
          Add Canonical Product
        </button>
      </div>

      <ErrorMessage message={error} />
      {actionMessage && (
        <div className="p-3 bg-emerald-50 border border-emerald-200 text-emerald-800 text-xs rounded-xl flex items-center gap-2">
          <CheckCircle className="w-4 h-4 text-emerald-600 shrink-0" />
          <span>{actionMessage}</span>
        </div>
      )}

      <div className="flex flex-col md:flex-row gap-8 items-start">
        <AdminSidebar
          currentTab={currentTab}
          onSelectTab={setCurrentTab}
          pendingMatchesCount={pendingMatches.length}
        />

        {/* Tab Content */}
        <div className="flex-1 w-full space-y-6">
          {currentTab === 'overview' && (
            <div className="space-y-6">
              {/* Stat Cards */}
              <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
                <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-xs">
                  <span className="text-[11px] font-semibold text-slate-400 uppercase tracking-wide block mb-1">
                    Total Products
                  </span>
                  <span className="text-2xl font-bold text-slate-900">{dashboard?.totalProducts || 0}</span>
                </div>
                <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-xs">
                  <span className="text-[11px] font-semibold text-slate-400 uppercase tracking-wide block mb-1">
                    Tracked Offers
                  </span>
                  <span className="text-2xl font-bold text-sky-600">{dashboard?.totalOffers || 0}</span>
                </div>
                <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-xs">
                  <span className="text-[11px] font-semibold text-slate-400 uppercase tracking-wide block mb-1">
                    Active Retailers
                  </span>
                  <span className="text-2xl font-bold text-emerald-600">{dashboard?.activeRetailers || 0}</span>
                </div>
                <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-xs">
                  <span className="text-[11px] font-semibold text-slate-400 uppercase tracking-wide block mb-1">
                    Active Alerts
                  </span>
                  <span className="text-2xl font-bold text-teal-600">{dashboard?.activeAlerts || 0}</span>
                </div>
              </div>

              {/* Recent Sync Logs in Overview */}
              <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-xs">
                <h3 className="text-sm font-bold text-slate-900 mb-4 flex items-center gap-2">
                  <Clock className="w-4 h-4 text-sky-600" />
                  Recent Synchronization Runs
                </h3>
                <div className="overflow-x-auto">
                  <table className="w-full text-left text-xs">
                    <thead>
                      <tr className="border-b border-slate-100 text-slate-400 uppercase text-[10px]">
                        <th className="py-2.5">Retailer</th>
                        <th className="py-2.5">Started At</th>
                        <th className="py-2.5">Status</th>
                        <th className="py-2.5 text-right">Offers Processed</th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-slate-100">
                      {(dashboard?.recentUpdateLogs || []).map((log) => (
                        <tr key={log.id}>
                          <td className="py-2.5 font-semibold text-slate-800">{log.retailerName}</td>
                          <td className="py-2.5 text-slate-500">{new Date(log.startedAt).toLocaleString()}</td>
                          <td className="py-2.5">
                            <span className={`px-2 py-0.5 rounded-full text-[10px] font-bold ${
                              log.status === 'SUCCESS' ? 'bg-emerald-50 text-emerald-700' : 'bg-rose-50 text-rose-700'
                            }`}>
                              {log.status}
                            </span>
                          </td>
                          <td className="py-2.5 text-right font-medium text-slate-700">{log.offersProcessed}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          )}

          {currentTab === 'products' && (
            <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm space-y-4">
              <div className="flex items-center justify-between">
                <div>
                  <h3 className="text-sm font-bold text-slate-900 flex items-center gap-2">
                    <ShoppingBag className="w-4 h-4 text-sky-600" />
                    Canonical Products Catalog ({products.length})
                  </h3>
                  <p className="text-xs text-slate-500">Products currently monitored for multi-retailer price tracking</p>
                </div>
                <button
                  onClick={() => setShowAddProductModal(true)}
                  className="inline-flex items-center gap-1.5 px-3 py-1.5 bg-sky-600 hover:bg-sky-700 text-white text-xs font-semibold rounded-xl shadow-sm transition"
                >
                  <Plus className="w-3.5 h-3.5" />
                  Add Product
                </button>
              </div>

              {products.length === 0 ? (
                <p className="text-xs text-slate-500 py-6 text-center">No canonical products found in catalog.</p>
              ) : (
                <div className="overflow-x-auto">
                  <table className="w-full text-left text-xs">
                    <thead>
                      <tr className="border-b border-slate-100 text-slate-400 uppercase text-[10px]">
                        <th className="py-2.5">Product</th>
                        <th className="py-2.5">Brand / Category</th>
                        <th className="py-2.5">Model</th>
                        <th className="py-2.5 text-right">Lowest Price</th>
                        <th className="py-2.5 text-center">Offers</th>
                        <th className="py-2.5 text-right">Actions</th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-slate-100">
                      {products.map((p) => (
                        <tr key={p.id} className="hover:bg-slate-50/60">
                          <td className="py-2.5">
                            <div className="flex items-center gap-2.5">
                              <img
                                src={p.imageUrl || 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=100&q=80'}
                                alt={p.name}
                                className="w-8 h-8 rounded-lg object-contain bg-slate-50 border p-0.5 shrink-0"
                              />
                              <span className="font-semibold text-slate-800 line-clamp-1 max-w-xs">{p.name}</span>
                            </div>
                          </td>
                          <td className="py-2.5 text-slate-600">
                            <span className="font-medium">{p.brandName}</span>
                            <span className="text-slate-400 block text-[10px]">{p.categoryName}</span>
                          </td>
                          <td className="py-2.5 font-mono text-[11px] text-slate-500">{p.modelNumber || '—'}</td>
                          <td className="py-2.5 text-right font-bold text-emerald-600">
                            {p.lowestPrice ? `₹${Number(p.lowestPrice).toLocaleString('en-IN')}` : 'No offers'}
                          </td>
                          <td className="py-2.5 text-center">
                            <span className="px-2 py-0.5 rounded-full text-[10px] font-semibold bg-slate-100 text-slate-600">
                              {p.offerCount || 0}
                            </span>
                          </td>
                          <td className="py-2.5 text-right space-x-2">
                            <Link
                              to={`/products/${p.id}`}
                              className="text-sky-600 hover:text-sky-800 font-semibold"
                            >
                              View
                            </Link>
                            <Link
                              to={`/compare/${p.id}`}
                              className="text-emerald-600 hover:text-emerald-800 font-semibold inline-flex items-center gap-0.5"
                            >
                              Compare
                              <ExternalLink className="w-3 h-3" />
                            </Link>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </div>
          )}

          {currentTab === 'retailers' && (
            <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-xs space-y-4">
              <h3 className="text-sm font-bold text-slate-900">Supported Retailer Connectors</h3>
              <div className="divide-y divide-slate-100">
                {retailers.map((r) => (
                  <div key={r.id} className="py-4 flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                    <div>
                      <div className="flex items-center gap-2">
                        <span className="font-bold text-slate-900 text-sm">{r.name}</span>
                        <span className="font-mono text-[11px] bg-slate-100 px-2 py-0.5 rounded text-slate-600">
                          {r.code}
                        </span>
                        <span className="text-[11px] font-semibold px-2 py-0.5 rounded-full bg-sky-50 text-sky-700">
                          {r.integrationType}
                        </span>
                      </div>
                      <a href={r.websiteUrl} target="_blank" rel="noreferrer" className="text-xs text-sky-600 hover:underline">
                        {r.websiteUrl}
                      </a>
                    </div>

                    <button
                      onClick={() => handleTriggerSync(r.id)}
                      disabled={syncingId === r.id}
                      className="inline-flex items-center gap-1.5 px-3 py-1.5 bg-slate-100 hover:bg-slate-200 text-slate-700 text-xs font-semibold rounded-xl transition disabled:opacity-50"
                    >
                      <RefreshCw className={`w-3.5 h-3.5 ${syncingId === r.id ? 'animate-spin' : ''}`} />
                      {syncingId === r.id ? 'Syncing...' : 'Trigger Sync'}
                    </button>
                  </div>
                ))}
              </div>
            </div>
          )}

          {currentTab === 'matches' && (
            <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-xs space-y-4">
              <div className="flex items-center justify-between">
                <div>
                  <h3 className="text-sm font-bold text-slate-900">Uncertain Product Matches for Review</h3>
                  <p className="text-xs text-slate-500">Flagged by Tier-5 fuzzy matching below automatic threshold</p>
                </div>
                <span className="px-2.5 py-0.5 bg-amber-100 text-amber-800 text-xs font-bold rounded-full">
                  {pendingMatches.length} Pending
                </span>
              </div>

              {pendingMatches.length === 0 ? (
                <p className="text-xs text-slate-500 py-6 text-center">No ambiguous matches currently pending review.</p>
              ) : (
                <div className="space-y-4">
                  {pendingMatches.map((m) => (
                    <div key={m.offerId} className="p-4 bg-slate-50 rounded-xl border border-slate-200 text-xs space-y-2">
                      <div className="flex items-center justify-between">
                        <span className="font-bold text-slate-900">{m.retailerName}: {m.externalTitle}</span>
                        <span className="font-semibold text-amber-700">Confidence: {Math.round(m.confidenceScore * 100)}%</span>
                      </div>
                      <p className="text-slate-600">
                        Proposed Canonical Product: <strong className="text-slate-800">{m.proposedProductName}</strong>
                      </p>
                      <div className="flex items-center justify-end gap-2 pt-2 border-t border-slate-200">
                        <button
                          onClick={() => handleResolveMatch(m.offerId, true)}
                          className="px-3 py-1 bg-emerald-600 hover:bg-emerald-700 text-white font-semibold rounded-lg"
                        >
                          Approve Link
                        </button>
                        <button
                          onClick={() => handleResolveMatch(m.offerId, false)}
                          className="px-3 py-1 bg-slate-200 hover:bg-slate-300 text-slate-700 font-semibold rounded-lg"
                        >
                          Reject
                        </button>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}

          {currentTab === 'logs' && (
            <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-xs space-y-4">
              <h3 className="text-sm font-bold text-slate-900">Full Update History Logs</h3>
              <div className="overflow-x-auto">
                <table className="w-full text-left text-xs">
                  <thead>
                    <tr className="border-b border-slate-100 text-slate-400 uppercase text-[10px]">
                      <th className="py-2.5">Retailer</th>
                      <th className="py-2.5">Started At</th>
                      <th className="py-2.5">Completed At</th>
                      <th className="py-2.5">Status</th>
                      <th className="py-2.5">Processed</th>
                      <th className="py-2.5">Errors</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-100">
                    {updateLogs.map((log) => (
                      <tr key={log.id}>
                        <td className="py-2.5 font-semibold text-slate-800">{log.retailerName}</td>
                        <td className="py-2.5 text-slate-500">{new Date(log.startedAt).toLocaleString()}</td>
                        <td className="py-2.5 text-slate-500">{log.completedAt ? new Date(log.completedAt).toLocaleString() : 'Running'}</td>
                        <td className="py-2.5">
                          <span className={`px-2 py-0.5 rounded-full text-[10px] font-bold ${
                            log.status === 'SUCCESS' ? 'bg-emerald-50 text-emerald-700' : 'bg-rose-50 text-rose-700'
                          }`}>
                            {log.status}
                          </span>
                        </td>
                        <td className="py-2.5 font-medium">{log.offersProcessed}</td>
                        <td className="py-2.5 text-rose-600 truncate max-w-xs">{log.errorMessage || 'None'}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Add Product Modal */}
      {showAddProductModal && (
        <div className="fixed inset-0 z-50 bg-slate-900/40 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="bg-white rounded-2xl border border-slate-200 shadow-xl max-w-md w-full p-6 space-y-4">
            <h3 className="text-sm font-bold text-slate-900">Add Canonical Product</h3>
            <form onSubmit={handleCreateProduct} className="space-y-3 text-xs">
              <div>
                <label className="block font-semibold mb-1">Product Name</label>
                <input
                  type="text"
                  required
                  value={newProduct.name}
                  onChange={(e) => setNewProduct({ ...newProduct, name: e.target.value })}
                  placeholder="e.g. OnePlus 12 5G (256 GB)"
                  className="w-full px-3 py-2 border rounded-xl"
                />
              </div>
              <div className="grid grid-cols-2 gap-2">
                <div>
                  <label className="block font-semibold mb-1">Model Number</label>
                  <input
                    type="text"
                    value={newProduct.modelNumber}
                    onChange={(e) => setNewProduct({ ...newProduct, modelNumber: e.target.value })}
                    placeholder="e.g. CPH2581"
                    className="w-full px-3 py-2 border rounded-xl"
                  />
                </div>
                <div>
                  <label className="block font-semibold mb-1">GTIN / EAN</label>
                  <input
                    type="text"
                    value={newProduct.productIdentifier}
                    onChange={(e) => setNewProduct({ ...newProduct, productIdentifier: e.target.value })}
                    placeholder="e.g. 6921815626244"
                    className="w-full px-3 py-2 border rounded-xl"
                  />
                </div>
              </div>
              <div>
                <label className="block font-semibold mb-1">Image URL</label>
                <input
                  type="url"
                  value={newProduct.imageUrl}
                  onChange={(e) => setNewProduct({ ...newProduct, imageUrl: e.target.value })}
                  placeholder="https://..."
                  className="w-full px-3 py-2 border rounded-xl"
                />
              </div>
              <div>
                <label className="block font-semibold mb-1">Description</label>
                <textarea
                  value={newProduct.description}
                  onChange={(e) => setNewProduct({ ...newProduct, description: e.target.value })}
                  rows={2}
                  className="w-full px-3 py-2 border rounded-xl"
                />
              </div>
              <div className="flex justify-end gap-2 pt-2">
                <button
                  type="button"
                  onClick={() => setShowAddProductModal(false)}
                  className="px-3 py-1.5 text-slate-500"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-4 py-1.5 bg-sky-600 text-white font-bold rounded-xl"
                >
                  Create Product
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

