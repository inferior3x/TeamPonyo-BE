package com.econovation.teamponyo.domains.exhibit.domain.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Location {
    private String address;
    private Coordinate coordinate;

    public static Location of(String address, Coordinate coordinate){
        return new Location(address, coordinate);
    }
}
