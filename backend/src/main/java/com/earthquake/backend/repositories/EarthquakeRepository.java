package com.earthquake.backend.repositories;

import com.earthquake.backend.models.Earthquake;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;


public interface EarthquakeRepository extends JpaRepository<Earthquake, Long> {
    List<Earthquake> findByMagnitudeGreaterThan(Double magnitudeIsGreaterThan);
    List<Earthquake> findByTimeAfter(Instant time);
    List<Earthquake> findByMagnitudeGreaterThanAndTimeAfter(Double magnitudeIsGreaterThan, Instant time);

}
