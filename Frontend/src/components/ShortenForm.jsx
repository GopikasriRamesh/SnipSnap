import { useState } from "react";
import { FiLink, FiCode, FiClock, FiCheckCircle } from "react-icons/fi";

const ShortenForm = ({ onShorten }) => {
  const [originalUrl, setOriginalUrl] = useState("");
  const [customCode, setCustomCode] = useState("");
  const [expiryDays, setExpiryDays] = useState("");
  const [generateQR, setGenerateQR] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!originalUrl.trim()) {
      alert("Please enter a valid URL.");
      return;
    }

    const payload = {
      originalUrl,
      customCode: customCode || null,
      expiryDays: expiryDays || null,
      generateQR,
    };

    onShorten(payload);
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="bg-gradient-to-br from-yellow-50 to-orange-100 p-8 rounded-3xl shadow-2xl w-full max-w-xl mx-auto mt-10 space-y-6"
    >
      <h2 className="text-2xl font-extrabold text-orange-700 flex items-center gap-2">
        <FiLink className="text-orange-500" /> Shorten a URL
      </h2>

      <div className="relative">
        <input
          type="url"
          placeholder="Enter your long URL"
          value={originalUrl}
          onChange={(e) => setOriginalUrl(e.target.value)}
          className="w-full border border-orange-300 bg-white rounded-xl p-4 pl-12 focus:ring-2 focus:ring-orange-400 outline-none"
          required
        />
        <FiLink className="absolute top-1/2 left-4 -translate-y-1/2 text-orange-400 text-lg" />
      </div>

      <div className="relative">
        <input
          type="text"
          placeholder="Optional custom short code"
          value={customCode}
          onChange={(e) => setCustomCode(e.target.value)}
          className="w-full border border-orange-300 bg-white rounded-xl p-4 pl-12"
        />
        <FiCode className="absolute top-1/2 left-4 -translate-y-1/2 text-orange-400 text-lg" />
      </div>

      <div className="relative">
        <input
          type="number"
          placeholder="Expiry in days"
          value={expiryDays}
          onChange={(e) => setExpiryDays(e.target.value)}
          className="w-full border border-orange-300 bg-white rounded-xl p-4 pl-12"
        />
        <FiClock className="absolute top-1/2 left-4 -translate-y-1/2 text-orange-400 text-lg" />
      </div>

      {/* <label className="flex items-center space-x-3 text-gray-700 font-medium">
        <input
          type="checkbox"
          checked={generateQR}
          onChange={() => setGenerateQR(!generateQR)}
          className="accent-orange-600 w-5 h-5"
        />
        <span className="flex items-center gap-1">
          <FiCheckCircle /> Generate QR Code
        </span>
      </label> */}

      <button
        type="submit"
        className="w-full bg-orange-500 hover:bg-orange-600 text-white font-bold text-base py-3 rounded-xl shadow-lg transition-all duration-200"
      >
        Shorten Now
      </button>
    </form>
  );
};

export default ShortenForm;