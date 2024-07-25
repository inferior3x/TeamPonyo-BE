package com.econovation.teamponyo.domains.exhibit.query.port.in.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record ExhibitBannerDTO(
        Long exhibitId,
        String posterUrl,
        String title,
        String nickname,
        String openTimes,
        String address
){}
