import React from 'react'

const Navbar = () => {
  return (
    <header className="bg-white border-b border-gray-200 shadow-sm sticky top-0 z-50">
      <div className="max-w-6xl mx-auto px-4 py-3 flex items-center justify-between">
        <h1 className="text-2xl font-bold text-orange-500">
          🚀 Snip<span className="text-yellow-500">Snap</span>
        </h1>
        {/* You can add links/buttons here later */}
      </div>
    </header>
  );
};

export default Navbar;