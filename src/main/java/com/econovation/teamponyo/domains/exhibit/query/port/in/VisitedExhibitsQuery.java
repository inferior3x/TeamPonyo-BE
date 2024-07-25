package com.econovation.teamponyo.domains.exhibit.query.port.in;

import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummariesDTO;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummaryDTO;
import java.util.List;

public interface VisitedExhibitsQuery {
    ExhibitSummariesDTO findVisited(Long userId, Integer number, Integer pageNumber);
}
