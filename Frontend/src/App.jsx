// src/App.jsx
import { useState } from "react";
import ShortenForm from "./components/ShortenForm";
import ResultBox from "./components/ResultBox";

function App() {
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState("");

  const handleShorten = async (formData) => {
    setLoading(true);
    setErrorMsg("");
    setResult(null);

    try {
      const response = await fetch("https://snipsnap-backend.onrender.com/shorten", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Something went wrong");
      }

      const data = await response.json();

      const shortUrl = `https://snipsnap-backend.onrender.com/${data.shortCode}`; // ✅ used for redirection
      const statsUrl = `https://snipsnap-backend.onrender.com/stats/${data.shortCode}`;
      const qrCodeUrl = data.qrPath ? `data:image/png;base64,${data.qrPath}` : null; // ✅ show QR

      setResult({ shortUrl, qrCodeUrl, statsUrl });
    } catch (err) {
      setErrorMsg(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="max-w-2xl mx-auto">
        <h1 className="text-3xl font-bold text-center text-gray-800 mb-6">
          🔗 SnipSnap URL Shortener
        </h1>

        <ShortenForm onShorten={handleShorten} />

        {loading && (
          <p className="text-center text-blue-600 mt-4">⏳ Generating your short URL...</p>
        )}

        {errorMsg && (
          <p className="text-center text-red-600 mt-4">❌ {errorMsg}</p>
        )}

        {result && (
          <ResultBox
            shortUrl={result.shortUrl}
            qrCodeUrl={result.qrCodeUrl}
            statsUrl={result.statsUrl}
          />
        )}
      </div>
    </div>
  );
}

export default App;
