package com.econovation.teamponyo.domains.user.command.adapter.out;

import com.econovation.teamponyo.domains.user.domain.model.TeamInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamInfoJpaRepository extends JpaRepository<TeamInfo, Long> {

}
