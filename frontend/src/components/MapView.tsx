import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import type { Earthquake } from "../types/Earthquake";

type Props = {
  data: Earthquake[];
};

export const MapView = ({ data }: Props) => {
  return (
    <MapContainer
      center={[35.9981, 21.4254]}
      zoom={1}
      style={{
        height: "400px",
        width: "100%",
        borderRadius: "12px",
      }}
    >
      <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

      {data.map((eq) => (
        <Marker key={eq.id} position={[eq.latitude, eq.longitude]}>
          <Popup>
            <strong>{eq.title}</strong>
            <br />
            {eq.place}
            <br />
            <span className="text-primary">
              Magnitude: {eq.magnitude} ({eq.magType})
            </span>
          </Popup>
        </Marker>
      ))}
    </MapContainer>
  );
};