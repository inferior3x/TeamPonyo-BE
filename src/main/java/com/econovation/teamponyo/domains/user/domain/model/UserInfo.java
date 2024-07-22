package com.econovation.teamponyo.domains.user.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfo {
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false, unique = true)
    private String email;
    private String introduction;
    private String phoneNumber;

    public UserInfo(String nickname, String email, String introduction, String phoneNumber) {
        this.nickname = nickname;
        this.email = email;
        this.introduction = introduction;
        this.phoneNumber = phoneNumber;
    }

    void changeIntroduction(String introduction){
        this.introduction = introduction;
    }
}
