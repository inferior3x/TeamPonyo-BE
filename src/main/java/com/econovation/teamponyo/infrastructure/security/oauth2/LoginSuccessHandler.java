package com.econovation.teamponyo.infrastructure.security.oauth2;

import static com.econovation.teamponyo.common.consts.CommonStatics.IS_REGISTERED;
import static com.econovation.teamponyo.common.consts.CommonStatics.OAUTH2_LOGIN_TOKEN;

import com.econovation.teamponyo.common.jwt.tokens.OAuth2LoginToken;
import com.econovation.teamponyo.common.jwt.tokens.OAuth2LoginTokenSerializer;
import com.econovation.teamponyo.common.utils.CookieFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilderFactory;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Qualifier("basic")
    private final CookieFactory cookieFactory;
    private final UriBuilderFactory uriBuilderFactory;
    private final OAuth2LoginTokenSerializer oAuth2LoginTokenSerializer;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof OAuth2UserInfo userInfo) {
            Cookie oAuth2LoginInfoTokenCookie = cookieFactory.create(
                    OAUTH2_LOGIN_TOKEN,
                    oAuth2LoginTokenSerializer.serialize(new OAuth2LoginToken(userInfo.getSocialProvider(), userInfo.getSocialId(), userInfo.getEmail()))
            );
            response.addCookie(oAuth2LoginInfoTokenCookie);

            String uri = uriBuilderFactory.uriString("/oauth/success")//TODO: Static
                    .queryParam(IS_REGISTERED, userInfo.isRegistered())
                    .build()
                    .toString();
            response.sendRedirect(uri);
            return;
        }
        throw new IllegalStateException("이러면 안되는데?");
    }
}
