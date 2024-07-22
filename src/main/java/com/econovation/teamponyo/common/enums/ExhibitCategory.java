package com.econovation.teamponyo.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum ExhibitCategory {
    EXHIBIT("전시회"),
    PERFORMANCE("공연"),
    CONTEST("공모전 및 대회"),
    ETC("기타");


    @Getter
    public static final List<String> koreanNames = Arrays.stream(ExhibitCategory.values())
            .map(ExhibitCategory::getKoreanName)
            .toList();

    private final String koreanName;

    ExhibitCategory(String koreanName) {
        this.koreanName = koreanName;
    }

    @JsonValue
    public String getKoreanName() {
        return koreanName;
    }

    @JsonCreator
    public static ExhibitCategory findByKoreanName(String koreanName){
        return Arrays.stream(values())
                .filter(exhibitCategory -> exhibitCategory.koreanName.equals(koreanName))
                .findAny()
                .orElseThrow(()->new IllegalArgumentException("등록되지 않은 전시 카테고리"));
    }

}