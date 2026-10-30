import React from 'react';
import { LayoutDashboard, ShoppingBag, Globe, History, AlertCircle } from 'lucide-react';

export default function AdminSidebar({ currentTab = 'overview', onSelectTab, pendingMatchesCount = 0 }) {
  const tabs = [
    { id: 'overview', label: 'Platform Overview', icon: LayoutDashboard },
    { id: 'products', label: 'Manage Products', icon: ShoppingBag },
    { id: 'retailers', label: 'Retailer Connectors', icon: Globe },
    { id: 'matches', label: 'Match Review', icon: AlertCircle, count: pendingMatchesCount },
    { id: 'logs', label: 'Update Logs', icon: History },
  ];

  return (
    <aside className="w-full md:w-64 bg-white border border-slate-200 rounded-2xl p-4 shadow-xs shrink-0 self-start">
      <div className="pb-3 mb-3 border-b border-slate-100">
        <span className="text-xs font-bold uppercase tracking-wider text-slate-400">Admin Control</span>
      </div>
      <nav className="space-y-1">
        {tabs.map((tab) => {
          const Icon = tab.icon;
          const isActive = currentTab === tab.id;
          return (
            <button
              key={tab.id}
              onClick={() => onSelectTab(tab.id)}
              className={`w-full flex items-center justify-between px-3 py-2.5 rounded-xl text-xs font-semibold transition ${
                isActive
                  ? 'bg-sky-50 text-sky-700'
                  : 'text-slate-600 hover:bg-slate-50 hover:text-slate-900'
              }`}
            >
              <div className="flex items-center gap-2.5">
                <Icon className={`w-4 h-4 ${isActive ? 'text-sky-600' : 'text-slate-400'}`} />
                <span>{tab.label}</span>
              </div>
              {tab.count > 0 && (
                <span className="px-1.5 py-0.5 rounded-full text-[10px] font-bold bg-amber-100 text-amber-700">
                  {tab.count}
                </span>
              )}
            </button>
          );
        })}
      </nav>
    </aside>
  );
}

