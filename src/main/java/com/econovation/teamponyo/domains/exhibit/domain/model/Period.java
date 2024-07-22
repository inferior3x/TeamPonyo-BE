package com.econovation.teamponyo.domains.exhibit.domain.model;

import com.econovation.teamponyo.common.enums.ExhibitStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public final class Period {
    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    public static Period of(LocalDate startDate, LocalDate endDate){
        if (startDate.isAfter(endDate))
            throw new IllegalArgumentException("종료 날짜가 시작 날짜보다 빠릅니다.");
        return new Period(startDate, endDate);
    }

    ExhibitStatus getExhibitStatus(){
        LocalDate now = LocalDate.now();
        if (startDate.isAfter(now))
            return ExhibitStatus.BEFORE;
        if (startDate.isBefore(now) && endDate.isAfter(now))
            return ExhibitStatus.ONGOING;
        return ExhibitStatus.AFTER;
    }
    public boolean isAvailable(){
        LocalDate now = LocalDate.now();
        return this.startDate.isBefore(now) && this.endDate.isAfter(now);
    }

    @Override
    public String toString() {
        return this.startDate + " ~ " + this.endDate;
    }
}
