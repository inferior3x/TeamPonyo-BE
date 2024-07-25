package com.econovation.teamponyo.domains.user.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//TODO: Form은 웹의 개념이므로 다른 단어로 나타내야 할듯
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FormCredentials {
//    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Column(unique = true, updatable = false)
    private String loginId;
    private String password;

    void changePassword(String oldPassword, String newPassword){
        if (!matchesPassword(oldPassword))
            throw new IllegalArgumentException("비밀번호가 맞지 않음");
        setPassword(newPassword);
    }

    public boolean matchesPassword(String rawPassword){
//        return passwordEncoder.matches(rawPassword, this.password);
        return rawPassword.equals(this.password);
    }

    private void setPassword(String rawPassword){
//        this.password = passwordEncoder.encode(rawPassword);
        this.password = rawPassword;
    }

    public FormCredentials(String loginId, String rawPassword) {
        this.loginId = loginId;
        setPassword(rawPassword);
    }
}
