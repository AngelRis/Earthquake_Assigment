package com.earthquake.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "earthquakes")
public class Earthquake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NonNull
    @Min(0)
    private Double magnitude;

    @Column(name = "mag_type", nullable = false)
    @NonNull
    @NotBlank
    private String magType;

    @Column(nullable = false)
    @NonNull
    @NotBlank
    private String place;

    @Column(nullable = false)
    @NonNull
    @NotBlank
    private String title;

    @Column(nullable = false)
    @NonNull
    private Instant time;

    @Column(nullable = false)
    @NonNull
    private Double latitude;

    @Column(nullable = false)
    @NonNull
    private Double longitude;

    @Column(name = "external_id", nullable = false)
    private String externalId;
}