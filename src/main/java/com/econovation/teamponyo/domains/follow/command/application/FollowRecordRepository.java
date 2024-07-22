package com.econovation.teamponyo.domains.follow.command.application;

import com.econovation.teamponyo.domains.follow.domain.Follow;

public interface FollowRecordRepository {
    void save(Follow follow);
    void delete(Long followerId, Long followeeId);
}
