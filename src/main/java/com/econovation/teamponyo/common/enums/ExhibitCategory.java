package com.econovation.teamponyo.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

public enum ExhibitCategory {
    ART("미술");
    private final String koreanName;

    ExhibitCategory(String koreanName) {
        this.koreanName = koreanName;
    }

    @JsonCreator
    public static ExhibitCategory findByKoreanName(String koreanName){
        return Arrays.stream(values())
                .filter(exhibitCategory -> exhibitCategory.koreanName.equals(koreanName))
                .findAny()
                .orElseThrow(()->new IllegalArgumentException("등록되지 않은 전시 카테고리"));
    }

}