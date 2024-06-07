package com.econovation.teamponyo.domains.user.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userId;

    @Embedded
    private FormLoginCredentials formLoginCredentials;

    @Embedded
    private SocialLoginInfo socialLoginInfo;

    @Embedded
    private UserInfo userInfo;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_info", unique = true)
    private TeamInfo teamInfo;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private AccountType accountType;

    private boolean emailSubscription;

    private LocalDateTime withdrawDate;

    @Builder
    public User(FormLoginCredentials formLoginCredentials, SocialLoginInfo socialLoginInfo,
            UserInfo userInfo, TeamInfo teamInfo, AccountType accountType,
            boolean emailSubscription) {
        this.formLoginCredentials = formLoginCredentials;
        this.socialLoginInfo = socialLoginInfo;
        this.userInfo = userInfo;
        this.teamInfo = teamInfo;
        this.accountType = accountType;
        this.emailSubscription = emailSubscription;
    }
}
