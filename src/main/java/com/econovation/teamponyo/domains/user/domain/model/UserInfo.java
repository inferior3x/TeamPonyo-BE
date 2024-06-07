package com.econovation.teamponyo.domains.user.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfo {
    private String nickname;
    private String phoneNumber;
    @Column(unique = true)
    private String email;
    private String introduction;

    public UserInfo createUserInfo(UserInfoValidator userInfoValidator, String nickname, String phoneNumber, String email, String introduction){
        userInfoValidator.validate(nickname, phoneNumber, email, introduction);
        return new UserInfo(nickname, phoneNumber, email, introduction);
    }
}
