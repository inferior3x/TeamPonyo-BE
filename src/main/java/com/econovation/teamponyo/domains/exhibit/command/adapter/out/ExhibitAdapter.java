package com.econovation.teamponyo.domains.exhibit.command.adapter.out;

import com.econovation.teamponyo.domains.exhibit.command.application.port.out.ExhibitLoadRepository;
import com.econovation.teamponyo.domains.exhibit.command.application.port.out.ExhibitRecordRepository;
import com.econovation.teamponyo.domains.exhibit.domain.model.Exhibit;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExhibitAdapter implements
        ExhibitLoadRepository,
        ExhibitRecordRepository {
    private final ExhibitJpaRepository exhibitJpaRepository;

    @Override
    public Optional<Exhibit> findById(Long exhibitId) {
        return exhibitJpaRepository.findById(exhibitId);
    }

    @Override
    public void save(Exhibit exhibit) {
        exhibitJpaRepository.save(exhibit);
    }
}
