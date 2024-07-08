package com.econovation.teamponyo.domains.user.domain;

import com.econovation.teamponyo.domains.user.domain.model.TeamInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamInfoRepository extends JpaRepository<TeamInfo, Long> {

}
