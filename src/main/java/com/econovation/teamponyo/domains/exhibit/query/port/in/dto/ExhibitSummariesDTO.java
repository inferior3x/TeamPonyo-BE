package com.econovation.teamponyo.domains.exhibit.query.port.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

@JsonNaming(SnakeCaseStrategy.class)
public record ExhibitSummariesDTO(
        int totalPages,
        @JsonProperty("exhibits")
        List<ExhibitSummaryDTO> exhibitSummaries
){}
