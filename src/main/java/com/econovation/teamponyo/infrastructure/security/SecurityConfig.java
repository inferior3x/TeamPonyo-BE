package com.econovation.teamponyo.infrastructure.security;

import com.econovation.teamponyo.common.jwt.tokens.AccessTokenSerializer;
import com.econovation.teamponyo.infrastructure.security.oauth2.ClientRegistrationRepositoryFactory;
import com.econovation.teamponyo.infrastructure.security.oauth2.LoginFailureHandler;
import com.econovation.teamponyo.infrastructure.security.oauth2.LoginSuccessHandler;
import com.econovation.teamponyo.infrastructure.security.oauth2.LogoutSuccessHandler;
import com.econovation.teamponyo.infrastructure.security.oauth2.OAuth2UserInfoLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/*
시큐리티가 하는 일
1. 사용자가 소셜 플랫폼에서 로그인을 진행하고 사용자의 정보를 가져오는 과정을 도와준다.
2. 로그인이 필요 없는 GET 요청은 비어있는 시큐리티 필터 체인으로 처리한다.
3. 로그인이 필요 없는 쓰기 요청은 인증 관련 필터가 없는 시큐리티 필터 체인으로 처리한다. (CORS, CSRF 등 보안 기능을 사용하기 위해)
4. 로그인이 필요한 엔드포인트에 대해 jwt를 검증한다.
   로그아웃 기능을 제공한다.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final ClientRegistrationRepositoryFactory clientRegistrationRepositoryFactory;
    private final OAuth2UserInfoLoader oAuth2UserInfoLoader;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final AccessTokenSerializer accessTokenSerializer; //TODO: Filter가 빈으로 등록되면 자동으로 톰캣 필터에 등록되는 듯하다... 따라서 주입 없이 직접 생성하는 경우 인자로 필요하기 때문에 어쩔 수 없이 넣었다... 더 좋은 방법이 있을지?

    //인증인가 필요 없는 엔드포인트 - GET
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web ->
                web.ignoring()
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/exhibits/categories",
                                "/api/v1/exhibits",
                                "/api/v1/exhibits/banners",
                                "/api/v1/users/*/profile",
                                "/api/v1/users/*/saved-exhibits",
                                "/api/v1/users/*/visited-exhibits",
                                "/api/v1/users/search",
                                "/api/v1/users/*/timelines"
                        )
                        .requestMatchers(
                                "/error"
                        );
    }

    //OAuth2로 유저 정보를 가져오는 로직만 담당
    @Bean
    @Order(1)
    public SecurityFilterChain oauth2FilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher(
                "/login", //TODO: 운용에선 제거
                "/oauth2/authorization/*",
                "/login/oauth2/code/*"
        );

        commonConfigurations(httpSecurity);

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(AbstractHttpConfigurer::disable);
        httpSecurity.oauth2Login(oauth2->
            oauth2
                    //.loginPage(null) //TODO: 운영에선 추가
                    .clientRegistrationRepository(clientRegistrationRepositoryFactory.create())
                    .userInfoEndpoint(config -> config.userService(oAuth2UserInfoLoader))
                    .successHandler(loginSuccessHandler)
                    .failureHandler(loginFailureHandler)
        );

        return httpSecurity.build();
    }

    //TODO: 로그인도 시큐리티가 처리하게 할까?
    //인증인가 필요 없는 엔드포인트 - POST, PUT, PATCH, DELETE
    @Bean
    @Order(2)
    public SecurityFilterChain noAuthenticationFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher(
                "/api/v1/tokens/*",
                "/api/v1/auth/login",
                "/api/v1/auth/signup/*",
                "/test/api/v1/auth/signup/team",
                "/test/api/v1/auth/signup/personal",
                "/test/api/v1/auth/login/*"
        );
        httpSecurity.authorizeHttpRequests(auth-> auth.anyRequest().permitAll());

        commonConfigurations(httpSecurity);

        httpSecurity.logout(AbstractHttpConfigurer::disable);
        httpSecurity.securityContext(AbstractHttpConfigurer::disable);
        //TODO: CORS 설정

        return httpSecurity.build();
    }
    //인증인가 필요하거나 상관 없는 엔드포인트 - 로그아웃, 게시글 작성 등
    @Bean
    @Order(3)
    public SecurityFilterChain authenticationFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher(
                "/**"
        );
        httpSecurity.authorizeHttpRequests(auth-> {
            //인증 필요
            auth.requestMatchers(HttpMethod.POST,
                    "/api/v1/auth/logout",
                    "/api/v1/exhibits",
                    "/api/v1/user/saved-exhibits",
                    "/api/v1/user/visited-exhibits",
                    "/api/v1/team/member",
                    "/api/v1/follows",
                    "/api/v1/timelines"
            ).authenticated();
            auth.requestMatchers(HttpMethod.PATCH,
                    "/api/v1/user/introduction",
                    "/api/v1/user/password"
            ).authenticated();
            auth.requestMatchers(HttpMethod.DELETE,
                    "/api/v1/user/saved-exhibits/*",
                    "/api/v1/user/visited-exhibits/*",
                    "/api/v1/follows",
                    "/api/v1/timelines/*"
            ).authenticated();
            //인증 상관 없음
            auth.requestMatchers(HttpMethod.GET,
                    "/api/v1/exhibits/*",
                    "/api/v1/teams/*/members",
                    "/api/v1/users/*/follow-info",
                    "/api/v1/users/*/followers",
                    "/api/v1/users/*/followings",
                    "/api/v1/user/my-info"
            ).permitAll();
            auth.anyRequest().denyAll();
        });

        commonConfigurations(httpSecurity);

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.addFilterAt(new JwtAuthenticationFilter(accessTokenSerializer), LogoutFilter.class);
        httpSecurity.logout(logout->{
            logout.logoutUrl("/api/v1/auth/logout");
            logout.logoutSuccessHandler(logoutSuccessHandler); //TODO: 리프레시 토큰 블랙리스트에 추가
        });

        return httpSecurity.build();
    }

    private void commonConfigurations(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.sessionManagement(AbstractHttpConfigurer::disable);
        httpSecurity.requestCache(AbstractHttpConfigurer::disable);
    }
}