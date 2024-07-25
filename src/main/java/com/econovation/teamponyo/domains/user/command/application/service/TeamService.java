package com.econovation.teamponyo.domains.user.command.application.service;

import com.econovation.teamponyo.common.event.Events;
import com.econovation.teamponyo.common.interfaces.RequesterInfo;
import com.econovation.teamponyo.domains.user.command.application.port.in.TeamInviteUserUseCase;
import com.econovation.teamponyo.domains.user.command.application.port.out.UserLoadRepository;
import com.econovation.teamponyo.domains.user.query.port.out.UserQueryDAO;
import com.econovation.teamponyo.domains.user.command.application.port.out.UserRecordRepository;
import com.econovation.teamponyo.domains.user.events.TeamJoined;
import com.econovation.teamponyo.domains.user.domain.model.TeamMember;
import com.econovation.teamponyo.domains.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService implements TeamInviteUserUseCase {
    private final UserLoadRepository userLoadRepository;
    private final UserRecordRepository userRecordRepository;
    private final UserQueryDAO userQueryDAO;
    private final RequesterInfo requesterInfo;

    @Transactional
    @Override
    public void invite(Long inviteeId) {
        Long teamId = requesterInfo.getUserId();
        if (inviteeId.equals(teamId))
            throw new IllegalArgumentException("자신은 초대할 수 없음");
        if (userQueryDAO.existsTeamMember(teamId, inviteeId))
            throw new IllegalArgumentException("이미 팀에 속한 유저입니다.");
        User team = userLoadRepository.getByUserId(teamId);
        User invitee = userLoadRepository.getByUserId(inviteeId);
        TeamMember teamMember = team.createTeamMember(invitee);
        userRecordRepository.save(teamMember);
        Events.raise(new TeamJoined(inviteeId, teamId));
    }
}
