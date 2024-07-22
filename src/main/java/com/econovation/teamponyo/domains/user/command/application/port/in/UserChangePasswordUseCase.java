package com.econovation.teamponyo.domains.user.command.application.port.in;

import com.econovation.teamponyo.domains.user.command.application.port.in.dto.UserChangePasswordCommand;

public interface UserChangePasswordUseCase {
    //TODO: command 하나로 합치기
    void changePassword(UserChangePasswordCommand command);
}
