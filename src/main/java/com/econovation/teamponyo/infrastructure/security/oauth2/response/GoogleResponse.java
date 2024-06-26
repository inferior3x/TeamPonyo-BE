package com.econovation.teamponyo.infrastructure.security.oauth2.response;

import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class GoogleResponse implements ProviderResponse {
    private final Map<String, Object> attributes;

    @Override
    public String getSocialId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }
}
