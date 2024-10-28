package com.econovation.teamponyo.domains.exhibit.query;

import com.econovation.teamponyo.domains.exhibit.query.port.in.ExhibitBannerQuery;
import com.econovation.teamponyo.domains.exhibit.query.port.in.ExhibitSummariesQuery;
import com.econovation.teamponyo.domains.exhibit.query.port.in.SavedExhibitsQuery;
import com.econovation.teamponyo.domains.exhibit.query.port.in.VisitedExhibitsQuery;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitBannerDTO;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitBannerReq;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummariesDTO;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummariesReq;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummaryDTO;
import com.econovation.teamponyo.domains.exhibit.query.port.out.ExhibitQueryDAO;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExhibitsQueryService implements
        ExhibitSummariesQuery,
        SavedExhibitsQuery,
        VisitedExhibitsQuery,
        ExhibitBannerQuery {
    private final JpaRepository jpaRepository;
    private final ExhibitQueryDAO exhibitQueryDAO;

    @Override
    public ExhibitSummariesDTO findSaved(Long userId, Integer number, Integer pageNumber) {
        return exhibitQueryDAO.findSavedExhibits(userId, number, pageNumber);
    }

    @Override
    public ExhibitSummariesDTO findVisited(Long userId, Integer number, Integer pageNumber) {
        return exhibitQueryDAO.findVisitedExhibits(userId, number, pageNumber);
    }

    @Override
    public ExhibitSummariesDTO get(@Valid ExhibitSummariesReq req) {
        return exhibitQueryDAO.findExhibitSummaries(req);
    }


    @Override
    public List<ExhibitBannerDTO> get(ExhibitBannerReq req) {
        return exhibitQueryDAO.findExhibitBanners(req);
    }
}