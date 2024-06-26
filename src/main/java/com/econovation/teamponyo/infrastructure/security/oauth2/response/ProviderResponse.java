package com.econovation.teamponyo.infrastructure.security.oauth2.response;

public interface ProviderResponse {
    String getSocialId();
    //TODO: NPE
    String getEmail();
}
