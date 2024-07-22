package com.econovation.teamponyo.domains.exhibit.command.application.port.in.dto;

import com.econovation.teamponyo.common.enums.ExhibitCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record ExhibitCreateCommand(
        @NotNull
        MultipartFile poster,
        ExhibitCategory exhibitCategory,
        @NotBlank
        String title,
        @NotBlank
        String address,
        String openTimes,
        String fee,
        String contact,
        @NotBlank
        String description,
        @NotNull
        List<MultipartFile> photos,
        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate endDate,
        @NotNull
        Coordinate coordinate
){

        public ExhibitCreateCommand {
                if (photos.size() > 10)
                        throw new IllegalArgumentException("최대 등록 가능한 전시 사진 개수는 10개");
        }
}

//TODO: url 검증
