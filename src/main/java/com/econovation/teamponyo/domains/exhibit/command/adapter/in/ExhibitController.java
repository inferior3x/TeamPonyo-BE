package com.econovation.teamponyo.domains.exhibit.command.adapter.in;

import com.econovation.teamponyo.domains.exhibit.command.adapter.in.dto.ExhibitCreateReq;
import com.econovation.teamponyo.domains.exhibit.command.application.port.in.ExhibitCreateUseCase;
import com.econovation.teamponyo.domains.exhibit.command.application.port.in.dto.ExhibitCreateCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ExhibitController {
    private final ExhibitCreateUseCase exhibitCreateUseCase;


    @PostMapping(value = "/exhibits", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Long> createExhibit(
            @RequestPart("poster") MultipartFile poster,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos,
            @RequestPart("info") ExhibitCreateReq req
            ) {
        return ResponseEntity.ok(exhibitCreateUseCase.create(
                new ExhibitCreateCommand(
                        poster,
                        req.exhibitCategory(), req.title(), req.address(), req.openTimes(), req.fee(), req.contact(), req.description(),
                        photos == null ? List.of() : photos,
                        req.startDate(), req.endDate(), req.coordinateDTO()
                )
        ));
    }
}
