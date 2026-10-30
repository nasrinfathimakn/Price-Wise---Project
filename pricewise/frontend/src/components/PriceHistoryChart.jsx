import React from 'react';
import {
  ResponsiveContainer,
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
} from 'recharts';
import { TrendingDown, ArrowDownRight, ArrowUpRight, Clock } from 'lucide-react';

export default function PriceHistoryChart({ priceHistory }) {
  if (!priceHistory || !priceHistory.isHistoryAvailable || !priceHistory.observations || priceHistory.observations.length === 0) {
    return (
      <div className="p-8 text-center bg-white rounded-2xl border border-slate-200">
        <Clock className="w-8 h-8 text-slate-300 mx-auto mb-2" />
        <h4 className="text-sm font-semibold text-slate-700 mb-1">Price history is not available yet.</h4>
        <p className="text-xs text-slate-400">
          Historical observations will automatically populate as periodic price snapshots are verified.
        </p>
      </div>
    );
  }

  const formatINR = (val) =>
    val != null
      ? new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(val)
      : '₹0';

  const chartData = priceHistory.observations.map((item) => ({
    date: item.formattedDate || new Date(item.observedAt).toLocaleDateString(),
    price: Number(item.price),
    retailer: item.retailerName,
  }));

  return (
    <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between pb-4 mb-6 border-b border-slate-100 gap-4">
        <div>
          <h3 className="text-base font-bold text-slate-900 flex items-center gap-2">
            <TrendingDown className="w-5 h-5 text-sky-600" />
            Price History Trends
          </h3>
          <p className="text-xs text-slate-500">Verified price movements recorded over time</p>
        </div>

        {/* Stats Row */}
        <div className="flex items-center gap-4 text-xs">
          <div className="p-2.5 rounded-xl bg-emerald-50 border border-emerald-100">
            <span className="text-[10px] text-emerald-600 block font-medium">Lowest Recorded</span>
            <span className="font-bold text-emerald-700 flex items-center gap-0.5">
              <ArrowDownRight className="w-3.5 h-3.5" />
              {formatINR(priceHistory.lowestRecordedPrice)}
            </span>
          </div>

          <div className="p-2.5 rounded-xl bg-rose-50 border border-rose-100">
            <span className="text-[10px] text-rose-600 block font-medium">Highest Recorded</span>
            <span className="font-bold text-rose-700 flex items-center gap-0.5">
              <ArrowUpRight className="w-3.5 h-3.5" />
              {formatINR(priceHistory.highestRecordedPrice)}
            </span>
          </div>

          <div className="p-2.5 rounded-xl bg-sky-50 border border-sky-100">
            <span className="text-[10px] text-sky-600 block font-medium">Latest Price</span>
            <span className="font-bold text-sky-700">
              {formatINR(priceHistory.latestRecordedPrice)}
            </span>
          </div>
        </div>
      </div>

      <div className="h-64 w-full">
        <ResponsiveContainer width="100%" height="100%">
          <LineChart data={chartData} margin={{ top: 10, right: 20, left: 20, bottom: 5 }}>
            <CartesianGrid strokeDasharray="3 3" stroke="#f1f5f9" />
            <XAxis dataKey="date" stroke="#94a3b8" fontSize={11} tickLine={false} />
            <YAxis
              stroke="#94a3b8"
              fontSize={11}
              tickLine={false}
              tickFormatter={(v) => `₹${v / 1000}k`}
              domain={['dataMin - 1000', 'dataMax + 1000']}
            />
            <Tooltip
              content={({ active, payload }) => {
                if (active && payload && payload.length) {
                  const data = payload[0].payload;
                  return (
                    <div className="bg-slate-900 text-white text-xs p-3 rounded-xl shadow-lg border border-slate-800 space-y-1">
                      <p className="font-medium text-slate-300">{data.date}</p>
                      <p className="text-emerald-400 font-bold text-sm">{formatINR(data.price)}</p>
                      <p className="text-slate-400 text-[11px]">{data.retailer}</p>
                    </div>
                  );
                }
                return null;
              }}
            />
            <Line
              type="monotone"
              dataKey="price"
              name="Price"
              stroke="#0284c7"
              strokeWidth={2.5}
              dot={{ r: 4, fill: '#0284c7', strokeWidth: 2, stroke: '#fff' }}
              activeDot={{ r: 6, fill: '#0369a1' }}
            />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}

