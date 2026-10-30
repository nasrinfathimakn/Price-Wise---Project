import React from 'react';
import { Tag, Shield, Zap, Sparkles } from 'lucide-react';

export default function Footer() {
  return (
    <footer className="bg-white border-t border-slate-200 mt-20">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8 mb-8">
          <div className="space-y-3">
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 rounded-lg bg-sky-600 flex items-center justify-center text-white">
                <Tag className="w-4 h-4" />
              </div>
              <span className="text-lg font-bold text-slate-800">PriceWise</span>
            </div>
            <p className="text-xs text-slate-500 leading-relaxed">
              Smart price comparison across India's top retailers (Amazon, Flipkart, Croma, Reliance Digital). Transparent delivery fees and strict variant verification.
            </p>
          </div>

          <div>
            <h4 className="text-xs font-semibold uppercase tracking-wider text-slate-900 mb-3">Popular Categories</h4>
            <ul className="space-y-2 text-xs text-slate-600">
              <li><a href="/search?category=2" className="hover:text-sky-600 transition">Smartphones</a></li>
              <li><a href="/search?category=3" className="hover:text-sky-600 transition">Laptops</a></li>
              <li><a href="/search?category=4" className="hover:text-sky-600 transition">Headphones & Audio</a></li>
              <li><a href="/search?category=5" className="hover:text-sky-600 transition">Smartwatches</a></li>
            </ul>
          </div>

          <div>
            <h4 className="text-xs font-semibold uppercase tracking-wider text-slate-900 mb-3">Supported Retailers</h4>
            <ul className="space-y-2 text-xs text-slate-600">
              <li>Amazon India</li>
              <li>Flipkart</li>
              <li>Croma Electronics</li>
              <li>Reliance Digital</li>
            </ul>
          </div>

          <div>
            <h4 className="text-xs font-semibold uppercase tracking-wider text-slate-900 mb-3">Verification Standards</h4>
            <p className="text-xs text-slate-500 leading-relaxed mb-2">
              Never combines differing RAM or storage variants. Stale offers flagged with clear timestamps.
            </p>
            <div className="flex items-center gap-2 text-xs text-emerald-700 font-medium">
              <Shield className="w-4 h-4" />
              <span>Independent & Fair</span>
            </div>
          </div>
        </div>

        <div className="border-t border-slate-100 pt-6 flex flex-col sm:flex-row items-center justify-between text-xs text-slate-500">
          <p>© {new Date().getFullYear()} PriceWise Inc. All product trademarks belong to their respective owners.</p>
          <p className="mt-2 sm:mt-0">Built with Spring Boot 3 & React</p>
        </div>
      </div>
    </footer>
  );
}

