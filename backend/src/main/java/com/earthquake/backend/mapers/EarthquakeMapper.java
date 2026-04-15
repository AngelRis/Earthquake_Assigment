package com.earthquake.backend.mapers;

import com.earthquake.backend.dtos.EarthquakeDTO;
import com.earthquake.backend.models.Earthquake;


public class EarthquakeMapper {
    public EarthquakeDTO toDTO(Earthquake entity) {
        if (entity == null) {
            return null;
        }
        return EarthquakeDTO.builder()
                .id(entity.getId())
                .magnitude(entity.getMagnitude())
                .magType(entity.getMagType())
                .place(entity.getPlace())
                .title(entity.getTitle())
                .time(entity.getTime())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .build();
    }
}
