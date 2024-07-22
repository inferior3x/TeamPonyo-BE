package com.econovation.teamponyo.domains.exhibit.command.application.port.out;

import com.econovation.teamponyo.common.enums.ExhibitCategory;
import com.econovation.teamponyo.domains.exhibit.domain.model.Exhibit;
import java.util.List;
import java.util.Optional;

public interface ExhibitLoadRepository {
    Optional<Exhibit> findById(Long exhibitId);
    default Exhibit getById(Long exhibitId) {
        return findById(exhibitId)
                .orElseThrow(() -> new IllegalArgumentException("전시가 없습니다."));
    }
}
