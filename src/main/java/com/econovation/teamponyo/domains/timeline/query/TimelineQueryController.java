package com.econovation.teamponyo.domains.timeline.query;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TimelineQueryController {
    private final TimelineQueryService timelineQueryService;

    @GetMapping("/users/{user-id}/timelines")
    public ResponseEntity<List<TimelineDTO>> getTimelines(@PathVariable("user-id") Long userId){
        return ResponseEntity.ok(timelineQueryService.getTimelines(userId));
    }

}
