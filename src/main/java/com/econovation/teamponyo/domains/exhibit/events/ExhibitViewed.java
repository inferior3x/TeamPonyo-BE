package com.econovation.teamponyo.domains.exhibit.events;

import com.econovation.teamponyo.common.event.Event;
import com.econovation.teamponyo.domains.exhibit.domain.model.Exhibit;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExhibitViewed extends Event {
    private final Exhibit exhibit;
}
