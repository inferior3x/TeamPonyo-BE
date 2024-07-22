package com.econovation.teamponyo.domains.timeline.command.port;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

@JsonNaming(SnakeCaseStrategy.class)
public record TimelineCreateReq(
        @NotNull
        Boolean manual,
        @Nullable
        Long teamId,
        @Nullable
        String teamName
){
    public TimelineCreateReq {
        if (manual.equals(Boolean.TRUE)) {
            if (teamName == null) throw new IllegalArgumentException("팀 닉네임이 없음");
        }else{
            if (teamId == null) throw new IllegalArgumentException("팀 아이디가 없음");
        }
    }
}
