package com.econovation.teamponyo.domains.user.domain.model;

import static com.econovation.teamponyo.common.consts.CommonStatics.DEFAULT_PROFILE_IMAGE_KEYNAME;

import com.econovation.teamponyo.common.enums.AccountType;
import com.econovation.teamponyo.common.enums.ExhibitCategory;
import com.econovation.teamponyo.common.event.Events;
import com.econovation.teamponyo.domains.exhibit.domain.model.Exhibit;
import com.econovation.teamponyo.domains.exhibit.domain.model.ExhibitPhotos;
import com.econovation.teamponyo.domains.exhibit.domain.model.Location;
import com.econovation.teamponyo.domains.exhibit.domain.model.Period;
import com.econovation.teamponyo.domains.user.events.UserEdited;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import java.util.HashSet;
import java.util.Set;
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
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET withdraw_date = null WHERE user_id = ?")
@SQLRestriction("withdraw_date IS NULL")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(nullable = false)
    @Builder.Default
    private String profileKeyName = DEFAULT_PROFILE_IMAGE_KEYNAME;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private AccountType accountType;

    @Column(nullable = false)
    private boolean emailSubscription;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "saved_exhibits", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "exhibit_id")
    private Set<Long> savedExhibits = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "visited_exhibits", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "exhibit_id")
    private Set<Long> visitedExhibits = new HashSet<>();

    @Column
    private LocalDateTime withdrawDate;


    //--domain logic--
    public void saveExhibit(Long exhibitId){
        if (!this.savedExhibits.add(exhibitId)) throw new IllegalArgumentException("이미 저장된 전시");
    }

    public void removeSavedExhibit(Long exhibitId){
        if (!this.savedExhibits.remove(exhibitId)) throw new IllegalArgumentException("저장하지 않았던 전시");
    }

    public void addVisitedExhibit(Long exhibitId){
        if (!this.visitedExhibits.add(exhibitId)) throw new IllegalArgumentException("이미 방문한 전시");
    }

    public void removeVisitedExhibit(Long exhibitId){
        if (!this.visitedExhibits.remove(exhibitId)) throw new IllegalArgumentException("방문했다고 체크한 적 없는 전시");
    }

    public void changePassword(String oldPassword, String newPassword){
        validateIsFormLoginUser();
        this.formCredentials.changePassword(oldPassword, newPassword);
        Events.raise(new UserEdited(this));
    }

    public void changeIntroduction(String introduction){
        this.userInfo.changeIntroduction(introduction);
        Events.raise(new UserEdited(this));
    }

    public boolean matchesPassword(String password){
       validateIsFormLoginUser();
       return this.formCredentials.matchesPassword(password);
    }

    public void changeProfileImage(String keyName){
        this.profileKeyName = keyName;
        Events.raise(new UserEdited(this));
    }

    public TeamMember createTeamMember(User invitee){
        this.validateIsApprovedTeam();
//        invitee.validateIsPersonal(); //TODO: 원래 검증해야 함 근데 시연을 위해 팀도 추가할 수 있게 만듦
        return TeamMember.of(this, invitee);
    }

    //--check--
    public boolean isSocialLoginUser(){
        return this.socialLoginInfo != null;
    }

    //--validate--
    public void validateIsTeam(){
        if (AccountType.TEAM != this.accountType)
            throw new IllegalArgumentException("팀 계정이 아님");
    }
    public void validateIsPersonal(){
        if (AccountType.PERSONAL != this.accountType)
            throw new IllegalArgumentException("개인 계정이 아님");
    }
    public void validateIsFormLoginUser(){
        if (this.formCredentials == null)
            throw new IllegalArgumentException("일반 로그인 유저가 아님");
    }

    public void validateIsApprovedTeam(){
        validateIsTeam();
        if (!this.teamInfo.isApproval())
            throw new IllegalArgumentException("허가되지 않은 팀 계정");
    }

    //--Aggregate Factory Method--
    public Exhibit createExhibit(String posterUrl, ExhibitCategory exhibitCategory, String title, Location location, String openTimes, String fee, String contact, String description, ExhibitPhotos exhibitPhotos, Period period){
        validateIsApprovedTeam();
        return Exhibit.create(
                this.userId,
                posterUrl,
                exhibitCategory,
                title,
                location,
                openTimes,
                fee,
                contact,
                description,
                exhibitPhotos,
                period
        );
    }

    //--Static Factory Method--
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

