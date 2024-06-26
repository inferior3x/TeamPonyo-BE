package com.econovation.teamponyo.domains.user.application.port.in.dto;

public record TokensRes(
        String accessToken,
        String refreshToken
) {

}
