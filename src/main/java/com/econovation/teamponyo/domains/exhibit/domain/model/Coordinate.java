package com.econovation.teamponyo.domains.exhibit.domain.model;

import jakarta.persistence.Embeddable;
import java.nio.DoubleBuffer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Embeddable
public final class Coordinate {
    private Double lat;
    private Double lng;

    public static Coordinate of(Double lat, Double lng){
        //여기다가 유효한 좌표인지 확인하는 로직을 넣을 수도 있겠음
        return new Coordinate(lat, lng);
    }
}
