package com.econovation.teamponyo.domains.user.command.application.port.in.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserChangePasswordCommand(
        @NotBlank
        String oldPassword,
        @Size(min = 5, max = 10) //TODO: 제한 수정 - 밸리데이터
        String newPassword
) {}
