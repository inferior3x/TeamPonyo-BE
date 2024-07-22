package com.econovation.teamponyo.domains.follow.command.adapter;

import com.econovation.teamponyo.domains.follow.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowJpaRepository extends JpaRepository<Follow, Long> {
    @Query("SELECT follow FROM Follow follow WHERE follow.followId.followerId = :followerId AND follow.followId.followeeId = :followeeId")
    Follow findByTeamIdAndMemberId(@Param("followerId") Long followerId, @Param("followeeId") Long followeeId);
    void deleteByFollowId_FollowerIdAndFollowId_FolloweeId(Long followerId, Long followeeId); //이렇게 하면 안되겠지^^..
}
