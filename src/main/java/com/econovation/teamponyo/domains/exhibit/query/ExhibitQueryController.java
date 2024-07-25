package com.econovation.teamponyo.domains.exhibit.query;

import com.econovation.teamponyo.common.enums.ExhibitCategory;
import com.econovation.teamponyo.domains.exhibit.command.adapter.in.dto.SavedExhibitReq;
import com.econovation.teamponyo.domains.exhibit.command.adapter.in.dto.VisitedExhibitReq;
import com.econovation.teamponyo.domains.exhibit.command.application.port.in.ExhibitDetailsQuery;
import com.econovation.teamponyo.domains.exhibit.query.port.in.ExhibitBannerQuery;
import com.econovation.teamponyo.domains.exhibit.query.port.in.ExhibitSummariesQuery;
import com.econovation.teamponyo.domains.exhibit.query.port.in.SavedExhibitsQuery;
import com.econovation.teamponyo.domains.exhibit.query.port.in.VisitedExhibitsQuery;
import com.econovation.teamponyo.domains.exhibit.command.application.port.in.dto.ExhibitDetailsDTO;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitBannerDTO;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitBannerReq;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummariesDTO;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummariesReq;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummaryDTO;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ExhibitQueryController {
    private final ExhibitDetailsQuery exhibitDetailsQuery;
    private final ExhibitSummariesQuery exhibitSummariesQuery;
    private final SavedExhibitsQuery savedExhibitsQuery;
    private final VisitedExhibitsQuery visitedExhibitsQuery;
    private final ExhibitBannerQuery exhibitBannerQuery;

    @GetMapping("/exhibits/{exhibit-id}")
    public ResponseEntity<ExhibitDetailsDTO> getExhibitDetails(@PathVariable("exhibit-id") Long exhibitId){
        return ResponseEntity.ok(exhibitDetailsQuery.findById(exhibitId));
    }

    @GetMapping("/exhibits/categories")
    public ResponseEntity<List<String>> getCategories(){
        return ResponseEntity.ok(ExhibitCategory.getKoreanNames());
    }

    @GetMapping("/exhibits")
    public ResponseEntity<ExhibitSummariesDTO> getExhibitSummaries(@ModelAttribute ExhibitSummariesReq req){
        return ResponseEntity.ok(exhibitSummariesQuery.get(req));
    }

    @GetMapping("/users/{user-id}/saved-exhibits")
    public ResponseEntity<ExhibitSummariesDTO> getSavedExhibits(
            @PathVariable("user-id") Long userId,
            @ModelAttribute @Valid SavedExhibitReq req){
        return ResponseEntity.ok(savedExhibitsQuery.findSaved(userId, req.number(), req.pageNumber()));
    }

    @GetMapping("/users/{user-id}/visited-exhibits")
    public ResponseEntity<ExhibitSummariesDTO> getVisitedExhibits(
            @PathVariable("user-id") Long userId,
            @ModelAttribute @Valid VisitedExhibitReq req){
        return ResponseEntity.ok(visitedExhibitsQuery.findVisited(userId, req.number(), req.pageNumber()));
    }

    @GetMapping("/exhibits/banners")
    public ResponseEntity<List<ExhibitBannerDTO>> getBanners(@RequestParam Integer number){
        return ResponseEntity.ok(exhibitBannerQuery.get(new ExhibitBannerReq(number)));
    }
}
