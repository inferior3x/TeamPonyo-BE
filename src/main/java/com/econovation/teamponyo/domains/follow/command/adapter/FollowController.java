package com.econovation.teamponyo.domains.follow.command.adapter;

import com.econovation.teamponyo.domains.follow.command.application.FollowUseCase;
import com.econovation.teamponyo.domains.follow.command.application.UnfollowUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FollowController {
    private final FollowUseCase followUseCase;
    private final UnfollowUseCase unfollowUseCase;

    @PostMapping("/follows")
    public ResponseEntity<String> follow(@RequestBody FollowReq req){
        followUseCase.follow(req.followeeId());
        return ResponseEntity.ok("팔로우 완료");
    }

    @DeleteMapping("/follows")
    public ResponseEntity<String> unfollow(@RequestBody UnfollowReq req){
        unfollowUseCase.unfollow(req.followeeId());
        return ResponseEntity.ok("언팔로우 완료");
    }
}
