package com.econovation.teamponyo.domains.user.adapter.in.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OAuth2UserSignupReq(
        @NotBlank
        String nickname,
        @NotNull
        Boolean emailSubscription
){}
