package com.econovation.teamponyo.domains.user.query.port.in.dto;

import com.econovation.teamponyo.common.enums.AccountType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record MemberProfileDTO(
        Long userId,
        String loginId,
        AccountType accountType,
        String profileImageUrl,
        String nickname,
        String introduction,
        Boolean followed
) {

    public MemberProfileDTO {
        if (introduction == null)
            introduction = "";
    }
}