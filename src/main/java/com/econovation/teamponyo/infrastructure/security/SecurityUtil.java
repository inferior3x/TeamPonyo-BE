package com.econovation.teamponyo.infrastructure.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static Long getUserId(){
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new IllegalArgumentException("인증되지 않은 요청");
        return authentication.getPrincipal();
    }
}
