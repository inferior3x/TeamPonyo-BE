package com.econovation.teamponyo.infrastructure.security.oauth2;

import com.econovation.teamponyo.common.enums.SocialProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public class OAuth2UserInfo implements OAuth2User {
    private final boolean isRegistered;
    private final SocialProvider socialProvider;
    private final String socialId;
    private final String email;

    public OAuth2UserInfo(boolean isRegistered, SocialProvider socialProvider, String socialId, String email) {
        this.isRegistered = isRegistered;
        this.socialProvider = socialProvider;
        this.socialId = socialId;
        this.email = email;
    }

    @Override
    public String getName() {
        return getSocialId();
    }

    //없애고 싶다.........
    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
}
