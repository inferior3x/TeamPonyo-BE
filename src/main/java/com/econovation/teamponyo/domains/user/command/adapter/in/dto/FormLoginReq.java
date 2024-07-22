package com.econovation.teamponyo.domains.user.command.adapter.in.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FormLoginReq(
        @NotBlank
        String loginId,
        @NotBlank
        String password,
        @NotNull
        Boolean autoLogin
) {}
