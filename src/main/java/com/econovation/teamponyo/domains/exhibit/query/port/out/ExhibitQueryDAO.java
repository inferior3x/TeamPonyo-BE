package com.econovation.teamponyo.domains.exhibit.query.port.out;

import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummariesDTO;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummaryDTO;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummariesReq;
import java.util.List;

public interface ExhibitQueryDAO {
    boolean isSavedExhibit(Long userId, Long exhibitId);
    boolean isVisitedExhibit(Long userId, Long exhibitId);
    ExhibitSummariesDTO findExhibitSummaries(ExhibitSummariesReq req);
    List<ExhibitSummaryDTO> findSavedExhibits(Long userId, Integer number, Integer pageNumber);
    List<ExhibitSummaryDTO> findVisitedExhibits(Long userId, Integer number, Integer pageNumber);
}