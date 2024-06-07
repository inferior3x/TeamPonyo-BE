package com.econovation.teamponyo.domains.user.domain.model;

//TODO: 패키지 옮기고 싶다.
@FunctionalInterface
public interface UserInfoValidator {
    void validate(String nickname, String phoneNumber, String email, String introduction);
}
