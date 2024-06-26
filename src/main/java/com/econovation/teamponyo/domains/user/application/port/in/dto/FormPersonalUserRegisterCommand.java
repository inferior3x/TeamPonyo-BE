package com.econovation.teamponyo.domains.user.application.port.in.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FormPersonalUserRegisterCommand(
        @NotBlank
        @Size(min = 6, max = 16)
        String loginId,
        @NotBlank
        String password,
        @NotBlank
        String nickname,
        @Email
        String email,
        @NotNull
        Boolean emailSubscription
) {

}
