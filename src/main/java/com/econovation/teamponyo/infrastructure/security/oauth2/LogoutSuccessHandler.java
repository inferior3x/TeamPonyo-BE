package com.econovation.teamponyo.infrastructure.security.oauth2;

import static com.econovation.teamponyo.common.consts.CommonStatics.REFRESH_TOKEN;

import com.econovation.teamponyo.domains.user.application.port.in.UserLogoutUseCase;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    private final UserLogoutUseCase userLogoutUseCase;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        Cookie[] cookies = Objects.requireNonNull(request.getCookies(), "no refresh token");
        Cookie refreshTokenCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(REFRESH_TOKEN))
                .findAny()
                .orElseThrow(() -> new NullPointerException("no refresh token"));

        userLogoutUseCase.logout(refreshTokenCookie.getValue());
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);
        response.sendRedirect("/");
    }
}
