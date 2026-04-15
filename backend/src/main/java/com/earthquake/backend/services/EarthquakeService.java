package com.earthquake.backend.services;

import com.earthquake.backend.dtos.EarthquakeDTO;

import java.time.Instant;
import java.util.List;

public interface EarthquakeService {
    void fetchAndStoreEarthquakes();
    void deleteEarthquakeById(Long id);
    List<EarthquakeDTO> filterEarthquakes(Double minMagnitude, Instant afterTime);
}
