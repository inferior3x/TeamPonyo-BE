package com.econovation.teamponyo.domains.user.query.adapter;

import com.econovation.teamponyo.domains.user.query.port.in.TeamGetMemberProfilesUseCase;
import com.econovation.teamponyo.domains.user.query.port.in.UserGetMyInfoUseCase;
import com.econovation.teamponyo.domains.user.query.port.in.UserGetUserProfileUseCase;
import com.econovation.teamponyo.domains.user.query.port.in.UserSearchUseCase;
import com.econovation.teamponyo.domains.user.query.port.in.dto.MemberProfileDTO;
import com.econovation.teamponyo.domains.user.query.port.in.dto.MyInfoDTO;
import com.econovation.teamponyo.domains.user.query.port.in.dto.SearchedUserDTO;
import com.econovation.teamponyo.domains.user.query.port.in.dto.UserProfileDTO;
import com.econovation.teamponyo.domains.user.query.port.in.dto.UserSearchReq;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserQueryController {
    private final UserGetUserProfileUseCase userGetProfileInfoUseCase;
    private final UserSearchUseCase userSearchUseCase;
    private final TeamGetMemberProfilesUseCase teamGetMemberProfilesUseCase;
    private final UserGetMyInfoUseCase userGetMyInfoUseCase;

    @GetMapping("/teams/{team-id}/members")
    public ResponseEntity<List<MemberProfileDTO>> getMemberProfiles(@PathVariable("team-id") Long teamId){
        return ResponseEntity.ok(teamGetMemberProfilesUseCase.getMembers(teamId));
    }

    @GetMapping("/users/{user-id}/profile")
    public ResponseEntity<UserProfileDTO> getProfile(@PathVariable("user-id") Long userId){
        return ResponseEntity.ok(userGetProfileInfoUseCase.get(userId));
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<SearchedUserDTO>> searchUsers(@ModelAttribute @Valid UserSearchReq req){
        return ResponseEntity.ok(userSearchUseCase.search(req));
    }

    @GetMapping("/user/my-info")
    public ResponseEntity<MyInfoDTO> getMyInfo(){
        return ResponseEntity.ok(userGetMyInfoUseCase.get());
    }
}
