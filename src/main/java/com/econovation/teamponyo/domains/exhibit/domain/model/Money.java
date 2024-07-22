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
public final class Money {
    private int amount;

    public static Money of(int amount){
        return new Money(amount);
    }

    @Override
    public String toString() {
        return amount == 0 ? "무료" : amount + "원";
    }
}
