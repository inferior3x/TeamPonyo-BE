package com.econovation.teamponyo.domains.follow.domain;

import com.econovation.teamponyo.domains.user.domain.model.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Follow {
    @EmbeddedId
    private FollowId followId;

    public static Follow create(User follower, User followee){
        return new Follow(FollowId.of(follower.getUserId(), followee.getUserId()));
    }
}
