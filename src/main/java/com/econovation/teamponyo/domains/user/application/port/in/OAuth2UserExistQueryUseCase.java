package com.econovation.teamponyo.domains.user.application.port.in;

import com.econovation.teamponyo.common.enums.SocialProvider;

public interface OAuth2UserExistQueryUseCase {
    boolean exists(SocialProvider socialProvider, String socialId);
}
