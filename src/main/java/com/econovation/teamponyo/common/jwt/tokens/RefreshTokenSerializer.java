package com.econovation.teamponyo.common.jwt.tokens;

import static com.econovation.teamponyo.common.consts.CommonStatics.TOKEN_ID;
import static com.econovation.teamponyo.common.consts.CommonStatics.USER_ID;

import com.econovation.teamponyo.common.jwt.JwtProperties;
import com.econovation.teamponyo.common.jwt.JwtUtil;
import com.econovation.teamponyo.common.jwt.TokenType;
import io.jsonwebtoken.Claims;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public final class RefreshTokenSerializer extends TokenSerializer<RefreshToken> {
    //TODO: AutoConstructor
    public RefreshTokenSerializer(JwtUtil jwtUtil, JwtProperties jwtProperties) {
        super(jwtUtil, jwtProperties);
    }

    @Override
    protected RefreshToken buildToken(Claims claims) {
        Long userId = Long.parseLong(claims.get(USER_ID, String.class));
        UUID tokenId = UUID.fromString(claims.get(TOKEN_ID, String.class));
        return new RefreshToken(userId, tokenId);
    }

    @Override
    protected Map<String, String> buildClaims(RefreshToken refreshToken) {
        return Map.of(
                USER_ID, refreshToken.getUserId().toString(),
                TOKEN_ID, refreshToken.getTokenId().toString()
        );
    }

    @Override
    protected void validate(Claims claims) {
        //TODO: claims.get(TOKEN_ID, String.class); 블랙리스트에 있으면 예외 던지기
        super.validate(claims);
    }

    @Override
    protected TokenType getTokenType() {
        return TokenType.REFRESH;
    }
}
