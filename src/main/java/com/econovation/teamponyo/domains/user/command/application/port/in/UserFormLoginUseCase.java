package com.econovation.teamponyo.domains.user.command.application.port.in;

import com.econovation.teamponyo.domains.user.command.application.port.in.dto.TokensRes;

public interface UserFormLoginUseCase {
    TokensRes login(String loginId, String password);
}
