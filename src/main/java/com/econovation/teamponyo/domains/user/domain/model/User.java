package com.econovation.teamponyo.domains.user.domain.model;

import com.econovation.teamponyo.common.enums.AccountType;
import com.econovation.teamponyo.common.enums.ExhibitCategory;
import com.econovation.teamponyo.domains.exhibit.domain.model.Exhibit;
import com.econovation.teamponyo.domains.exhibit.domain.model.ExhibitPhotos;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
//@ToString
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

    @Setter
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
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

    public boolean matchesPassword(String password){
        if (!isFormLoginUser())
            throw new IllegalArgumentException("일반 로그인 유저가 아님");
        return this.formCredentials.matchesPassword(password);
    }

    public boolean isApprovedTeam(){
        if (!isTeam()) //TODO: validate로 빼기
            throw new IllegalArgumentException("팀 계정이 아님");
        return this.teamInfo.isApproval();
    }
    public boolean isTeam(){
        return AccountType.TEAM == this.accountType;
    }

    private boolean isFormLoginUser(){
        return this.formCredentials != null;
    }
    private boolean isSocialLoginUser(){
        return this.socialLoginInfo != null;
    }

    public Exhibit createExhibit(String posterUrl, ExhibitCategory exhibitCategory, String title, String address, String openTimes, int fee, String contact, String description, ExhibitPhotos exhibitPhotos, LocalDate startDate, LocalDate endDate){
        if (!isApprovedTeam())
            throw new IllegalArgumentException("허가되지 않은 팀 계정");
        //날짜 제대로 되었는지 확인
        return Exhibit.create(
                this.userId,
                posterUrl,
                exhibitCategory,
                title,
                address,
                openTimes,
                fee,
                contact,
                description,
                exhibitPhotos,
                startDate,
                endDate
        );
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
        User user = User.builder()
                .formCredentials(formCredentials)
                .userInfo(userInfo)
                .teamInfo(teamInfo)
                .accountType(AccountType.TEAM)
                .emailSubscription(emailSubscription)
                .build();
        teamInfo.setUser(user);
        return user;
    }
    public static User createAdmin(FormCredentials formCredentials, UserInfo userInfo){
        return User.builder()
                .formCredentials(formCredentials)
                .userInfo(userInfo)
                .accountType(AccountType.ADMIN)
                .build();
    }

}

