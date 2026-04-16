package com.earthquake.backend.controllers;

import com.earthquake.backend.exceptions.DatabaseException;
import com.earthquake.backend.exceptions.EarthquakeApiException;
import com.earthquake.backend.exceptions.InvalidGeoJsonException;
import com.earthquake.backend.exceptions.ResourceNotFoundException;
import com.earthquake.backend.services.EarthquakeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;


@RestController
@RequestMapping("/api/earthquakes")
@CrossOrigin(origins = {"*", "http://localhost:5173"})
public class EarthquakeController {

    private final EarthquakeService earthquakeService;

    public EarthquakeController(EarthquakeService earthquakeService) {
        this.earthquakeService = earthquakeService;
    }

    @GetMapping
    public ResponseEntity<?> getEarthquakes(@RequestParam(required = false) Double minMagnitude,
                                                       @RequestParam(required = false)
                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                       Instant afterTime) {
        try {
            earthquakeService.fetchAndStoreEarthquakes();
            return ResponseEntity.ok(earthquakeService.filterEarthquakes(minMagnitude, afterTime));
        } catch (EarthquakeApiException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        } catch (InvalidGeoJsonException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DatabaseException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteEarthquake/{id}")
    public ResponseEntity<?> deleteEarthquake(@PathVariable Long id) {
        try {
            earthquakeService.deleteEarthquakeById(id);
            return ResponseEntity.ok(earthquakeService.filterEarthquakes(null, null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DatabaseException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
