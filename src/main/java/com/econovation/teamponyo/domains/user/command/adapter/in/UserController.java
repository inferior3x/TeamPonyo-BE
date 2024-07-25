package com.econovation.teamponyo.domains.user.command.adapter.in;

import static com.econovation.teamponyo.common.consts.CommonStatics.OAUTH2_LOGIN_TOKEN;
import static com.econovation.teamponyo.common.consts.CommonStatics.REFRESH_TOKEN;

import com.econovation.teamponyo.common.jwt.JwtProperties;
import com.econovation.teamponyo.common.jwt.TokenType;
import com.econovation.teamponyo.common.utils.CookieFactory;
import com.econovation.teamponyo.domains.user.command.adapter.in.dto.AccessTokenRes;
import com.econovation.teamponyo.domains.user.command.adapter.in.dto.ChangeIntroductionReq;
import com.econovation.teamponyo.domains.user.command.adapter.in.dto.ExhibitReq;
import com.econovation.teamponyo.domains.user.command.adapter.in.dto.FormLoginReq;
import com.econovation.teamponyo.domains.user.command.adapter.in.dto.OAuth2UserSignupReq;
import com.econovation.teamponyo.domains.user.command.application.port.in.TokenReissueUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserChangeIntroductionUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserChangePasswordUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserFormLoginUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserFormRegisterUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserLogoutUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserOAuth2LoginUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserOAuth2RegisterUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserSavedExhibitUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserVisitedExhibitUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.dto.FormPersonalUserRegisterCommand;
import com.econovation.teamponyo.domains.user.command.application.port.in.dto.FormTeamUserRegisterCommand;
import com.econovation.teamponyo.domains.user.command.application.port.in.dto.OAuth2UserRegisterCommand;
import com.econovation.teamponyo.domains.user.command.application.port.in.dto.TokensRes;
import com.econovation.teamponyo.domains.user.command.application.port.in.dto.UserChangePasswordCommand;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//TODO: 반환 형식

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserOAuth2RegisterUseCase userOAuth2RegisterUseCase;
    private final UserFormRegisterUseCase userFormRegisterUseCase;
    private final UserOAuth2LoginUseCase userOAuth2LoginUseCase;
    private final UserFormLoginUseCase userFormLoginUseCase;
    private final UserLogoutUseCase userLogoutUseCase;
    private final TokenReissueUseCase tokenReissueUseCase;
    private final UserChangePasswordUseCase userChangePasswordUseCase;
    private final CookieFactory cookieFactory;
    private final UserChangeIntroductionUseCase userChangeIntroductionUseCase;
    private final UserSavedExhibitUseCase userSavedExhibitUseCase;
    private final UserVisitedExhibitUseCase userVisitedExhibitUseCase;
    private final JwtProperties jwtProperties;

    @PostMapping("/auth/signup/personal")
    public ResponseEntity<Long> formPersonalSignup(@RequestBody @Valid FormPersonalUserRegisterCommand req){
        return ResponseEntity.ok(userFormRegisterUseCase.register(req));
    }

    @PostMapping("/auth/signup/team")
    public ResponseEntity<Long> formTeamSignup(@RequestBody @Valid FormTeamUserRegisterCommand req){
        return ResponseEntity.ok(userFormRegisterUseCase.register(req));
    }

    @PostMapping("/auth/signup/oauth")
    public ResponseEntity<Long> oAuth2UserSignup(@CookieValue(name = OAUTH2_LOGIN_TOKEN) Cookie oAuth2TokenCookie, @RequestBody @Valid OAuth2UserSignupReq req){
        return ResponseEntity.ok(
                userOAuth2RegisterUseCase.register(new OAuth2UserRegisterCommand(oAuth2TokenCookie.getValue(), req.nickname(), req.emailSubscription()))
        );
    }


    @PostMapping("/auth/login")
    public ResponseEntity<AccessTokenRes> formLogin(@RequestBody @Valid FormLoginReq req, HttpServletResponse response){
        TokensRes tokensRes = userFormLoginUseCase.login(req.loginId(), req.password());

        Cookie refreshTokenCookie = req.autoLogin() ?
                cookieFactory.create(REFRESH_TOKEN, tokensRes.refreshToken(), jwtProperties.getTokenExpiry(TokenType.REFRESH)) :
                cookieFactory.create(REFRESH_TOKEN, tokensRes.refreshToken());
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok(new AccessTokenRes(tokensRes.accessToken()));
    }


    @PostMapping("/auth/login/oauth")
    public ResponseEntity<AccessTokenRes> oAuth2UserLogin(@CookieValue(name = OAUTH2_LOGIN_TOKEN) Cookie oAuth2TokenCookie, HttpServletResponse response){
        TokensRes tokensRes = userOAuth2LoginUseCase.login(oAuth2TokenCookie.getValue());
        Cookie refreshTokenCookie = cookieFactory.create(REFRESH_TOKEN, tokensRes.refreshToken(), jwtProperties.getTokenExpiry(TokenType.REFRESH));
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok(new AccessTokenRes(tokensRes.accessToken()));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout(@CookieValue(name = REFRESH_TOKEN) Cookie refreshToken, HttpServletResponse response){
        userLogoutUseCase.logout(refreshToken.getValue());
        response.addCookie(cookieFactory.createDeletionCookie(REFRESH_TOKEN));
        return ResponseEntity.ok("로그아웃 완료");
    }

    @PostMapping("/auth/tokens/access")
    public ResponseEntity<AccessTokenRes> reissueAccessToken(
            @CookieValue(name = REFRESH_TOKEN) Cookie refreshTokenCookie){
        String newAccessToken = tokenReissueUseCase.access(refreshTokenCookie.getValue());
        return ResponseEntity.ok(new AccessTokenRes(newAccessToken));
    }

    @PostMapping("/auth/tokens/refresh")
    public ResponseEntity<String> reissueRefreshToken(
            @CookieValue(name = REFRESH_TOKEN) Cookie refreshTokenCookie, HttpServletResponse response){

        Cookie newRefreshTokenCookie = cookieFactory.create(
                REFRESH_TOKEN,
                tokenReissueUseCase.refresh(refreshTokenCookie.getValue()),
                jwtProperties.getTokenExpiry(TokenType.REFRESH));
        response.addCookie(newRefreshTokenCookie);
        return ResponseEntity.ok("리프레시 토큰 발급 완료");
    }

    @PatchMapping("/user/password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid UserChangePasswordCommand command){

        userChangePasswordUseCase.changePassword(command);
        return ResponseEntity.ok("비밀번호 변경 완료");
    }

    @PostMapping("/user/saved-exhibits")
    public ResponseEntity<String> saveExhibit(@RequestBody ExhibitReq req){
        userSavedExhibitUseCase.saveExhibit(req.exhibitId());
        return ResponseEntity.ok("저장 완료");
    }

    @DeleteMapping("/user/saved-exhibits/{exhibit-id}")
    public ResponseEntity<String> removeSavedExhibit(@PathVariable("exhibit-id") Long exhibitId){
        userSavedExhibitUseCase.removeSavedExhibit(exhibitId);
        return ResponseEntity.ok("삭제 완료");
    }

    @PostMapping("/user/visited-exhibits")
    public ResponseEntity<String> addVisitedExhibit(@RequestBody ExhibitReq req){
        userVisitedExhibitUseCase.addVisitedExhibit(req.exhibitId());
        return ResponseEntity.ok("저장 완료");
    }

    @DeleteMapping("/user/visited-exhibits/{exhibit-id}")
    public ResponseEntity<String> removeVisitedExhibit(@PathVariable("exhibit-id") Long exhibitId){
        userVisitedExhibitUseCase.removeVisitedExhibit(exhibitId);
        return ResponseEntity.ok("삭제 완료");
    }

    @PatchMapping("/user/introduction")
    public ResponseEntity<String> changeIntroduction(@RequestBody @Valid ChangeIntroductionReq req){
        userChangeIntroductionUseCase.changeIntroduction(req.introduction());
        return ResponseEntity.ok("소개 메세지 변경 완료");
    }

}
