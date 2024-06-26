package com.econovation.teamponyo.domains.user.application.port.in.dto;

import jakarta.validation.constraints.Email;

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
