package com.econovation.teamponyo.domains.timeline.command.adapter;

import com.econovation.teamponyo.domains.timeline.command.port.TimelineRepository;
import com.econovation.teamponyo.domains.timeline.domain.Timeline;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimelineAdapter implements TimelineRepository {
    private final TimelineJpaRepository timelineJpaRepository;

    @Override
    public void save(Timeline timeline) {
        timelineJpaRepository.save(timeline);
    }

    @Override
    public Optional<Timeline> findById(Long timelineId) {
        return timelineJpaRepository.findById(timelineId);
    }

    @Override
    public List<Timeline> findAllByUserId(Long userId) {
        return timelineJpaRepository.findAllByUserId(userId);
    }

    @Override
    public void deleteById(Long timelineId) {
        timelineJpaRepository.deleteById(timelineId);
    }
}
