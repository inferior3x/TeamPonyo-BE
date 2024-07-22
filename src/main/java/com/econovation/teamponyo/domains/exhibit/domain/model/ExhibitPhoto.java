package com.econovation.teamponyo.domains.exhibit.domain.model;

import com.econovation.teamponyo.domains.exhibit.domain.model.dto.ExhibitPhotoCreateDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExhibitPhoto {
    @Column(nullable = false) //TODO: UNIQUE의 인덱스
    @Getter
    private String keyName;
    static ExhibitPhoto create(String keyName){
        return new ExhibitPhoto(keyName);
    }
}
