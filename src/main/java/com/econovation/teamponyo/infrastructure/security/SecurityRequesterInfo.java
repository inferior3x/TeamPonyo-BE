package com.econovation.teamponyo.infrastructure.security;

import com.econovation.teamponyo.common.interfaces.RequesterInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityRequesterInfo implements RequesterInfo {

    @Override
    public Long getUserId() {
        return ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
    }
}
