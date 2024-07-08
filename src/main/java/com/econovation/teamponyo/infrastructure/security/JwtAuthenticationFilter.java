package com.econovation.teamponyo.infrastructure.security;

import static com.econovation.teamponyo.common.consts.CommonStatics.AUTHORIZATION;

import com.econovation.teamponyo.common.jwt.tokens.AccessToken;
import com.econovation.teamponyo.common.jwt.tokens.AccessTokenSerializer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AccessTokenSerializer accessTokenSerializer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(AUTHORIZATION);

        if (token == null) throw new IllegalArgumentException("토큰이 없습니다"); //TODO

        AccessToken accessToken = accessTokenSerializer.deserialize(token.substring(7)); //TODO: 7

        JwtAuthentication authentication = new JwtAuthentication(
                accessToken.getUserId(),
                Collections.singletonList(new SimpleGrantedAuthority(accessToken.getAccountType().name()))
        ); //TODO: ROLE
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
