package com.econovation.teamponyo.domains.exhibit.query.port.in.dto;

import com.econovation.teamponyo.common.enums.ExhibitStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record ExhibitSummaryDTO(
        Long exhibitId,
        String posterUrl,
        String title,
        String period,
        ExhibitStatus exhibitStatus
){}