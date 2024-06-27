package com.econovation.teamponyo.domains.user.application.service;

import com.econovation.teamponyo.common.jwt.tokens.OAuth2LoginToken;
import com.econovation.teamponyo.common.jwt.tokens.TokenSerializer;
import com.econovation.teamponyo.domains.user.application.port.in.FormUserRegisterUseCase;
import com.econovation.teamponyo.domains.user.application.port.in.OAuth2UserRegisterUseCase;
import com.econovation.teamponyo.domains.user.application.port.in.dto.FormPersonalUserRegisterCommand;
import com.econovation.teamponyo.domains.user.application.port.in.dto.FormTeamUserRegisterCommand;
import com.econovation.teamponyo.domains.user.application.port.in.dto.OAuth2UserRegisterCommand;
import com.econovation.teamponyo.domains.user.application.port.out.UserRecordPort;
import com.econovation.teamponyo.domains.user.domain.model.FormCredentials;
import com.econovation.teamponyo.domains.user.domain.model.SocialLoginInfo;
import com.econovation.teamponyo.domains.user.domain.model.TeamInfo;
import com.econovation.teamponyo.domains.user.domain.model.User;
import com.econovation.teamponyo.domains.user.domain.model.UserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisterService implements
        OAuth2UserRegisterUseCase,
        FormUserRegisterUseCase{

    private final UserRecordPort userRecordPort;
    private final TokenSerializer<OAuth2LoginToken> oAuth2LoginTokenSerializer;

    //TODO: 생성하는건 DTO로 받아서 검증이 필수 기록
    @Override
    public void register(@Valid FormPersonalUserRegisterCommand command) {
        User user = User.createPersonal(
                new FormCredentials(command.loginId(), command.password()),
                new UserInfo(command.nickname(), command.email(), null, null),
                command.emailSubscription()
        );
        userRecordPort.save(user);
    }


    @Override
    public void register(@Valid FormTeamUserRegisterCommand command) {
        User user = User.createTeam(
                new FormCredentials(command.loginId(), command.password()),
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

}
