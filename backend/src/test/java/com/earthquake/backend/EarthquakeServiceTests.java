package com.earthquake.backend;

import com.earthquake.backend.dtos.EarthquakeDTO;
import com.earthquake.backend.exceptions.ResourceNotFoundException;
import com.earthquake.backend.models.Earthquake;
import com.earthquake.backend.repositories.EarthquakeRepository;
import com.earthquake.backend.services.EarthquakeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EarthquakeServiceTests {

    @Autowired
    private EarthquakeRepository earthquakeRepository;

    @Autowired
    private EarthquakeService earthquakeService;


    @BeforeEach
    void setUp() {
        earthquakeRepository.deleteAll();
    }

    @Test
    void testDeleteEarthquakeById() {
        Earthquake eq = Earthquake.builder()
                .magnitude(4.0)
                .magType("ml")
                .place("Delete Test")
                .title("Delete Me")
                .time(Instant.now())
                .latitude(10.0)
                .longitude(20.0)
                .externalId("del123")
                .build();

        eq = earthquakeRepository.save(eq);

        earthquakeService.deleteEarthquakeById(eq.getId());
        assertFalse(earthquakeRepository.existsById(eq.getId()));
    }

    @Test
    void testDeleteEarthquakeById_NotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> earthquakeService.deleteEarthquakeById(999L));
    }

    @Test
    void testFilterEarthquakesByMagnitude() {
        Earthquake eq = Earthquake.builder()
                .magnitude(4.5)
                .magType("ml")
                .place("Filter Test")
                .title("Strong Earthquake")
                .time(Instant.now())
                .latitude(15.0)
                .longitude(25.0)
                .externalId("flt123")
                .build();

        earthquakeRepository.save(eq);

        List<EarthquakeDTO> result = earthquakeService.filterEarthquakes(4.0, null);
        assertTrue(result.stream().anyMatch(e -> e.getTitle().equals("Strong Earthquake")));
    }

    @Test
    void testFilterEarthquakesByTime() {
        Earthquake oldEq = Earthquake.builder()
                .magnitude(3.0)
                .magType("ml")
                .place("Old EQ")
                .title("Old Earthquake")
                .time(Instant.now().minusSeconds(3600))
                .latitude(12.0)
                .longitude(22.0)
                .externalId("old123")
                .build();

        Earthquake newEq = Earthquake.builder()
                .magnitude(3.0)
                .magType("ml")
                .place("New EQ")
                .title("New Earthquake")
                .time(Instant.now())
                .latitude(13.0)
                .longitude(23.0)
                .externalId("new123")
                .build();

        earthquakeRepository.saveAll(List.of(oldEq, newEq));

        List<EarthquakeDTO> result = earthquakeService.filterEarthquakes(null, Instant.now().minusSeconds(1800));
        assertTrue(result.stream().anyMatch(e -> e.getTitle().equals("New Earthquake")));
        assertFalse(result.stream().anyMatch(e -> e.getTitle().equals("Old Earthquake")));
    }

    @Test
    void testFilterEarthquakes_NoCriteria() {
        Earthquake eq = Earthquake.builder()
                .magnitude(2.5)
                .magType("ml")
                .place("No Criteria EQ")
                .title("EQ")
                .time(Instant.now())
                .latitude(14.0)
                .longitude(24.0)
                .externalId("nc123")
                .build();

        earthquakeRepository.save(eq);

        List<EarthquakeDTO> result = earthquakeService.filterEarthquakes(null, null);
        assertEquals(1, result.size());
    }

}
