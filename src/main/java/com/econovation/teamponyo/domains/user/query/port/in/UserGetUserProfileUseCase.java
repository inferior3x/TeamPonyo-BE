package com.econovation.teamponyo.domains.user.query.port.in;

import com.econovation.teamponyo.domains.user.query.port.in.dto.UserProfileDTO;

public interface UserGetUserProfileUseCase {
    UserProfileDTO get(Long userId);
}
