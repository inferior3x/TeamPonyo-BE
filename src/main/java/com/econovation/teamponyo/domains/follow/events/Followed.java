package com.econovation.teamponyo.domains.follow.events;

import com.econovation.teamponyo.common.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Followed extends Event {
    private final Long followerId;
    private final Long followeeId;
}
