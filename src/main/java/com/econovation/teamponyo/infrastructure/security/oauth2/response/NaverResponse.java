package com.econovation.teamponyo.infrastructure.security.oauth2.response;

import java.util.Map;

public final class NaverResponse implements ProviderResponse {
    private final Map<String, Object> attributes;

    @SuppressWarnings("unchecked")
    public NaverResponse(Map<String, Object> attributes){
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getSocialId() {
        return this.attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return this.attributes.get("email").toString();
    }
}
