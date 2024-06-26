package com.econovation.teamponyo.common.jwt;

import static com.econovation.teamponyo.common.consts.CommonStatics.ONJEON;
import static com.econovation.teamponyo.common.consts.CommonStatics.TOKEN_TYPE;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtProperties jwtProperties;

    public String createJwt(TokenType tokenType, Map<String, String> claims, Duration expiryDuration) {
        Instant now = Instant.now();
        return Jwts.builder()
                .claim(TOKEN_TYPE, tokenType.toString().toLowerCase())
                .claims(claims)
                .issuer(ONJEON)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiryDuration)))
                .signWith(jwtProperties.getSecretKey())
                .compact();
    }

    public Claims getClaims(String token){
        return Jwts.parser().verifyWith(jwtProperties.getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    public String getClaim(Claims claims, String claim) {
        return Objects.requireNonNull(claims.get(claim, String.class), "잘못된 토큰");
    }

    public Date getExpiryDate(Claims claims){
        return claims.getExpiration();
    }

    //TODO: 이걸 여기다 둘 필요가 있을까
    public void ensureTokenTypeMatches(Claims claims, TokenType tokenType) {
        if (TokenType.findByName(getClaim(claims, TOKEN_TYPE)) != tokenType)
            throw new IllegalArgumentException("맞지 않은 토큰 타입");
    }
    public void ensureNotExpired(Claims claims){
        if (getExpiryDate(claims).before(new Date()))
            throw new IllegalArgumentException("만료된 토큰");
    }
}