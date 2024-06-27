package com.econovation.teamponyo.domains.user.application.service;

import com.econovation.teamponyo.common.jwt.tokens.AccessToken;
import com.econovation.teamponyo.common.jwt.tokens.RefreshToken;
import com.econovation.teamponyo.common.jwt.tokens.TokenSerializer;
import com.econovation.teamponyo.domains.user.application.port.in.TokenReissueUseCase;
import com.econovation.teamponyo.domains.user.application.port.out.UserLoadPort;
import com.econovation.teamponyo.domains.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserTokenService implements TokenReissueUseCase {
    private final UserLoadPort userLoadPort;
    private final TokenSerializer<AccessToken> accessTokenSerializer;
    private final TokenSerializer<RefreshToken> refreshTokenSerializer;

    @Override
    public String access(String refreshToken) {
        RefreshToken refresh = refreshTokenSerializer.deserialize(refreshToken);
        User user = userLoadPort.getByUserId(refresh.getUserId());
        return accessTokenSerializer.serialize(new AccessToken(user.getUserId(), user.getAccountType()));
    }

    @Override
    public String refresh(String refreshToken) {
        //TODO: 모든 곳에 탈퇴 유저인지 확인하는 로직
        RefreshToken refresh = refreshTokenSerializer.deserialize(refreshToken);
        User user = userLoadPort.getByUserId(refresh.getUserId());
        //TODO: 블랙리스트에 추가 refresh.getTokenId()
        return refreshTokenSerializer.serialize(new RefreshToken(refresh.getUserId()));
    }
}
