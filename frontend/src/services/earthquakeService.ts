import axios from "axios";
import type { Earthquake } from "../types/Earthquake";

const API_URL = "http://localhost:8080/api/earthquakes";

export const getEarthquakes = async (): Promise<Earthquake[]> => {
  try {
    const res = await axios.get(API_URL);
    return res.data;
  } catch (err: any) {
    throw err.response?.data || "Error fetching earthquakes";
  }
};

export const deleteEarthquake = async (id: number): Promise<Earthquake[]> => {
  try {
    const res = await axios.delete(`${API_URL}/deleteEarthquake/${id}`);
    return res.data;
  } catch (err: any) {
    throw err.response?.data || "Error deleting earthquake";
  }
};