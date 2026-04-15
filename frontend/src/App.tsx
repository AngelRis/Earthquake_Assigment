import { useEffect, useState } from "react";
import type { Earthquake } from "./types/Earthquake";
import { getEarthquakes, deleteEarthquake } from "./services/earthquakeService";
import { EarthquakeTable } from "./components/EarthquakeTable";
import { MapView } from "./components/MapView";

function App() {
  const [data, setData] = useState<Earthquake[]>([]);
  const [error, setError] = useState<string | null>(null);

  const loadData = async () => {
    try {
      const res = await getEarthquakes();
      setData(res);
      setError(null);
    } catch (e: any) {
      setError(e.message || "Failed to load data");
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleDelete = async (id: number) => {
    try {
      const res = await deleteEarthquake(id);
      setData(res);
      setError(null);
    } catch (e: any) {
      setError(e.message || "Failed to delete earthquake");
    }
  };

  return (
    <div className="container py-5">
      <h1 className="text-center fw-bold text-primary mb-4">
        🌍 Earthquake Dashboard
      </h1>

      {error && (
        <div className="alert alert-danger shadow-sm">{error}</div>
      )}

      <div className="card shadow-sm mb-4">
        <div className="card-body">
          <EarthquakeTable data={data} onDelete={handleDelete} />
        </div>
      </div>

      <div className="card shadow-sm">
        <div className="card-body p-0">
          <MapView data={data} />
        </div>
      </div>
    </div>
  );
}

export default App;