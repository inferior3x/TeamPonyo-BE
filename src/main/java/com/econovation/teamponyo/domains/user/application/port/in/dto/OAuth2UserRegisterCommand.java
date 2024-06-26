package com.econovation.teamponyo.domains.user.application.port.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OAuth2UserRegisterCommand(
        @NotNull
        String oAuth2LoginToken,
        @NotBlank
        String nickname,
        @NotNull
        Boolean emailSubscription
) {
        public OAuth2UserRegisterCommand {
                //TODO: 닉네임 검증

        }
}
