package com.econovation.teamponyo.domains.follow.query;

import com.econovation.teamponyo.domains.follow.query.dto.res.FollowInfo;
import com.econovation.teamponyo.domains.user.query.port.in.dto.MemberProfileDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FollowQueryController {
    private final FollowQueryService followQueryService;

    @GetMapping("/users/{user-id}/follow-info")
    public ResponseEntity<FollowInfo> getFollowInfo(@PathVariable("user-id") Long userId){
        return ResponseEntity.ok(followQueryService.getFollowInfo(userId));
    }
    @GetMapping("/users/{user-id}/followers")
    public ResponseEntity<List<MemberProfileDTO>> getFollowerProfiles(@PathVariable("user-id") Long userId){
        return ResponseEntity.ok(followQueryService.getFollowerProfiles(userId));
    }
    @GetMapping("/users/{user-id}/followings")
    public ResponseEntity<List<MemberProfileDTO>> getFollowingProfiles(@PathVariable("user-id") Long userId){
        return ResponseEntity.ok(followQueryService.getFollowingProfiles(userId));
    }
}
