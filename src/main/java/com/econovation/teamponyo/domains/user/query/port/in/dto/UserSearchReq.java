package com.econovation.teamponyo.domains.user.query.port.in.dto;

import com.econovation.teamponyo.common.enums.AccountType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import java.beans.ConstructorProperties;

public record UserSearchReq(
        @Nullable
        AccountType accountType,
        @Nullable
        Long teamId,
        @Nullable
        String nicknameOrLoginId,
        @Nullable
        Long inviterId,
        @Nullable
        Long searcherId
){
        @ConstructorProperties({"account-type", "team-id", "nickname-or-login-id", "inviter-id", "searcher-id"})
        public UserSearchReq {
        }
}
