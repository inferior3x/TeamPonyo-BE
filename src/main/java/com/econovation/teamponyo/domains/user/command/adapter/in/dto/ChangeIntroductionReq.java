package com.econovation.teamponyo.domains.user.command.adapter.in.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;

@JsonNaming(SnakeCaseStrategy.class)
public record ChangeIntroductionReq(
        @NotNull
        String introduction
){}