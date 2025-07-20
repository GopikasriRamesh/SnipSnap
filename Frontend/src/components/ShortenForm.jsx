import { useState } from "react";

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

    onShorten(payload); // ✅ correctly call parent handler
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="bg-white p-6 rounded-2xl shadow-md w-full max-w-xl mx-auto mt-10 space-y-5"
    >
      <h2 className="text-xl font-semibold text-gray-800">Shorten your URL 🔗</h2>

      <input
        type="url"
        placeholder="Enter your long URL"
        value={originalUrl}
        onChange={(e) => setOriginalUrl(e.target.value)}
        className="w-full border border-gray-300 rounded-xl p-3 focus:outline-none focus:ring-2 focus:ring-orange-500"
        required
      />

      <input
        type="text"
        placeholder="Optional custom short code (e.g. mylink123)"
        value={customCode}
        onChange={(e) => setCustomCode(e.target.value)}
        className="w-full border border-gray-300 rounded-xl p-3"
      />

      <input
        type="number"
        placeholder="Expiry in days (e.g. 30)"
        value={expiryDays}
        onChange={(e) => setExpiryDays(e.target.value)}
        className="w-full border border-gray-300 rounded-xl p-3"
      />

      <label className="flex items-center space-x-2 text-gray-700">
        <input
          type="checkbox"
          checked={generateQR}
          onChange={() => setGenerateQR(!generateQR)}
        />
        <span>Generate QR Code</span>
      </label>

      <button
        type="submit"
        className="w-full bg-orange-500 hover:bg-orange-600 text-white font-medium py-3 rounded-xl transition"
      >
        Shorten URL
      </button>
    </form>
  );
};

export default ShortenForm;
