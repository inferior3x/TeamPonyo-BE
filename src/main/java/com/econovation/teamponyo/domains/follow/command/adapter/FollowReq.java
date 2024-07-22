package com.econovation.teamponyo.domains.follow.command.adapter;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record FollowReq(
        Long followeeId
){}
