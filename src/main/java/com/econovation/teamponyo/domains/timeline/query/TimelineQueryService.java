package com.econovation.teamponyo.domains.timeline.query;

import com.econovation.teamponyo.domains.timeline.command.port.TimelineRepository;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TimelineQueryService {
    private final TimelineRepository timelineRepository;
    public List<TimelineDTO> getTimelines(Long userId){
        return timelineRepository.findAllByUserId(userId).stream()
                .map(timeline -> new TimelineDTO(timeline.getTimelineId(), timeline.getTeamName(), timeline.getTeamId()))
                .sorted(Comparator.comparing(TimelineDTO::timelineId).reversed())
                .toList();
    }
}
