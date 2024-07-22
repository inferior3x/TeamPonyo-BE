package com.econovation.teamponyo.domains.exhibit.query;

import com.econovation.teamponyo.common.interfaces.RequesterInfo;
import com.econovation.teamponyo.domains.exhibit.command.application.port.in.dto.Coordinate;
import com.econovation.teamponyo.domains.exhibit.command.application.port.out.ExhibitLoadRepository;
import com.econovation.teamponyo.domains.exhibit.domain.model.Exhibit;
import com.econovation.teamponyo.domains.exhibit.domain.model.Location;
import com.econovation.teamponyo.domains.exhibit.query.port.in.ExhibitDetailsQuery;
import com.econovation.teamponyo.domains.exhibit.query.port.in.ExhibitSummariesQuery;
import com.econovation.teamponyo.domains.exhibit.query.port.in.SavedExhibitsQuery;
import com.econovation.teamponyo.domains.exhibit.query.port.in.VisitedExhibitsQuery;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitDetailsDTO;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummariesDTO;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummariesReq;
import com.econovation.teamponyo.domains.exhibit.query.port.in.dto.ExhibitSummaryDTO;
import com.econovation.teamponyo.domains.exhibit.query.port.out.ExhibitQueryDAO;
import com.econovation.teamponyo.domains.user.command.application.port.out.UserLoadRepository;
import com.econovation.teamponyo.domains.user.domain.model.User;
import com.econovation.teamponyo.infrastructure.s3.S3Uploader;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExhibitsQueryService implements
        ExhibitDetailsQuery,
        ExhibitSummariesQuery,
        SavedExhibitsQuery,
        VisitedExhibitsQuery {
    private final ExhibitLoadRepository exhibitLoadRepository;
    private final UserLoadRepository userLoadRepository;
    private final ExhibitQueryDAO exhibitQueryDAO;
    private final RequesterInfo requesterInfo;
    private final S3Uploader s3Uploader;

    @Override
    public List<ExhibitSummaryDTO> findSaved(Long userId, Integer number, Integer pageNumber) {
        return exhibitQueryDAO.findSavedExhibits(userId, number, pageNumber);
    }

    @Override
    public List<ExhibitSummaryDTO> findVisited(Long userId, Integer number, Integer pageNumber) {
        return exhibitQueryDAO.findVisitedExhibits(userId, number, pageNumber);
    }

    @Override
    public ExhibitSummariesDTO findExhibitSummaries(@Valid ExhibitSummariesReq req) {
        return exhibitQueryDAO.findExhibitSummaries(req);
    }

    @Override
    public ExhibitDetailsDTO findById(Long exhibitId) {
        Exhibit exhibit = exhibitLoadRepository.getById(exhibitId);
        User team = userLoadRepository.getByUserId(exhibit.getTeamId());
        Optional<Long> requester = requesterInfo.findUserId();

        boolean editable = requester.filter(exhibit::isEditableBy).isPresent();
        boolean saved = requester.filter(userId-> exhibitQueryDAO.isSavedExhibit(userId, exhibitId)).isPresent();
        boolean visited = requester.filter(userId-> exhibitQueryDAO.isVisitedExhibit(userId, exhibitId)).isPresent();

        Location location = exhibit.getLocation();
        Coordinate coordinate = new Coordinate(
                location.getCoordinate().getLat(),
                location.getCoordinate().getLng());
        return new ExhibitDetailsDTO(
                exhibit.getExhibitStatus(), editable,
                s3Uploader.getPublicUrl(exhibit.getPosterKeyName()),
                exhibit.getTitle(), team.getUserInfo().getNickname(),
                location.getAddress(),
                coordinate, exhibit.getPeriod().toString(),
                exhibit.getOpenTimes(), exhibit.getFee(), exhibit.getContact(), exhibit.getDescription(),
                saved, visited,
                s3Uploader.getPublicUrls(exhibit.getExhibitPhotos().toKeyNames())
        );
    }
}