package com.econovation.teamponyo.infrastructure.security;

import com.econovation.teamponyo.common.interfaces.RequesterInfo;
import io.jsonwebtoken.Jwt;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityRequesterInfo implements RequesterInfo {

    @Override
    public Optional<Long> findUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthentication jwtAuthentication) {
            Long userId = jwtAuthentication.getPrincipal();
            return Optional.ofNullable(userId);
        }
        return Optional.empty();

    }
}
