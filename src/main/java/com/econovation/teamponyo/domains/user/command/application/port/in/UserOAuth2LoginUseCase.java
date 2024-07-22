package com.econovation.teamponyo.domains.user.command.application.port.in;

import com.econovation.teamponyo.domains.user.command.application.port.in.dto.TokensRes;

public interface UserOAuth2LoginUseCase {
    TokensRes login(String oAuthLoginToken);
}
