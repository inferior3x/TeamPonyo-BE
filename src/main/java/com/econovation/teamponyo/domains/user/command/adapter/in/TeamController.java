package com.econovation.teamponyo.domains.user.command.adapter.in;

import com.econovation.teamponyo.domains.user.command.adapter.in.dto.InviteUserReq;
import com.econovation.teamponyo.domains.user.command.application.port.in.TeamInviteUserUseCase;
import com.econovation.teamponyo.domains.user.query.port.in.TeamGetMemberProfilesUseCase;
import com.econovation.teamponyo.domains.user.query.port.in.dto.MemberProfileDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TeamController {
    private final TeamInviteUserUseCase teamInviteUserUseCase;


    @PostMapping("/team/member")
    public ResponseEntity<String> invitePersonal(@RequestBody InviteUserReq req){
        teamInviteUserUseCase.invite(req.inviteeId());
        return ResponseEntity.ok("팀 초대 완료");
    }
}
