import React from "react";

const StatsBox = ({ stats }) => {
  return (
    <div className="bg-white border border-green-300 p-6 rounded-2xl shadow-md w-full max-w-xl mx-auto mt-4 space-y-2 animate-fadeIn">
      <h4 className="text-lg font-semibold text-green-800">📊 Link Statistics</h4>

      <div>
        <span className="font-medium text-orange-700">Original:</span>{" "}
        <a href={stats.originalUrl} target="_blank" rel="noreferrer" className="text-blue-600 underline break-all">{stats.originalUrl}</a>
      </div>

      <div>
        <span className="font-medium text-orange-700">Code:</span> {stats.shortCode}
      </div>

      <div>
        <span className="font-medium text-orange-700">Created:</span> {new Date(stats.createdAt).toLocaleString()}
      </div>

      <div>
        <span className="font-medium text-orange-700">Expires:</span> {stats.expiryDate ? new Date(stats.expiryDate).toLocaleString() : "No Expiry"}
      </div>

      <div>
        <span className="font-medium text-orange-700">Clicks:</span> {stats.clickCount}
      </div>
    </div>
  );
};

export default StatsBox;
