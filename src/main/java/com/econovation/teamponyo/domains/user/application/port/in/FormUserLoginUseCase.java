package com.econovation.teamponyo.domains.user.application.port.in;

import com.econovation.teamponyo.domains.user.application.port.in.dto.TokensRes;

public interface FormUserLoginUseCase {
    TokensRes login(String loginId, String password);
}
