package com.econovation.teamponyo.domains.follow.command.application;

import com.econovation.teamponyo.common.event.Events;
import com.econovation.teamponyo.common.interfaces.RequesterInfo;
import com.econovation.teamponyo.domains.follow.domain.Follow;
import com.econovation.teamponyo.domains.follow.events.Followed;
import com.econovation.teamponyo.domains.follow.events.Unfollowed;
import com.econovation.teamponyo.domains.user.command.application.port.out.UserLoadRepository;
import com.econovation.teamponyo.domains.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService implements
        FollowUseCase,
        UnfollowUseCase{
    private final UserLoadRepository userLoadRepository;
    private final FollowRecordRepository followRecordRepository;
    private final FollowLoadRepository followLoadRepository;
    private final RequesterInfo requesterInfo;

    @Transactional
    @Override
    public void follow(Long followeeId) {
        Long followerId = requesterInfo.getUserId();
        if (followerId.equals(followeeId))
            throw new IllegalArgumentException("자신을 팔로우할 수 없습니다.");
        User follower = userLoadRepository.getByUserId(followerId);
        User followee = userLoadRepository.getByUserId(followeeId);
        followRecordRepository.save(Follow.create(follower, followee));
        Events.raise(new Followed(followerId, followeeId));
    }

    @Transactional
    @Override
    public void unfollow(Long followeeId) {
        Long followerId = requesterInfo.getUserId();
        followRecordRepository.delete(followerId, followeeId);
        Events.raise(new Unfollowed(followerId, followeeId));
    }
}
