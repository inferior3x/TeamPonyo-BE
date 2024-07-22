package com.econovation.teamponyo.domains.exhibit.command.application.service;

import com.econovation.teamponyo.common.interfaces.RequesterInfo;
import com.econovation.teamponyo.common.utils.ImageValidator;
import com.econovation.teamponyo.domains.exhibit.command.application.port.in.ExhibitCreateUseCase;
import com.econovation.teamponyo.domains.exhibit.command.application.port.in.dto.ExhibitCreateCommand;
import com.econovation.teamponyo.domains.exhibit.command.application.port.out.ExhibitLoadRepository;
import com.econovation.teamponyo.domains.exhibit.command.application.port.out.ExhibitRecordRepository;
import com.econovation.teamponyo.domains.exhibit.domain.model.Coordinate;
import com.econovation.teamponyo.domains.exhibit.domain.model.Exhibit;
import com.econovation.teamponyo.domains.exhibit.domain.model.ExhibitPhotos;
import com.econovation.teamponyo.domains.exhibit.domain.model.Location;
import com.econovation.teamponyo.domains.exhibit.domain.model.Period;
import com.econovation.teamponyo.domains.user.command.application.port.out.UserLoadRepository;
import com.econovation.teamponyo.domains.user.domain.model.User;
import com.econovation.teamponyo.infrastructure.s3.S3Uploader;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExhibitService implements
        ExhibitCreateUseCase {
    //TODO: 다른 애그리거트의 레포써도 되나?
    private final UserLoadRepository userLoadRepository;
    private final ExhibitLoadRepository exhibitLoadRepository;
    private final ExhibitRecordRepository exhibitRecordRepository;
    private final RequesterInfo requesterInfo;
    private final S3Uploader s3Uploader;

    @Transactional
    @Override
    public Long create(@Valid ExhibitCreateCommand command) {
        Long teamId = requesterInfo.getUserId();
        User team = userLoadRepository.findByUserId(teamId)
                .orElseThrow(()-> new IllegalArgumentException("유저가 없음"));

        ImageValidator.validateImageFile(command.poster());


        String posterKeyName;
        posterKeyName = s3Uploader.uploadFile(command.poster());

        command.photos().forEach(ImageValidator::validateImageFile);
        List<String> photoKeyNames = new ArrayList<>(s3Uploader.uploadFiles(command.photos()));

        Exhibit exhibit = team.createExhibit(
                posterKeyName,
                command.exhibitCategory(),
                command.title(),
                Location.of(
                        command.address(),
                        Coordinate.of(command.coordinate().lat(), command.coordinate().lng())
                ),
                command.openTimes(),
                command.fee(),
                command.contact(),
                command.description(),
                ExhibitPhotos.create(photoKeyNames),
                Period.of(command.startDate(),command.endDate())
        );
        exhibitRecordRepository.save(exhibit);
        return exhibit.getExhibitId();
    }


}
