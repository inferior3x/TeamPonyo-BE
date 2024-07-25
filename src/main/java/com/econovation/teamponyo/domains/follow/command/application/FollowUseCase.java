package com.econovation.teamponyo.domains.follow.command.application;

public interface FollowUseCase {
    void follow(Long followeeId);
    //TODO: follow와 unfollow는 떨어질 수 없는 기능이므로 unfollow로 묶는게 나을듯.
}
