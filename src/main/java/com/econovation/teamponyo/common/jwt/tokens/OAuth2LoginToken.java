package com.econovation.teamponyo.common.jwt.tokens;

import com.econovation.teamponyo.common.enums.SocialProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class OAuth2LoginToken extends Token {
    private final SocialProvider socialProvider;
    private final String socialId;
    private final String email;
}
