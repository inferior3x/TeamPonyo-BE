package com.econovation.teamponyo.domains.user.application.port.in;

import com.econovation.teamponyo.domains.user.application.port.in.dto.OAuth2UserRegisterCommand;

public interface OAuth2UserRegisterUseCase {
    void register(OAuth2UserRegisterCommand command);
}
