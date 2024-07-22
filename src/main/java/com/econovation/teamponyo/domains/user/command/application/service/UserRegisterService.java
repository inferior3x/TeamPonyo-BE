package com.econovation.teamponyo.domains.user.command.application.service;

import com.econovation.teamponyo.common.event.Events;
import com.econovation.teamponyo.common.jwt.tokens.OAuth2LoginToken;
import com.econovation.teamponyo.common.jwt.tokens.TokenSerializer;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserFormRegisterUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserOAuth2RegisterUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.dto.FormPersonalUserRegisterCommand;
import com.econovation.teamponyo.domains.user.command.application.port.in.dto.FormTeamUserRegisterCommand;
import com.econovation.teamponyo.domains.user.command.application.port.in.dto.OAuth2UserRegisterCommand;
import com.econovation.teamponyo.domains.user.command.application.port.out.UserRecordRepository;
import com.econovation.teamponyo.domains.user.events.UserCreated;
import com.econovation.teamponyo.domains.user.domain.model.FormCredentials;
import com.econovation.teamponyo.domains.user.domain.model.SocialLoginInfo;
import com.econovation.teamponyo.domains.user.domain.model.TeamInfo;
import com.econovation.teamponyo.domains.user.domain.model.User;
import com.econovation.teamponyo.domains.user.domain.model.UserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserRegisterService implements
        UserOAuth2RegisterUseCase,
        UserFormRegisterUseCase {
    private final UserRecordRepository userRecordRepository;
    private final TokenSerializer<OAuth2LoginToken> oAuth2LoginTokenSerializer;

    //TODO: 생성하는건 DTO로 받아서 검증이 필수 기록
    @Transactional
    @Override
    public Long register(@Valid FormPersonalUserRegisterCommand command) {
        User user = User.createPersonal(
                new FormCredentials(command.loginId(), command.password()),
                new UserInfo(command.nickname(), command.email(), null, null),
                command.emailSubscription()
        );
        userRecordRepository.save(user);
        Events.raise(new UserCreated(user));
        return user.getUserId();
    }

    @Transactional
    @Override
    public Long register(@Valid FormTeamUserRegisterCommand command) {
        User team = User.createTeam(
                new FormCredentials(command.loginId(), command.password()),
                new UserInfo(command.nickname(), command.email(), null, command.phoneNumber()),
                TeamInfo.create(command.representativeName(), command.evidenceUrl()),
                command.emailSubscription()
        );
        userRecordRepository.save(team);
        Events.raise(new UserCreated(team));
        return team.getUserId();
    }

    @Transactional
    @Override
    public Long register(@Valid OAuth2UserRegisterCommand command) {
        OAuth2LoginToken oAuth2Login = oAuth2LoginTokenSerializer.deserialize(command.oAuth2LoginToken());
        //TODO: 블랙리스트 추가
        User user = User.createOAuth2(
                new SocialLoginInfo(oAuth2Login.getSocialProvider(), oAuth2Login.getSocialId()),
                new UserInfo(command.nickname(), oAuth2Login.getEmail(), null, null),
                command.emailSubscription()
        );
        userRecordRepository.save(user);
        Events.raise(new UserCreated(user));
        return user.getUserId();
    }

}
