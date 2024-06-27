package com.econovation.teamponyo.domains.user.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FormCredentials {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Column(unique = true, updatable = false)
    private String loginId;
    private String password;

    public void changePassword(String oldPassword, String newPassword){
        if (!matchPassword(oldPassword))
            throw new IllegalArgumentException("비밀번호가 맞지 않음");
        setPassword(newPassword);
    }

    public boolean matchPassword(String rawPassword){
        return passwordEncoder.matches(rawPassword, this.password);
    }

    private void setPassword(String rawPassword){
        this.password = passwordEncoder.encode(rawPassword);
    }

    public FormCredentials(String loginId, String rawPassword) {
        this.loginId = loginId;
        setPassword(rawPassword);
    }
}
