package com.econovation.teamponyo.common.jwt.tokens;

import com.econovation.teamponyo.common.enums.AccountType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class AccessToken extends Token {
    private final Long userId;
    private final AccountType accountType;
}
