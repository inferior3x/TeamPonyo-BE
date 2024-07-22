package com.econovation.teamponyo.domains.exhibit.query.port.in;

import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummaryDTO;
import java.util.List;

public interface SavedExhibitsQuery {
    List<ExhibitSummaryDTO> findSaved(Long userId, Integer number, Integer pageNumber);
}
