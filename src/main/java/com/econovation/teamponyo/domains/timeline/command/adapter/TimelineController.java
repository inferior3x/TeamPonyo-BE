package com.econovation.teamponyo.domains.timeline.command.adapter;

import com.econovation.teamponyo.domains.timeline.command.port.TimelineCreateReq;
import com.econovation.teamponyo.domains.timeline.command.port.TimelineCreateUseCase;
import com.econovation.teamponyo.domains.timeline.command.port.TimelineRemoveUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TimelineController {
    private final TimelineCreateUseCase timelineCreateUseCase;
    private final TimelineRemoveUseCase timelineRemoveUseCase;

    @PostMapping("/timelines")
    public ResponseEntity<Long> addTimeline(@RequestBody @Valid TimelineCreateReq req){
        Long timelineId;
        if (req.manual())
            timelineId = timelineCreateUseCase.create(req.teamName());
        else
            timelineId = timelineCreateUseCase.create(req.teamId());
        return ResponseEntity.ok(timelineId);
    }

    @DeleteMapping("/timelines/{timeline-id}")
    public ResponseEntity<String> removeTimeline(@PathVariable("timeline-id") Long timelineId){
        timelineRemoveUseCase.remove(timelineId);
        return ResponseEntity.ok("타임라인 삭제 완료");
    }

}
