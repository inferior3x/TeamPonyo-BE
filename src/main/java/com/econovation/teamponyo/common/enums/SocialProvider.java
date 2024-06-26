package com.econovation.teamponyo.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialProvider {
    NAVER("naver", true),
    GOOGLE("google", true),
    KAKAO("kakao", true);
    private final String providerName;
    private final boolean registerable;

    public static SocialProvider findByProviderName(String providerName){
        for (SocialProvider socialProvider : SocialProvider.values()){
            if (socialProvider.providerName.equals(providerName))
                return socialProvider;
        }
        throw new IllegalStateException("등록되지 않은 프로바이더");
    }
}
