package com.econovation.teamponyo.domains.user.events;

import com.econovation.teamponyo.common.event.Event;
import com.econovation.teamponyo.domains.user.domain.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserEdited extends Event {
    private final User user;
}
