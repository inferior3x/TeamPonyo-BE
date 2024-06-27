package com.econovation.teamponyo.domains.user.application.service;

import com.econovation.teamponyo.common.enums.SocialProvider;
import com.econovation.teamponyo.domains.user.application.port.in.OAuth2UserExistQueryUseCase;
import com.econovation.teamponyo.domains.user.application.port.out.UserLoadPort;
import com.econovation.teamponyo.domains.user.domain.model.SocialLoginInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserQueryService implements OAuth2UserExistQueryUseCase {
    private final UserLoadPort userLoadPort;

    @Transactional(readOnly = true)
    @Override
    public boolean exists(SocialProvider socialProvider, String socialId) {
        return userLoadPort.existsBySocialLoginInfo(new SocialLoginInfo(socialProvider, socialId));
    }
}
