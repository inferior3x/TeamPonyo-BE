package com.econovation.teamponyo.domains.user.command.application.port.in;

import com.econovation.teamponyo.domains.user.command.application.port.in.dto.FormPersonalUserRegisterCommand;
import com.econovation.teamponyo.domains.user.command.application.port.in.dto.FormTeamUserRegisterCommand;

public interface UserFormRegisterUseCase {
    //TODO: Valid
    Long register(FormPersonalUserRegisterCommand command);
    Long register(FormTeamUserRegisterCommand command);
}
