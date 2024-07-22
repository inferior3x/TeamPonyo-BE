package com.econovation.teamponyo.domains.user.events;

import com.econovation.teamponyo.common.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TeamJoined extends Event {
    private final Long userId;
    private final Long teamId;
}
