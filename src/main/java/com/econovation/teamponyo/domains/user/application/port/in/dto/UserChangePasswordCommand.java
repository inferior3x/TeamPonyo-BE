package com.econovation.teamponyo.domains.user.application.port.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserChangePasswordCommand(
        @NotBlank
        Long userId,
        @NotBlank
        String oldPassword,
        @Size(min = 5, max = 10) //TODO: 제한 수정 - 밸리데이터
        String newPassword
) {}
