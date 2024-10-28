package com.econovation.teamponyo.domains.user.query.port.in.dto;

import com.econovation.teamponyo.common.enums.AccountType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record UserProfileDTO(
    Long userId,
    String loginId,
    String nickname,
    String profileImageUrl,
    AccountType accountType,
    String introduction
){

    public UserProfileDTO {
        if (introduction == null)
            introduction = "";
    }

}