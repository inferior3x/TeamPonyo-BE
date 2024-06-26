package com.econovation.teamponyo.domains.user.application.port.in;

import com.econovation.teamponyo.domains.user.application.port.in.dto.TokensRes;

public interface OAuth2UserLoginUseCase {
    TokensRes login(String oAuthLoginToken);
}
