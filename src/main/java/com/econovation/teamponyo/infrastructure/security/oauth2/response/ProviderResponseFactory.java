package com.econovation.teamponyo.infrastructure.security.oauth2.response;

import com.econovation.teamponyo.common.enums.SocialProvider;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProviderResponseFactory {
    private static final Map<SocialProvider, Function<Map<String, Object>, ProviderResponse>> oauth2ResponseCreators = new EnumMap<>(SocialProvider.class);
    static{
        oauth2ResponseCreators.put(SocialProvider.GOOGLE, GoogleResponse::new);
        oauth2ResponseCreators.put(SocialProvider.NAVER, NaverResponse::new);
        oauth2ResponseCreators.put(SocialProvider.KAKAO, KakaoResponse::new);
        //TODO: 테스트로 넣기
        Arrays.stream(SocialProvider.values())
                .forEach(socialProvider -> {
                   if (!oauth2ResponseCreators.containsKey(socialProvider))
                       throw new IllegalStateException("");
                });
    }
    public static ProviderResponse create(SocialProvider socialProvider, Map<String, Object> attributes){
        return oauth2ResponseCreators.get(socialProvider).apply(attributes);
    }
}
