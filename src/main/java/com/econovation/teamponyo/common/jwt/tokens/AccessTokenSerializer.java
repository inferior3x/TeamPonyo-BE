package com.econovation.teamponyo.common.jwt.tokens;

import static com.econovation.teamponyo.common.consts.CommonStatics.ACCOUNT_TYPE;
import static com.econovation.teamponyo.common.consts.CommonStatics.USER_ID;

import com.econovation.teamponyo.common.enums.AccountType;
import com.econovation.teamponyo.common.jwt.JwtProperties;
import com.econovation.teamponyo.common.jwt.JwtUtil;
import com.econovation.teamponyo.common.jwt.TokenType;
import io.jsonwebtoken.Claims;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public final class AccessTokenSerializer extends TokenSerializer<AccessToken> {
    public AccessTokenSerializer(JwtUtil jwtUtil, JwtProperties jwtProperties) {
        super(jwtUtil, jwtProperties);
    }

    @Override
    protected AccessToken buildToken(Claims claims) {
        Long userId = Long.parseLong(claims.get(USER_ID, String.class));
        AccountType accountType = AccountType.findByName(claims.get(ACCOUNT_TYPE, String.class));
        return new AccessToken(userId, accountType);
    }

    @Override
    public Map<String, String> buildClaims(AccessToken accessToken) {
        return Map.of(
                USER_ID, accessToken.getUserId().toString(),
                ACCOUNT_TYPE, accessToken.getAccountType().toString()
        );
    }

    @Override
    protected TokenType getTokenType() {
        return TokenType.ACCESS;
    }
}
