package com.econovation.teamponyo.domains.follow.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class FollowId implements Serializable {
    @Column
    private Long followerId;
    @Column
    private Long followeeId;

    public static FollowId of(Long followerId, Long followeeId){
        return new FollowId(followerId, followeeId);
    }
}
