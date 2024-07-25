package com.econovation.teamponyo.domains.user.command.adapter.in;

import static com.econovation.teamponyo.common.consts.CommonStatics.REFRESH_TOKEN;

import com.econovation.teamponyo.common.jwt.JwtProperties;
import com.econovation.teamponyo.common.jwt.TokenType;
import com.econovation.teamponyo.common.utils.CookieFactory;
import com.econovation.teamponyo.domains.user.command.adapter.in.dto.AccessTokenRes;
import com.econovation.teamponyo.domains.user.command.adapter.in.dto.FormLoginReq;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserFormLoginUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.UserFormRegisterUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.in.dto.FormPersonalUserRegisterCommand;
import com.econovation.teamponyo.domains.user.command.application.port.in.dto.FormTeamUserRegisterCommand;
import com.econovation.teamponyo.domains.user.command.application.port.in.dto.TokensRes;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/api/v1")
@RequiredArgsConstructor
public class TestController {
    private final UserFormRegisterUseCase userFormRegisterUseCase;
    private final UserFormLoginUseCase userFormLoginUseCase;
    private final CookieFactory cookieFactory;
    private final JwtProperties jwtProperties;

    @PostMapping("/auth/signup/personal")
    public ResponseEntity<AccessTokenRes> formPersonalSignup(HttpServletResponse response){
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        FormPersonalUserRegisterCommand testReq = new FormPersonalUserRegisterCommand(uuid, "password", "전남대학교 에코노"+uuid, uuid+"@naver.com", false);
        userFormRegisterUseCase.register(testReq);
        TokensRes tokensRes = userFormLoginUseCase.login(uuid, "password");
        Cookie refreshTokenCookie = cookieFactory.create(REFRESH_TOKEN, tokensRes.refreshToken(), jwtProperties.getTokenExpiry(TokenType.REFRESH));
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok(new AccessTokenRes(tokensRes.accessToken()));
    }

    @PostMapping("/auth/signup/team")
    public ResponseEntity<AccessTokenRes> formTeamSignup(HttpServletResponse response){
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        FormTeamUserRegisterCommand testReq = new FormTeamUserRegisterCommand(uuid, "password", "전남대학교 에코노"+uuid, uuid+"@naver.com", "01051256008", "대표자이름"+uuid, "증빙자료url", false);
        userFormRegisterUseCase.register(testReq);
        TokensRes tokensRes = userFormLoginUseCase.login(uuid, "password");
        Cookie refreshTokenCookie = cookieFactory.create(REFRESH_TOKEN, tokensRes.refreshToken(), jwtProperties.getTokenExpiry(TokenType.REFRESH));
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok(new AccessTokenRes(tokensRes.accessToken()));
    }


    @PostMapping("/auth/login")
    public ResponseEntity<AccessTokenRes> formLogin(@RequestBody @Valid FormLoginReq req, HttpServletResponse response){
        TokensRes tokensRes = userFormLoginUseCase.login(req.loginId(), req.password());

        Cookie refreshTokenCookie = req.autoLogin() ?
                cookieFactory.create(REFRESH_TOKEN, tokensRes.refreshToken(), jwtProperties.getTokenExpiry(
                        TokenType.REFRESH)) :
                cookieFactory.create(REFRESH_TOKEN, tokensRes.refreshToken());
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok(new AccessTokenRes(tokensRes.accessToken()));
    }
}
