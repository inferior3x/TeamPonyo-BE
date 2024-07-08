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
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/*
이 프로젝트에서 시큐리티가 하는 일
1. 사용자가 소셜 플랫폼에서 로그인을 진행하고 사용자의 정보를 가져오는 과정을 도와준다.
2. 로그인이 필요 없는 GET 요청은 비어있는 시큐리티 필터 체인으로 처리한다.
3. 로그인이 필요 없는 쓰기 요청은 인증 관련 필터가 없는 시큐리티 필터 체인으로 처리한다.
4. 로그인이 필요한 엔드포인트에 대해 jwt를 검증한다.
   로그아웃 기능을 제공한다.
 */
@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final ClientRegistrationRepositoryFactory clientRegistrationRepositoryFactory;
    private final OAuth2UserInfoLoader oAuth2UserInfoLoader;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final AccessTokenSerializer accessTokenSerializer;

    //인증인가 필요 없는 엔드포인트 - GET
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web ->
                web.ignoring()
                        .requestMatchers(HttpMethod.GET, "asdf");
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
                "/api/v1/auth/login*",
                "/api/v1/auth/signup*"
        );
        httpSecurity.authorizeHttpRequests(auth-> {
            auth.anyRequest().permitAll();
        });

        commonConfigurations(httpSecurity);

        httpSecurity.logout(AbstractHttpConfigurer::disable);
        httpSecurity.securityContext(AbstractHttpConfigurer::disable);
        //TODO: CORS 설정

        return httpSecurity.build();
    }
    //인증인가 필요한 엔드포인트 - 로그아웃, 게시글 작성 등
    @Bean
    @Order(3)
    public SecurityFilterChain authenticationFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher(
                "/api/v1/auth/logout",
                "/api/v1/exhibits"
        );
        httpSecurity.authorizeHttpRequests(auth-> {
            auth.requestMatchers(
                    "/api/v1/auth/logout",
                    "/api/v1/exhibits"
            ).authenticated();
            //hasAuthority()
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
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.sessionManagement(AbstractHttpConfigurer::disable);
        httpSecurity.requestCache(AbstractHttpConfigurer::disable);
    }
}