package com.econovation.teamponyo.domains.user.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FormCredentials {
    @Column(unique = true, updatable = false)
    private String loginId;
    private String password;

    public void changePassword(String password){

    }

    public boolean matchPassword(String password){

        return true;
    }

    public FormCredentials(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
