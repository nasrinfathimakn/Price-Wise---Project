// PriceWise REST API Client Module
const API_BASE_URL = '/api/v1';

async function request(endpoint, options = {}) {
  const token = localStorage.getItem('pricewise_token');
  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...options.headers,
  };

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers,
  });

  const contentType = response.headers.get('content-type');
  let data = null;
  if (contentType && contentType.includes('application/json')) {
    data = await response.json();
  } else {
    data = await response.text();
  }

  if (!response.ok) {
    const errorMsg = data?.message || data?.error || 'An error occurred while communicating with the server';
    const error = new Error(errorMsg);
    error.status = response.status;
    error.details = data?.details || [];
    throw error;
  }

  return data;
}

export const api = {
  // Public product search & details
  searchProducts: (params = {}) => {
    const query = new URLSearchParams();
    if (params.q) query.append('q', params.q);
    if (params.brand) query.append('brand', params.brand);
    if (params.category) query.append('category', params.category);
    if (params.minPrice) query.append('minPrice', params.minPrice);
    if (params.maxPrice) query.append('maxPrice', params.maxPrice);
    if (params.sort) query.append('sort', params.sort);
    if (params.page !== undefined) query.append('page', params.page);
    if (params.size !== undefined) query.append('size', params.size);
    return request(`/products?${query.toString()}`);
  },

  getProduct: (id) => request(`/products/${id}`),
  getProductSpecifications: (id) => request(`/products/${id}/specifications`),
  getRelatedProducts: (id) => request(`/products/${id}/related`),
  getSearchSuggestions: (q) => request(`/search/suggestions?q=${encodeURIComponent(q)}`),

  // Comparison & Price history
  getProductComparison: (id) => request(`/products/${id}/comparison`),
  getProductOffers: (id) => request(`/products/${id}/offers`),
  getPriceHistory: (id) => request(`/products/${id}/price-history`),

  // Categories
  getCategories: () => request('/categories'),
  getCategoryProducts: (id, params = {}) => {
    const query = new URLSearchParams(params);
    return request(`/categories/${id}/products?${query.toString()}`);
  },

  // Authentication
  login: (credentials) => request('/auth/login', { method: 'POST', body: JSON.stringify(credentials) }),
  register: (userData) => request('/auth/register', { method: 'POST', body: JSON.stringify(userData) }),
  logout: () => request('/auth/logout', { method: 'POST' }),
  getCurrentUser: () => request('/auth/me'),

  // User Watchlist
  getWatchlist: () => request('/watchlist'),
  addToWatchlist: (productId) => request('/watchlist', { method: 'POST', body: JSON.stringify({ productId }) }),
  removeFromWatchlist: (productId) => request(`/watchlist/${productId}`, { method: 'DELETE' }),

  // Price Alerts
  getPriceAlerts: () => request('/price-alerts'),
  createPriceAlert: (alertData) => request('/price-alerts', { method: 'POST', body: JSON.stringify(alertData) }),
  togglePriceAlert: (alertId, active) => request(`/price-alerts/${alertId}`, { method: 'PATCH', body: JSON.stringify({ active }) }),
  deletePriceAlert: (alertId) => request(`/price-alerts/${alertId}`, { method: 'DELETE' }),

  // Admin APIs
  getAdminDashboard: () => request('/admin/dashboard'),
  getAdminRetailers: () => request('/admin/retailers'),
  triggerRetailerSync: (retailerId) => request(`/admin/retailers/${retailerId}/sync`, { method: 'POST' }),
  getAdminUpdateLogs: () => request('/admin/update-logs'),
  getAdminPendingMatches: () => request('/admin/product-matches/review'),
  resolveProductMatch: (matchId, approve) => request(`/admin/product-matches/${matchId}/resolve`, {
    method: 'POST',
    body: JSON.stringify({ approve }),
  }),
  createProduct: (productData) => request('/admin/products', { method: 'POST', body: JSON.stringify(productData) }),
  updateProduct: (id, productData) => request(`/admin/products/${id}`, { method: 'PATCH', body: JSON.stringify(productData) }),
};

