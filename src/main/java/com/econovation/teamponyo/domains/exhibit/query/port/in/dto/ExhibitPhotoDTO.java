package com.econovation.teamponyo.domains.exhibit.query.port.in.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ExhibitPhotoDTO(
        String photoUrl
){}