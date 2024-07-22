package com.econovation.teamponyo.domains.exhibit.domain.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public final class ExhibitPhotos {
    @Getter(AccessLevel.PACKAGE)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "exhibit_photos", joinColumns = @JoinColumn(name = "exhibit_id"))
    private List<ExhibitPhoto> exhibitPhotoList;

    public List<String> toKeyNames(){
        return this.exhibitPhotoList.stream()
                .map(ExhibitPhoto::getKeyName)
                .toList();
    }

    //TODO: 생성자에서 검증 필요
    public static ExhibitPhotos create(List<String> photoKeyNames){
        return new ExhibitPhotos(
                photoKeyNames.stream()
                        .map(ExhibitPhoto::create)
                        .toList()
        );
    }
}
