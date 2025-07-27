import React from 'react';
import { AiFillLinkedin, AiFillInstagram, AiFillGithub } from 'react-icons/ai';

const Navbar = () => {
  return (
    <header className="bg-gradient-to-r from-orange-400 to-yellow-300 shadow-md sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-black text-white tracking-wide hover:opacity-90 transition">
            🚀 Snip<span className="text-gray-900">Snap</span>
          </h1>
          <p className="text-sm text-white mt-1 font-medium">Smart, Fast & Clean URL Shortener</p>
        </div>

        <div className="flex items-center space-x-4 text-white text-2xl">
          <a href="https://www.linkedin.com/in/gopika-sri-" target="_blank" rel="noreferrer" className="hover:text-blue-900 transition">
            <AiFillLinkedin />
          </a>
          <a href="https://www.instagram.com/me_srii_" target="_blank" rel="noreferrer" className="hover:text-pink-600 transition">
            <AiFillInstagram />
          </a>
          <a href="https://github.com/GopikasriRamesh" target="_blank" rel="noreferrer" className="hover:text-black transition">
            <AiFillGithub />
          </a>
        </div>
      </div>
    </header>
  );
};

export default Navbar;