package com.econovation.teamponyo.domains.timeline.command;

import com.econovation.teamponyo.common.interfaces.RequesterInfo;
import com.econovation.teamponyo.domains.timeline.command.port.TimelineCreateUseCase;
import com.econovation.teamponyo.domains.timeline.command.port.TimelineRemoveUseCase;
import com.econovation.teamponyo.domains.timeline.domain.Timeline;
import com.econovation.teamponyo.domains.timeline.domain.TimelineDomainService;
import com.econovation.teamponyo.domains.timeline.command.port.TimelineRepository;
import com.econovation.teamponyo.domains.user.command.application.port.out.UserLoadRepository;
import com.econovation.teamponyo.domains.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TimelineService implements
        TimelineCreateUseCase,
        TimelineRemoveUseCase {
    private final TimelineRepository timelineRepository;
    private final UserLoadRepository userLoadRepository;
    private final RequesterInfo requesterInfo;
    private final TimelineDomainService timelineDomainService;
    //command에선 쿼리의 조회 모델을 쓰면 안될까?
    @Transactional
    @Override
    public Long create(Long teamId) {
        Long requesterId = requesterInfo.getUserId();
        User user = userLoadRepository.getByUserId(requesterId); //유저가 존재하는지 확인할 목적으로 이렇게 불러오는게 맞을까?
        User team = userLoadRepository.getByUserId(teamId);

        Timeline timeline = timelineDomainService.createTimelineWithExistingTeam(user, team);
        timelineRepository.save(timeline);
        return timeline.getTimelineId();
    }

    @Transactional
    @Override
    public Long create(String teamName) {
        Long requesterId = requesterInfo.getUserId();
        User user = userLoadRepository.getByUserId(requesterId);

        Timeline timeline = Timeline.createWithoutTeam(user.getUserId(), teamName);
        timelineRepository.save(timeline);
        return timeline.getTimelineId();
    }

    @Transactional
    @Override
    public void remove(Long timelineId) {
        Long requesterId = requesterInfo.getUserId();
        Timeline timeline = timelineRepository.getById(timelineId);
        timeline.validateIsMine(requesterId);
        timelineRepository.deleteById(timelineId);
    }
}
