package com.econovation.teamponyo.domains.user.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FormLoginCredentials {
    @Column(unique = true, updatable = false)
    private String loginId;
    private String password;

    public void changePassword(String password){

    }

    public FormLoginCredentials(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
