package com.econovation.teamponyo.domains.follow.command.adapter;

import com.econovation.teamponyo.domains.follow.command.application.FollowLoadRepository;
import com.econovation.teamponyo.domains.follow.command.application.FollowRecordRepository;
import com.econovation.teamponyo.domains.follow.domain.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowAdapter implements FollowRecordRepository,
        FollowLoadRepository {
    private final FollowJpaRepository followJpaRepository;
    @Override
    public void save(Follow follow) {
        followJpaRepository.save(follow);
    }

    @Override
    public void delete(Long followerId, Long followeeId) {
        followJpaRepository.deleteByFollowId_FollowerIdAndFollowId_FolloweeId(followerId, followeeId);
    }

    @Override
    public Follow findByTeamIdAndMemberId(Long followerId, Long followeeId) {
        return followJpaRepository.findByTeamIdAndMemberId(followerId, followeeId);
    }
}
