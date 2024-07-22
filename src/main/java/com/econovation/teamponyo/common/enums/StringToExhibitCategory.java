package com.econovation.teamponyo.common.enums;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringToExhibitCategory implements Converter<String, ExhibitCategory> {

    @Override
    public ExhibitCategory convert(String koreanName) {
        return ExhibitCategory.findByKoreanName(koreanName);
    }
}
