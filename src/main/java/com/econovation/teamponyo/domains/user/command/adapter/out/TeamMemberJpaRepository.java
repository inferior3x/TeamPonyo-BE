package com.econovation.teamponyo.domains.user.command.adapter.out;

import com.econovation.teamponyo.domains.user.domain.model.TeamMember;
import com.econovation.teamponyo.domains.user.domain.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberJpaRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findAllByTeam(User user);
}
