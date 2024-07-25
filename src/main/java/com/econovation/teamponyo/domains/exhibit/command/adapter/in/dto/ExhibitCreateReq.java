package com.econovation.teamponyo.domains.exhibit.command.adapter.in.dto;

import com.econovation.teamponyo.common.enums.ExhibitCategory;
import com.econovation.teamponyo.domains.exhibit.command.application.port.in.dto.CoordinateDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDate;

@JsonNaming(SnakeCaseStrategy.class)
public record ExhibitCreateReq(
        ExhibitCategory exhibitCategory,
        String title,
        String address,
        String openTimes,
        String fee,
        String contact,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        @JsonProperty("position")
        CoordinateDTO coordinateDTO
) {}
