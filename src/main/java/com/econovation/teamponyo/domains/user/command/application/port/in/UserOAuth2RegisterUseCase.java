package com.econovation.teamponyo.domains.user.command.application.port.in;

import com.econovation.teamponyo.domains.user.command.application.port.in.dto.OAuth2UserRegisterCommand;

public interface UserOAuth2RegisterUseCase {
    Long register(OAuth2UserRegisterCommand command);
}
