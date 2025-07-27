import { useState } from "react";
import ShortenForm from "./components/ShortenForm";
import ResultBox from "./components/ResultBox";
import Navbar from "./components/Navbar";
import StatsBox from "./components/StatsBox";

function App() {
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState("");
  const [stats, setStats] = useState(null);
  const [showStats, setShowStats] = useState(false);

  const handleShorten = async (formData) => {
    setLoading(true);
    setErrorMsg("");
    setResult(null);
    setStats(null);
    setShowStats(false);

    try {
      const response = await fetch("https://snipsnap-backend.onrender.com/shorten", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || "Failed to shorten URL");
      }

      const data = await response.json();
      const shortCode = data.shortCode;
      const shortUrl = `https://snipsnap-backend.onrender.com/${shortCode}`;
      const qrCodeUrl = data.qrPath ? `data:image/png;base64,${data.qrPath}` : null;
      const statsUrl = `https://snipsnap-backend.onrender.com/stats/${shortCode}`;

      setResult({ shortCode, shortUrl, qrCodeUrl, statsUrl });

      const statsRes = await fetch(statsUrl);
      if (statsRes.ok) {
        const statsData = await statsRes.json();
        setStats(statsData);
      }
    } catch (err) {
      setErrorMsg(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleViewStats = () => {
    setShowStats((prev) => !prev);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-orange-100 via-yellow-50 to-white flex flex-col justify-between">
      <div>
        <Navbar />

        <div className="max-w-2xl mx-auto px-4 py-10">
          <ShortenForm onShorten={handleShorten} />

          {loading && (
            <p className="text-center text-orange-600 mt-6 animate-pulse text-lg">
              ⏳ Generating your short URL...
            </p>
          )}

          {errorMsg && (
            <div className="mt-6 text-center text-red-600 font-semibold border border-red-300 bg-red-50 py-3 px-4 rounded-2xl shadow-md">
              ❌ {errorMsg}
            </div>
          )}

          {result && (
            <>
              <ResultBox {...result} onViewStats={handleViewStats} />
              {showStats && stats && <StatsBox stats={stats} />}
            </>
          )}
        </div>
      </div>

      <footer className="text-center text-sm text-gray-500 py-6">
        Made with ❤️ by <a href="https://github.com/GopikasriRamesh" target="_blank" rel="noreferrer" className="underline text-orange-500 hover:text-orange-600">Gopika Sri</a>
      </footer>

      <style>
        {`
          @keyframes fadeIn {
            0% { opacity: 0; transform: translateY(10px); }
            100% { opacity: 1; transform: translateY(0); }
          }
        `}
      </style>
    </div>
  );
}

export default App;