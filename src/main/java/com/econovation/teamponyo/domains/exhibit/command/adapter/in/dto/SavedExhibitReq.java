package com.econovation.teamponyo.domains.exhibit.command.adapter.in.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.beans.ConstructorProperties;

public record SavedExhibitReq(
        @Min(1) @Max(50)
        Integer number,
        @NotNull
        Integer pageNumber
){
        @ConstructorProperties({"number", "page-number"})
        public SavedExhibitReq {}
}
