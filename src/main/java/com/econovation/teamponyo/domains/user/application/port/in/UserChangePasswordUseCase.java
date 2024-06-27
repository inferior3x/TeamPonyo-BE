package com.econovation.teamponyo.domains.user.application.port.in;

import com.econovation.teamponyo.domains.user.application.port.in.dto.UserChangePasswordCommand;

public interface UserChangePasswordUseCase {
    //TODO: command 하나로 합치기
    void changePassword(Long userId, UserChangePasswordCommand command);
}
