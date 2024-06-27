package com.econovation.teamponyo.domains.user.domain.model;

import com.econovation.teamponyo.common.enums.AccountType;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET withdraw_date = null WHERE user_id = ?")
@SQLRestriction("withdraw_date IS NULL")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userId;

    @Embedded
    private FormCredentials formCredentials;

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

    @Column(nullable = false)
    private boolean emailSubscription;

    private LocalDateTime withdrawDate;

    public void changePassword(String oldPassword, String newPassword){
        if (!isFormLoginUser())
            throw new IllegalArgumentException("일반 로그인 유저가 아님");
        this.formCredentials.changePassword(oldPassword, newPassword);
    }

    public boolean matchPassword(String password){
        if (!isFormLoginUser())
            throw new IllegalArgumentException("일반 로그인 유저가 아님");
        return this.formCredentials.matchPassword(password);
    }

    private boolean isFormLoginUser(){
        return this.formCredentials != null;
    }
    private boolean isSocialLoginUser(){
        return this.socialLoginInfo != null;
    }

    public static User createOAuth2(SocialLoginInfo socialLoginInfo, UserInfo userInfo, boolean emailSubscription){
        return User.builder()
                .socialLoginInfo(socialLoginInfo)
                .userInfo(userInfo)
                .accountType(AccountType.PERSONAL) //OAuth2는 개인 계정만 가입 가능하다.
                .emailSubscription(emailSubscription)
                .build();
    }

    public static User createPersonal(FormCredentials formCredentials, UserInfo userInfo, boolean emailSubscription){
        return User.builder()
                .formCredentials(formCredentials)
                .userInfo(userInfo)
                .accountType(AccountType.PERSONAL)
                .emailSubscription(emailSubscription)
                .build();
    }

    public static User createTeam(FormCredentials formCredentials, UserInfo userInfo, TeamInfo teamInfo, boolean emailSubscription){
        return User.builder()
                .formCredentials(formCredentials)
                .userInfo(userInfo)
                .teamInfo(teamInfo)
                .accountType(AccountType.TEAM)
                .emailSubscription(emailSubscription)
                .build();
    }
    public static User createAdmin(FormCredentials formCredentials, UserInfo userInfo){
        return User.builder()
                .formCredentials(formCredentials)
                .userInfo(userInfo)
                .accountType(AccountType.ADMIN)
                .build();
    }

}

