package com.econovation.teamponyo.domains.user.command.adapter.in.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record InviteUserReq(Long inviteeId){}
