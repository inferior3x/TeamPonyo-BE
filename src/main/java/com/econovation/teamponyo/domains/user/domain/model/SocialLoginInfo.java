package com.econovation.teamponyo.domains.user.domain.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLoginInfo {
    @Nonnull
    private SocialProvider socialProvider;
    @Nonnull
    @Column(unique = true, updatable = false)
    private String socialId;

    public SocialLoginInfo(@Nonnull SocialProvider socialProvider, @Nonnull String socialId) {
        this.socialProvider = socialProvider;
        this.socialId = socialId;
    }
}
