package com.econovation.teamponyo.domains.user.command.application.port.out;

import com.econovation.teamponyo.domains.user.domain.model.TeamMember;
import com.econovation.teamponyo.domains.user.domain.model.User;
import java.util.List;

public interface TeamMemberRepository {
    List<TeamMember> findAllByTeam(User team);
}
