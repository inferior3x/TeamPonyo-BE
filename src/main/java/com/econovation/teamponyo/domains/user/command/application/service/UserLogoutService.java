package com.econovation.teamponyo.domains.user.command.application.service;

import com.econovation.teamponyo.common.jwt.tokens.RefreshToken;
import com.econovation.teamponyo.common.jwt.tokens.TokenSerializer;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserLogoutUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.out.UserLoadRepository;
import com.econovation.teamponyo.domains.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserLogoutService implements
        UserLogoutUseCase {
    private final UserLoadRepository userLoadRepository;
    private final TokenSerializer<RefreshToken> refreshTokenSerializer;

    @Transactional
    @Override
    public void logout(String refreshToken) {
        RefreshToken refresh = refreshTokenSerializer.deserialize(refreshToken);
        User user = userLoadRepository.getByUserId(refresh.getUserId());
        //TODO: 블랙리스트 추가
    }
}
