package com.econovation.teamponyo.domains.user.command.application.port.in.dto;

public record TokensRes(
        String accessToken,
        String refreshToken
) {

}
