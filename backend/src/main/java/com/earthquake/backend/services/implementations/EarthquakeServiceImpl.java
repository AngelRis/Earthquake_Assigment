package com.earthquake.backend.services.implementations;

import com.earthquake.backend.dtos.EarthquakeDTO;
import com.earthquake.backend.exceptions.DatabaseException;
import com.earthquake.backend.exceptions.EarthquakeApiException;
import com.earthquake.backend.exceptions.InvalidGeoJsonException;
import com.earthquake.backend.exceptions.ResourceNotFoundException;
import com.earthquake.backend.mapers.EarthquakeMapper;
import com.earthquake.backend.models.Earthquake;
import com.earthquake.backend.repositories.EarthquakeRepository;
import com.earthquake.backend.services.EarthquakeService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Service
public class EarthquakeServiceImpl implements EarthquakeService {

    private final RestTemplate restTemplate;
    private final EarthquakeRepository earthquakeRepository;
    private final EarthquakeMapper earthquakeMapper = new EarthquakeMapper();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(EarthquakeServiceImpl.class);
    @Value("${earthquake.api.url}")
    private String url;


    public EarthquakeServiceImpl(RestTemplate restTemplate, EarthquakeRepository earthquakeRepository) {
        this.restTemplate = restTemplate;
        this.earthquakeRepository = earthquakeRepository;
    }

    @Override
    @Transactional
    public void fetchAndStoreEarthquakes() {
        try {
            String response = fetchEarthquakeData(url);
            JsonNode root = objectMapper.readTree(response);

            List<Earthquake> earthquakes = parseEarthquakes(root);

            Instant time = Instant.now().minus(Duration.ofHours(1));
            earthquakes = filterFetchedEarthquakesByMagnitudeAndTimeAfter(earthquakes, 2.0, time);

            saveEarthquakes(earthquakes);

        } catch (EarthquakeApiException | InvalidGeoJsonException e) {
            throw e;
        } catch (Exception e) {
            throw new EarthquakeApiException("Failed to fetch earthquake data", e);
        }
    }


    @Override
    public void deleteEarthquakeById(Long id) {
        if (!earthquakeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Earthquake not found: " + id);
        }
        try {
            earthquakeRepository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseException("Error deleting earthquake", e);
        }
    }

    @Override
    public List<EarthquakeDTO> filterEarthquakes(Double minMagnitude, Instant afterTime) {
        try {
            if (minMagnitude != null && afterTime != null) {
                return earthquakeRepository.findByMagnitudeGreaterThanAndTimeAfter(minMagnitude, afterTime).stream().map(earthquakeMapper::toDTO).toList();
            }

            if (minMagnitude != null) {
                return earthquakeRepository.findByMagnitudeGreaterThan(minMagnitude).stream().map(earthquakeMapper::toDTO).toList();
            }

            if (afterTime != null) {
                return earthquakeRepository.findByTimeAfter(afterTime).stream().map(earthquakeMapper::toDTO).toList();
            }
            return earthquakeRepository.findAll().stream().map(earthquakeMapper::toDTO).toList();
        }catch (Exception e) {
            throw new DatabaseException("Error reading from DB", e);
        }

    }


    private String fetchEarthquakeData(String url) {
        String response = restTemplate.getForObject(url, String.class);
        if (response == null || response.isBlank()) {
            throw new EarthquakeApiException("Empty response from API", null);
        }
        return response;
    }

    private List<Earthquake> parseEarthquakes(JsonNode root) {
        JsonNode features = root.get("features");
        if (features == null || !features.isArray()) {
            throw new InvalidGeoJsonException("Invalid GeoJSON: missing features");
        }

        List<Earthquake> earthquakes = new ArrayList<>();
        for (JsonNode feature : features) {
            try {
                Earthquake eq = parseSingleEarthquake(feature);
                if (eq != null) earthquakes.add(eq);
            } catch (Exception e) {
                logger.warn("Error parsing earthquake: {}", e.getMessage());

            }
        }
        return earthquakes;
    }

    private Earthquake parseSingleEarthquake(JsonNode feature) {
        JsonNode properties = feature.get("properties");
        JsonNode geometry = feature.get("geometry");
        JsonNode id = feature.get("id");

        if (properties == null || geometry == null || id == null) return null;

        JsonNode coordinates = geometry.get("coordinates");
        if (coordinates == null || coordinates.size() < 2) return null;

        Double mag = safeDouble(properties.get("mag"));
        String magType = safeText(properties.get("magType"));
        String place = safeText(properties.get("place"));
        String title = safeText(properties.get("title"));
        Long time = safeLong(properties.get("time"));
        String externalId = safeText(id);

        Double longitude = safeDouble(coordinates.get(0));
        Double latitude = safeDouble(coordinates.get(1));

        if (mag == null || magType == null || place == null ||
                title == null || time == null ||
                latitude == null || longitude == null || externalId == null) {
            return null;
        }

        return Earthquake.builder()
                .magnitude(mag)
                .magType(magType)
                .place(place)
                .title(title)
                .time(Instant.ofEpochMilli(time))
                .latitude(latitude)
                .longitude(longitude)
                .externalId(externalId)
                .build();
    }

    private void saveEarthquakes(List<Earthquake> earthquakes) {
        try {
            earthquakeRepository.deleteAll();
            earthquakeRepository.saveAll(earthquakes);
        } catch (Exception e) {
            throw new DatabaseException("Error saving earthquakes to DB", e);
        }
    }

    public List<Earthquake> filterFetchedEarthquakesByMagnitudeAndTimeAfter(
            List<Earthquake> earthquakes,
            Double magnitude,
            Instant time
    ) {
        return earthquakes.stream()
                .filter(e -> e.getMagnitude() > magnitude && e.getTime().isAfter(time))
                .toList();
    }

    private Double safeDouble(JsonNode node) {
        return (node != null && !node.isNull()) ? node.asDouble() : null;
    }

    private Long safeLong(JsonNode node) {
        return (node != null && !node.isNull()) ? node.asLong() : null;
    }

    private String safeText(JsonNode node) {
        return (node != null && !node.isNull()) ? node.asString() : null;
    }
}
