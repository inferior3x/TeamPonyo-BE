package com.econovation.teamponyo.domains.exhibit.query.port.in;

import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummariesDTO;

public interface SavedExhibitsQuery {
    ExhibitSummariesDTO findSaved(Long userId, Integer number, Integer pageNumber);
}
