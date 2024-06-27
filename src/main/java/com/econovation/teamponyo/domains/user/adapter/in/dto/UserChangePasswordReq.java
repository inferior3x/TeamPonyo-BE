package com.econovation.teamponyo.domains.user.adapter.in.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserChangePasswordReq(
        @NotBlank
        String oldPassword,
        @NotBlank
        String newPassword
){}
