package com.econovation.teamponyo.domains.exhibit.query.port.out;

import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitBannerDTO;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitBannerReq;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummariesDTO;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummaryDTO;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummariesReq;
import java.util.List;

public interface ExhibitQueryDAO {
    boolean isSavedExhibit(Long userId, Long exhibitId);
    boolean isVisitedExhibit(Long userId, Long exhibitId);
    ExhibitSummariesDTO findExhibitSummaries(ExhibitSummariesReq req);
    List<ExhibitBannerDTO> findExhibitBanners(ExhibitBannerReq req);
    ExhibitSummariesDTO findSavedExhibits(Long userId, Integer number, Integer pageNumber);
    ExhibitSummariesDTO findVisitedExhibits(Long userId, Integer number, Integer pageNumber);
}