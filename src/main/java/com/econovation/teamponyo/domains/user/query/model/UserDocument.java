package com.econovation.teamponyo.domains.user.query.model;

import com.econovation.teamponyo.common.enums.AccountType;
import com.econovation.teamponyo.domains.user.domain.model.User;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
//@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "users", writeTypeHint = WriteTypeHint.FALSE)
public class UserDocument {
    @Id
    private Long userId;
    private String nickname;
    private String loginId;
    private String profileImageKeyName;
    private AccountType accountType;
    private String introduction;
    private List<Long> teamIds; //팀 조회용
    private int followerNumber;
    private Set<Long> followers;
    private int followingNumber;
    private Set<Long> followings;

    public static UserDocument of(User user, UserDocument userDocument){
        return new UserDocument(
                user.getUserId(),
                user.getUserInfo().getNickname(),
                user.isSocialLoginUser() ? user.getUserInfo().getEmail() : user.getFormCredentials().getLoginId(),
                user.getProfileKeyName(),
                user.getAccountType(),
                user.getUserInfo().getIntroduction(),
                userDocument.teamIds,
                userDocument.followerNumber,
                userDocument.followers,
                userDocument.followingNumber,
                userDocument.followings
        );
    }

    public static UserDocument newDoc(User user) {
        return new UserDocument(
                user.getUserId(),
                user.getUserInfo().getNickname(),
                user.isSocialLoginUser() ? user.getUserInfo().getEmail() : user.getFormCredentials().getLoginId(),
                user.getProfileKeyName(),
                user.getAccountType(),
                user.getUserInfo().getIntroduction(),
                new ArrayList<>(),
                0,
                new HashSet<>(),
                0,
                new HashSet<>()
        );
    }
    public void addFollower(Long followerId){
        if (followers.add(followerId))
            followerNumber++;

    }
    public void removeFollower(Long followerId){
        if (followers.remove(followerId))
            followerNumber--;
    }
    public void addFollowing(Long followeeId){
        if (followings.add(followeeId))
            followingNumber++;
    }
    public void removeFollowing(Long followeeId){
        if (followings.remove(followeeId))
            followingNumber--;
    }
}
