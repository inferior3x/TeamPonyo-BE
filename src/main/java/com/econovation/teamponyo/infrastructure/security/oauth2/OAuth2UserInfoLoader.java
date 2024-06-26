package com.econovation.teamponyo.infrastructure.security.oauth2;

import com.econovation.teamponyo.common.enums.SocialProvider;
import com.econovation.teamponyo.domains.user.application.port.in.OAuth2UserExistQueryUseCase;
import com.econovation.teamponyo.infrastructure.security.oauth2.response.ProviderResponse;
import com.econovation.teamponyo.infrastructure.security.oauth2.response.ProviderResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2UserInfoLoader extends DefaultOAuth2UserService {
    private final OAuth2UserExistQueryUseCase oAuth2UserExistQueryUseCase;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String providerName = userRequest.getClientRegistration().getRegistrationId();
        SocialProvider socialProvider = SocialProvider.findByProviderName(providerName);
        ProviderResponse providerResponse = ProviderResponseFactory.create(socialProvider, oAuth2User.getAttributes());
        String socialId = providerResponse.getSocialId();
        String email = providerResponse.getEmail();
        boolean registered = oAuth2UserExistQueryUseCase.exists(socialProvider, socialId);

        return new OAuth2UserInfo(registered, socialProvider, socialId, email);
    }
}