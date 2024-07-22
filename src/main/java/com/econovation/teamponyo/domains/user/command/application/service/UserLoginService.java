package com.econovation.teamponyo.domains.user.command.application.service;

import com.econovation.teamponyo.common.jwt.tokens.AccessToken;
import com.econovation.teamponyo.common.jwt.tokens.OAuth2LoginToken;
import com.econovation.teamponyo.common.jwt.tokens.RefreshToken;
import com.econovation.teamponyo.common.jwt.tokens.TokenSerializer;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserFormLoginUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserOAuth2LoginUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.dto.TokensRes;
import com.econovation.teamponyo.domains.user.command.application.port.out.UserLoadRepository;
import com.econovation.teamponyo.domains.user.domain.model.SocialLoginInfo;
import com.econovation.teamponyo.domains.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserLoginService implements
        UserOAuth2LoginUseCase,
        UserFormLoginUseCase {

    private final UserLoadRepository userLoadRepository;
    private final TokenSerializer<AccessToken> accessTokenSerializer;
    private final TokenSerializer<RefreshToken> refreshTokenSerializer;
    private final TokenSerializer<OAuth2LoginToken> oAuth2LoginTokenSerializer;

    @Transactional(readOnly = true)
    @Override
    public TokensRes login(String oAuth2LoginToken) {
        OAuth2LoginToken oAuth2Login = oAuth2LoginTokenSerializer.deserialize(oAuth2LoginToken);
        //TODO: 블랙리스트
        User user = userLoadRepository.findBySocialLoginInfo(new SocialLoginInfo(oAuth2Login.getSocialProvider(), oAuth2Login.getSocialId()))
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 유저"));
        return new TokensRes(
                accessTokenSerializer.serialize(new AccessToken(user.getUserId(), user.getAccountType())),
                refreshTokenSerializer.serialize(new RefreshToken(user.getUserId()))
        );
    }

    @Transactional(readOnly = true)
    @Override
    public TokensRes login(String loginId, String password) {
        User user = userLoadRepository.findByLoginId(loginId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 유저"));

        return new TokensRes(
                accessTokenSerializer.serialize(new AccessToken(user.getUserId(), user.getAccountType())),
                refreshTokenSerializer.serialize(new RefreshToken(user.getUserId()))
        );
    }
}
