package com.econovation.teamponyo.domains.user.command.application.port.out;

import com.econovation.teamponyo.domains.user.domain.model.TeamMember;
import com.econovation.teamponyo.domains.user.domain.model.User;

public interface UserRecordRepository {
    void save(User user);
    void save(TeamMember teamMember);
    void saveTeam(User user);
}
