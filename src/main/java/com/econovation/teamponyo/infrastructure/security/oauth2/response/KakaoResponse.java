package com.econovation.teamponyo.infrastructure.security.oauth2.response;

import java.util.Map;

public final class KakaoResponse implements ProviderResponse {
    private final Map<String, Object> attributes;
    private final Map<String, Object> kakaoAccountAttributes;

    @SuppressWarnings("unchecked")
    public KakaoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.kakaoAccountAttributes = (Map<String, Object>) attributes.get("kakao_account");
    }

    @Override
    public String getSocialId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return kakaoAccountAttributes.get("email").toString();
    }
}
