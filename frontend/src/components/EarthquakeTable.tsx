import type { Earthquake } from "../types/Earthquake";

type Props = {
  data: Earthquake[];
  onDelete: (id: number) => void;
};

export const EarthquakeTable = ({ data, onDelete }: Props) => {
  return (
    <div className="table-responsive">
      <table className="table table-hover align-middle mb-0">
        <thead className="table-dark">
          <tr>
            <th>Mag</th>
            <th>Type</th>
            <th>Title</th>
            <th>Place</th>
            <th>Time</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {data.length === 0 ? (
            <tr>
              <td colSpan={6} className="text-center text-muted py-4">
                No earthquake data available
              </td>
            </tr>
          ) : (
            data.map((eq) => (
              <tr key={eq.id}>
                <td className="fw-bold text-primary">
                  {eq.magnitude}
                </td>
                <td>{eq.magType}</td>
                <td className="text-truncate" style={{ maxWidth: "200px" }}>
                  {eq.title}
                </td>
                <td>{eq.place}</td>
                <td className="text-muted small">
                  {new Date(eq.time).toLocaleString()}
                </td>
                <td className="text-end">
                  <button
                    className="btn btn-sm btn-outline-danger"
                    onClick={() => onDelete(eq.id)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};