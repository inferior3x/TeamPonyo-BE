package com.econovation.teamponyo.domains.exhibit.query.port.in.dto;

import com.econovation.teamponyo.common.enums.ExhibitCategory;
import com.econovation.teamponyo.common.enums.ExhibitStatus;
import com.econovation.teamponyo.domains.exhibit.query.port.in.SortStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.beans.ConstructorProperties;

@JsonNaming(SnakeCaseStrategy.class)
public record ExhibitSummariesReq(
        @Nullable
        Long teamId,
        @Min(1) @Max(50)
        Integer number,
        @NotNull
        Integer pageNumber,
        @NotNull
        SortStrategies sortStrategies,
        @Nullable
        ExhibitCategory exhibitCategory,
        @Nullable
        ExhibitStatus exhibitStatus
){
        @ConstructorProperties({"team-id", "number", "page-number", "sort", "exhibit-category", "exhibit-status"})
        public ExhibitSummariesReq {
                if (pageNumber == null) {
                        pageNumber = 1;
                }
        }
}