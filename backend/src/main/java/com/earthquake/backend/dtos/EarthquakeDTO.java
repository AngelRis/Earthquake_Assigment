package com.earthquake.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EarthquakeDTO {
    private Long id;
    private Double magnitude;
    private String magType;
    private String place;
    private String title;
    private Instant time;
    private Double latitude;
    private Double longitude;
}