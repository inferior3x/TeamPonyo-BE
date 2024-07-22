package com.econovation.teamponyo.domains.timeline.domain;

import com.econovation.teamponyo.domains.user.domain.model.User;
import org.springframework.stereotype.Service;

@Service
public class TimelineDomainService {
    public Timeline createTimelineWithExistingTeam(User user, User team){
        team.validateIsTeam();
        return Timeline.createWithTeam(user.getUserId(), team.getUserId(), team.getUserInfo().getNickname());
    }
}
