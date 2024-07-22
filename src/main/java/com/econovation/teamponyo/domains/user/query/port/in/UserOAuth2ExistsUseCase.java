package com.econovation.teamponyo.domains.user.query.port.in;

import com.econovation.teamponyo.common.enums.SocialProvider;

public interface UserOAuth2ExistsUseCase {
    boolean exists(SocialProvider socialProvider, String socialId);
}
