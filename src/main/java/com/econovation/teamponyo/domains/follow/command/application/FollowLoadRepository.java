package com.econovation.teamponyo.domains.follow.command.application;

import com.econovation.teamponyo.domains.follow.domain.Follow;

public interface FollowLoadRepository {
    Follow findByTeamIdAndMemberId(Long followerId, Long followeeId);
}
