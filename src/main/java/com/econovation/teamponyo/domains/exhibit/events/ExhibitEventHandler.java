package com.econovation.teamponyo.domains.exhibit.events;

import com.econovation.teamponyo.domains.exhibit.command.application.port.out.ExhibitRecordRepository;
import com.econovation.teamponyo.domains.exhibit.domain.model.Exhibit;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExhibitEventHandler {
    private final ExhibitRecordRepository exhibitRecordRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener(ExhibitViewed.class)
    public void increaseViews(ExhibitViewed event){
        Exhibit exhibit = event.getExhibit();
        exhibit.increaseViewCount();
        exhibitRecordRepository.save(exhibit);
    }
}
