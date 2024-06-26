package com.econovation.teamponyo.domains.user.application.service;

import com.econovation.teamponyo.common.enums.SocialProvider;
import com.econovation.teamponyo.common.jwt.tokens.AccessToken;
import com.econovation.teamponyo.common.jwt.tokens.OAuth2LoginToken;
import com.econovation.teamponyo.common.jwt.tokens.RefreshToken;
import com.econovation.teamponyo.common.jwt.tokens.TokenSerializer;
import com.econovation.teamponyo.domains.user.application.port.in.FormUserLoginUseCase;
import com.econovation.teamponyo.domains.user.application.port.in.FormUserRegisterUseCase;
import com.econovation.teamponyo.domains.user.application.port.in.OAuth2UserExistQueryUseCase;
import com.econovation.teamponyo.domains.user.application.port.in.OAuth2UserLoginUseCase;
import com.econovation.teamponyo.domains.user.application.port.in.OAuth2UserRegisterUseCase;
import com.econovation.teamponyo.domains.user.application.port.in.TokenReissueUseCase;
import com.econovation.teamponyo.domains.user.application.port.in.UserLogoutUseCase;
import com.econovation.teamponyo.domains.user.application.port.in.dto.FormPersonalUserRegisterCommand;
import com.econovation.teamponyo.domains.user.application.port.in.dto.FormTeamUserRegisterCommand;
import com.econovation.teamponyo.domains.user.application.port.in.dto.OAuth2UserRegisterCommand;
import com.econovation.teamponyo.domains.user.application.port.in.dto.TokensRes;
import com.econovation.teamponyo.domains.user.application.port.out.UserLoadPort;
import com.econovation.teamponyo.domains.user.application.port.out.UserRecordPort;
import com.econovation.teamponyo.domains.user.domain.model.FormLoginCredentials;
import com.econovation.teamponyo.domains.user.domain.model.SocialLoginInfo;
import com.econovation.teamponyo.domains.user.domain.model.TeamInfo;
import com.econovation.teamponyo.domains.user.domain.model.User;
import com.econovation.teamponyo.domains.user.domain.model.UserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserService implements
        OAuth2UserRegisterUseCase,
        FormUserRegisterUseCase,
        OAuth2UserExistQueryUseCase,
        OAuth2UserLoginUseCase,
        FormUserLoginUseCase,
        UserLogoutUseCase,
        TokenReissueUseCase {

    private final UserLoadPort userLoadPort;
    private final UserRecordPort userRecordPort;
    private final TokenSerializer<AccessToken> accessTokenSerializer;
    private final TokenSerializer<RefreshToken> refreshTokenSerializer;
    private final TokenSerializer<OAuth2LoginToken> oAuth2LoginTokenSerializer;

    //TODO: 생성하는건 DTO로 받아서 검증이 필수 기록
    @Override
    public void register(@Valid FormPersonalUserRegisterCommand command) {
        User user = User.createPersonal(
                new FormLoginCredentials(command.loginId(), command.password()),
                new UserInfo(command.nickname(), command.email(), null, null),
                command.emailSubscription()
        );
        userRecordPort.save(user);
    }


    @Override
    public void register(@Valid FormTeamUserRegisterCommand command) {
        User user = User.createTeam(
                new FormLoginCredentials(command.loginId(), command.password()),
                new UserInfo(command.nickname(), command.email(), null, command.phoneNumber()),
                TeamInfo.create(command.representativeName(), command.evidenceUrl()),
                command.emailSubscription()
        );
        userRecordPort.save(user);
    }

    @Override
    public void register(@Valid OAuth2UserRegisterCommand command) {
        OAuth2LoginToken oAuth2Login = oAuth2LoginTokenSerializer.deserialize(command.oAuth2LoginToken());
        //TODO: 블랙리스트 추가
        User user = User.createOAuth2(
                new SocialLoginInfo(oAuth2Login.getSocialProvider(), oAuth2Login.getSocialId()),
                new UserInfo(command.nickname(), oAuth2Login.getEmail(), null, null),
                command.emailSubscription()
        );
        userRecordPort.save(user);
    }

    @Override
    public boolean exists(SocialProvider socialProvider, String socialId) {
        return userLoadPort.existsBySocialLoginInfo(new SocialLoginInfo(socialProvider, socialId));
    }

    @Override
    public TokensRes login(String oAuth2LoginToken) {
        OAuth2LoginToken oAuth2Login = oAuth2LoginTokenSerializer.deserialize(oAuth2LoginToken);
        //TODO: 블랙리스트
        User user = userLoadPort.findBySocialLoginInfo(new SocialLoginInfo(oAuth2Login.getSocialProvider(), oAuth2Login.getSocialId()))
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 유저"));
        return new TokensRes(
                accessTokenSerializer.serialize(new AccessToken(user.getUserId(), user.getAccountType())),
                refreshTokenSerializer.serialize(new RefreshToken(user.getUserId()))
        );
    }

    @Override
    public TokensRes login(String loginId, String password) {
        User user = userLoadPort.findByFormLoginCredentials(new FormLoginCredentials(loginId, password))
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 유저"));
        return new TokensRes(
                accessTokenSerializer.serialize(new AccessToken(user.getUserId(), user.getAccountType())),
                refreshTokenSerializer.serialize(new RefreshToken(user.getUserId()))
        );
    }

    @Override
    public void logout(String refreshToken) {
        RefreshToken refresh = refreshTokenSerializer.deserialize(refreshToken);
        User user = userLoadPort.getByUserId(refresh.getUserId());
        //TODO: 블랙리스트 추가
    }

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
