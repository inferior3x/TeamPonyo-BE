package com.econovation.teamponyo.domains.user.command.application.port.in.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FormTeamUserRegisterCommand(
        String loginId,
        String password,
        String nickname,
        @Email
        String email,
        String phoneNumber,
        String representativeName,
        String evidenceUrl,
        Boolean emailSubscription
){}
