package com.econovation.teamponyo.domains.timeline.command.port;

import com.econovation.teamponyo.domains.timeline.domain.Timeline;
import java.util.List;
import java.util.Optional;

public interface TimelineRepository {
    void save(Timeline timeline);
    Optional<Timeline> findById(Long timelineId);
    List<Timeline> findAllByUserId(Long userId);
    default Timeline getById(Long timelineId){
        return findById(timelineId).orElseThrow(()->new IllegalArgumentException("타임라인이 없습니다."));
    }
    void deleteById(Long timelineId);
}
