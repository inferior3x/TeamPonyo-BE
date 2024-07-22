package com.econovation.teamponyo.domains.timeline.command.adapter;

import com.econovation.teamponyo.domains.timeline.domain.Timeline;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimelineJpaRepository extends JpaRepository<Timeline, Long> {
    List<Timeline> findAllByUserId(Long userId);
}
