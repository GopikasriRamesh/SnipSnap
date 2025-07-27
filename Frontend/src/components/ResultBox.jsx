import { useState } from "react";

const ResultBox = ({ shortCode, shortUrl, qrCodeUrl, statsUrl, onViewStats }) => {
  const [copied, setCopied] = useState(false);

  const handleCopy = () => {
    navigator.clipboard.writeText(shortUrl);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="bg-white border border-green-300 p-6 rounded-2xl shadow-md w-full max-w-xl mx-auto mt-6 space-y-4">
      <h2 className="text-lg font-semibold text-green-800">✅ Short URL Created!</h2>

      <div className="flex items-center justify-between bg-green-50 px-4 py-3 rounded-xl border border-gray-300">
        <a href={shortUrl} target="_blank" rel="noopener noreferrer" className="text-blue-600 underline break-all">
          {shortCode}
        </a>
        <button
          onClick={handleCopy}
          className="ml-4 text-sm bg-orange-500 hover:bg-orange-600 text-white py-1 px-3 rounded-xl"
        >
          {copied ? "Copied!" : "Copy"}
        </button>
      </div>

      {qrCodeUrl && (
        <div className="flex flex-col items-start space-y-2">
          <img src={qrCodeUrl} alt="QR Code" className="w-40 h-40 border rounded-xl" />
          <a
            href={qrCodeUrl}
            download={`snipsnap_qr_${shortCode}.png`}
            className="text-sm text-blue-600 underline"
          >
            ⬇ Download QR Code
          </a>
        </div>
      )}

      {statsUrl && (
        <button
          onClick={onViewStats}
          className="text-sm text-white bg-orange-500 hover:bg-orange-600 px-3 py-2 rounded-lg transition"
        >
          📊 View URL Stats
        </button>
      )}
    </div>
  );
};

export default ResultBox;
