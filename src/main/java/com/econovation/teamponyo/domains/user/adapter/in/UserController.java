package com.econovation.teamponyo.domains.user.adapter.in;

import static com.econovation.teamponyo.common.consts.CommonStatics.OAUTH2_LOGIN_TOKEN;
import static com.econovation.teamponyo.common.consts.CommonStatics.REFRESH_TOKEN;

import com.econovation.teamponyo.common.jwt.JwtProperties;
import com.econovation.teamponyo.common.jwt.TokenType;
import com.econovation.teamponyo.common.utils.CookieFactory;
import com.econovation.teamponyo.domains.user.adapter.in.dto.AccessTokenRes;
import com.econovation.teamponyo.domains.user.adapter.in.dto.FormLoginReq;
import com.econovation.teamponyo.domains.user.adapter.in.dto.OAuth2UserSignupReq;
import com.econovation.teamponyo.domains.user.application.port.in.FormUserLoginUseCase;
import com.econovation.teamponyo.domains.user.application.port.in.FormUserRegisterUseCase;
import com.econovation.teamponyo.domains.user.application.port.in.OAuth2UserLoginUseCase;
import com.econovation.teamponyo.domains.user.application.port.in.OAuth2UserRegisterUseCase;
import com.econovation.teamponyo.domains.user.application.port.in.TokenReissueUseCase;
import com.econovation.teamponyo.domains.user.application.port.in.UserLogoutUseCase;
import com.econovation.teamponyo.domains.user.application.port.in.dto.FormPersonalUserRegisterCommand;
import com.econovation.teamponyo.domains.user.application.port.in.dto.OAuth2UserRegisterCommand;
import com.econovation.teamponyo.domains.user.application.port.in.dto.TokensRes;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//TODO: 반환 형식

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final OAuth2UserRegisterUseCase oAuth2UserRegisterUseCase;
    private final FormUserRegisterUseCase formUserRegisterUseCase;
    private final OAuth2UserLoginUseCase oAuth2UserLoginUseCase;
    private final FormUserLoginUseCase formUserLoginUseCase;
    private final UserLogoutUseCase userLogoutUseCase;
    private final TokenReissueUseCase tokenReissueUseCase;
    private final CookieFactory cookieFactory;
    private final JwtProperties jwtProperties;


    @PostMapping("/signup")
    public ResponseEntity<String> formSignup(@RequestBody @Valid FormPersonalUserRegisterCommand req){
        formUserRegisterUseCase.register(req);
        return ResponseEntity.ok("회원가입 완료");
    }


    @PostMapping("/signup/oauth")
    public ResponseEntity<String> oAuth2UserSignup(@CookieValue(name = OAUTH2_LOGIN_TOKEN) Cookie oAuth2TokenCookie, @RequestBody @Valid OAuth2UserSignupReq req){
        oAuth2UserRegisterUseCase.register(
                new OAuth2UserRegisterCommand(oAuth2TokenCookie.getValue(), req.nickname(), req.emailSubscription())
        );
        return ResponseEntity.ok("회원가입 완료");
    }


    @PostMapping("/login")
    public ResponseEntity<AccessTokenRes> formLogin(@RequestBody @Valid FormLoginReq req, HttpServletResponse response){
        TokensRes tokensRes = formUserLoginUseCase.login(req.loginId(), req.password());

        Cookie refreshTokenCookie = req.autoLogin() ?
                cookieFactory.create(REFRESH_TOKEN, tokensRes.refreshToken(), jwtProperties.getTokenExpiry(TokenType.REFRESH)) :
                cookieFactory.create(REFRESH_TOKEN, tokensRes.refreshToken());
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok(new AccessTokenRes(tokensRes.accessToken()));
    }


    @PostMapping("/login/oauth")
    public ResponseEntity<AccessTokenRes> oAuth2UserLogin(@CookieValue(name = OAUTH2_LOGIN_TOKEN) Cookie oAuth2TokenCookie, HttpServletResponse response){
        TokensRes tokensRes = oAuth2UserLoginUseCase.login(oAuth2TokenCookie.getValue());
        Cookie refreshTokenCookie = cookieFactory.create(REFRESH_TOKEN, tokensRes.refreshToken(), jwtProperties.getTokenExpiry(TokenType.REFRESH));
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok(new AccessTokenRes(tokensRes.accessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(name = REFRESH_TOKEN) Cookie refreshToken, HttpServletResponse response){
        userLogoutUseCase.logout(refreshToken.getValue());
        response.addCookie(cookieFactory.createDeletionCookie(REFRESH_TOKEN));
        return ResponseEntity.ok("로그아웃 완료");
    }

    @PostMapping("/tokens/access")
    public ResponseEntity<AccessTokenRes> reissueAccessToken(
            @CookieValue(name = REFRESH_TOKEN) Cookie refreshTokenCookie){
        String newAccessToken = tokenReissueUseCase.access(refreshTokenCookie.getValue());
        return ResponseEntity.ok(new AccessTokenRes(newAccessToken));
    }

    @PostMapping("/tokens/refresh")
    public ResponseEntity<String> reissueRefreshToken(
            @CookieValue(name = REFRESH_TOKEN) Cookie refreshTokenCookie, HttpServletResponse response){

        Cookie newRefreshTokenCookie = cookieFactory.create(
                REFRESH_TOKEN,
                tokenReissueUseCase.refresh(refreshTokenCookie.getValue()),
                jwtProperties.getTokenExpiry(TokenType.REFRESH));
        response.addCookie(newRefreshTokenCookie);
        return ResponseEntity.ok("리프레시 토큰 발급 완료");
    }
}
