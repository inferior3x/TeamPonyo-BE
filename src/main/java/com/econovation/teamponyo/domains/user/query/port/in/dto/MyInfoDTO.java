package com.econovation.teamponyo.domains.user.query.port.in.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record MyInfoDTO(
        Long userId,
        String nickname
){}
